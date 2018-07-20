package com.fabriciolribeiro.ldf.services;

import com.fabriciolribeiro.ldf.entities.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductGroupingSvcTest {

    @MockBean
    private List<Product> productsList;

    @Autowired
    private ProductGroupingSvc service;

    @Before
    public void setUp() throws Exception {
        // fazer
    }



}
