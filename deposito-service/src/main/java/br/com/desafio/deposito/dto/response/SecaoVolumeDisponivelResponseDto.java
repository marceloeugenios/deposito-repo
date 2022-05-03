package br.com.desafio.deposito.dto.response;

import br.com.desafio.deposito.model.Secao;
import br.com.desafio.deposito.model.util.EBebidaTipo;
import br.com.desafio.deposito.util.FormatterUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Schema(name = "SecaoVolumeDisponivelExtrato")
public class SecaoVolumeDisponivelResponseDto {

    @Schema(description = "Identificador da Secao", defaultValue = "0", example = "1")
    private Integer id;
    @Schema(description = "Nome da Secao", defaultValue = "Secao de Bebidas em Garrafa",
            example = "Secao de Bebidas em Garrafa")
    private String nome;
    @JsonInclude(NON_EMPTY)
    @Schema(description = "Tipo da Bebida", defaultValue = "ALCOOLICA", example = "ALCOOLICA")
    private EBebidaTipo bebidaTipo;
    @JsonInclude(NON_EMPTY)
    @Schema(description = "Volume atual", defaultValue = "80", example = "80")
    private Float volumeAtual;
    @JsonInclude(NON_EMPTY)
    @Schema(description = "Volume disponivel", defaultValue = "320", example = "320")
    private Float volumeDisponivel;

    public SecaoVolumeDisponivelResponseDto(BebidaSecaoExtratoResponseDto bebidaSecaoExtratoResponseDto) {
        this.id = bebidaSecaoExtratoResponseDto.getIdSecao();
        this.nome = bebidaSecaoExtratoResponseDto.getNomeSecao();
        if (bebidaSecaoExtratoResponseDto.getVolumeAtual() > 0) {
            this.volumeAtual = FormatterUtil.formatDecimal(bebidaSecaoExtratoResponseDto.getVolumeAtual());
            this.bebidaTipo = bebidaSecaoExtratoResponseDto.getBebidaTipo();
            this.volumeDisponivel = FormatterUtil.formatDecimal(bebidaSecaoExtratoResponseDto.getBebidaTipo().getVolumeMaximo() - volumeAtual);
        }
    }

    public SecaoVolumeDisponivelResponseDto(Secao secao) {
        this.id = secao.getId();
        this.nome = secao.getNome();
    }
}