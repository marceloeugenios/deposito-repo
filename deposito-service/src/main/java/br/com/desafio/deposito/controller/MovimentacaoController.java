package br.com.desafio.deposito.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.desafio.deposito.dto.response.BebidaTipoExtratoResponseDto;
import br.com.desafio.deposito.dto.response.MovimentacaoResponseDto;
import br.com.desafio.deposito.dto.response.SecaoVolumeDisponivelResponseDto;
import br.com.desafio.deposito.dto.request.MovimentacaoRequestDto;
import br.com.desafio.deposito.dto.response.BebidaSecaoExtratoResponseDto;
import br.com.desafio.deposito.model.util.EBebidaTipo;
import br.com.desafio.deposito.search.SearchCriteria;
import br.com.desafio.deposito.servico.MovimentacaoServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/movimentacao")
public class MovimentacaoController {

  private final MovimentacaoServico movimentacaoServico;

  @Operation(
      operationId = "gerarMovimentacao",
      summary = "Gera Movimentação de estoque podendo ser de entrada ou de saída",
      responses = {
          @ApiResponse(responseCode = "201"),
          @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)
      }
  )
  @RolesAllowed("admin")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<MovimentacaoResponseDto> gerarMovimentacao(
      @Valid @RequestBody MovimentacaoRequestDto movimentacaoRequestDto) {

    log.info("Recebendo movimentacao: {}", movimentacaoRequestDto);

    var movimentacao = movimentacaoServico.salvar(movimentacaoRequestDto);

    return ResponseEntity.ok(new MovimentacaoResponseDto(movimentacao));
  }

  @Operation(
      operationId = "extratoMovimentacao",
      summary = "Extrato de entrada e saída de bebidas em caso de venda e recebimento",
      description = "Consulta de entradas e saídas por tipo de bebida e seção. Deve retornar os resultados " +
          "ordenados por data e seção, podendo alterar a ordenação via parâmetros. " +
          "Campos disponiveis para filtro: bebidaTipo e nome. " +
          "Campos disponivis para ordenacao: dataMovimentacao e nome.",
      responses = {
          @ApiResponse(responseCode = "200"),
          @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)
      }
  )
  @RolesAllowed("admin")
  @ResponseStatus(HttpStatus.OK)
  @PostMapping(value = "/search", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MovimentacaoResponseDto>> getExtratoMovimentacao(
      @Valid @RequestBody SearchCriteria searchCriteria) {
    var movimentacoes = movimentacaoServico.buscarTodos(searchCriteria)
        .stream()
        .map(MovimentacaoResponseDto::new)
        .collect(Collectors.toList());

    return ResponseEntity.ok(movimentacoes);
  }

  @Operation(
      operationId = "extratoBebidaSecao",
      summary = "Consulta das bebidas armazenadas em cada secao",
      responses = {
          @ApiResponse(responseCode = "200"),
          @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)
      }
  )
  @RolesAllowed("admin")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/bebidaPorSecao", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BebidaSecaoExtratoResponseDto>> getExtratoBebida() {

    var extratoBebidaSecao = movimentacaoServico.buscarPorBebidaSecaoExtrato();

    return ResponseEntity.ok(extratoBebidaSecao);
  }

  @Operation(
      operationId = "extratoPorTipoBebida",
      summary = "Consulta do volume total no estoque por cada tipo de bebida",
      responses = {
          @ApiResponse(responseCode = "200"),
          @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)
      }
  )
  @RolesAllowed("admin")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/volumeEstoquePorTipoBebida", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<BebidaTipoExtratoResponseDto>> getExtratoTipoBebida() {

    var bebidaTipoExtrato = movimentacaoServico.buscarPorBebidaTipoExtrato();

    return ResponseEntity.ok(bebidaTipoExtrato);
  }

  @Operation(
      operationId = "secaoDisponivelBebidaVolume",
      summary = "Consulta dos locais disponíveis de armazenamento de um determinado volume de bebida",
      responses = {
          @ApiResponse(responseCode = "200"),
          @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)
      }
  )
  @RolesAllowed("admin")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/verificarSecaoDisponivelPorBebidaVolume/{idBebida}/{volume}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<SecaoVolumeDisponivelResponseDto>> verificarSecaoDisponivelPorBebidaVolume
      (@PathVariable(name = "idBebida") Integer idBebida, @PathVariable("volume") Float volume) {

    var secaoVolumeDisponivel = movimentacaoServico
        .buscarSecaoDisponivelPorBebidaVolume(idBebida, volume);

    return ResponseEntity.ok(secaoVolumeDisponivel);
  }

  @Operation(
      operationId = "secaoDisponivelTipoBebida",
      summary = "Consulta das seções disponíveis para venda de determinado tipo de bebida",
      responses = {
          @ApiResponse(responseCode = "200"),
          @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)
      }
  )
  @RolesAllowed("admin")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/secaoComVolumePorBebidaTipo/{bebidaTipo}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<SecaoVolumeDisponivelResponseDto>> verificarSecaoDisponivelPorTipoBebida(
      @PathVariable(name = "bebidaTipo") EBebidaTipo bebidaTipo) {

    var bebidaTipoExtrato = movimentacaoServico
        .buscarSecaoDisponivelPorTipoBebida(bebidaTipo);

    return ResponseEntity.ok(bebidaTipoExtrato);
  }
}
