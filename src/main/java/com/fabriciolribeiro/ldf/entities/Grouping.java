package com.fabriciolribeiro.ldf.entities;

import java.util.List;

public class Grouping {
	private String description;
	private List<Product> products;
	
	public Grouping(String description, List<Product> products) {
		this.description = description;
		this.products = products;
	}

	public Grouping() {
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Product> getProdutos() {
		return products;
	}
	
	public void setProdutos(List<Product> produtos) {
		this.products = produtos;
	}
	
	public boolean addProduto(Product produto) {
		boolean added = false;
		for (Product p: products) {
			if (p.equals(produto)) {
				System.out.println("Produto já existente.");
				return added;
			}	
		}
		products.add(produto);
		return true;
	}
	

}
