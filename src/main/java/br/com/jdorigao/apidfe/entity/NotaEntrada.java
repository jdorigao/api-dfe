package br.com.jdorigao.apidfe.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "nota_entrada")
@SequenceGenerator(name = "NotaEntradaSeq", sequenceName = "SEQ_NOTA_ENTRADA", allocationSize = 1)
@Data
public class NotaEntrada {

    @Id
    @GeneratedValue(generator = "NotaEntradaSeq", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String schema;
    private String chave;
    private String nomeEmitente;
    private String cnpjEmitente;
    private BigDecimal valor;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] xml;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Empresa empresa;
}
