package com.fabriciolribeiro.ldf.entities;

import java.util.ArrayList;
import java.util.List;

public class ProductList {
	private ArrayList<Product> listaProdutos;
	
	public ProductList() {
	}
	
	public ProductList(List<Product> produtos) {
		this.listaProdutos = new ArrayList<Product>(produtos);
	}
	
	public void addProduto(Product produto) {
		listaProdutos.add(produto);
	}
	
//	public Produto findProdutoById(String id) {
//		for (Produto produto : listaProdutos) {
//			if (produto.getId().equals(id)) {
//				return produto;
//			} else {
//				return null;
//			}
//		}
//	}

}
