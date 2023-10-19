package br.com.smartconsulting.ordermanager.api.user;

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
public class UserControllerTest {
	
	@Autowired
    private MockMvc mock;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @Transactional
    public void whenTheSystemReceivesAPostRequestItShouldCreateTheUser() throws Exception {
    	UserWritingDTO user = new UserWritingDTO();
    	user.setName("Wesley Ramos");
    	user.setEmail("wesley.ramos@gmail.com");
        
        mock.perform(post("/v1/users")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    
    @Test
    @Sql(scripts = { "/scenarios/user/user.sql" })
    @Transactional
    public void whenTheSystemReceivesAPutRequestItShouldUpdateTheUser() throws Exception {
    	UserWritingDTO user = new UserWritingDTO();
    	user.setName("Wesley Ramos");
    	user.setEmail("wesley.ramos@gmail.com");
        
        mock.perform(put("/v1/users/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    
    @Test
    @Sql(scripts = { "/scenarios/user/user.sql" })
    @Transactional
    public void whenTheSystemReceivesADeletetRequestItShouldDeleteTheUser() throws Exception {
        mock.perform(delete("/v1/users/1")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    
    @Test
    @Sql(scripts = { "/scenarios/user/user.sql" })
    @Transactional
    public void whenTheSystemReceivesAGetRequestItShouldReturnTheUsers() throws Exception {
        mock.perform(get("/v1/users")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(Matchers.equalTo(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(Matchers.equalTo("Wesley Ramos")))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(Matchers.equalTo("wesley.ramos@gmail.com")))
			.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(Matchers.equalTo(2)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(Matchers.equalTo("Werik Ramos")))
			.andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value(Matchers.equalTo("werik.ramos@gmail.com")));
    }
    
    @Test
    @Sql(scripts = { "/scenarios/user/user.sql" })
    @Transactional
    public void whenTheSystemReceivesAGetRequestWithTheIdItShouldReturnASpecificUser() throws Exception {
        mock.perform(get("/v1/users/1")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(Matchers.equalTo(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.equalTo("Wesley Ramos")))
			.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(Matchers.equalTo("wesley.ramos@gmail.com")));
    }
}
