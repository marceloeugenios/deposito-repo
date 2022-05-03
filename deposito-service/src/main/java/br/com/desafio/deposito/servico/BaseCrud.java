package br.com.desafio.deposito.servico;

import java.util.List;

interface BaseCrud<T, U> {

    T salvar(T t);

    T alterar(T t);

    List<T> buscarTodos();

    T buscarPorId(U u);

}
