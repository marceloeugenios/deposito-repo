package br.com.desafio.deposito.servico;

import br.com.desafio.deposito.dto.request.CredencialRequest;
import br.com.desafio.deposito.dto.response.AutenticacaoResponseDto;

import java.util.Optional;

public interface AutenticacaoServico {

    Optional<AutenticacaoResponseDto> login(CredencialRequest credencialRequest);

    void logout(String refreshToken) throws Exception;
}
