package com.example.enoca.Task5.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.enoca.Task5.Dto.CustomerDto;
import com.example.enoca.Task5.Service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping("/save")
	public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customerDto) {
		CustomerDto savedCustomer = customerService.save(customerDto);
		return ResponseEntity.ok(savedCustomer);
	}

}
