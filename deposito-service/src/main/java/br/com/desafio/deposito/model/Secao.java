package br.com.desafio.deposito.model;

import br.com.desafio.deposito.model.util.ESituacao;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
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
public class Secao {

  @Id
  @Setter(AccessLevel.NONE)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secao_seq")
  @SequenceGenerator(name = "secao_seq", sequenceName = "secaoSeq", initialValue = 20, allocationSize = 1)
  private Integer id;
    @NotEmpty
    @Schema(description = "Nome da Secao", maxLength = 200, required = true)
    @Column(name = "nome", length = 200, unique = true, nullable = false)
    private String nome;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Situacao da Secao", maxLength = 20, required = true)
    @Column(name = "situacao", length = 20, nullable = false)
    private ESituacao situacao;
    @Version
    private Integer version;
    @Setter(AccessLevel.NONE)
    @Column(name = "ultima_alteracao")
    private LocalDateTime ultimaAlteracao;

    public Secao(String nome, ESituacao situacao) {
        this.nome = nome;
        this.situacao = situacao;
    }

    public Secao(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
