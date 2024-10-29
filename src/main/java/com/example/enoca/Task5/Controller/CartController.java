package com.example.enoca.Task5.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.enoca.Task5.Dto.CartDto;
import com.example.enoca.Task5.Model.Cart;
import com.example.enoca.Task5.Service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@GetMapping("/get/{customerId}")
	public ResponseEntity<CartDto> getCart(@PathVariable Long customerId) {
	    Cart cart = cartService.getCartByCustomerId(customerId);
	    CartDto cartDto =cartService.convertToCartDto(cart);
	    return ResponseEntity.ok(cartDto);
	}
	
	@DeleteMapping("/customer/{customerId}/cart/remove/{productId}")
	public ResponseEntity<CartDto> removeProductFromCart(@PathVariable Long customerId, @PathVariable Long productId) {
	    Cart updatedCart = cartService.removeProductFromCart(customerId, productId);
	    CartDto cartDto = cartService.convertToCartDto(updatedCart);
	    return ResponseEntity.ok(cartDto);
	}
	@DeleteMapping("/empty/{cartId}")
	public ResponseEntity<String> emptyCart(@PathVariable Long cartId) {
		cartService.emptyCart(cartId);
		return ResponseEntity.ok("Cart has been emptied successfully.");
	}


	@PutMapping("/customer/{customerId}/cart/update")
	public ResponseEntity<CartDto> updateCart(@PathVariable Long customerId,
	        @RequestBody List<Map<String, Object>> updatedProducts) {
	    Cart updatedCart = cartService.updateCart(customerId, updatedProducts);
	    CartDto cartDto = new CartDto();
	    cartDto.setId(updatedCart.getId());
	    cartDto.setTotalPrice(updatedCart.getTotalPrice());
	    cartDto.setCustomerId(updatedCart.getCustomer().getId());

	    Map<String, Integer> productMap = updatedCart.getProducts().entrySet().stream()
	            .collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
	    cartDto.setProducts(productMap);

	    return ResponseEntity.ok(cartDto);
	}




	@PostMapping("/customer/{customerId}/cart/add")
	public ResponseEntity<CartDto> addProductToCart(@PathVariable Long customerId, @RequestParam Long productId,
			@RequestParam int quantity) {
		Cart updatedCart = cartService.addProductToCart(customerId, productId, quantity);

		CartDto cartDto = new CartDto();
		cartDto.setId(updatedCart.getId());
		cartDto.setTotalPrice(updatedCart.getTotalPrice());
		cartDto.setCustomerId(updatedCart.getCustomer().getId());

		Map<String, Integer> productMap = updatedCart.getProducts().entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
		cartDto.setProducts(productMap);

		return ResponseEntity.ok(cartDto);
	}
	



}
