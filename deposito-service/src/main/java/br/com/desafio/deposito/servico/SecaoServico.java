package br.com.desafio.deposito.servico;

import br.com.desafio.deposito.model.Secao;

public interface SecaoServico extends BaseCrud<Secao, Integer> {

  void atualizarDataAlteracao(Secao secao);

  Secao buscarPorIdLock(Integer id);

}
