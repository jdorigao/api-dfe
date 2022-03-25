package br.com.jdorigao.apidfe.service;

import br.com.jdorigao.apidfe.entity.Empresa;
import br.com.jdorigao.apidfe.exception.SistemException;
import br.com.jdorigao.apidfe.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository) {
        this.repository = repository;
    }

    private Empresa salvar(Empresa empresa) {
        validar(empresa);
        return repository.save(empresa);
    }

    private void deletar(Long idEmpresa) {
        repository.deleteById(idEmpresa);
    }

    private List<Empresa> listaTudo() {
        return repository.findAll();
    }

    private void validar(Empresa empresa) {
        Optional.ofNullable(empresa.getCpfCnpj()).orElseThrow(() -> new SistemException("Campo cpf/cnpj obrigatorio."));
        Optional.ofNullable(empresa.getCertificado()).orElseThrow(() -> new SistemException("Campo certificado obrigatorio."));
    }
}
