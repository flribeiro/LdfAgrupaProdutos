package com.fabriciolribeiro.ldf.entities;

import java.util.ArrayList;
import java.util.List;

public class Grouping {
	private String description;
	private List<Product> items;
	
	public Grouping(String description, List<Product> products) {
		this.description = description;
		this.items = products;
	}

	public Grouping() {
		this.items = new ArrayList<>();
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Product> getItems() {
		return items;
	}
	
	public void setItems(List<Product> produtos) {
		this.items = produtos;
	}
	
	public boolean addProduto(Product produto) {
		boolean added = false;
		for (Product p: items) {
			if (p.equals(produto)) {
				System.out.println("Produto já existente.");
				return added;
			}	
		}
		items.add(produto);
		return true;
	}
	
	public void clear() {
		this.description = "";
		this.items.clear();
	}
	
	@Override
	public String toString() {
		String representation = "Object Grouping with " + this.items.size() + " products: ";
		for (Product p: this.items) {
			representation += p.toString() + " || ";
		}
		return representation;
	}

}
