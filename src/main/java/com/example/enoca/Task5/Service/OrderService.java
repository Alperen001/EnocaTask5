package com.example.enoca.Task5.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.enoca.Task5.Dto.OrderDto;
import com.example.enoca.Task5.Dto.OrderProductDto;
import com.example.enoca.Task5.Model.Cart;
import com.example.enoca.Task5.Model.Customer;
import com.example.enoca.Task5.Model.Order;
import com.example.enoca.Task5.Model.OrderProduct;
import com.example.enoca.Task5.Model.Product;
import com.example.enoca.Task5.Repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartService cartService;

	public Order placeOrder(Long customerId) {
		Customer customer = customerService.findById(customerId);
		Cart cart = cartService.getCartByCustomerId(customerId);

		if (cart.getProducts().isEmpty()) {
			throw new RuntimeException("Cannot place order: Cart is empty.");
		}

		Order order = new Order();
		order.setCustomer(customer);
		order.setTotalPrice(cart.getTotalPrice());
		order.setOrderCode(UUID.randomUUID().toString());

		for (Map.Entry<Product, Integer> entry : cart.getProducts().entrySet()) {
			Product product = entry.getKey();
			Integer quantity = entry.getValue();

			if (product.getStock() < quantity) {
				throw new RuntimeException("Yeterli stok bulunmamaktadır. " + product.getId());
			}

			OrderProduct orderProduct = new OrderProduct();
			orderProduct.setProduct(product);
			orderProduct.setQuantity(quantity);
			orderProduct.setPriceAtOrderTime(product.getPrice());

			order.getProducts().add(orderProduct);

			product.setStock(product.getStock() - quantity);
		}

		Order savedOrder = orderRepository.save(order);
		cartService.emptyCart(cart.getId());

		return savedOrder;
	}

	public Order getOrderForCode(String orderCode) {
		return orderRepository.findByOrderCode(orderCode)
				.orElseThrow(() -> new RuntimeException("Order not found with code: " + orderCode));
	}

	public List<OrderDto> getAllOrdersForCustomer(Long customerId) {
		Customer customer = customerService.findById(customerId);
		List<Order> orders = orderRepository.findByCustomer(customer);

		return orders.stream().map(this::convertToOrderDTO).collect(Collectors.toList());
	}

	public OrderDto convertToOrderDTO(Order order) {
		OrderDto orderDTO = new OrderDto();
		orderDTO.setId(order.getId());
		orderDTO.setOrderCode(order.getOrderCode());
		orderDTO.setTotalPrice(order.getTotalPrice());

		List<OrderProductDto> products = order.getProducts().stream()
				.map(orderProduct -> new OrderProductDto(orderProduct.getProduct().getId(),
						orderProduct.getProduct().getName(), orderProduct.getQuantity(),
						orderProduct.getPriceAtOrderTime()))
				.collect(Collectors.toList());

		orderDTO.setProducts(products);
		return orderDTO;
	}

	public OrderDto convertToDTO(Order order) {
		OrderDto orderDTO = new OrderDto();
		orderDTO.setId(order.getId());
		orderDTO.setOrderCode(order.getOrderCode());
		orderDTO.setTotalPrice(order.getTotalPrice());
		orderDTO.setCustomerId(order.getCustomer() != null ? order.getCustomer().getId() : null);

		List<OrderProductDto> orderProductDTOs = order.getProducts().stream()
				.map(orderProduct -> new OrderProductDto(orderProduct.getProduct().getId(),
						orderProduct.getProduct().getName(), orderProduct.getQuantity(),
						orderProduct.getPriceAtOrderTime()))
				.collect(Collectors.toList());

		orderDTO.setProducts(orderProductDTOs);
		return orderDTO;
	}

}
