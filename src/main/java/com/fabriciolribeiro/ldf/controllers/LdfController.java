package com.fabriciolribeiro.ldf.controllers;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
									  @RequestParam(value="filter", required=false) String filter, 
									  @RequestParam(value="order_by", required=false) String order_by) {
		
		LOG.info("Submetendo produtos para agrupamento.");
		
		for (Product product: products)
			LOG.info(product.toString());
		
		productsList.addAll(products);
		
		String filterD = "";
		String orderD = "";
		
		if (filter != null && filter.length() > 0) {
			filterD = filter;
		}
		
		if (order_by != null && order_by.length() > 0) {
			orderD = order_by;
		}
				
		Result produtosProcessados = service.masterGrouping(productsList, filterD, orderD);
		productsList.clear();

		return produtosProcessados;
	}	

	
	
}
