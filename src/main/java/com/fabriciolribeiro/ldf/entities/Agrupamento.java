package com.fabriciolribeiro.ldf.entities;

import java.util.ArrayList;

public class Agrupamento {
	private String description;
	private ArrayList<Produto> produtos;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public ArrayList<Produto> getProdutos() {
		return produtos;
	}
	
	public void setProdutos(ArrayList<Produto> produtos) {
		this.produtos = produtos;
	}
	
	public boolean addProduto(Produto produto) {
		boolean added = false;
		for (Produto p: produtos) {
			if (p.equals(produto)) {
				System.out.println("Produto já existente.");
				return added;
			}	
		}
		produtos.add(produto);
		return true;
	}
	

}
