package br.com.desafio.deposito.controller;

import br.com.desafio.deposito.dto.response.BebidaTipoExtratoResponseDto;
import br.com.desafio.deposito.model.Secao;
import br.com.desafio.deposito.controller.util.RequestUtil;
import br.com.desafio.deposito.dto.request.MovimentacaoRequestDto;
import br.com.desafio.deposito.dto.response.BebidaSecaoExtratoResponseDto;
import br.com.desafio.deposito.dto.response.SecaoVolumeDisponivelResponseDto;
import br.com.desafio.deposito.error.ErrorResponseDto;
import br.com.desafio.deposito.model.Bebida;
import br.com.desafio.deposito.model.util.ETipoMovimentacao;
import br.com.desafio.deposito.search.SearchCriteria;
import br.com.desafio.deposito.search.SearchQuery;
import br.com.desafio.deposito.search.SortQuery;
import br.com.desafio.deposito.search.util.SortedBy;
import br.com.desafio.deposito.servico.MovimentacaoServico;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;

import static br.com.desafio.deposito.controller.util.RequestUtil.MOVIMENTACAO_URL;
import static br.com.desafio.deposito.model.util.EBebidaTipo.ALCOOLICA;
import static br.com.desafio.deposito.model.util.ETipoMovimentacao.ENTRADA;
import static br.com.desafio.deposito.model.util.ETipoMovimentacao.SAIDA;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(value = "deposito", roles = {"admin", "user"})
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
class MovimentacaoControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private MovimentacaoServico movimentacaoServico;

    private Float volumeAlcoolicoEntrada;
    private Float volumeAlcoolicoSaida;
    private Float volumeAlcoolicoEsperado;

    private Float volumeNaoAlcoolicoEntrada;
    private Float volumeNaoAlcoolicoSaida;
    private Float volumeNaoAlcoolicoEsperado;

    private Bebida bebidaAlcoolica;
    private Secao secaoAlcoolica;

    private Bebida bebidaNaoAlcoolica;
    private Secao secaoNaoAlcoolica;

    @BeforeEach
    public void setup() {
        /*
            Cleanup antes de executar cada teste para nao sujar cada um deles com dados do outro
         */
        movimentacaoServico.deleteAll();

        volumeAlcoolicoEntrada = 100F;
        volumeAlcoolicoSaida = 20F;
        volumeAlcoolicoEsperado = volumeAlcoolicoEntrada - volumeAlcoolicoSaida;

        bebidaAlcoolica = requestUtil.getBebidaById(1);
        secaoAlcoolica = requestUtil.getSecaoById(1);

        volumeNaoAlcoolicoEntrada = 240F;
        volumeNaoAlcoolicoSaida = 212F;
        volumeNaoAlcoolicoEsperado = volumeNaoAlcoolicoEntrada - volumeNaoAlcoolicoSaida;

        bebidaNaoAlcoolica = requestUtil.getBebidaById(4);
        secaoNaoAlcoolica = requestUtil.getSecaoById(4);
    }

    @Test
    @DisplayName("Faz movimentacao de entrada e uma de saida de volume suficiente")
    void fazMovimentacaoDeEntradaSaidaComVolumeSuficiente() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoSaida, SAIDA);

    }

    @Test
    @DisplayName("Faz movimentacao de entrada e faz uma saida de volume insuficiente")
    void fazMovimentacaoDeEntradaSaidaComVolumeInsuficiente() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);


        fazerMovimentacaoBadRequest(bebidaAlcoolica, secaoAlcoolica, bebidaAlcoolica.getBebidaTipo().getVolumeMaximo() + 1, SAIDA);

    }

    @Test
    @DisplayName("Faz movimentacao de entrada e tenta adicionar volume maior que o permitido para o tipo de bebida")
    void fazMovimentacaoDeEntradaTentaAdicionarVolumeMaiorQueMaximoPermitidoParaTipoDeBebida() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacaoBadRequest(bebidaAlcoolica, secaoAlcoolica, bebidaAlcoolica.getBebidaTipo().getVolumeMaximo() + 1, ENTRADA);

    }

    @Test
    @DisplayName("Faz movimentacao de entrada e saida, adiciona o maximo permitido e tenta adicionar mais volume")
    void fazMovimentacaoDeEntradaSaidaAdicionaMaximoPermitidoTentaAdicionarMaisVolume() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, SAIDA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, bebidaAlcoolica.getBebidaTipo().getVolumeMaximo(), ENTRADA);

        fazerMovimentacaoBadRequest(bebidaAlcoolica, secaoAlcoolica, 1F, ENTRADA);

    }

    @Test
    @DisplayName("Tenta fazer saida com sem ter feito nenhuma entrada")
    void tentaFazerSaidaComSecaoSemBebida() {

        fazerMovimentacaoBadRequest(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoSaida, SAIDA);
    }

    @Test
    @DisplayName("Faz entrada e saida no mesmo volume e tenta fazer uma nova saida sem volume em estoque")
    void fazEntradaComSaidaTentaTirarComSecaoZerada() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, SAIDA);

        fazerMovimentacaoBadRequest(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, SAIDA);

    }

    @Test
    @DisplayName("Faz entrada com volume maior que o maximo permitido para o tipo de bebida")
    void fazEntradaComVolumeMaiorQueVolumeMaximoPermitido() {

        fazerMovimentacaoBadRequest(bebidaAlcoolica, secaoAlcoolica,
                bebidaAlcoolica.getBebidaTipo().getVolumeMaximo() + 1, ENTRADA);

    }

    @Test
    @DisplayName("Faz a entrada de bebidas nao alcoolicas em secao que ja teve alcoolica no mesmo dia")
    void fazEntradaDeDoisTiposDeBebidasDiferentes() {

        fazerMovimentacao(bebidaNaoAlcoolica, secaoNaoAlcoolica, volumeNaoAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaNaoAlcoolica, secaoNaoAlcoolica, volumeNaoAlcoolicoEntrada, SAIDA);

        fazerMovimentacao(bebidaAlcoolica, secaoNaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

    }

    @Test
    @DisplayName("Faz movimentacao de bebida nao alcoolica em secao que ja teve alcoolica")
    void fazMovimentacaoDeBebidaNaoAlcoolicaDepoisDaAlcoolicaTerSidoRemovida() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoSaida, SAIDA);

        fazerMovimentacaoBadRequest(bebidaNaoAlcoolica, secaoAlcoolica, volumeNaoAlcoolicoEntrada, ENTRADA);

    }

    @Test
    @DisplayName("Faz a busca de movimentacoes sem filtro e sem ordenacao definida")
    void buscarTodasMovimentacoesSemFiltroSemOrdenacao() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoSaida, SAIDA);

        var movimentacoes = requestUtil.searchMovimentacao(new SearchCriteria());

        assertNotNull(movimentacoes);
        assertEquals(2, movimentacoes.size());

        assertEquals(1, movimentacoes.get(0).getIdSecao());
        assertEquals(volumeAlcoolicoEntrada, movimentacoes.get(0).getVolume());
        assertEquals(ENTRADA, movimentacoes.get(0).getTipoMovimentacao());

        assertEquals(1, movimentacoes.get(1).getIdSecao());
        assertEquals(volumeAlcoolicoSaida, movimentacoes.get(1).getVolume());
        assertEquals(SAIDA, movimentacoes.get(1).getTipoMovimentacao());
    }

    @Test
    @DisplayName("Faz duas entradas com cada tipo de bebida e retorna apenas do tipo Alcoolica baseada no filtro")
    void fazDuasEntradasEmSecoesDiferentesEFazBuscaApenasPorTipoAcoolico() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);
        fazerMovimentacao(bebidaNaoAlcoolica, secaoNaoAlcoolica, volumeNaoAlcoolicoEntrada, ENTRADA);

        var searchCriteria = new SearchCriteria(
                List.of(new SearchQuery("bebidaTipo", ALCOOLICA.name()),
                        new SearchQuery("nome", "Secao 1")),
                List.of(new SortQuery("nome", SortedBy.ASC))
        );

        var movimentacaoResponseDtos = requestUtil.searchMovimentacao(searchCriteria);

        assertNotNull(movimentacaoResponseDtos);
        assertEquals(1, movimentacaoResponseDtos.size());
        assertEquals(1, movimentacaoResponseDtos.get(0).getIdSecao());
        assertEquals(volumeAlcoolicoEntrada, movimentacaoResponseDtos.get(0).getVolume());
        assertEquals(ENTRADA, movimentacaoResponseDtos.get(0).getTipoMovimentacao());
    }

    @Test
    @DisplayName("Faz duas entradas pro mesmo tipo de bebida e retorna ordenado por ASC e por DESC")
    void fazDuasEntradaProMesmoTipoDeBebidaEConsultaMovimentacoesOrdenadasaPorSecaoDesc() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        var secaoAdicional = requestUtil.getSecaoById(2);

        fazerMovimentacao(bebidaAlcoolica, secaoAdicional, volumeAlcoolicoEntrada, ENTRADA);

        var searchCriteria = new SearchCriteria(List.of(new SearchQuery("bebidaTipo", ALCOOLICA.name())),
                List.of(new SortQuery("nome", SortedBy.ASC))
        );

        var movimentacaoResponseDtos = requestUtil.searchMovimentacao(searchCriteria);

        assertNotNull(movimentacaoResponseDtos);
        assertEquals(2, movimentacaoResponseDtos.size());

        assertEquals(1, movimentacaoResponseDtos.get(0).getIdSecao());
        assertEquals(volumeAlcoolicoEntrada, movimentacaoResponseDtos.get(0).getVolume());
        assertEquals(ENTRADA, movimentacaoResponseDtos.get(0).getTipoMovimentacao());

        assertEquals(2, movimentacaoResponseDtos.get(1).getIdSecao());
        assertEquals(volumeAlcoolicoEntrada, movimentacaoResponseDtos.get(1).getVolume());
        assertEquals(ENTRADA, movimentacaoResponseDtos.get(1).getTipoMovimentacao());

        searchCriteria = new SearchCriteria(List.of(new SearchQuery("bebidaTipo", ALCOOLICA.name())),
                List.of(new SortQuery("nome", SortedBy.DESC))
        );

        movimentacaoResponseDtos = requestUtil.searchMovimentacao(searchCriteria);

        assertNotNull(movimentacaoResponseDtos);
        assertEquals(2, movimentacaoResponseDtos.size());

        assertEquals(2, movimentacaoResponseDtos.get(0).getIdSecao());
        assertEquals(volumeAlcoolicoEntrada, movimentacaoResponseDtos.get(0).getVolume());
        assertEquals(ENTRADA, movimentacaoResponseDtos.get(0).getTipoMovimentacao());

        assertEquals(1, movimentacaoResponseDtos.get(1).getIdSecao());
        assertEquals(volumeAlcoolicoEntrada, movimentacaoResponseDtos.get(1).getVolume());
        assertEquals(ENTRADA, movimentacaoResponseDtos.get(1).getTipoMovimentacao());

        searchCriteria = new SearchCriteria(List.of(new SearchQuery("bebidaTipo", ALCOOLICA.name()),
                new SearchQuery("nome", secaoAdicional.getNome())),
                List.of(new SortQuery("nome", SortedBy.DESC))
        );

        movimentacaoResponseDtos = requestUtil.searchMovimentacao(searchCriteria);

        assertNotNull(movimentacaoResponseDtos);
        assertEquals(1, movimentacaoResponseDtos.size());

        assertEquals(2, movimentacaoResponseDtos.get(0).getIdSecao());
        assertEquals(volumeAlcoolicoEntrada, movimentacaoResponseDtos.get(0).getVolume());
        assertEquals(ENTRADA, movimentacaoResponseDtos.get(0).getTipoMovimentacao());

    }

    @Test
    @DisplayName("Exibe o extrato analitico de bebidas por secao")
    void buscarExtratoBebidaPorSecao() throws Exception {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        var response = mockMvc.perform(get(MOVIMENTACAO_URL + "/bebidaPorSecao"))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        var secaoExtratoResponseDtos = objectMapper.readValue(response,
                new TypeReference<List<BebidaSecaoExtratoResponseDto>>() {
                });

        assertNotNull(secaoExtratoResponseDtos);
        assertFalse(secaoExtratoResponseDtos.isEmpty());
        assertEquals(1, secaoExtratoResponseDtos.size());
        assertEquals(secaoAlcoolica.getId(), secaoExtratoResponseDtos.get(0).getIdSecao());
        assertEquals(bebidaAlcoolica.getId(), secaoExtratoResponseDtos.get(0).getIdBebida());
        assertEquals(bebidaAlcoolica.getBebidaTipo(), secaoExtratoResponseDtos.get(0).getBebidaTipo());
        assertEquals(volumeAlcoolicoEntrada, secaoExtratoResponseDtos.get(0).getVolumeAtual());
    }

    @Test
    @DisplayName("Gera uma entrada e uma saida para cada tipo de bebida e checa se o saldo atual bate com o esperado")
    void buscarExtratoBebidaPorTipoBebida() throws Exception {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoSaida, SAIDA);

        fazerMovimentacao(bebidaNaoAlcoolica, secaoNaoAlcoolica, volumeNaoAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaNaoAlcoolica, secaoNaoAlcoolica, volumeNaoAlcoolicoSaida, SAIDA);

        var response = mockMvc.perform(get(MOVIMENTACAO_URL + "/volumeEstoquePorTipoBebida"))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        var bebidaTipoExtratoResponseDtos = objectMapper.readValue(response,
                new TypeReference<List<BebidaTipoExtratoResponseDto>>() {
                });

        assertNotNull(bebidaTipoExtratoResponseDtos);
        assertFalse(bebidaTipoExtratoResponseDtos.isEmpty());

        assertEquals(2, bebidaTipoExtratoResponseDtos.size());

        assertEquals(bebidaAlcoolica.getBebidaTipo(), bebidaTipoExtratoResponseDtos.get(0).getBebidaTipo());
        assertEquals(volumeAlcoolicoEsperado, bebidaTipoExtratoResponseDtos.get(0).getVolumeAtual());

        assertEquals(bebidaNaoAlcoolica.getBebidaTipo(), bebidaTipoExtratoResponseDtos.get(1).getBebidaTipo());
        assertEquals(volumeNaoAlcoolicoEsperado, bebidaTipoExtratoResponseDtos.get(1).getVolumeAtual());
    }

    @Test
    @DisplayName("Adiciona entradas e saidas Alcoolicas em duas secoes diferentes e confere o volume disponivel")
    void buscarSessaoComVolumePorBebidaTipo() throws Exception {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoSaida, SAIDA);

        var secaoAlcoolicaAdicional = requestUtil.getSecaoById(2);
        var volumeAlcoolicoEntradaAdicional = volumeAlcoolicoEntrada + new Random().nextInt(200);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolicaAdicional, volumeAlcoolicoEntradaAdicional, ENTRADA);

        var url = format("%s/secaoComVolumePorBebidaTipo/%s", MOVIMENTACAO_URL, bebidaAlcoolica.getBebidaTipo());
        var response = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        var secaoVolumeDisponivelResponseDtos = objectMapper.readValue(response,
                new TypeReference<List<SecaoVolumeDisponivelResponseDto>>() {
                });

        assertNotNull(secaoVolumeDisponivelResponseDtos);
        assertEquals(2, secaoVolumeDisponivelResponseDtos.size());

        assertEquals(bebidaAlcoolica.getBebidaTipo(), secaoVolumeDisponivelResponseDtos.get(0).getBebidaTipo());
        assertEquals(secaoAlcoolica.getId(), secaoVolumeDisponivelResponseDtos.get(0).getId());
        assertEquals(volumeAlcoolicoEsperado, secaoVolumeDisponivelResponseDtos.get(0).getVolumeAtual());
        assertEquals(bebidaAlcoolica.getBebidaTipo().getVolumeMaximo() - volumeAlcoolicoEsperado,
                secaoVolumeDisponivelResponseDtos.get(0).getVolumeDisponivel());

        assertEquals(bebidaAlcoolica.getBebidaTipo(), secaoVolumeDisponivelResponseDtos.get(1).getBebidaTipo());
        assertEquals(secaoAlcoolicaAdicional.getId(), secaoVolumeDisponivelResponseDtos.get(1).getId());
        assertEquals(volumeAlcoolicoEntradaAdicional, secaoVolumeDisponivelResponseDtos.get(1).getVolumeAtual());
        assertEquals(bebidaAlcoolica.getBebidaTipo().getVolumeMaximo() - volumeAlcoolicoEntradaAdicional,
                secaoVolumeDisponivelResponseDtos.get(1).getVolumeDisponivel());
    }

    @Test
    @DisplayName("Faz entrada e remove tudo de uma secao com bebidas alcoolidas e tenta adicionar nao alcoolicas")
    void adicionaBebidaAlcoolicaNaSecaoRemoveTentaAdicionarNaoAlcoolica() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, SAIDA);

        fazerMovimentacaoBadRequest(bebidaNaoAlcoolica, secaoAlcoolica, volumeNaoAlcoolicoEntrada, ENTRADA);

    }

    @Test
    @DisplayName("Faz entrada e remove tudo de uma secao com bebidas nao alcoolidas e adiciona alcoolicas")
    void adicionaBebidaNaoAlcoolicaNaSecaoRemoveAdicionaAlcoolica() {

        fazerMovimentacao(bebidaNaoAlcoolica, secaoNaoAlcoolica, volumeNaoAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaNaoAlcoolica, secaoNaoAlcoolica, volumeNaoAlcoolicoEntrada, SAIDA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

    }

    @Test
    @DisplayName("Faz uma entrada e uma saida em quantidades baixadas na mesma secao para a bebida alcoolica " +
            "e verifica se todas as secoes estao disponiveis")
    void fazUmaEntradaSaidaDoMesmoTipoDeBebidaNaMesmaSecaoComCapacidadeParaReceberNovoVolumeDeBebidas() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoSaida, SAIDA);

        var secaoVolumeDisponivelResponseDtos = requestUtil
                .verificarSecaoDisponivelParaVolumeDesejado(bebidaAlcoolica, 10F);

        assertNotNull(secaoVolumeDisponivelResponseDtos);
        assertEquals(5, secaoVolumeDisponivelResponseDtos.size());

        assertEquals(1, secaoVolumeDisponivelResponseDtos.get(0).getId());
        assertEquals(2, secaoVolumeDisponivelResponseDtos.get(1).getId());
        assertEquals(3, secaoVolumeDisponivelResponseDtos.get(2).getId());
        assertEquals(4, secaoVolumeDisponivelResponseDtos.get(3).getId());
        assertEquals(5, secaoVolumeDisponivelResponseDtos.get(4).getId());
    }

    @Test
    @DisplayName("Faz duas entradas para secoes e tipos de bebidas diferentes e verifica " +
            "se a respectiva secao nao aparece para o tipo de bebida invalido para ela para um volume de bebidas baixo")
    void fazDuasEntradasParaSecaoDiferenteComCapacidadeParaReceberNovoVolumeDeBebidasBaixo() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaNaoAlcoolica, secaoNaoAlcoolica, volumeNaoAlcoolicoEntrada, ENTRADA);

        var secaoVolumeDisponivelResponseDtos = requestUtil
                .verificarSecaoDisponivelParaVolumeDesejado(bebidaAlcoolica, 10F);

        assertNotNull(secaoVolumeDisponivelResponseDtos);
        assertEquals(4, secaoVolumeDisponivelResponseDtos.size());

        assertEquals(1, secaoVolumeDisponivelResponseDtos.get(0).getId());
        assertEquals(2, secaoVolumeDisponivelResponseDtos.get(1).getId());
        assertEquals(3, secaoVolumeDisponivelResponseDtos.get(2).getId());
        assertEquals(5, secaoVolumeDisponivelResponseDtos.get(3).getId());
    }

    @Test
    @DisplayName("Faz duas entradas para secoes e tipos de bebidas diferentes e verifica " +
            "se a respectiva secao nao aparece para o tipo de bebida invalido para ela para um volume de bebidas alto")
    void fazDuasEntradasParaSecaoDiferenteComCapacidadeParaReceberNovoVolumeDeBebidasAlto() {

        fazerMovimentacao(bebidaAlcoolica, secaoAlcoolica, volumeAlcoolicoEntrada, ENTRADA);

        fazerMovimentacao(bebidaNaoAlcoolica, secaoNaoAlcoolica, volumeNaoAlcoolicoEntrada, ENTRADA);

        var secaoVolumeDisponivelResponseDtos = requestUtil
                .verificarSecaoDisponivelParaVolumeDesejado(bebidaAlcoolica,
                        bebidaAlcoolica.getBebidaTipo().getVolumeMaximo());

        assertNotNull(secaoVolumeDisponivelResponseDtos);
        assertEquals(3, secaoVolumeDisponivelResponseDtos.size());

        assertEquals(2, secaoVolumeDisponivelResponseDtos.get(0).getId());
        assertEquals(3, secaoVolumeDisponivelResponseDtos.get(1).getId());
        assertEquals(5, secaoVolumeDisponivelResponseDtos.get(2).getId());
    }

    private void fazerMovimentacao(Bebida bebida,
                                   Secao secao,
                                   Float volume,
                                   ETipoMovimentacao tipoMovimentacao) {
        var movimentacaoDto = MovimentacaoRequestDto.builder()
                .bebida(bebida.getId())
                .secao(secao.getId())
                .volume(volume)
                .tipoMovimentacao(tipoMovimentacao)
                .build();

        var movimentacao = requestUtil.postMovimentacao(movimentacaoDto);

        assertNotNull(movimentacao);
        assertNotNull(movimentacao.getId());
        assertNotNull(movimentacao.getDataMovimentacao());
        assertNotNull(movimentacao.getUsuarioId());
        assertNotNull(movimentacao.getTipoMovimentacao());
    }

    private ErrorResponseDto fazerMovimentacaoBadRequest(Bebida bebida,
                                                         Secao secao,
                                                         Float volume,
                                                         ETipoMovimentacao tipoMovimentacao) {
        var movimentacaoDto = MovimentacaoRequestDto.builder()
                .bebida(bebida.getId())
                .secao(secao.getId())
                .volume(volume)
                .tipoMovimentacao(tipoMovimentacao)
                .build();

        return requestUtil.postMovimentacaoBadRequest(movimentacaoDto);
    }
}