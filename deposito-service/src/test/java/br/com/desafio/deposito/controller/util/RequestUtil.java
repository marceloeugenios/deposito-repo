package br.com.desafio.deposito.controller.util;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.desafio.deposito.dto.response.MovimentacaoResponseDto;
import br.com.desafio.deposito.dto.response.SecaoVolumeDisponivelResponseDto;
import br.com.desafio.deposito.model.Bebida;
import br.com.desafio.deposito.model.Marca;
import br.com.desafio.deposito.model.Secao;
import br.com.desafio.deposito.search.SearchCriteria;
import br.com.desafio.deposito.dto.request.MovimentacaoRequestDto;
import br.com.desafio.deposito.error.ErrorResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

@Component
@RequiredArgsConstructor
public class RequestUtil {

  public static final String BASE_URL = "/api/v1";
  public static final String MOVIMENTACAO_URL = BASE_URL + "/movimentacao";
  public static final String BEBIDA_URL = BASE_URL + "/bebida";
  public static final String SECAO_URL = BASE_URL + "/secao";
  public static final String MARCA_URL = BASE_URL + "/marca";
  private final MockMvc mockMvc;
  private final ObjectMapper objectMapper;

  public static String gerarStringAleatoria() {
    return UUID.randomUUID().toString();
  }

  public Bebida getBebidaById(Integer id) {
    var url = format("%s/%d", BEBIDA_URL, id);

    return getRequestOk(Bebida.class, url);
  }

  public Secao getSecaoById(Integer id) {
    var url = format("%s/%d", SECAO_URL, id);

    return getRequestOk(Secao.class, url);
  }

  public Marca getMarcaById(Integer id) {
    var url = format("%s/%d", MARCA_URL, id);

    return getRequestOk(Marca.class, url);
  }

  public MovimentacaoResponseDto postMovimentacao(MovimentacaoRequestDto movimentacaoRequestDto) {
    return postRequestOk(MovimentacaoResponseDto.class, movimentacaoRequestDto, MOVIMENTACAO_URL);
  }

  public Marca postMarca(Marca marca) {
    return postRequestOk(Marca.class, marca, MARCA_URL);
  }

  public Secao postSecao(Secao secao) {
    return postRequestOk(Secao.class, secao, SECAO_URL);
  }

  public Bebida postBebida(Bebida bebida) {
    return postRequestOk(Bebida.class, bebida, BEBIDA_URL);
  }

  public ErrorResponseDto postMovimentacaoBadRequest(MovimentacaoRequestDto movimentacaoRequestDto) {
    try {
      var response = mockMvc.perform(post(MOVIMENTACAO_URL)
              .contentType(APPLICATION_JSON_VALUE)
              .content(objectMapper.writeValueAsString(movimentacaoRequestDto)))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse()
          .getContentAsString();
      return objectMapper.readValue(response, ErrorResponseDto.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

  public List<SecaoVolumeDisponivelResponseDto> verificarSecaoDisponivelParaVolumeDesejado(Bebida bebida,
                                                                                           Float volume) {
    try {
      var url = format("%s/verificarSecaoDisponivelPorBebidaVolume/%d/%f", MOVIMENTACAO_URL,
          bebida.getId(), volume);

      var response = mockMvc.perform(get(url))
          .andExpect(status().isOk())
          .andReturn().getResponse()
          .getContentAsString();

      return objectMapper.readValue(response, new TypeReference<>() {
      });
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

  public List<MovimentacaoResponseDto> searchMovimentacao(SearchCriteria searchCriteria) {
    try {
      var response = mockMvc.perform(post(MOVIMENTACAO_URL + "/search")
              .contentType(APPLICATION_JSON_VALUE)
              .content(objectMapper.writeValueAsString(searchCriteria)))
          .andExpect(status().isOk())
          .andReturn().getResponse()
          .getContentAsString();
      return Collections.unmodifiableList(objectMapper.readValue(response, new TypeReference<>() {
      }));
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

  public List<Secao> getSecoes() {
    try {
      var response = mockMvc.perform(get(SECAO_URL))
          .andExpect(status().isOk())
          .andReturn().getResponse()
          .getContentAsString();
      return Collections.unmodifiableList(objectMapper.readValue(response, new TypeReference<>() {
      }));
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

  public void getByIdBadRequest(String url) throws Exception {
    url = format("%s/%d", url, new Random().nextInt(1000) + 1_0000);
    mockMvc.perform(get(url)
            .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  private <T> T postRequestOk(Class<T> clazz, Object request, String url) {
    try {
      var response = mockMvc.perform(post(url)
              .contentType(APPLICATION_JSON_VALUE)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andReturn().getResponse()
          .getContentAsString();

      return objectMapper.readValue(response, clazz);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

  private <T> T getRequestOk(Class<T> clazz, String url) {
    try {
      var response = mockMvc.perform(get(url))
          .andExpect(status().isOk()).andReturn().getResponse()
          .getContentAsString();
      return objectMapper.readValue(response, clazz);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }
}
