package com.example.enoca.Task5.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.enoca.Task5.Model.Product;

	public interface ProductRepository extends JpaRepository<Product, Long>{
		 List<Product> findAll();
}
