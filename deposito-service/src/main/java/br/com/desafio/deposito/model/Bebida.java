package br.com.desafio.deposito.model;

import br.com.desafio.deposito.model.util.EBebidaTipo;
import br.com.desafio.deposito.model.util.ESituacao;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Table
@Entity
@Builder
@ToString
@EqualsAndHashCode
public class Bebida {

  @Id
  @Setter(AccessLevel.NONE)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bebida_seq")
  @SequenceGenerator(name = "bebida_seq", sequenceName = "bebidaSeq", initialValue = 20, allocationSize = 1)
  private Integer id;
    @NotEmpty
    @Schema(description = "Informe o nome", maxLength = 11, required = true)
    @Column(name = "nome", length = 200, nullable = false)
    private String nome;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo de Bebida", maxLength = 100, required = true)
    @Column(name = "bebida_tipo", length = 100, nullable = false, updatable = false)
    private EBebidaTipo bebidaTipo;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Situacao da Marca", maxLength = 50, required = true)
    @Column(name = "situacao", length = 50, nullable = false)
    private ESituacao situacao;
    @NotNull
    @JoinColumn(name = "_marca", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Marca marca;

    @Tolerate
    public Bebida() {

    }
}
