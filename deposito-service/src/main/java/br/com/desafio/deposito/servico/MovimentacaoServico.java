package br.com.desafio.deposito.servico;

import br.com.desafio.deposito.dto.response.BebidaTipoExtratoResponseDto;
import br.com.desafio.deposito.dto.response.SecaoExtratoDataResponseDto;
import br.com.desafio.deposito.dto.response.SecaoVolumeDisponivelResponseDto;
import br.com.desafio.deposito.model.Movimentacao;
import br.com.desafio.deposito.dto.request.MovimentacaoRequestDto;
import br.com.desafio.deposito.dto.response.BebidaSecaoExtratoResponseDto;
import br.com.desafio.deposito.model.util.EBebidaTipo;
import br.com.desafio.deposito.search.SearchCriteria;

import java.util.List;
import java.util.Optional;

public interface MovimentacaoServico {

    Movimentacao salvar(MovimentacaoRequestDto movimentacaoRequestDto);

    List<Movimentacao> buscarTodos(SearchCriteria searchCriteria);

    List<SecaoExtratoDataResponseDto> buscarPorExtrato(Integer idSecao);

//    List<SecaoExtratoDataResponseDto> buscarPorExtrato();

    List<BebidaTipoExtratoResponseDto> buscarPorBebidaTipoExtrato();

    Optional<BebidaSecaoExtratoResponseDto> buscarPorExtrato(Integer idSecao, Integer idBebida);

    List<BebidaSecaoExtratoResponseDto> buscarPorBebidaSecaoExtrato();

    List<SecaoVolumeDisponivelResponseDto> buscarSecaoDisponivelPorBebidaVolume(Integer idBebida, Float volume);

    List<SecaoVolumeDisponivelResponseDto> buscarSecaoDisponivelPorTipoBebida(EBebidaTipo bebidaTipo);

    void deleteAll();
}
