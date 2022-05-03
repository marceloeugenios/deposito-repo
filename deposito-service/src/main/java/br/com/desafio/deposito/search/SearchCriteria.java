package br.com.desafio.deposito.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SearchCriteria")
public class SearchCriteria {

    @Schema(description = "Objeto usado para filtrar")
    private List<SearchQuery> searchQuery = Collections.emptyList();
    @Schema(description = "Objeto usado para ordenar colunas")
    private List<SortQuery> sortQuery = Collections.emptyList();
}
