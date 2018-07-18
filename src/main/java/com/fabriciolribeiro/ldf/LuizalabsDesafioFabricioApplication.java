package com.fabriciolribeiro.ldf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fabriciolribeiro.ldf.entities.Product;

@SpringBootApplication
public class LuizalabsDesafioFabricioApplication {
	
	private List<Product> products;

	public static void main(String[] args) {
		SpringApplication.run(LuizalabsDesafioFabricioApplication.class, args);
	}
	
	@Bean("products")
	public List<Product> getProducts() {
		products = new ArrayList<>();
		return products;
	}
}
