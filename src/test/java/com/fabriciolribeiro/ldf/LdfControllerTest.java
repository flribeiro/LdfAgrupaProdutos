package com.fabriciolribeiro.ldf;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fabriciolribeiro.ldf.entities.Product;
import com.fabriciolribeiro.ldf.services.ProductGroupingSvc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LdfControllerTest {
	
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ProductGroupingSvc service;
	
	private static final String API_GROUPING_URL = "/ldf/grouping";	
	private static final String FILTER = "?filter=ean:7898100848355";
	private static final String ORDER = "?order_by=id:asc";
	
	@Test
	public void testSimpleGroupingStandardOrdenation() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(API_GROUPING_URL)
				.content(this.obtainJsonPostRequest())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	private String obtainJsonPostRequest() throws JsonProcessingException {
		Product p1 = new Product("123", "7898100848355", "Cruzador espacial Nikana - 3000m - sem garantia", "nikana", BigDecimal.valueOf(820900.90d), 1);
		Product p2 = new Product("u7042", "7898054800492", "Espada de fótons Nikana Azul", "nikana", BigDecimal.valueOf(2199.90d), 82);
		Product p3 = new Product("bb2r3s0", "2059251400402", "Corredor POD 3000hp Nikana", "nikana", BigDecimal.valueOf(17832.90d), 8);
		Product p4 = new Product("321", "7898100848355", "Cruzador espacial Nikana - 3000m - sem garantia", "trek", BigDecimal.valueOf(790300.90d), 0);
		Product p5 = new Product("80092", "", "Espada de Fótons REDAV Azul", "redav", BigDecimal.valueOf(1799.90d), 0);
		Product p6 = new Product("7728uu", "7898100848355", "Cruzador espacial Ekul - 3000m - sem garantia", "ekul", BigDecimal.valueOf(1300000.00d), 1);
		List<Product> input = new ArrayList<Product>() {
		      /**
			 * Lista de produtos para servir de input para os testes.
			 */
			private static final long serialVersionUID = 1L;

			{
				add(p1);
		        add(p2);
		        add(p3);
		        add(p4);
		        add(p5);
		        add(p6);
			}};
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(input);
	}
	    
}
