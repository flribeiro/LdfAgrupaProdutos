package com.fabriciolribeiro.ldf.configuration;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

/**
 * Configurações básicas para a aplicação.
 * */
@Component
public class ApplicationConfiguration implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {	 
	
	/**
	 * Define nome padrão para aplicação "/ldf"
	 * */
	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		factory.setContextPath("/ldf");			
	}
	
}
