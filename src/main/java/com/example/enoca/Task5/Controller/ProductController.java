package com.example.enoca.Task5.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.enoca.Task5.Dto.ProductDto;
import com.example.enoca.Task5.Model.Product;
import com.example.enoca.Task5.Service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/{productId}")
	public ResponseEntity<?> getProduct(@PathVariable Long productId,
			@RequestParam(required = false, defaultValue = "1") int quantity) {
		Product product = productService.findById(productId);

		if (product == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ürün bulunamadı.");
		}

		if (!productService.isProductInStock(product, quantity)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Yeterli stok bulunmamaktadır.");
		}

		ProductDto productDTO = new ProductDto();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		productDTO.setStock(product.getStock());

		return ResponseEntity.ok(productDTO);
	}

	@PostMapping("/createProduct")
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDTO) {
		Product product = new Product();
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setStock(productDTO.getStock());
		Product createdProduct = productService.createProduct(product);
		ProductDto createdProductDTO = new ProductDto();
		createdProductDTO.setId(createdProduct.getId());
		createdProductDTO.setName(createdProduct.getName());
		createdProductDTO.setPrice(createdProduct.getPrice());
		createdProductDTO.setStock(createdProduct.getStock());
		return ResponseEntity.ok(createdProductDTO);
	}

	@PutMapping("/updateProduct")
	public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
		Product updatedProduct = productService.updateProduct(productDto);
		ProductDto responseDto = productService.convertToProductDto(updatedProduct);
		return ResponseEntity.ok(responseDto);
	}

	@DeleteMapping("/deleteProduct/{productId}")
	public ResponseEntity<Product> deleteProduct(@PathVariable Long productId) {
		Product deletedProduct = productService.deleteProduct(productId);
		return ResponseEntity.ok(deletedProduct);
	}

}
