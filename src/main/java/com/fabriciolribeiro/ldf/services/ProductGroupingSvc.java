package com.fabriciolribeiro.ldf.services;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fabriciolribeiro.ldf.entities.Product;

@Service
public class ProductGroupingSvc {

	private static final Logger LOG = LoggerFactory.getLogger(ProductGroupingSvc.class);
	
	@Autowired
	@Qualifier("products")
	private List<Product> listProducts;
	
	/**
	 * Método a ser chamado pela camada de controle para fazer o agrupoamento.
	 * 
	 * @param listProducts
	 * @return Map<String, List<Produto>>
	 */
	public Map<String, List<Product>> masterGrouping(List<Product> produtos, String filtro, String ordenacao) {
		
		if (validaFiltro(filtro)) {
			
		}
		
		if (validaOrdenacao(ordenacao)) {
			
		}
		
		
		Map<String, List<Product>> produtosPorEan = agrupaProdutosPorEan(produtos);
		
		Map<String, List<Product>> produtosPorTitle = agrupaProdutosPorTitle(produtos);
		// ordenação
		Map<String, List<Product>> produtosPorBrand = agrupaProdutosPorBrand(produtos);
		// ordenação
		
		Map<String, List<Product>> produtosAgrupadosCombinados = produtosPorEan;
		produtosAgrupadosCombinados.putAll(produtosPorTitle);
		produtosAgrupadosCombinados.putAll(produtosPorBrand);
		return produtosAgrupadosCombinados;
	}
	
	private Boolean validaCampo(String param) {
		String[] campos = {"id", "ean", "title", "brand", "price", "stock"};
		
		if (Arrays.asList(campos).contains(param)) {
			return true;
		}
		
		return false;
	}
	
	private Boolean validaFiltro(String filtro) {
		String[] sFiltro;
		if (filtro.contains(":")) {
		    sFiltro = filtro.split(":");
		} else {
			return false;
		}
		
		if (validaCampo(sFiltro[0])) {
			return true;
		} else {
		    return false; 
		}
	}
	
	private Boolean validaOrdenacao(String ordenacao) {
		String[] sOrdenacao;
		if (ordenacao.contains(":")) {
			sOrdenacao = ordenacao.split(":");
		} else {
			return false;
		}
		
		if (validaCampo(sOrdenacao[0])) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Retorna produtos similares de acordo com EAN.
	 * 
	 *  @param listProducts
	 *  @return Map<String, List<Produto>>
	 */
	private Map<String, List<Product>> agrupaProdutosPorEan(List<Product> produtos) {
		LOG.info("Agrupando produtos por EAN.");
		Map<String, List<Product>> agrupamentoPorMarca = produtos.stream().collect(Collectors.groupingBy(Product::getBrand));
		
		Map<String, List<Product>> agrupamentoPorMarcaPorEan = new HashMap<>();
		
		for (Map.Entry<String, List<Product>> entry: agrupamentoPorMarca.entrySet()) {
			agrupamentoPorMarcaPorEan = entry.getValue().stream().collect(Collectors.groupingBy(Product::getEan));
		}
		
		return agrupamentoPorMarcaPorEan;		
	}
	
	
	/**
	 * Retorna produtos similares de acordo com título.
	 * 
	 *  @param listProducts
	 *  @return Map<String, List<Produto>>
	 */
	private Map<String, List<Product>> agrupaProdutosPorTitle(List<Product> produtos) {
		LOG.info("Agrupando produtos por título.");
		return produtos.stream().collect(Collectors.groupingBy(Product::getTitle));
	}
	
	/**
	 * Retorna produtos similares de acordo com marca.
	 * 
	 *  @param listProducts
	 *  @return Map<String, List<Produto>>
	 */
	private Map<String, List<Product>> agrupaProdutosPorBrand(List<Product> produtos) {
		LOG.info("Agrupando produtos por marca.");
		return produtos.stream().collect(Collectors.groupingBy(Product::getBrand));
	}
	
	/**
	 * Ordena lista de produtos segundo critério padrão, ou critério definido pelo cliente.
	 * @param listaProdutos
	 * @param campo
	 * @param orientacao
	 */
	public void ordenaLista(List<Product> listaProdutos, String campo, String orientacao) {
		Comparator<Product> comparator = (p1, p2)->p1.getStock()-p2.getStock();
		switch (campo) {
			case "id":
				comparator = (p1, p2)->p1.getId().compareTo(p2.getId());
			case "ean":
				comparator = (p1, p2)->p1.getEan().compareTo(p2.getEan());
			case "title":
				comparator = (p1, p2)->p1.getTitle().compareTo(p2.getTitle());
			case "brand":
				comparator = (p1, p2)->p1.getBrand().compareTo(p2.getBrand());
			case "price":
				comparator = (p1, p2)->p1.getPrice().compareTo(p2.getPrice());
			default:
				break;
		}
		
		if (orientacao == "desc") {
			listaProdutos.sort(comparator.reversed());
		} else {
			listaProdutos.sort(comparator);
		}
		
	}

	
	
}
