package br.com.desafio.deposito.servico.impl;

import static br.com.desafio.deposito.model.util.EBebidaTipo.ALCOOLICA;
import static br.com.desafio.deposito.model.util.ETipoMovimentacao.ENTRADA;
import static java.lang.String.format;

import br.com.desafio.deposito.dto.response.BebidaTipoExtratoResponseDto;
import br.com.desafio.deposito.dto.response.SecaoExtratoDataResponseDto;
import br.com.desafio.deposito.dto.response.SecaoVolumeDisponivelResponseDto;
import br.com.desafio.deposito.model.Movimentacao;
import br.com.desafio.deposito.model.Secao;
import br.com.desafio.deposito.repository.MovimentacaoRepository;
import br.com.desafio.deposito.dto.request.MovimentacaoRequestDto;
import br.com.desafio.deposito.dto.response.BebidaSecaoExtratoResponseDto;
import br.com.desafio.deposito.model.Bebida;
import br.com.desafio.deposito.model.util.EBebidaTipo;
import br.com.desafio.deposito.search.SearchCriteria;
import br.com.desafio.deposito.servico.BebidaServico;
import br.com.desafio.deposito.servico.MovimentacaoServico;
import br.com.desafio.deposito.servico.SecaoServico;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovimentacaoServicoImpl implements MovimentacaoServico {

  private static final Map<String, String> SORT_MAP;

  static {
    SORT_MAP = new HashMap<>();
    SORT_MAP.put("dataMovimentacao", "dataMovimentacao");
    SORT_MAP.put("nome", "s.nome");
  }

  private final MovimentacaoRepository movimentacaoRepository;
  private final BebidaServico bebidaServico;
  private final SecaoServico secaoServico;

  @Transactional
  public Movimentacao salvar(MovimentacaoRequestDto movimentacaoRequestDto) {
    UUID token = getTokenUsuarioLogado();

    Bebida bebida = bebidaServico.buscarPorId(movimentacaoRequestDto.getBebida());
    Secao secao = secaoServico.buscarPorIdLock(movimentacaoRequestDto.getSecao());

    var movimentacao = Movimentacao.builder(bebida,
            secao, movimentacaoRequestDto.getVolume(),
            movimentacaoRequestDto.getTipoMovimentacao())
        .usuarioId(token).build();

    if (ENTRADA.equals(movimentacaoRequestDto.getTipoMovimentacao())) {
      validarEntrada(movimentacao);
    } else {
      validarSaida(movimentacao);
    }
    movimentacao = movimentacaoRepository.save(movimentacao);
    secaoServico.atualizarDataAlteracao(secao);
    return movimentacao;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Movimentacao> buscarTodos(SearchCriteria searchCriteria) {
    List<Sort.Order> orders;
    if (Objects.nonNull(searchCriteria.getSortQuery()) && !searchCriteria.getSortQuery().isEmpty()) {
      orders = searchCriteria.getSortQuery()
          .stream()
          .filter(sortQuery -> Objects.nonNull(sortQuery.getColuna()))
          .map(sortQuery -> new Sort.Order(sortQuery.getOrdenacao().getOriginalOrder(),
              SORT_MAP.getOrDefault(sortQuery.getColuna(), "s.id")))
          .collect(Collectors.toList());
    } else {
      orders = SORT_MAP.values().stream()
          .map(s -> new Sort.Order(Sort.Direction.ASC, s))
          .collect(Collectors.toList());
    }
    var movimentacoes = movimentacaoRepository.findAll(Sort.by(orders));

    /**
     * Specification API seria uma boa opcao para fazer queries dinamicas com reflections pra tratar os tipos de cada atributo
     */
    if (Objects.nonNull(searchCriteria.getSearchQuery())) {
      var bebidaTipo = searchCriteria.getSearchQuery()
          .stream()
          .filter(searchQuery -> Objects.nonNull(searchQuery.getColumn())
              && searchQuery.getColumn().equalsIgnoreCase("bebidaTipo"))
          .findFirst();
      var nomeSecao = searchCriteria.getSearchQuery()
          .stream()
          .filter(searchQuery -> Objects.nonNull(searchQuery.getColumn()) &&
              searchQuery.getColumn().equalsIgnoreCase("nome"))
          .findFirst();

      if (bebidaTipo.isPresent()) {
        movimentacoes = movimentacoes.stream()
            .filter(movimentacao -> movimentacao.getBebida().getBebidaTipo()
                .equals(EBebidaTipo.valueOf(bebidaTipo.get().getValue().toString().toUpperCase())))
            .collect(Collectors.toList());
      }
      if (nomeSecao.isPresent()) {
        movimentacoes = movimentacoes.stream()
            .filter(movimentacao -> movimentacao.getSecao().getNome().toUpperCase()
                .contains(nomeSecao.get().getValue().toString().toUpperCase()))
            .collect(Collectors.toList());
      }
    }
    return movimentacoes;
  }

  @Override
  @Transactional(readOnly = true)
  public List<SecaoExtratoDataResponseDto> buscarPorExtrato(Integer idSecao) {
    return movimentacaoRepository.findByExtrato(idSecao);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BebidaTipoExtratoResponseDto> buscarPorBebidaTipoExtrato() {
    return movimentacaoRepository.findByBebidaTipoExtrato();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<BebidaSecaoExtratoResponseDto> buscarPorExtrato(Integer idSecao, Integer idBebida) {
    return movimentacaoRepository.findByExtrato(idSecao, idBebida);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BebidaSecaoExtratoResponseDto> buscarPorBebidaSecaoExtrato() {
    return movimentacaoRepository.findByBebidaSecaoExtrato()
        .stream().filter(bebidaSecaoExtratoResponseDto -> bebidaSecaoExtratoResponseDto.getVolumeAtual() > 0)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<SecaoVolumeDisponivelResponseDto> buscarSecaoDisponivelPorBebidaVolume(Integer idBebida,
                                                                                     Float volumeDesejado) {
    Bebida bebida = bebidaServico.buscarPorId(idBebida);

    var secoesValidas = movimentacaoRepository.findByBebidaSecaoExtrato()
        .stream()
        .filter(bebidaSecao -> bebidaSecao.getBebidaTipo().equals(bebida.getBebidaTipo())
            && (bebidaSecao.getVolumeAtual() + volumeDesejado)
            <= bebidaSecao.getBebidaTipo().getVolumeMaximo())
        .map(SecaoVolumeDisponivelResponseDto::new)
        .collect(Collectors.toSet());

    var secoesIds = secoesValidas
        .stream()
        .map(SecaoVolumeDisponivelResponseDto::getId)
        .collect(Collectors.toList());

    var secoesNaoIncluidas = secaoServico.buscarTodos()
        .stream()
        .filter(secao -> !secoesIds.contains(secao.getId()))
        .collect(Collectors.toList());

    secoesNaoIncluidas.forEach(disponivel -> {
      try {
        var secao = new Secao(disponivel.getId(), disponivel.getNome());
        validarEntrada(Movimentacao.builder(bebida, secao, volumeDesejado, ENTRADA).build());
        secoesValidas.add(new SecaoVolumeDisponivelResponseDto(secao));
      } catch (IllegalArgumentException e) {
        log.warn("Secao {} nao disponivel para {}", disponivel.getId(), bebida.getBebidaTipo());
      }
    });

    return secoesValidas
        .stream()
        .sorted(Comparator.comparing(SecaoVolumeDisponivelResponseDto::getId))
        .collect(Collectors.toUnmodifiableList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<SecaoVolumeDisponivelResponseDto> buscarSecaoDisponivelPorTipoBebida(EBebidaTipo bebidaTipo) {
    return movimentacaoRepository.findByBebidaSecaoExtrato()
        .stream()
        .filter(bebidaSecao -> bebidaSecao.getBebidaTipo().equals(bebidaTipo))
        .filter(bebidaSecao -> (bebidaSecao.getVolumeAtual() > 0))
        .map(SecaoVolumeDisponivelResponseDto::new)
        .distinct()
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void deleteAll() {
    log.warn("Deletando records");
    movimentacaoRepository.deleteAll();
  }

  private void validarSaida(Movimentacao movimentacao) {
    var extrato = buscarPorExtrato(movimentacao.getSecao().getId(),
        movimentacao.getBebida().getId());
    extrato.ifPresentOrElse(bebidaSecaoExtratoResponseDto -> {
      if (movimentacao.getVolume() > bebidaSecaoExtratoResponseDto.getVolumeAtual()) {
        if (bebidaSecaoExtratoResponseDto.getVolumeAtual() == 0) {
          throw new IllegalArgumentException("Não há esta bebida nesta seção");
        } else {
          throw new IllegalArgumentException(format("Não há esta bebida suficiente nesta seção. " +
                  "Volume disponível %.2f",
              bebidaSecaoExtratoResponseDto.getVolumeAtual()));
        }
      }
    }, () -> {
      throw new IllegalArgumentException("Não há esta bebida em estoque nesta seção!");
    });
  }

  private void validarEntrada(Movimentacao movimentacao) {
    var bebidaTipo = movimentacao.getBebida().getBebidaTipo();

    var extrato = buscarPorExtrato(movimentacao.getSecao().getId());

    if (extrato.isEmpty()) {
      if (movimentacao.getVolume() > movimentacao.getBebida().getBebidaTipo().getVolumeMaximo()) {
        var excedente = movimentacao.getBebida().getBebidaTipo().getVolumeMaximo() - movimentacao.getVolume();
        throw new IllegalArgumentException(format("Volume máximo ainda permitido %.2f", excedente));
      }
    } else {
      extrato.forEach(ext -> {
        if (!ext.getBebidaTipo().equals(bebidaTipo) && ext.getVolumeAtual() > 0) {
          throw new IllegalArgumentException(format("Seção já contém bebidas de outro tipo %s", ext.getBebidaTipo()));
        }
        if (ext.getBebidaTipo().equals(bebidaTipo)) {
          var totalAdicionado = movimentacao.getVolume() + ext.getVolumeAtual();
          if (totalAdicionado > ext.getBebidaTipo().getVolumeMaximo()) {
            var volumeAindaPermitido = ext.getBebidaTipo().getVolumeMaximo() - ext.getVolumeAtual();
            if (volumeAindaPermitido > 0) {
              throw new IllegalArgumentException(format("Volume máximo ainda permitido %.2f", volumeAindaPermitido));
            } else {
              throw new IllegalArgumentException("Secao com volume máximo permitido");
            }
          }
        }
        if (ext.getData().equals(LocalDate.now()) && ALCOOLICA.equals(ext.getBebidaTipo())
            && !ext.getBebidaTipo().equals(bebidaTipo)) {
          throw new IllegalArgumentException(format("Secao ja recebeu outro tipo de bebida no dia de hoje %s",
              ext.getBebidaTipo()));
        }
      });
    }
  }

  private UUID getTokenUsuarioLogado() {
    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      KeycloakPrincipal principal = (KeycloakPrincipal) auth.getPrincipal();
      return UUID.fromString(principal.toString());
    } catch (Exception e) {
      return UUID.randomUUID();
    }
  }
}
