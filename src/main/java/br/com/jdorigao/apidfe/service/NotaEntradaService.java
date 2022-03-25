package br.com.jdorigao.apidfe.service;

import br.com.jdorigao.apidfe.entity.NotaEntrada;
import br.com.jdorigao.apidfe.exception.SistemException;
import br.com.jdorigao.apidfe.repository.NotaEntradaRepository;
import br.com.swconsultoria.nfe.util.ObjetoUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotaEntradaService {

    private final NotaEntradaRepository repository;

    public NotaEntradaService(NotaEntradaRepository repository) {
        this.repository = repository;
    }

    public NotaEntrada salvar(NotaEntrada notaEntrada) {
        validar(notaEntrada);
        return repository.save(notaEntrada);
    }

    public void deletar(Long idNotaEntrada) {
        repository.deleteById(idNotaEntrada);
    }

    public List<NotaEntrada> listaTudo() {
        return repository.findAll();
    }

    private void validar(NotaEntrada notaEmpresa) {
        ObjetoUtil.verifica(notaEmpresa.getChave()).orElseThrow(() -> new SistemException("Campo chave obrigatória."));
    }
}
