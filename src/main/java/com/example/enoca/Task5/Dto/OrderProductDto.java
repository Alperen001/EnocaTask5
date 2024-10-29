package com.example.enoca.Task5.Dto;

public class OrderProductDto {
	
	private Long productId;
	private String productName;
	private int quantity;
	private double priceAtOrderTime;
	


	public OrderProductDto(Long productId, String productName, int quantity, double priceAtOrderTime) {
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.priceAtOrderTime = priceAtOrderTime;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPriceAtOrderTime() {
		return priceAtOrderTime;
	}

	public void setPriceAtOrderTime(double priceAtOrderTime) {
		this.priceAtOrderTime = priceAtOrderTime;
	}


}
