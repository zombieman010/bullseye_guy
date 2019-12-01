package com.nathanielpautzke.bullseyeguy.controller;

import com.nathanielpautzke.bullseyeguy.controller.Web;
import com.nathanielpautzke.bullseyeguy.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(Web.class)
public class TestWeb {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Web web;

    @Test
    public void getEndpointTest() throws Exception {
        Product product = Product.builder().name("Test").build();
        ResponseEntity<Product> response = ResponseEntity.ok(product);

        given(web.getProduct("1234")).willReturn(response);

        mockMvc.perform(get("/api/v1/products/1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
