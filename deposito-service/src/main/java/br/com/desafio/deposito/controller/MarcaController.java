package br.com.desafio.deposito.controller;

import br.com.desafio.deposito.model.Marca;
import br.com.desafio.deposito.servico.MarcaServico;
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
@RequestMapping(value = "api/v1/marca")
public class MarcaController implements BaseCrudApi<Marca, Integer> {

    private final MarcaServico marcaServico;

    @Override
    @Operation(
            operationId = "salvarMarca",
            summary = "Altera bebida ja existente",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<Marca> salvar(Marca marca) {

        marca = marcaServico.salvar(marca);

        return ResponseEntity.ok(marca);

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
    public ResponseEntity<Marca> alterar(Marca marca) {

        marca = marcaServico.alterar(marca);

        return ResponseEntity.ok(marca);
    }

    @Override
    @Operation(
            operationId = "buscarMarcaPorId",
            summary = "Retorna marca por id",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<Marca> buscarPorId(Integer id) {

        var bebidaMarca = marcaServico.buscarPorId(id);

        return ResponseEntity.ok(bebidaMarca);
    }

    @Override
    @Operation(
            operationId = "buscarTodasMascas",
            summary = "Retorna todas as marcas",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", description = "Illegal Argument Exception", content = @Content)

            }
    )
    @RolesAllowed("admin")
    public ResponseEntity<List<Marca>> buscarTodos() {

        var bebidasMarcas = marcaServico.buscarTodos();

        return ResponseEntity.ok(bebidasMarcas);
    }
}
