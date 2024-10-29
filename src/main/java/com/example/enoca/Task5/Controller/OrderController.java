package com.example.enoca.Task5.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.enoca.Task5.Dto.OrderDto;
import com.example.enoca.Task5.Model.Order;
import com.example.enoca.Task5.Service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping("/placeOrder/{customerId}")
	public ResponseEntity<OrderDto> placeOrder(@PathVariable Long customerId) {
		Order order = orderService.placeOrder(customerId);
		OrderDto orderDTO = orderService.convertToDTO(order);
		return ResponseEntity.ok(orderDTO);
	}

	@GetMapping("/customer/{customerId}/orders")
	public ResponseEntity<List<OrderDto>> getAllOrdersForCustomer(@PathVariable Long customerId) {
		List<OrderDto> orders = orderService.getAllOrdersForCustomer(customerId);
		return ResponseEntity.ok(orders);
	}

	@GetMapping("/getOrderForCode")
	public ResponseEntity<OrderDto> getOrderForCode(@RequestParam String orderCode) {
		try {
			Order order = orderService.getOrderForCode(orderCode);
			if (order == null) {
				return ResponseEntity.notFound().build();
			}
			OrderDto orderDto = orderService.convertToOrderDTO(order);

			return ResponseEntity.ok(orderDto);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
}
