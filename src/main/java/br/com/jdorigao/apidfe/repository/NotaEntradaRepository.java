package br.com.jdorigao.apidfe.repository;

import br.com.jdorigao.apidfe.entity.NotaEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaEntradaRepository extends JpaRepository<NotaEntrada, Long> {

}
