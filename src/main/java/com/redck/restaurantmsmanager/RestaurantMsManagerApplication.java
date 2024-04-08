package com.redck.restaurantmsmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

//implements CommandLineRunner
@SpringBootApplication
@EntityScan("com.redck.restaurantmsmanager.domain")
public class RestaurantMsManagerApplication{

	public static void main(String[] args) {
		SpringApplication.run(RestaurantMsManagerApplication.class, args);
	}

}
