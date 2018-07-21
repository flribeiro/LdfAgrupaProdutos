package com.fabriciolribeiro.ldf.services;

import com.fabriciolribeiro.ldf.entities.Grouping;
import com.fabriciolribeiro.ldf.entities.Product;
import com.fabriciolribeiro.ldf.entities.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductGroupingSvcTest {


    @Autowired
    private ProductGroupingSvc service;

    private final Product p1 = new Product("123", "7898100848355", "Cruzador espacial Nikana - 3000m - sem garantia", "nikana", BigDecimal.valueOf(820900.90d), 1);
    private final Product p2 = new Product("u7042", "7898054800492", "Espada de fótons Nikana Azul", "nikana", BigDecimal.valueOf(2199.90d), 82);
    private final Product p3 = new Product("bb2r3s0", "2059251400402", "Corredor POD 3000hp Nikana", "nikana", BigDecimal.valueOf(17832.90d), 8);
    private final Product p4 = new Product("321", "7898100848355", "Cruzador espacial Nikana - 3000m - sem garantia", "trek", BigDecimal.valueOf(790300.90d), 0);
    private final Product p5 = new Product("80092", "", "Espada de Fótons REDAV Azul", "redav", BigDecimal.valueOf(1799.90d), 0);
    private final Product p6 = new Product("7728uu", "7898100848355", "Cruzador espacial Ekul - 3000m - sem garantia", "ekul", BigDecimal.valueOf(1300000.00d), 1);
    private final Product p7 = new Product("abc123", "7898100848355", "Caneca de louça Branca Trending", "ekul", BigDecimal.valueOf(13.00d), 0);
    private final Product p8 = new Product("987", "7898100848355", "Caneca de louça Branca FlyAway", "ekul", BigDecimal.valueOf(18.00d), 15);
    private final Product p9 = new Product("112233", "7898100848355", "Caneca de louça Branca Tudor", "ekul", BigDecimal.valueOf(25.00d), 2);
    private final Product p10 = new Product("bb2r3s1", "2059251400402", "Corredor POD 4000hp Nikana", "nikana", BigDecimal.valueOf(18800.99d), 1);


    @Test
    public void testFilterProductsWithInvalidProducts() {
        List<Product> listInput = new ArrayList<>();
        listInput.add(p1);
        listInput.add(p2);
        listInput.add(p4);
        String[] filterValue = {"", ""};
        List<Product> listResult = this.service.filterProducts(listInput, filterValue);

        assertTrue(listResult.size() == 2);
    }

    @Test
    public void testFilterProductsWithFilterParameter() {
        List<Product> listInput = new ArrayList<>();
        listInput.add(p1);
        listInput.add(p2);
        String[] filterValue = {"ean", "7898100848355"};
        List<Product> listResult = this.service.filterProducts(listInput, filterValue);

        assertTrue(listResult.size() == 1 && listResult.get(0).getEan().equals("7898100848355"));
    }

    @Test
    public void testValidateFilterWithInvalidField() {
        String filter = "nome:blablabla";

        assertFalse(this.service.validateFilter(filter));
    }

    @Test
    public void testValidateFilterWithValidField() {
        String filter = "id:blebleble";

        assertTrue(this.service.validateFilter(filter));
    }

    @Test
    public void testValidateOrderWithInvalidValues() {
        String filter = "none:none";

        assertFalse(this.service.validateFilter(filter));
    }

    @Test
    public void testValidateOrderWithNullValues() {
        String filter = new String();

        assertFalse(this.service.validateFilter(filter));
    }

    @Test
    public void testValidateOrderWithValidValues() {
        String filter = "ean:789000000000";

        assertTrue(this.service.validateFilter(filter));
    }

    @Test
    public void testGroupProductsByEan() {
        List<Product> listInput = new ArrayList<>();
        listInput.add(p1);
        listInput.add(p2);
        listInput.add(p3);
        listInput.add(p4);
        listInput.add(p5);
        listInput.add(p6);
        listInput.add(p7);
        listInput.add(p8);
        listInput.add(p9);
        listInput.add(p10);

        Result output = service.groupProductsByEan(listInput);
        assertEquals(2, output.getData().size());
    }

    @Test
    public void testGroupProductsByTitle() {
        List<Product> listInput = new ArrayList<>();
        listInput.add(p1);
        listInput.add(p2);
        listInput.add(p3);
        listInput.add(p4);
        listInput.add(p5);
        listInput.add(p6);
        listInput.add(p7);
        listInput.add(p8);
        listInput.add(p9);
        Result output = service.groupProductsByTitle(listInput);
        assertEquals(4, output.getData().size());
    }

    @Test
    public void testGroupProductsByBrand() {
        List<Product> listInput = new ArrayList<>();
        listInput.add(p1);
        listInput.add(p2);
        listInput.add(p3);
        listInput.add(p4);
        listInput.add(p5);
        listInput.add(p6);
        listInput.add(p7);
        listInput.add(p8);
        listInput.add(p9);
        Result output = service.groupProductsByBrand(listInput);
        assertEquals(2, output.getData().size());
    }

    @Test
    public void testOrderProductList() {
        List<Product> listInput = new ArrayList<>();
        listInput.add(p1);
        listInput.add(p2);
        listInput.add(p3);
        listInput.add(p4);
        Grouping grouping = new Grouping("Teste", listInput);
        String[] orderValue = {"stock", "desc"};
        service.orderProductList(grouping, orderValue);
        assertEquals(p2, grouping.getItems().get(0));
    }

}
