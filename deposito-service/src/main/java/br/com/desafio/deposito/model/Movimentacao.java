package br.com.desafio.deposito.model;

import br.com.desafio.deposito.model.util.ETipoMovimentacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder(builderMethodName = "hiddenBuilder")
public class Movimentacao {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movimentacao_seq")
    @SequenceGenerator(name = "movimentacao_seq", sequenceName = "movimentacaoSeq", initialValue = 20, allocationSize = 1)
    private Long id;
    @Setter(AccessLevel.NONE)
    @Column(name = "data_movimentacao")
    private LocalDateTime dataMovimentacao;
    @NotNull
    @Schema(description = "Identificador do usuario que fez o cadastro da secao", required = true)
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;
    @NotNull
    @Schema(description = "Volume da Bebida", required = true)
    @Column(name = "volume", nullable = false, columnDefinition = "FLOAT(5,2)")
    private Float volume;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo da Movimentacao", maxLength = 20, required = true)
    @Column(name = "tipo_movimentacao", length = 20, nullable = false)
    private ETipoMovimentacao tipoMovimentacao;
    @NotNull
    @JoinColumn(name = "_bebida", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Bebida bebida;
    @NotNull
    @JoinColumn(name = "_secao", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Secao secao;

    @Tolerate
    protected Movimentacao() {

    }

    public static MovimentacaoBuilder builder(Bebida bebida, Secao secao, Float volume, ETipoMovimentacao tipoMovimentacao) {
        return hiddenBuilder()
                .bebida(bebida)
                .secao(secao)
                .volume(volume)
                .tipoMovimentacao(tipoMovimentacao);
    }

    @PrePersist
    protected void setupDate() {
        this.dataMovimentacao = LocalDateTime.now();
    }
}
