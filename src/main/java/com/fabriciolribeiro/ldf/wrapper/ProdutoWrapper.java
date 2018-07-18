package com.fabriciolribeiro.ldf.wrapper;

import java.util.List;

import com.fabriciolribeiro.ldf.entities.Product;

public class ProdutoWrapper {
	private List<Product> produtos;

    /**
     * @return produtos
     */
    public List<Product> getProdutos() {
        return produtos;
    }

    /**
     * @param produtos
     */
    public void setProdutos(List<Product> produtos) {
        this.produtos = produtos;
    }
}
