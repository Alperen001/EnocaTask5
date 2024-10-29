package com.example.enoca.Task5.Dto;


import java.util.List;

public class OrderDto {
	private Long id;
	private String orderCode;
	private List<OrderProductDto> products;
	private double totalPrice;
	private Long customerId;
	

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public List<OrderProductDto> getProducts() {
		return products;
	}

	public void setProducts(List<OrderProductDto> products) {
		this.products = products;
	}

}
