package br.com.desafio.deposito.search;

import br.com.desafio.deposito.search.util.SortedBy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortQuery {

    @Schema(description = "Coluna para ordenacao", defaultValue = "nome", example = "nome")
    private String coluna;
    @Schema(description = "Direcao da ordenacao - ASC ou DESC", defaultValue = "ASC", example = "ASC")
    private SortedBy ordenacao;

}
