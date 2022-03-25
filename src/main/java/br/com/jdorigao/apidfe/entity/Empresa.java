package br.com.jdorigao.apidfe.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "EmpresaSeq", sequenceName = "SEQ_EMPRESA", allocationSize = 1)
@Data
public class Empresa {

    @Id
    @GeneratedValue(generator = "EmpresaSeq", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String cpfCnpj;
    private String razaoSocial;
    private byte[] certificado;
    private String senhaCertificado;
    private String nsu;

}
