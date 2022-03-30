package br.com.jdorigao.apidfe.service;

import br.com.jdorigao.apidfe.entity.Empresa;
import br.com.jdorigao.apidfe.entity.NotaEntrada;
import br.com.jdorigao.apidfe.exception.SistemException;
import br.com.jdorigao.apidfe.repository.EmpresaRepository;
import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.Evento;
import br.com.swconsultoria.nfe.dom.enuns.*;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema.envConfRecebto.TEnvEvento;
import br.com.swconsultoria.nfe.schema.envConfRecebto.TRetEnvEvento;
import br.com.swconsultoria.nfe.schema.resnfe.ResNFe;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNfeProc;
import br.com.swconsultoria.nfe.util.ManifestacaoUtil;
import br.com.swconsultoria.nfe.util.ObjetoUtil;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DistribuicaoService {

    private final EmpresaRepository empresaRepository;
    private final NotaEntradaService notaEntradaService;

    public DistribuicaoService(EmpresaRepository empresaRepository, NotaEntradaService notaEntradaService) {
        this.empresaRepository = empresaRepository;
        this.notaEntradaService = notaEntradaService;
    }

    public void consultaNotas() throws CertificadoException, NfeException, IOException, JAXBException {

        List<Empresa> empresas = empresaRepository.findAll();
        for (Empresa empresa : empresas) {
            efetuarConsulta(empresa);
        }
    }

    private void efetuarConsulta(Empresa empresa) throws CertificadoException, NfeException, IOException, JAXBException {
        ConfiguracoesNfe configuracao = criaConfiguracao(empresa);
        List<String> listaNotasManifestar = new ArrayList<>();
        List<NotaEntrada> listasNotasSalvar = new ArrayList<>();
        boolean existeMais = true;

        while (existeMais) {
            RetDistDFeInt retorno = Nfe.distribuicaoDfe(configuracao, PessoaEnum.JURIDICA, empresa.getCpfCnpj(),
                    ConsultaDFeEnum.NSU, ObjetoUtil.verifica(empresa.getNsu()).orElse("000000000000000"));

            if (!retorno.getCStat().equals(StatusEnum.DOC_LOCALIZADO_PARA_DESTINATARIO.getCodigo())) {
                if (retorno.getCStat().equals(StatusEnum.CONSUMO_INDEVIDO.getCodigo())) {
                    break;
                } else {
                    throw new SistemException("Erro ao pesquisar notas: " + retorno.getCStat() + " - " + retorno.getXMotivo());
                }
            }

            populaLista(empresa, listaNotasManifestar, listasNotasSalvar, retorno);
            existeMais = !retorno.getUltNSU().equals(retorno.getMaxNSU());
            empresa.setNsu(retorno.getUltNSU());

        }

        empresaRepository.save(empresa);
        notaEntradaService.salvar(listasNotasSalvar);
        manifestaListaNotas(listaNotasManifestar, empresa, configuracao);
    }

    private void populaLista(Empresa empresa, List<String> listaNotasManifestar, List<NotaEntrada> listasNotasSalvar, RetDistDFeInt retorno) throws IOException, JAXBException {
        for (RetDistDFeInt.LoteDistDFeInt.DocZip doc : retorno.getLoteDistDFeInt().getDocZip()) {
            String xml = XmlNfeUtil.gZipToXml(doc.getValue());
            log.info("Xml: " + xml);
            log.info("Schema: " + doc.getSchema());
            log.info("Nsu: " + doc.getNSU());

            switch (doc.getSchema()) {
                case "resNFe_v1.01.xsd":
                    ResNFe resNFe = XmlNfeUtil.xmlToObject(xml, ResNFe.class);
                    String chave = resNFe.getChNFe();
                    listaNotasManifestar.add(chave);
                    break;
                case "procNFe_v4.00.xsd":
                    TNfeProc nfe = XmlNfeUtil.xmlToObject(xml, TNfeProc.class);
                    NotaEntrada notaEntrada = new NotaEntrada();
                    notaEntrada.setChave(nfe.getNFe().getInfNFe().getId().substring(3));
                    notaEntrada.setEmpresa(empresa);
                    notaEntrada.setSchema(doc.getSchema());
                    notaEntrada.setCnpjEmitente(nfe.getNFe().getInfNFe().getEmit().getCNPJ());
                    notaEntrada.setNomeEmitente(nfe.getNFe().getInfNFe().getEmit().getXNome());
                    notaEntrada.setValor(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF()));
                    //notaEntrada.setXml(ArquivoUtil.compactaXml(xml)); TODO: ArquivoUtil.compactaXml(xml)
                    listasNotasSalvar.add(notaEntrada);
                default:
                    break;
            }
        }
    }

    private void manifestaListaNotas(List<String> chaves, Empresa empresa, ConfiguracoesNfe configuracoesNfe) throws NfeException {

        for (String chave : chaves) {
            Evento manifesta = new Evento();
            manifesta.setChave(chave);
            manifesta.setCnpj(empresa.getCpfCnpj());
            manifesta.setMotivo("Manifestacao notas Resumo");
            manifesta.setDataEvento(LocalDateTime.now());
            manifesta.setTipoManifestacao(ManifestacaoEnum.CIENCIA_DA_OPERACAO);

            //Monta o Evento de Manifestação
            TEnvEvento enviEvento = ManifestacaoUtil.montaManifestacao(manifesta, configuracoesNfe);
            //Envia o Evento de Manifestação
            TRetEnvEvento retorno = Nfe.manifestacao(configuracoesNfe, enviEvento, false);
            if (!retorno.getRetEvento().get(0).getInfEvento().getCStat().equals(StatusEnum.EVENTO_VINCULADO.getCodigo())) {
                log.error("Erro ao Manifestar Chave " + chave + " : " + retorno.getCStat() + " - " + retorno.getXMotivo());
            }
        }
    }

    private ConfiguracoesNfe criaConfiguracao(Empresa empresa) throws CertificadoException {
        Certificado certificado = CertificadoService.certificadoPfxBytes(empresa.getCertificado(), empresa.getSenhaCertificado());
        return ConfiguracoesNfe.criarConfiguracoes(
                EstadosEnum.valueOf(empresa.getUf()),
                empresa.getAmbiente(),
                certificado,
                "/mnt/ssd/Workspace/api-dfe/schemas/");
    }
}
