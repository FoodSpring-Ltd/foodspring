package com.juaracoding.foodspring;

import com.juaracoding.foodspring.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodspringApplication {
	@Autowired
	private DiscountRepository discountRepository;
	public static void main(String[] args) {
		SpringApplication.run(FoodspringApplication.class, args);
	}

}
