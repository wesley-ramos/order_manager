package br.com.smartconsulting.ordermanager.api.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.smartconsulting.ordermanager.ApplicationStarter;
import br.com.smartconsulting.ordermanager.api.IntegrationTest;

@SpringBootTest(classes = ApplicationStarter.class)
@AutoConfigureMockMvc
@IntegrationTest
public class ProductControllerTest {
	
	@Autowired
    private MockMvc mock;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @Transactional
    public void whenTheSystemReceivesAPostRequestItShouldCreateTheProduct() throws Exception {
    	ProductWritingDTO product = new ProductWritingDTO();
    	product.setName("Coca cola 2L");
    	
        mock.perform(post("/v1/products")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(product)))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    
    @Test
    @Sql(scripts = { "/scenarios/product/product.sql" })
    @Transactional
    public void whenTheSystemReceivesAPutRequestItShouldUpdateTheProduct() throws Exception {
    	ProductWritingDTO product = new ProductWritingDTO();
    	product.setName("Coca cola 1.5L");
        
        mock.perform(put("/v1/products/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(product)))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    
    @Test
    @Sql(scripts = { "/scenarios/product/product.sql" })
    @Transactional
    public void whenTheSystemReceivesADeletetRequestItShouldDeleteTheProduct() throws Exception {
        mock.perform(delete("/v1/products/1")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    
    @Test
    @Sql(scripts = { "/scenarios/product/product.sql" })
    @Transactional
    public void whenTheSystemReceivesAGetRequestItShouldReturnTheProducts() throws Exception {
        mock.perform(get("/v1/products")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(Matchers.equalTo(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(Matchers.equalTo("Coca cola 2L")))
			.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(Matchers.equalTo(2)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(Matchers.equalTo("Fanta uva 2L")));
    }
    
    @Test
    @Sql(scripts = { "/scenarios/product/product.sql" })
    @Transactional
    public void whenTheSystemReceivesAGetRequestWithTheIdItShouldReturnASpecificProduct() throws Exception {
        mock.perform(get("/v1/products/1")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(Matchers.equalTo(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.equalTo("Coca cola 2L")));
    }
}
