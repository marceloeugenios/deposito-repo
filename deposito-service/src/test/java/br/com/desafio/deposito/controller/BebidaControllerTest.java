package br.com.desafio.deposito.controller;

import static br.com.desafio.deposito.controller.util.RequestUtil.BEBIDA_URL;
import static br.com.desafio.deposito.controller.util.RequestUtil.gerarStringAleatoria;
import static br.com.desafio.deposito.model.util.EBebidaTipo.ALCOOLICA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.desafio.deposito.model.Bebida;
import br.com.desafio.deposito.model.Marca;
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
class BebidaControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RequestUtil requestUtil;

  @Test
  @DisplayName("Cadastra bebida")
  void salvaBebida() {
    var marca = requestUtil.postMarca(new Marca(gerarStringAleatoria(), ESituacao.ATIVO));
    var bebida = Bebida.builder()
        .nome(gerarStringAleatoria())
        .bebidaTipo(ALCOOLICA)
        .marca(marca)
        .situacao(ESituacao.ATIVO)
        .build();

    var postBebida = requestUtil.postBebida(bebida);

        assertNotNull(postBebida);
        assertNotNull(postBebida.getId());
        assertEquals(bebida.getNome(), postBebida.getNome());
    }

    @Test
    void alterar() throws Exception {
        var marca = requestUtil.postMarca(new Marca(gerarStringAleatoria(), ESituacao.ATIVO));
        var bebida = Bebida.builder()
                .nome(gerarStringAleatoria())
                .bebidaTipo(ALCOOLICA)
                .marca(marca)
                .situacao(ESituacao.ATIVO)
                .build();

        var savedBebida = requestUtil.postBebida(bebida);

        assertNotNull(savedBebida);
        assertNotNull(savedBebida.getId());
        assertEquals(bebida.getNome(), savedBebida.getNome());

        var bebidaRecuperada = requestUtil.getBebidaById(savedBebida.getId());

        bebidaRecuperada.setNome(gerarStringAleatoria());

        var response = mockMvc.perform(put(BEBIDA_URL)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bebidaRecuperada)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var bebidaAlterada = objectMapper.readValue(response, Bebida.class);

        assertNotNull(bebidaAlterada);
        assertNotNull(bebidaAlterada.getId());
        assertEquals(bebidaRecuperada.getNome(), bebidaAlterada.getNome());
    }

    @Test
    void buscarTodos() throws Exception {
        var marca = requestUtil.postMarca(new Marca(gerarStringAleatoria(), ESituacao.ATIVO));
        var bebida = Bebida.builder()
                .nome(gerarStringAleatoria())
                .bebidaTipo(ALCOOLICA)
                .marca(marca)
                .situacao(ESituacao.ATIVO)
                .build();

        var postBebida = requestUtil.postBebida(bebida);

        assertNotNull(postBebida);
        assertNotNull(postBebida.getId());
        assertEquals(bebida.getNome(), postBebida.getNome());

        var response = mockMvc.perform(get(BEBIDA_URL)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var bebidas = objectMapper.readValue(response, new TypeReference<List<Bebida>>() {
        });

        assertNotNull(bebidas);
        assertFalse(bebidas.isEmpty());
    }

    @Test
    void buscarPorIdNaoExistente() throws Exception {
        requestUtil.getByIdBadRequest(BEBIDA_URL);
    }
}