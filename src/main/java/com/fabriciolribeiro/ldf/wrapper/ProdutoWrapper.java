package com.fabriciolribeiro.ldf.wrapper;

import java.util.List;

import com.fabriciolribeiro.ldf.entities.Produto;

public class ProdutoWrapper {
	private List<Produto> produtos;

    /**
     * @return produtos
     */
    public List<Produto> getProdutos() {
        return produtos;
    }

    /**
     * @param produtos
     */
    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
