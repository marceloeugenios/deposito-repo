package br.com.desafio.deposito.search.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
@AllArgsConstructor
public enum SortedBy {

    ASC(Sort.Direction.ASC), DESC(Sort.Direction.DESC);

    private Sort.Direction originalOrder;

}
