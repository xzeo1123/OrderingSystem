package com.orderingsystem.orderingsystem;

import com.orderingsystem.orderingsystem.entity.*;
import com.orderingsystem.orderingsystem.exception.ResourceNotFoundException;
import com.orderingsystem.orderingsystem.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;

@SpringBootApplication
public class OrderingsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderingsystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			UsersRepository usersRepository,
			CategoriesRepository categoriesRepository,
			TablesRepository tablesRepository,
			ProductsRepository productsRepository,
			BillsRepository billsRepository,
			ReceiptsRepository receiptsRepository
	) {
		return args -> {
			// USERS
			Users user = usersRepository.findById(1).orElse(null);
			if (user == null) {
				user = new Users(null, "temp_user", "temp_pass", 0, (byte) 1);
				usersRepository.save(user);
			}

			// CATEGORIES
			Categories category = categoriesRepository.findById(1).orElse(null);
			if (category == null) {
				category = new Categories(null, "temp_category", "default fallback category", (byte) 1);
				categoriesRepository.save(category);
			}

			// TABLES
			Tables table = tablesRepository.findById(1).orElse(null);
			if (table == null) {
				table = new Tables(null, 0, (byte) 1);
				tablesRepository.save(table);
			}

			// PRODUCTS
			Products product = productsRepository.findById(1).orElse(null);
			if (product == null) {
				product = new Products(
						null,
						"temp_product",
						"https://res.cloudinary.com/dtuqv5roa/image/upload/v1750823733/template_product_image_vhhm6j.jpg",
						"template_product_image_vhhm6j",
						0.0f,
						0.0f,
						0,
						(byte) 1,
						category
				);
				productsRepository.save(product);
			}

			// BILLS
			if (billsRepository.count() == 0) {
				Bills bill = new Bills(
						null,
						0.0f,
						LocalDateTime.now(),
						"temp bill",
						(byte) 1,
						user,
						table
				);
				billsRepository.save(bill);
			}

			// RECEIPTS
			if (receiptsRepository.count() == 0) {
				Receipts receipt = new Receipts(
						null,
						0.0f,
						LocalDateTime.now(),
						"temp receipt",
						(byte) 1
				);
				receiptsRepository.save(receipt);
			}
		};
	}
}
