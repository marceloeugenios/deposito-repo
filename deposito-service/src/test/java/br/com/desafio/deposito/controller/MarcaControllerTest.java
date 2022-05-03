package br.com.desafio.deposito.controller;

import static br.com.desafio.deposito.controller.util.RequestUtil.MARCA_URL;
import static br.com.desafio.deposito.controller.util.RequestUtil.gerarStringAleatoria;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.desafio.deposito.model.Marca;
import br.com.desafio.deposito.model.util.ESituacao;
import br.com.desafio.deposito.controller.util.RequestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
class MarcaControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RequestUtil requestUtil;

  @Test
  @DisplayName("Cadastra marca")
  void salvaMarca() {
    var marca = new Marca(gerarStringAleatoria(), ESituacao.ATIVO);

    var savedMarca = requestUtil.postMarca(marca);

    assertNotNull(savedMarca);
    assertNotNull(savedMarca.getId());
    assertEquals(marca.getNome(), savedMarca.getNome());
  }

    @Test
    void alterar() throws Exception {
        var marca = new Marca(gerarStringAleatoria(), ESituacao.ATIVO);

        var savedMarca = requestUtil.postMarca(marca);

        assertNotNull(savedMarca);
        assertNotNull(savedMarca.getId());
        assertEquals(marca.getNome(), savedMarca.getNome());

        var marcaRecuperada = requestUtil.getMarcaById(savedMarca.getId());

        marcaRecuperada.setNome(gerarStringAleatoria());

        var response = mockMvc.perform(put(MARCA_URL)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(marcaRecuperada)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var marcaAlterada = objectMapper.readValue(response, Marca.class);

        assertNotNull(marcaAlterada);
        assertNotNull(marcaAlterada.getId());
        assertEquals(marcaRecuperada.getNome(), marcaAlterada.getNome());
    }

    @Test
    void buscarTodos() throws Exception {
        var marca = new Marca(gerarStringAleatoria(), ESituacao.ATIVO);

        var savedMarca = requestUtil.postMarca(marca);

        assertNotNull(savedMarca);
        assertNotNull(savedMarca.getId());
        assertEquals(marca.getNome(), savedMarca.getNome());

        var response = mockMvc.perform(get(MARCA_URL)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var marcas = objectMapper.readValue(response, new TypeReference<List<Marca>>() {
        });

        assertNotNull(marcas);
        Assertions.assertFalse(marcas.isEmpty());
    }

    @Test
    void buscarPorIdNaoExistente() throws Exception {
        requestUtil.getByIdBadRequest(MARCA_URL);
    }
}