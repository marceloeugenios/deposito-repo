package br.com.desafio.deposito.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SearchQuery")
public class SearchQuery {

    @Schema(description = "Coluna para filtrar", defaultValue = "nome", example = "nome")
    private String column;
    @Schema(description = "Valor a ser filtrado na coluna informada", defaultValue = "Secao 1", example = "Secao 1")
    private Object value;

}
