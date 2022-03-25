package br.com.jdorigao.apidfe.service;

import br.com.jdorigao.apidfe.entity.Empresa;
import br.com.jdorigao.apidfe.exception.SistemException;
import br.com.jdorigao.apidfe.repository.EmpresaRepository;
import br.com.swconsultoria.nfe.util.ObjetoUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository) {
        this.repository = repository;
    }

    public Empresa salvar(Empresa empresa) {
        validar(empresa);
        return repository.save(empresa);
    }

    public void deletar(Long idEmpresa) {
        repository.deleteById(idEmpresa);
    }

    public List<Empresa> listaTudo() {
        return repository.findAll();
    }

    public Empresa listarPorId(Long idEmpresa) {
        return repository.findById(idEmpresa).orElseThrow(() -> new SistemException("Empresa não Encontrada com id: "+idEmpresa));
    }

    private void validar(Empresa empresa) {
        ObjetoUtil.verifica(empresa.getCpfCnpj()).orElseThrow(() -> new SistemException("Campo cpf/cnpj obrigatório."));
        ObjetoUtil.verifica(empresa.getCertificado()).orElseThrow(() -> new SistemException("Campo certificado obrigatório."));
    }
}
