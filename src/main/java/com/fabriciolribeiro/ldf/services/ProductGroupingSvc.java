package com.fabriciolribeiro.ldf.services;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fabriciolribeiro.ldf.entities.Grouping;
import com.fabriciolribeiro.ldf.entities.Product;
import com.fabriciolribeiro.ldf.entities.Result;

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
	public Result masterGrouping(List<Product> products, String filter, String order) {
		
		if (validateFilter(filter)) {
			
		}
		
		if (validateOrder(order)) {
			
		}
		
		Result result = new Result();
		
		result.join(groupProductsByEan(products));
		
		result.join(groupProductsByTitle(products));

		result.join(groupProductsByBrand(products));
		// ordenação
		
		return result;
	}
	
	private Boolean validateField(String field) {
		String[] fields = {"id", "ean", "title", "brand", "price", "stock"};
		
		if (Arrays.asList(fields).contains(field)) {
			return true;
		}
		
		return false;
	}
	
	private Boolean validateDirection(String direction) {
		if (direction == "asc" || direction == "desc") {
			return true;
		}
		
		return false;
	}
	
	private Boolean validateFilter(String filter) {
		String[] sFilter;
		if (filter.contains(":")) {
		    sFilter = filter.split(":");
		} else {
			return false;
		}
		
		if (validateField(sFilter[0])) {
			return true;
		}
		
		return false;
	}
	
	private Boolean validateOrder(String order) {
		String[] sOrder;
		if (order.contains(":")) {
			sOrder = order.split(":");
		} else {
			return false;
		}
		
		if (validateField(sOrder[0]) && validateDirection(sOrder[1])) {
			return true;
		} 
		
		return false;
	}
	
	/**
	 * Retorna produtos similares de acordo com EAN.
	 * 
	 *  @param listProducts
	 *  @return Map<String, List<Produto>>
	 */
	private Result groupProductsByEan(List<Product> products) {
		LOG.info("Agrupando produtos por EAN.");
		
		Map<String, List<Product>> groupingByEanMap = products.stream().collect(Collectors.groupingBy(Product::getEan));
		
		Grouping groupingByEan = new Grouping();
		Result result = new Result();
		
		// Eliminar únicos
		for (Map.Entry<String, List<Product>> entry: groupingByEanMap.entrySet()) {
			if (entry.getValue().size() == 1) {
				groupingByEanMap.remove(entry.getKey());
			} else {
				groupingByEan.setDescription(entry.getKey());
				groupingByEan.setProdutos(entry.getValue());
				result.addGrouping(groupingByEan);
			}
		}
		
		return result;		
	}
	
	
	/**
	 * Retorna produtos similares de acordo com título.
	 * 
	 *  @param listProducts
	 *  @return Map<String, List<Produto>>
	 */
	private Result groupProductsByTitle(List<Product> products) {
		LOG.info("Agrupando produtos por título.");
		
		Result result = new Result();
		
		for (int i = 0; i < products.size(); i++) {
			String[] p0Title = products.get(i).getTitle().split(" ");
			String[] p1Title = products.get(i++).getTitle().split(" ");
			if (compareTitles(p0Title, p1Title)) {
				// serão agrupados
			} else {
				// testa com próximo
			}
		}
		
		return result;
	}
	
	private Boolean compareTitles(String[] p0, String[] p1) {
		// encontrar quantidade de palavras em comum entre os dois arrays de string
		Set<String> s1 = new HashSet<String>(Arrays.asList(p0));
		Set<String> s2 = new HashSet<String>(Arrays.asList(p1));
		s1.retainAll(s2);

		if (s1.size() >= 2) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Retorna produtos similares de acordo com marca.
	 * 
	 *  @param listProducts
	 *  @return Map<String, List<Produto>>
	 */
	private Result groupProductsByBrand(List<Product> products) {
		LOG.info("Agrupando produtos por marca.");
		Map<String, List<Product>> groupingByBrandMap = products.stream().collect(Collectors.groupingBy(Product::getBrand));
		
		Grouping groupingByBrand = new Grouping();
		Result result = new Result();
		
		// Eliminar únicos
		for (Map.Entry<String, List<Product>> entry: groupingByBrandMap.entrySet()) {
			if (entry.getValue().size() == 1) {
				groupingByBrandMap.remove(entry.getKey());
			} else {
				groupingByBrand.setDescription(entry.getKey());
				groupingByBrand.setProdutos(entry.getValue());
				result.addGrouping(groupingByBrand);
			}
		}
		
		return result;
	}
	
	/**
	 * Ordena lista de produtos segundo critério padrão, ou critério definido pelo cliente.
	 * @param products
	 * @param field
	 * @param direction
	 */
	public void orderProductList(List<Product> products, String field, String direction) {
		Comparator<Product> comparator = (p1, p2)->p1.getStock()-p2.getStock();
		switch (field) {
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
		
		if (direction == "desc") {
			products.sort(comparator.reversed());
		} else {
			products.sort(comparator);
		}
		
	}

	
	
}
