package com.fabriciolribeiro.ldf.controllers;

import java.util.List;

import com.fabriciolribeiro.ldf.entities.Grouping;
import com.fabriciolribeiro.ldf.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
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
	 * @param products
	 * @return ResponseEntity<Response<ListaProdutos>>
	 */	
	@PostMapping
	public ResponseEntity<Response<List<Grouping>>> groupProducts(@RequestBody List<Product> products,
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

        // Início do código trazido do Service
        String[] filterValue = new String[2];
        if (filterD.length() > 0 && service.validateFilter(filterD))
            filterValue = filterD.toLowerCase().split(":");

        products = service.filterProducts(products, filterValue);
        LOG.info("Nova lista de Produtos após ignorar inválidos contém " + products.size() + " produtos: \n" + products.toString());

        Result result = new Result();

        result.join(service.groupProductsByEan(products));

        result.join(service.groupProductsByTitle(products));

        result.join(service.groupProductsByBrand(products));

        // ordenação
        String[] orderValue = new String[2];

        if (service.validateOrder(orderD)) {
            orderValue = orderD.toLowerCase().split(":");
        } else {
            orderValue[0] = "stock";
            orderValue[1] = "desc";
        }


        for (Grouping g: result.getData()) {
            service.orderProductList(g.getItems(), orderValue);
        }
        // Fim do código trazido do Service

		Response<List<Grouping>> response = new Response<List<Grouping>>();

		response.setData(result.getData());

		productsList.clear();
		return ResponseEntity.ok(response);
	}	

	
	
}
