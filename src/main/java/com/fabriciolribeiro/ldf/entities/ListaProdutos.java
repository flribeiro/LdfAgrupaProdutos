package com.fabriciolribeiro.ldf.entities;

import java.util.ArrayList;
import java.util.List;

public class ListaProdutos {
	private ArrayList<Produto> listaProdutos;
	
	public ListaProdutos() {
	}
	
	public ListaProdutos(List<Produto> produtos) {
		this.listaProdutos = new ArrayList<Produto>(produtos);
	}
	
	public void addProduto(Produto produto) {
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
