package br.com.desafio.deposito.controller;

import static br.com.desafio.deposito.controller.util.RequestUtil.SECAO_URL;
import static br.com.desafio.deposito.controller.util.RequestUtil.gerarStringAleatoria;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.desafio.deposito.model.Secao;
import br.com.desafio.deposito.model.util.ESituacao;
import br.com.desafio.deposito.controller.util.RequestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(value = "deposito", roles = {"admin", "user"})
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
class SecaoControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RequestUtil requestUtil;

  @Test
  @DisplayName("Cadastra marca")
  void salvaMarca() {
    var secao = new Secao(gerarStringAleatoria(), ESituacao.ATIVO);

    var saveSecao = requestUtil.postSecao(secao);

    assertNotNull(saveSecao);
    assertNotNull(saveSecao.getId());
    assertEquals(secao.getNome(), saveSecao.getNome());
  }

    @Test
    void alterar() throws Exception {
        var secao = new Secao(gerarStringAleatoria(), ESituacao.ATIVO);

        var saveSecao = requestUtil.postSecao(secao);

        assertNotNull(saveSecao);
        assertNotNull(saveSecao.getId());
        assertEquals(secao.getNome(), saveSecao.getNome());

        var secaoRecuperada = requestUtil.getMarcaById(saveSecao.getId());

        secaoRecuperada.setNome(gerarStringAleatoria());

        var response = mockMvc.perform(put(SECAO_URL)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(secaoRecuperada)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var secaoAlterada = objectMapper.readValue(response, Secao.class);

        assertNotNull(secaoAlterada);
        assertNotNull(secaoAlterada.getId());
        assertEquals(secaoRecuperada.getNome(), secaoAlterada.getNome());
    }

    @Test
    void buscarTodos() throws Exception {
        var secao = new Secao(gerarStringAleatoria(), ESituacao.ATIVO);

        var saveSecao = requestUtil.postSecao(secao);

        assertNotNull(saveSecao);
        assertNotNull(saveSecao.getId());
        assertEquals(secao.getNome(), saveSecao.getNome());

        var response = mockMvc.perform(get(SECAO_URL)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var secoes = objectMapper.readValue(response, new TypeReference<List<Secao>>() {
        });

        assertNotNull(secoes);
        assertFalse(secoes.isEmpty());
    }

    @Test
    void buscarPorIdNaoExistente() throws Exception {
        requestUtil.getByIdBadRequest(SECAO_URL);
    }
}