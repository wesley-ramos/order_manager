package br.com.smartconsulting.ordermanager.api.stock;

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
public class StockMovementControllerTest {
	
	@Autowired
    private MockMvc mock;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @Sql(scripts = { "/scenarios/stock-movements/movement.sql" })
    @Transactional
    public void whenTheSystemReceivesAPostRequestItShouldCreateAMovement() throws Exception {
    	StockMovementWritingDTO movement = new StockMovementWritingDTO();
    	movement.setProductId(1l);
    	movement.setQuantity(3l);
    	
        mock.perform(post("/v1/stock-movements")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(movement)))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    
    @Test
    @Sql(scripts = { "/scenarios/stock-movements/movement.sql" })
    @Transactional
    public void whenTheSystemReceivesAPutRequestItShouldUpdateTheMovement() throws Exception {
    	StockMovementWritingDTO movement = new StockMovementWritingDTO();
    	movement.setProductId(1l);
    	movement.setQuantity(20l);
        
        mock.perform(put("/v1/stock-movements/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(movement)))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    
    @Test
    @Sql(scripts = { "/scenarios/stock-movements/movement.sql" })
    @Transactional
    public void whenTheSystemReceivesADeletetRequestItShouldDeleteTheMovement() throws Exception {
        mock.perform(delete("/v1/stock-movements/1")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    
    @Test
    @Sql(scripts = { "/scenarios/stock-movements/movement.sql" })
    @Transactional
    public void whenTheSystemReceivesAGetRequestItShouldReturnTheMovements() throws Exception {
        mock.perform(get("/v1/stock-movements")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(Matchers.equalTo(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].productId").value(Matchers.equalTo(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(Matchers.equalTo(10)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].createdAt").value(Matchers.equalTo("2023-10-20T15:21:00")))
			.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(Matchers.equalTo(2)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[1].productId").value(Matchers.equalTo(2)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(Matchers.equalTo(5)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[1].createdAt").value(Matchers.equalTo("2023-10-20T15:30:00")));
    }
    
    @Test
    @Sql(scripts = { "/scenarios/stock-movements/movement.sql" })
    @Transactional
    public void whenTheSystemReceivesAGetRequestWithTheIdItShouldReturnASpecificMovement() throws Exception {
        mock.perform(get("/v1/stock-movements/1")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(Matchers.equalTo(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(Matchers.equalTo(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(Matchers.equalTo(10)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(Matchers.equalTo("2023-10-20T15:21:00")));
    }
}
