package br.com.jdorigao.apidfe.service;

import br.com.jdorigao.apidfe.entity.NotaEntrada;
import br.com.jdorigao.apidfe.exception.SistemException;
import br.com.jdorigao.apidfe.repository.NotaEntradaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotaEntradaService {

    private final NotaEntradaRepository repository;

    public NotaEntradaService(NotaEntradaRepository repository) {
        this.repository = repository;
    }

    private NotaEntrada salvar(NotaEntrada notaEntrada) {
        validar(notaEntrada);
        return repository.save(notaEntrada);
    }

    private void deletar(Long idNotaEntrada) {
        repository.deleteById(idNotaEntrada);
    }

    private List<NotaEntrada> listaTudo() {
        return repository.findAll();
    }

    private void validar(NotaEntrada notaEmpresa) {
        Optional.ofNullable(notaEmpresa.getChave()).orElseThrow(() -> new SistemException("Campo chave obrigatoria."));
    }
}
