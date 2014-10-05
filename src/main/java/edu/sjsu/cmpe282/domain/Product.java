package edu.sjsu.cmpe282.domain;

public class Product {
	int productId;
	String prodName;
	String price;
	int quantity;
	String categoryId;
	String prodDescr;
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getProdDescr() {
		return prodDescr;
	}
	public void setProdDescr(String prodDescr) {
		this.prodDescr = prodDescr;
	}
}
