package br.com.desafio.deposito.dto.response;

import br.com.desafio.deposito.model.util.EBebidaTipo;
import br.com.desafio.deposito.util.FormatterUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Schema(name = "BebidaSecaoExtrato")
public class BebidaSecaoExtratoResponseDto {

    private final Integer idSecao;
    private final String nomeSecao;

    private final Integer idBebida;
    private final String nomeBebida;
    private final EBebidaTipo bebidaTipo;

    private final Float volumeAtual;

    public BebidaSecaoExtratoResponseDto(Integer idSecao, String nomeSecao,
                                         Integer idBebida, String nomeBebida,
                                         EBebidaTipo bebidaTipo,
                                         Float volumeAtual) {
        this.idSecao = idSecao;
        this.nomeSecao = nomeSecao;
        this.idBebida = idBebida;
        this.nomeBebida = nomeBebida;
        this.bebidaTipo = bebidaTipo;
        this.volumeAtual = FormatterUtil.formatDecimal(volumeAtual);
    }
}