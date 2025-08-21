package com.orderingsystem.orderingsystem;

import com.orderingsystem.orderingsystem.entity.*;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableCaching
public class OrderingsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderingsystemApplication.class, args);
	}
}
