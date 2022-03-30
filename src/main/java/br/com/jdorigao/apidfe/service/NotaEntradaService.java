package br.com.jdorigao.apidfe.service;

import br.com.jdorigao.apidfe.entity.NotaEntrada;
import br.com.jdorigao.apidfe.exception.SistemException;
import br.com.jdorigao.apidfe.repository.NotaEntradaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class NotaEntradaService {

    private final NotaEntradaRepository repository;

    public NotaEntradaService(NotaEntradaRepository repository) {
        this.repository = repository;
    }

    public void salvar(List<NotaEntrada> notaEntrada) {
        repository.saveAll(notaEntrada);
    }

    public List<NotaEntrada> listaTudo() {
        return repository.findAll();
    }

    public NotaEntrada listarPorId(Long idNota) {
        return repository.findById(idNota)
                .orElseThrow(() -> new SistemException("Nota não encontrada com o id: " + idNota));
    }

    public byte[] getXml(Long idNota) throws IOException {
        NotaEntrada nota = listarPorId(idNota);
        return nota.getXml();
        // return ArquivoUtil.descompactaXml(nota.getXml()); TODO: Descompactar notas
    }

    public NotaEntrada getPorChave(String chave) {
        return repository.findFirstByChave(chave)
                .orElseThrow(() -> new SistemException("Nota não encontrada com a chave: " + chave));
    }
}
