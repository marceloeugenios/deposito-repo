package br.com.desafio.deposito.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CredencialRequest {

    @Schema(description = "Usuário para autenticacao", example = "deposito", defaultValue = "deposito")
    private String usuario;
    @Schema(description = "Senha para autenticacao", example = "12345", defaultValue = "12345")
    private String senha;

}
