package br.com.desafio.deposito.controller;

import br.com.desafio.deposito.model.Secao;
import br.com.desafio.deposito.servico.SecaoServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/secao")
public class SecaoController implements BaseCrudApi<Secao, Integer> {

    private final SecaoServico secaoServico;

    @Override
    @Operation(
            operationId = "salvarSecao",
            summary = "Altera secao ja existente",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<Secao> salvar(Secao secao) {

        secao = secaoServico.salvar(secao);

        return ResponseEntity.ok(secao);

    }

    @Override
    @Operation(
            operationId = "alterarMarca",
            summary = "Altera marca ja existente",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<Secao> alterar(Secao secao) {

        secao = secaoServico.alterar(secao);

        return ResponseEntity.ok(secao);
    }

    @Override
    @Operation(
            operationId = "buscarSecaoPorId",
            summary = "Retorna secao por id",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<Secao> buscarPorId(Integer id) {

        var secao = secaoServico.buscarPorId(id);

        return ResponseEntity.ok(secao);
    }

    @Override
    @Operation(
            operationId = "buscarTodasSecoes",
            summary = "Retorna todas as secoes",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<List<Secao>> buscarTodos() {

        var secoes = secaoServico.buscarTodos();

        return ResponseEntity.ok(secoes);
    }
}
