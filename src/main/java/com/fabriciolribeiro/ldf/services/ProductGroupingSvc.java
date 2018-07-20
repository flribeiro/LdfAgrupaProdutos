package com.fabriciolribeiro.ldf.services;

import java.math.BigDecimal;
import java.util.ArrayList;
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
		
		
		String[] filterValue = new String[2];
		if (filter.length() > 0 && validateFilter(filter)) 
			filterValue = filter.toLowerCase().split(":");

		products = filterProducts(products, filterValue);
		LOG.info("Nova lista de Produtos após ignorar inválidos contém " + products.size() + " produtos: \n" + products.toString());
		
		Result result = new Result();
		
		result.join(groupProductsByEan(products));
		
		result.join(groupProductsByTitle(products));

		result.join(groupProductsByBrand(products));

		// ordenação
		String[] orderValue = new String[2];
		
		if (validateOrder(order)) {
			orderValue = order.toLowerCase().split(":");
		} else {
			orderValue[0] = "stock";
			orderValue[1] = "desc";
		}
		
				
		for (Grouping g: result.getData()) {
			orderProductList(g.getItems(), orderValue);
		}
		
		products.clear();
		
		return result;
	}
	
	private List<Product> filterProducts(List<Product> products, String[] filterValue) {
		
		List<Product> validProducts = new ArrayList<>();
		for (Product p: products) {
			if (p.getTitle().toLowerCase().contains(p.getBrand().toLowerCase())) {
				validProducts.add(p);
			}
		}
		
		LOG.info(validProducts.size() + " produtos após ignorar inválidos: " + validProducts.toString());
		
		String field = filterValue[0];
		String value = filterValue[1];
		
		
		if (field != null && !field.isEmpty()) {
			List<Product> filteredProducts = new ArrayList<>();
			
			switch (field) {
				case "id":
					filteredProducts = validProducts.stream().filter(prod -> prod.getId()
							.equals(value)).collect(Collectors.toList());
					break;
				case "ean":
					 filteredProducts = validProducts.stream().filter(prod -> prod.getEan()
								.equals(value)).collect(Collectors.toList());
					 break;
				case "title":
					filteredProducts = validProducts.stream().filter(prod -> prod.getTitle()
							.equals(value)).collect(Collectors.toList());
					break;
				case "brand":
					filteredProducts = validProducts.stream().filter(prod -> prod.getBrand()
							.equals(value)).collect(Collectors.toList());
					break;
				case "price":
					value.replaceAll(",","");
					BigDecimal bdPrice = new BigDecimal(value);
					filteredProducts = validProducts.stream().filter(prod -> prod.getPrice()
							.equals(bdPrice)).collect(Collectors.toList());
					break;
				case "stock":
					filteredProducts = validProducts.stream().filter(prod -> Integer.toString(prod.getStock())
							.equals(value)).collect(Collectors.toList());
					break;
				default:
					break;
			}
			
			LOG.info(filteredProducts.size() + " produtos após filtragem.");
			
			if (!filteredProducts.isEmpty())
				return filteredProducts;
		}
		
		return validProducts;
	}
	
	private Boolean validateField(String field) {
		String[] fields = {"id", "ean", "title", "brand", "price", "stock"};
		
		if (Arrays.asList(fields).contains(field))
			return true;
		
		return false;
	}
	
	private Boolean validateDirection(String direction) {
		if (direction.equals("asc") || direction.equals("desc")) 
			return true;
		
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
		if (order.contains(":")) 
			sOrder = order.split(":");
		else
			return false;
		
		if (validateField(sOrder[0]) && validateDirection(sOrder[1]))
			return true;
		
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
		for (String ean: groupingByEanMap.keySet()) {
			if (groupingByEanMap.get(ean).size() > 1) {
				groupingByEan.setDescription(ean);
				groupingByEan.setItems(groupingByEanMap.get(ean));
				
			}
			
		}
		
		// Eliminando marcas diferentes para mesmo EAN.
		groupingByEanMap.clear();
		groupingByEanMap = groupingByEan.getItems().stream().collect(Collectors.groupingBy(Product::getBrand));
		groupingByEan.clear();
		
		for (String brand: groupingByEanMap.keySet()) {
			if (groupingByEanMap.get(brand).size() > 1) {
				groupingByEan.setDescription(groupingByEanMap.get(brand).get(0).getEan());
			}
		}
		
		if (groupingByEan.getItems().size() > 0)
			result.addGrouping(groupingByEan);
		
		return result;		
	}
	
	
	/**
	 * Retorna produtos similares de acordo com título.
	 * 
	 *  @param listProducts
	 *  @return Result
	 */
	private Result groupProductsByTitle(List<Product> products) {
		LOG.info("Agrupando produtos por título.");
		
		Result result = new Result();
		Grouping groupingByTitle = new Grouping();
		Map<String, List<Product>> groupingByTitleMap = new HashMap<String, List<Product>>();
		List<Product> productsByTitle = new ArrayList<>();
		
		
		for (int i = 0; i < products.size(); i++) {
			String[] p0Title = products.get(i).getTitle().toLowerCase().split(" ");
			productsByTitle.add(products.get(i));
			
			for (int j = i+1; j < products.size(); j++) {
				String[] p1Title = products.get(j).getTitle().toLowerCase().split(" ");
				if (compareTitles(p0Title, p1Title))
					productsByTitle.add(products.get(j));
			}
			if (productsByTitle.size() > 1) {
				groupingByTitleMap.put(products.get(i).getTitle(), new ArrayList<Product>(productsByTitle));
			}
			productsByTitle.clear();
			
		}
		
		for (String title: groupingByTitleMap.keySet()) {
			groupingByTitle.setDescription(title);
			groupingByTitle.setItems(groupingByTitleMap.get(title));
			result.addGrouping(new Grouping(groupingByTitle.getDescription(), groupingByTitle.getItems()));
		}
		
		return result;
	}
	
	/**
	 * Encontra a quantidade de palavras em comum entre os dois arrays de string e retorna true se obedecer ao critério de similaridade,
	 * ou seja, 2 ou mais palavras com mais de 2 letras coincidentes.
	 * 
	 *  @params p0, p1
	 *  @return Boolean 
	 */
	private Boolean compareTitles(String[] p0, String[] p1) {
		// encontrar quantidade de palavras em comum entre os dois arrays de string
	    Arrays.sort(p0);
	    Arrays.sort(p1);
	 
	    ArrayList<String> list = new ArrayList<String>();
	    for(int i=0; i<p0.length; i++){
	        if(i==0 || (i>0 && p0[i]!=p0[i-1])){
	            if(Arrays.binarySearch(p1, p0[i])>-1){
	            	if(p0[i].length() > 2)
	            		list.add(p0[i]);
	            }
	        }
	    }
	 
	    
	    if (list.size() > 2)
	    	return true;
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
		for (String brand: groupingByBrandMap.keySet()) {
			if (groupingByBrandMap.get(brand).size() > 1) {
				groupingByBrand.setDescription(brand);
				groupingByBrand.setItems(groupingByBrandMap.get(brand));
				result.addGrouping(groupingByBrand);
			}
		}
		
		return result;
	}
	
	/**
	 * Ordena lista de produtos segundo critério padrão, ou critério definido pelo cliente.
	 * @param products
	 * @param orderValue
	 */
	private void orderProductList(List<Product> products, String[] orderValue) {
		String field = orderValue[0];
		String direction = orderValue[1];
		Comparator<Product> comparator = (p1, p2)->p1.getStock()-p2.getStock();
		
		LOG.info("Ordenando por " + field + " em ordem " + direction + ".");
		
		switch (field) {
			case "id":
				LOG.info("Entrou no switch de ordenação por Id.");
				comparator = (p1, p2)->p1.getId().compareTo(p2.getId());
				break;
			case "ean":
				comparator = (p1, p2)->p1.getEan().compareTo(p2.getEan());
				break;
			case "title":
				comparator = (p1, p2)->p1.getTitle().compareTo(p2.getTitle());
				break;
			case "brand":
				comparator = (p1, p2)->p1.getBrand().compareTo(p2.getBrand());
				break;
			case "price":
				comparator = (p1, p2)->p1.getPrice().compareTo(p2.getPrice());
				break;
			default:
				break;
		}
		
		if (direction.equals("desc")) {
			products.sort(comparator.reversed());
			
		} else {
			products.sort(comparator);
		}
		
	}

	
	
}
