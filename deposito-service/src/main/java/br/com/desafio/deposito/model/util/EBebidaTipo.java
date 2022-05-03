package br.com.desafio.deposito.model.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum EBebidaTipo {

    ALCOOLICA("Alcoólica", 500F),
    NAO_ALCOOLICA("Não Alcoólica", 400F);

    private String descricao;
    private Float volumeMaximo;

}