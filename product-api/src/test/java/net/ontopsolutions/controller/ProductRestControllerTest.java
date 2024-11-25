package net.ontopsolutions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ontopsolutions.Application;
import net.ontopsolutions.product.api.model.ProblemDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRestControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired protected ObjectMapper mapper;

    @Test
    void shouldGetProductById() throws Exception {
        //arrange
        final int productId=1000;
        //act
        MvcResult result = request(get("/products/"+productId))
                .andExpect(status().isNotFound())
                .andReturn();

        //asserts
        assertThat(result.getResponse().getStatus()).isEqualTo(404);
        ProblemDetail problemDetail = mapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        assertThat(problemDetail).isNotNull()
                .extracting(ProblemDetail::getType)
                .isNotNull()
                .isEqualTo("NOT_FOUND");
    }


    @Test
    void shouldGetProductsByPage() throws Exception {
        //arrange

        //act
        MvcResult result = request(get("/products")
                .queryParam("page", "0")
                .queryParam("size", "20"))
                .andExpect(status().isOk())
                .andReturn();

        //asserts
        assertThat(result.getResponse().getStatus()).isEqualTo(200);

    }

    protected ResultActions request(MockHttpServletRequestBuilder httpMethod) throws Exception {
        return mockMvc.perform(httpMethod.contentType(APPLICATION_JSON_VALUE)).andDo(print());
    }
}
