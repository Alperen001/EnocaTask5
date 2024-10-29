package com.example.enoca.Task5.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.enoca.Task5.Model.Customer;
import com.example.enoca.Task5.Model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findAll();

	List<Order> findByCustomer(Customer customer);

	Optional<Order> findByOrderCode(String orderCode);
}
