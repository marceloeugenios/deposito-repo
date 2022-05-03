package br.com.desafio.deposito.controller;

import br.com.desafio.deposito.model.Bebida;
import br.com.desafio.deposito.servico.BebidaServico;
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
@RequestMapping(value = "api/v1/bebida")
public class BebidaController implements BaseCrudApi<Bebida, Integer> {

    private final BebidaServico bebidaServico;

    @Override
    @Operation(
            operationId = "salvarBebida",
            summary = "Faz o cadastro de bebida",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<Bebida> salvar(Bebida bebida) {

        bebida = bebidaServico.salvar(bebida);

        return ResponseEntity.ok(bebida);
    }

    @Override
    @Operation(
            operationId = "alterarBebida",
            summary = "Altera bebida ja existente",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<Bebida> alterar(Bebida bebida) {
        return ResponseEntity.ok(bebidaServico.alterar(bebida));
    }

    @Override
    @Operation(
            operationId = "buscarBebidaPorId",
            summary = "Retorna bebida por id",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<Bebida> buscarPorId(Integer id) {
        return ResponseEntity.ok(bebidaServico.buscarPorId(id));
    }

    @Override
    @Operation(
            operationId = "buscarTodasBebidas",
            summary = "Retorna todas as bebidas",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<List<Bebida>> buscarTodos() {

        var bebidas = bebidaServico.buscarTodos();

        return ResponseEntity.ok(bebidas);
    }
}
