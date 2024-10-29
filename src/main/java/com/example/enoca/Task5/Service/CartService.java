package com.example.enoca.Task5.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.enoca.Task5.Dto.CartDto;
import com.example.enoca.Task5.Model.Cart;
import com.example.enoca.Task5.Model.Customer;
import com.example.enoca.Task5.Model.Product;
import com.example.enoca.Task5.Repository.CartRepository;
import jakarta.transaction.Transactional;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private CustomerService customerService;

	public Cart getCartByCustomerId(Long customerId) {
		return cartRepository.findByCustomerId(customerId)
				.orElseThrow(() -> new RuntimeException("Cart not found for customer with id: " + customerId));
	}

	public Cart updateCart(Long customerId, List<Map<String, Object>> updatedProducts) {
		Customer customer = customerService.findById(customerId);
		Cart cart = customer.getCart();

		if (cart == null) {
			throw new RuntimeException("Cart not found for customer with id: " + customerId);
		}

		for (Map<String, Object> entry : updatedProducts) {
			Long productId = Long.valueOf(entry.get("productId").toString());
			Integer quantity = Integer.valueOf(entry.get("quantity").toString());
			Product product = productService.findById(productId);

			if (cart.getProducts().containsKey(product)) {
				cart.getProducts().put(product, quantity);
			} else {
				cart.getProducts().put(product, quantity);
			}
			cart.setTotalPrice(cart.getProducts().entrySet().stream()
					.mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum());
		}

		return cartRepository.save(cart);
	}

	public Cart addProductToCart(Long customerId, Long productId, int quantity) {
		Customer customer = customerService.findById(customerId);
		Product product = productService.findById(productId);
		Cart cart = customer.getCart();

		if (cart == null) {
			cart = new Cart();
			cart.setCustomer(customer);
			cart.setProducts(new HashMap<>());
			cart.setTotalPrice(0.0);
		}

		cart.getProducts().merge(product, quantity, Integer::sum);

		cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));

		return cartRepository.save(cart);
	}

	public Cart findCartByCustomerId(Long customerId) {

		return cartRepository.findByCustomerId(customerId)
				.orElseThrow(() -> new RuntimeException("Cart not found for customer ID: " + customerId));
	}

	public Cart removeProductFromCart(Long customerId, Long productId) {
		Customer customer = customerService.findById(customerId);
		Product product = productService.findById(productId);

		Cart cart = customer.getCart();

		if (cart.getProducts().containsKey(product)) {
			Integer quantity = cart.getProducts().get(product);
			cart.setTotalPrice(cart.getTotalPrice() - (product.getPrice() * quantity));
			cart.getProducts().remove(product);
		}

		return cartRepository.save(cart);
	}

	public void emptyCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

		cart.getProducts().clear();

		cart.setTotalPrice(0.0);

		cartRepository.save(cart);
	}

	@Transactional
	public Cart updateProductQuantity(Cart cart, Product product, int newQuantity) {
		if (!productService.isProductInStock(product, newQuantity)) {
			throw new RuntimeException("Not enough stock for the new quantity");
		}

		cart.getProducts().put(product, newQuantity);
		updateTotalPrice(cart);
		return cartRepository.save(cart);
	}

	private void updateTotalPrice(Cart cart) {
		double totalPrice = cart.getProducts().entrySet().stream()
				.mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue()).sum();
		cart.setTotalPrice(totalPrice);
	}

	public Cart getCartForCustomer(Customer customer) {
		return customer.getCart();
	}

	public CartDto convertToCartDto(Cart cart) {
		CartDto cartDto = new CartDto();
		cartDto.setId(cart.getId());
		cartDto.setTotalPrice(cart.getTotalPrice());
		cartDto.setCustomerId(cart.getCustomer().getId());
		Map<String, Integer> productNames = new HashMap<>();
		for (Map.Entry<Product, Integer> entry : cart.getProducts().entrySet()) {
			productNames.put(entry.getKey().getName(), entry.getValue());
		}
		cartDto.setProducts(productNames);

		return cartDto;
	}

}
