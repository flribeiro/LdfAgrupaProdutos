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

import com.fabriciolribeiro.ldf.entities.Produto;

@Service
public class ListaProdutosSvc {

	private static final Logger LOG = LoggerFactory.getLogger(ListaProdutosSvc.class);
	
	@Autowired
	@Qualifier("produtos")
	private List<Produto> listaProdutos;
	
	/**
	 * Método a ser chamado pela camada de controle para fazer o agrupoamento.
	 * 
	 * @param listaProdutos
	 * @return Map<String, List<Produto>>
	 */
	public Map<String, List<Produto>> agrupaProdutosMaster(List<Produto> produtos, String filtro, String ordenacao) {
		
		if (validaFiltro(filtro)) {
			
		}
		
		if (validaOrdenacao(ordenacao)) {
			
		}
		
		
		Map<String, List<Produto>> produtosPorEan = agrupaProdutosPorEan(produtos);
		
		Map<String, List<Produto>> produtosPorTitle = agrupaProdutosPorTitle(produtos);
		// ordenação
		Map<String, List<Produto>> produtosPorBrand = agrupaProdutosPorBrand(produtos);
		// ordenação
		
		Map<String, List<Produto>> produtosAgrupadosCombinados = produtosPorEan;
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
	 *  @param listaProdutos
	 *  @return Map<String, List<Produto>>
	 */
	private Map<String, List<Produto>> agrupaProdutosPorEan(List<Produto> produtos) {
		LOG.info("Agrupando produtos por EAN.");
		Map<String, List<Produto>> agrupamentoPorMarca = produtos.stream().collect(Collectors.groupingBy(Produto::getBrand));
		
		Map<String, List<Produto>> agrupamentoPorMarcaPorEan = new HashMap<>();
		
		for (Map.Entry<String, List<Produto>> entry: agrupamentoPorMarca.entrySet()) {
			agrupamentoPorMarcaPorEan = entry.getValue().stream().collect(Collectors.groupingBy(Produto::getEan));
		}
		
		return agrupamentoPorMarcaPorEan;		
	}
	
	
	/**
	 * Retorna produtos similares de acordo com título.
	 * 
	 *  @param listaProdutos
	 *  @return Map<String, List<Produto>>
	 */
	private Map<String, List<Produto>> agrupaProdutosPorTitle(List<Produto> produtos) {
		LOG.info("Agrupando produtos por título.");
		return produtos.stream().collect(Collectors.groupingBy(Produto::getTitle));
	}
	
	/**
	 * Retorna produtos similares de acordo com marca.
	 * 
	 *  @param listaProdutos
	 *  @return Map<String, List<Produto>>
	 */
	private Map<String, List<Produto>> agrupaProdutosPorBrand(List<Produto> produtos) {
		LOG.info("Agrupando produtos por marca.");
		return produtos.stream().collect(Collectors.groupingBy(Produto::getBrand));
	}
	
	/**
	 * Ordena lista de produtos segundo critério padrão, ou critério definido pelo cliente.
	 * @param listaProdutos
	 * @param campo
	 * @param orientacao
	 */
	public void ordenaLista(List<Produto> listaProdutos, String campo, String orientacao) {
		Comparator<Produto> comparator = (p1, p2)->p1.getStock()-p2.getStock();
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
