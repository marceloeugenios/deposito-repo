package br.com.desafio.deposito.dto.response;

import br.com.desafio.deposito.model.util.EBebidaTipo;
import br.com.desafio.deposito.util.FormatterUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Schema(name = "BebidaTipoExtrato")
public class BebidaTipoExtratoResponseDto {

    private final EBebidaTipo bebidaTipo;
    private final Float volumeAtual;

    public BebidaTipoExtratoResponseDto(EBebidaTipo bebidaTipo, Float volumeAtual) {
        this.bebidaTipo = bebidaTipo;
        this.volumeAtual = FormatterUtil.formatDecimal(volumeAtual);
    }
}