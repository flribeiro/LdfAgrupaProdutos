package com.fabriciolribeiro.ldf.controllers;

import java.util.List;
import java.util.Map;
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

import com.fabriciolribeiro.ldf.entities.Produto;
import com.fabriciolribeiro.ldf.services.ListaProdutosSvc;

@RestController
@RequestMapping(value="/agrupamento")
public class LdfController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LdfController.class);
	
	@Autowired
	private ListaProdutosSvc listaProdutosSvc;
	
	@Autowired
	@Qualifier("produtos")
	private List<Produto> listaProdutos;
	
	
	/**
	 * Submete um grupo de produtos para serem processados pelo serviço.
	 * 
	 * @param listaProdutos
	 * @param result
	 * @return ResponseEntity<Response<ListaProdutos>>
	 */	
	@PostMapping
	public Map<String, List<Produto>> groupProducts(@RequestBody List<Produto> produtos,
									  @PathVariable Optional<String> filter, 
									  @PathVariable Optional<String> order_by) {
		
		LOG.info("Submetendo produtos para agrupamento.");
		
		for (Produto produto: produtos)
			LOG.info(produto.toString());
		
		listaProdutos.addAll(produtos);
		
		String filterD = "";
		String orderD = "";
		
		if (filter.isPresent()) {
			filterD = filter.toString();
		}
		
		if (order_by.isPresent()) {
			orderD = order_by.toString();
		}
		
		Map<String, List<Produto>> mapProdutosProcessados = listaProdutosSvc.agrupaProdutosMaster(listaProdutos, filterD, orderD);
		

		return mapProdutosProcessados;
	}	

	
	
}
