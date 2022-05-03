package br.com.desafio.deposito.dto.response;

import br.com.desafio.deposito.model.util.EBebidaTipo;
import br.com.desafio.deposito.util.FormatterUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(name = "SecaoExtrato")
public class SecaoExtratoDataResponseDto {

    @Schema(description = "Identificador da Secao", defaultValue = "0", example = "1")
    private final Integer id;
    @Schema(description = "Nome da Secao", defaultValue = "Secao de Bebidas em Garrafa",
            example = "Secao de Bebidas em Garrafa")
    private final String nome;
    @Schema(description = "Tipo da Bebida", defaultValue = "ALCOOLICA", example = "ALCOOLICA")
    private final EBebidaTipo bebidaTipo;
    @Schema(description = "Data Atual", defaultValue = "2022-01-20 12:15:12", example = "2022-01-20 12:15:12")
    private final LocalDate data;
    @Schema(description = "Volume atual", defaultValue = "80", example = "80")
    private final Float volumeAtual;

    public SecaoExtratoDataResponseDto(Integer id,
                                       String nome,
                                       EBebidaTipo bebidaTipo,
                                       String data,
                                       Float volumeAtual) {
        this.id = id;
        this.nome = nome;
        this.bebidaTipo = bebidaTipo;
        this.data = FormatterUtil.fromStringToLocalDate(data);
        this.volumeAtual = FormatterUtil.formatDecimal(volumeAtual);
    }
}