package com.example.produtos.api.infra.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //precisa do container para rodar o controller (pq tem que rodar no servidor)
@AutoConfigureMockMvc //autoconfigura o Spring Boot Web Mockado (nos entrega um container com um Servlet)
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired //as anotações @SpringBootTest + @AutoConfigureMockMvc permitem realizar essa injeção
    private MockMvc mvc; //objeto injetado para realizar a chamada no end-point

    @Test
    void endpointLoginComUsuarioExistenteESenhaCorretaEspera200Ok() throws Exception {
        //ARRANGE
        String json = """
            {
              "email": "admin@email.com",
              "senha": "123"
            }
            """;

        //ACT
        var response = mvc.perform( //performa uma requisição
            post("/api/v1/login") //verbo POST na rota
                .content(json) //o body da requisição
                .contentType(MediaType.APPLICATION_JSON) //o header Content-type
        ).andReturn().getResponse(); //a response da requisição

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }


    @Test
    void endpointLoginComUsuarioInexistenteEspera400() throws Exception {
        //ARRANGE
        String json = """
            {
              "email": "admin",
              "senha": "123"
            }
            """;

        //ACT
        var response = mvc.perform( //performa uma requisição
            post("/api/v1/login") //verbo POST na rota
                .content(json) //o body da requisição
                .contentType(MediaType.APPLICATION_JSON) //o header Content-type
        ).andReturn().getResponse(); //a response da requisição

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void endpointLoginComUsuarioExistenteESenhaIncorretaEspera403() throws Exception {
        //ARRANGE
        String json = """
            {
              "email": "admin@email.com",
              "senha": "123456"
            }
            """;

        //ACT
        var response = mvc.perform( //performa uma requisição
            post("/api/v1/login") //verbo POST na rota
                .content(json) //o body da requisição
                .contentType(MediaType.APPLICATION_JSON) //o header Content-type
        ).andReturn().getResponse(); //a response da requisição

        //ASSERT
        Assertions.assertEquals(403, response.getStatus());
    }

    @Test
    void endpointCadastrarVerboPostComBodyIncorretoEspera400BadRequest() throws Exception {
        //ARRANGE
        String json = """
            {
              "email": "adminemail",
              "senha": "123"
            }
            """;

        //ACT
        var response = mvc.perform( //performa uma requisição
            post("/api/v1/usuarios/cadastrar") //verbo POST na rota
                .content(json) //o body da requisição
                .contentType(MediaType.APPLICATION_JSON) //o header Content-type
        ).andReturn().getResponse(); //a response da requisição

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void endpointCadastrarVerboGetEspera403Forbidden() throws Exception {
        //ARRANGE + ACT + ASSERT
        this.mvc.perform(get("/api/v1/usuarios/cadastrar"))
            .andExpect(status().isForbidden());
    }

    @Test
    void endpointCadastrarVerboPutComBodyCorretoEspera403Forbidden() throws Exception {
        //ARRANGE
        String json = """
            {
              "email": "admin@email.com",
              "senha": "123"
            }
            """;

        //ACT
        var response = mvc.perform( //performa uma requisição
            put("/api/v1/usuarios/cadastrar") //verbo PUT na rota
                .content(json) //o body da requisição
                .contentType(MediaType.APPLICATION_JSON) //o header Content-type
        ).andReturn().getResponse(); //a response da requisição

        //ASSERT
        Assertions.assertEquals(403, response.getStatus());
    }

    @Test
    void anyEndPointVerboGetQuandoNaoEstaAutenticadoEspera403Forbidden() throws Exception {
        //ARRANGE + ACT + ASSERT
        this.mvc.perform(get("/any"))
            .andExpect(status().isForbidden());
    }

    @Test
    void anyEndPointVerboPostQuandoNaoEstaAutenticadoEspera403Forbidden() throws Exception {
        //ARRANGE
        String json = """
            {
              "email": "",
              "senha": ""
            }
            """;

        //ACT + ASSERT
        this.mvc.perform(post("/any")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void anyEndPointVerboPutQuandoNaoEstaAutenticadoEspera403Forbidden() throws Exception {
        //ARRANGE
        String json = """
            {
              "email": "",
              "senha": ""
            }
            """;

        //ACT + ASSERT
        this.mvc.perform(put("/any")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void anyEndPointVerboDeleteQuandoNaoEstaAutenticadoEspera403Forbidden() throws Exception {

        //ACT + ASSERT
        this.mvc.perform(delete("/any")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }
}