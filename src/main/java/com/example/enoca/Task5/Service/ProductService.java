package com.example.enoca.Task5.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.enoca.Task5.Dto.ProductDto;
import com.example.enoca.Task5.Model.Product;
import com.example.enoca.Task5.Repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	public Product updateProductStock(Long productId, int newStockQuantity) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));
		product.setStock(newStockQuantity);
		return productRepository.save(product);
	}

	public boolean isProductInStock(Product product, int quantity) {
		return product.getStock() >= quantity;
	}

	public Product getProduct() {
		List<Product> products = new ArrayList<>();
		productRepository.findAll().forEach(products::add);
		return (Product) products;
	}

	public Product findById(Long productId) {
		return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
	}

	public Product createProduct(Product product) {

		return productRepository.save(product);
	}
	
	public Product updateProduct(ProductDto productDto) {
	    Product product = productRepository.findById(productDto.getId())
	            .orElseThrow(() -> new RuntimeException("Product not found"));

	    product.setName(productDto.getName());
	    product.setPrice(productDto.getPrice());
	    product.setStock(productDto.getStock());

	    return productRepository.save(product);
	}
	
	public ProductDto convertToProductDto(Product product) {
	    ProductDto productDto = new ProductDto();
	    productDto.setId(product.getId());
	    productDto.setName(product.getName());
	    productDto.setPrice(product.getPrice());
	    productDto.setStock(product.getStock());
	    return productDto;
	}

	public Product deleteProduct(Long productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));
		productRepository.delete(product);
		return product;
	}
}
