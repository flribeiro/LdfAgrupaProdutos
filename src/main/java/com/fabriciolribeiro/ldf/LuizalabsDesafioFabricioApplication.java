package com.fabriciolribeiro.ldf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fabriciolribeiro.ldf.entities.Product;

@SpringBootApplication
public class LuizalabsDesafioFabricioApplication {
	
	private List<Product> produtos;

	public static void main(String[] args) {
		SpringApplication.run(LuizalabsDesafioFabricioApplication.class, args);
	}
	
	@Bean("produtos")
	public List<Product> getProdutos() {
		produtos = new ArrayList<>();
		return produtos;
	}
}
