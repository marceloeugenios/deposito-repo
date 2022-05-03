package br.com.desafio.deposito.model;

import br.com.desafio.deposito.model.util.ESituacao;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Table
@Entity
@NoArgsConstructor
public class Marca {

  @Id
  @Setter(AccessLevel.NONE)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marca_seq")
  @SequenceGenerator(name = "marca_seq", sequenceName = "marcaSeq", initialValue = 20, allocationSize = 1)
  private Integer id;
    @NotEmpty
    @Schema(description = "Nome da marca", maxLength = 200, required = true)
    @Column(name = "nome", length = 200, unique = true, nullable = false)
    private String nome;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Situacao da Marca", maxLength = 20, required = true)
    @Column(name = "situacao", length = 20, nullable = false)
    private ESituacao situacao;

    public Marca(String nome, ESituacao situacao) {
        this.nome = nome;
        this.situacao = situacao;
    }
}
