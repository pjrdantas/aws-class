package com.aws.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DistanciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCalcularDistanciaEmMetrosEntreDoisPontos() throws Exception {
        String request = """
                {
                  "pontoA": {"latitude": -23.561684, "longitude": -46.655981},
                  "pontoB": {"latitude": -22.951916, "longitude": -43.210487}
                }
                """;

        mockMvc.perform(post("/distancia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distanciaMetros", closeTo(358450.9, 1.0)))
                .andExpect(jsonPath("$.mensagem").value("Distancia calculada com sucesso"));
    }

    @Test
    void deveRetornarBadRequestQuandoLatitudeNaoForEnviada() throws Exception {
        String request = """
                {
                  "pontoA": {"longitude": -46.655981},
                  "pontoB": {"latitude": -22.951916, "longitude": -43.210487}
                }
                """;

        mockMvc.perform(post("/distancia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Bad Request"))
                .andExpect(jsonPath("$.mensagem").value("A latitude deve ser informada."));
    }

    @Test
    void deveRetornarBadRequestQuandoLongitudeNaoForEnviada() throws Exception {
        String request = """
                {
                  "pontoA": {"latitude": -23.561684, "longitude": -46.655981},
                  "pontoB": {"latitude": -22.951916}
                }
                """;

        mockMvc.perform(post("/distancia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Bad Request"))
                .andExpect(jsonPath("$.mensagem").value("A longitude deve ser informada."));
    }

    @Test
    void deveRetornarBadRequestQuandoCoordenadasNaoForemEnviadas() throws Exception {
        mockMvc.perform(post("/distancia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Bad Request"))
                .andExpect(jsonPath("$.mensagem").value("As coordenadas devem ser informadas."));
    }

    @Test
    void deveRetornarBadRequestQuandoCampoVierEmBranco() throws Exception {
        String request = """
                {
                  "pontoA": {"latitude": "", "longitude": -46.655981},
                  "pontoB": {"latitude": -22.951916, "longitude": -43.210487}
                }
                """;

        mockMvc.perform(post("/distancia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Bad Request"))
                .andExpect(jsonPath("$.mensagem").value("A latitude deve ser informada."));
    }

    @Test
    void deveRetornarBadRequestQuandoJsonForInvalido() throws Exception {
        mockMvc.perform(post("/distancia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Bad Request"))
                .andExpect(jsonPath("$.mensagem", containsString("coordenadas")));
    }

    @Test
    void deveRetornarNotFoundQuandoRecursoNaoExistir() throws Exception {
        mockMvc.perform(post("/recurso-inexistente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Not Found"))
                .andExpect(jsonPath("$.mensagem").value("Recurso nao encontrado."));
    }
}
