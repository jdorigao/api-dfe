package br.com.jdorigao.apidfe.repository;

import br.com.jdorigao.apidfe.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
