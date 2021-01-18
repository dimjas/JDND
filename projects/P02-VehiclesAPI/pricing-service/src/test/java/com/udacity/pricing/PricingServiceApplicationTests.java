package com.udacity.pricing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {

	}

	@Test
	public void getAllPrices() throws Exception {
		mockMvc.perform(get("/prices")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(content().json("{}"))
				.andExpect(jsonPath("$._embedded").exists())
				.andExpect(jsonPath("$._embedded.prices").isNotEmpty())
				.andExpect(jsonPath("$._embedded.prices[*].price").isNotEmpty());
	}

	@Test
	public void whenValidId_thenPriceShouldBeFound() throws Exception {
		mockMvc.perform(get("/prices/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
						.andExpect(content().json("{}"))
						.andExpect(jsonPath("$.price").value(100000.00));
	}

	@Test
	public void whenNotValidId_thenPriceShouldNotBeFound() throws Exception {
		mockMvc.perform(get("/prices/{id}", 123))
				.andExpect(status().isNotFound());
	}
}
