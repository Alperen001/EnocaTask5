package com.example.enoca.Task5.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.enoca.Task5.Model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	List<Cart> findAll();

	Optional<Cart> findByCustomerId(Long customerId);
}
