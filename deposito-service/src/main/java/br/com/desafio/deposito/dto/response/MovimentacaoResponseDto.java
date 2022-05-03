package br.com.desafio.deposito.dto.response;

import br.com.desafio.deposito.model.Movimentacao;
import br.com.desafio.deposito.model.util.ETipoMovimentacao;
import br.com.desafio.deposito.util.FormatterUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
@Schema(name = "Movimentacao")
public class MovimentacaoResponseDto {

    @Schema(description = "Identificador Movimentacao", example = "123", defaultValue = "123")
    private Long id;
    @Schema(description = "Data da Movimentacao", example = "2022-04-12 12:12:12",
            defaultValue = "2022-04-12 12:12:12", format = "date-time")
    private String dataMovimentacao;
    @Schema(description = "Identificador da Secao", example = "1", defaultValue = "1")
    private Integer idSecao;
    @Schema(description = "Nome da Secao", example = "Secao de Bebidas Destiladas",
            defaultValue = "Secao de Bebidas Destiladas")
    private String nomeSecao;
    @Schema(description = "Identificador da Bebida", example = "143", defaultValue = "143")
    private Integer idBebida;
    @Schema(description = "Nome da Bebida", example = "Coca-Coca Zero", defaultValue = "Coca-Coca Zero")
    private String nomeBebida;
    @Schema(description = "Identificador do usuario que fez a Movimentacao",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", defaultValue = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID usuarioId;
    @Schema(description = "Volume da Bebida em litros", example = "10", defaultValue = "10")
    private Float volume;
    @Schema(description = "Tipo da Movimentacao", example = "ENTRADA", defaultValue = "ENTRADA")
    private ETipoMovimentacao tipoMovimentacao;

    public MovimentacaoResponseDto(Movimentacao movimentacao) {
        this.id = movimentacao.getId();
        this.dataMovimentacao = movimentacao.getDataMovimentacao().toString();
        this.idSecao = movimentacao.getSecao().getId();
        this.nomeSecao = movimentacao.getSecao().getNome();
        this.idBebida = movimentacao.getBebida().getId();
        this.nomeBebida = movimentacao.getBebida().getNome();
        this.usuarioId = movimentacao.getUsuarioId();
        this.volume = FormatterUtil.formatDecimal(movimentacao.getVolume());
        this.tipoMovimentacao = movimentacao.getTipoMovimentacao();
    }
}
