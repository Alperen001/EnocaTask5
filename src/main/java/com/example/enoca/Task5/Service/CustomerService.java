package com.example.enoca.Task5.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.enoca.Task5.Dto.CartDto;
import com.example.enoca.Task5.Dto.CustomerDto;
import com.example.enoca.Task5.Dto.OrderDto;
import com.example.enoca.Task5.Model.Cart;
import com.example.enoca.Task5.Model.Customer;
import com.example.enoca.Task5.Model.Order;
import com.example.enoca.Task5.Repository.CartRepository;
import com.example.enoca.Task5.Repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CartRepository cartRepository;

	public Customer findById(Long customerId) {
		return customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
	}

	public List<Customer> findAllCustomers() {
		return customerRepository.findAll();
	}

	public CustomerDto save(CustomerDto customerDto) {
		Customer customer = toCustomerEntity(customerDto);
		customer = customerRepository.save(customer);

		Cart cart = new Cart();
		cart.setCustomer(customer);
		cart.setTotalPrice(0);
		cart = cartRepository.save(cart);

		customer.setCart(cart);
		return toCustomerDTO(customer);
	}

	private Customer toCustomerEntity(CustomerDto customerDto) {
		Customer customer = new Customer();
		customer.setId(customerDto.getId());
		customer.setFirstName(customerDto.getFirstName());
		customer.setLastName(customerDto.getLastName());
		customer.setEmail(customerDto.getEmail());
		return customer;
	}

	private CustomerDto toCustomerDTO(Customer customer) {
		CustomerDto dto = new CustomerDto();
		dto.setId(customer.getId());
		dto.setFirstName(customer.getFirstName());
		dto.setLastName(customer.getLastName());
		dto.setEmail(customer.getEmail());

		if (customer.getCart() != null) {
			CartDto cartDTO = new CartDto();
			cartDTO.setId(customer.getCart().getId());
			cartDTO.setTotalPrice(customer.getCart().getTotalPrice());
			cartDTO.setCustomerId(customer.getId());
			dto.setCart(cartDTO);
		}

		List<OrderDto> orderDTOs = new ArrayList<>();
		if (customer.getOrders() != null) {
			for (Order order : customer.getOrders()) {
				OrderDto orderDTO = new OrderDto();
				orderDTO.setId(order.getId());
				orderDTO.setTotalPrice(order.getTotalPrice());
				orderDTO.setCustomerId(customer.getId());
				orderDTOs.add(orderDTO);
			}
			dto.setOrders(orderDTOs);
		}
		return dto;
	}
}
