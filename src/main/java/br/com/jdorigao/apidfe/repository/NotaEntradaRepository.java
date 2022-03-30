package br.com.jdorigao.apidfe.repository;

import br.com.jdorigao.apidfe.entity.NotaEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotaEntradaRepository extends JpaRepository<NotaEntrada, Long> {

    Optional<NotaEntrada> findFirstByChave(String chave);
}
