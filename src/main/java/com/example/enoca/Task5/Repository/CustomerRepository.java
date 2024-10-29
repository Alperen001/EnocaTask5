package com.example.enoca.Task5.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.enoca.Task5.Model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}

