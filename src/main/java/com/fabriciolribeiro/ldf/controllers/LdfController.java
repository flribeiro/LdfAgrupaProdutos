package com.fabriciolribeiro.ldf.controllers;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabriciolribeiro.ldf.entities.Product;
import com.fabriciolribeiro.ldf.entities.Result;
import com.fabriciolribeiro.ldf.services.ProductGroupingSvc;

@RestController
@RequestMapping(value="/grouping")
public class LdfController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LdfController.class);
	
	@Autowired
	private ProductGroupingSvc service;
	
	@Autowired
	@Qualifier("products")
	private List<Product> productsList;
	
	
	/**
	 * Submete um grupo de produtos para serem processados pelo serviço.
	 * 
	 * @param listaProdutos
	 * @param result
	 * @return ResponseEntity<Response<ListaProdutos>>
	 */	
	@PostMapping
	public Result groupProducts(@RequestBody List<Product> products,
									  @PathVariable Optional<String> filter, 
									  @PathVariable Optional<String> order_by) {
		
		LOG.info("Submetendo produtos para agrupamento.");
		
		for (Product product: products)
			LOG.info(product.toString());
		
		productsList.addAll(products);
		
		String filterD = "";
		String orderD = "";
		
		if (filter.isPresent()) {
			filterD = filter.toString();
		}
		
		if (order_by.isPresent()) {
			orderD = order_by.toString();
		}
		
		Result mapProdutosProcessados = service.masterGrouping(productsList, filterD, orderD);
		

		return mapProdutosProcessados;
	}	

	
	
}
