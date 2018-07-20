package com.fabriciolribeiro.ldf.entities;

import java.math.BigDecimal;

public class Product {
	private String id;
	private String ean;
	private String title;
	private String brand;
	private BigDecimal price;
	private int stock;
	
	public Product() {
		
	}
	
    public Product(String id, String ean, String title, String brand, BigDecimal price, int stock) {
        this.id = id;
        this.ean = ean;
        this.title = title;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	@Override
	public String toString() {
		return "Produto [id = " + id + ", ean = " + ean + ", title = " + title + ", brand = " +
				brand + ", price = " + price + ", stock = " + stock + "]";
	}
}

