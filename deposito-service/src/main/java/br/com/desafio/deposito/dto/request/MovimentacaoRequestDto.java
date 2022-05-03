package br.com.desafio.deposito.dto.request;

import br.com.desafio.deposito.model.util.ETipoMovimentacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Schema(name = "MovimentacaoRequest")
public class MovimentacaoRequestDto {

    @NotNull(message = "Informe a bebida")
    @Min(value = 0, message = "Informe um identificador valido para a bebida")
    @Schema(description = "Identificador da Bebida", example = "1", defaultValue = "1", required = true)
    private Integer bebida;
    @NotNull(message = "Informe a secao")
    @Min(value = 0, message = "Informe um identificador valido para a secao")
    @Schema(description = "Identificador da Secao", example = "1", defaultValue = "1", required = true)
    private Integer secao;
    @NotNull(message = "Informe o volume da movimentacao")
    @Min(value = 0, message = "Informe um valor valido para o volume")
    @Schema(description = "Volume da Movimentacao em litros", example = "100", defaultValue = "100", required = true)
    private Float volume;
    @NotNull(message = "Informe o tipo da movimentacao")
    @Schema(description = "Tipo da Movimentacao", example = "ENTRADA", defaultValue = "ENTRADA", required = true)
    private ETipoMovimentacao tipoMovimentacao;

}
