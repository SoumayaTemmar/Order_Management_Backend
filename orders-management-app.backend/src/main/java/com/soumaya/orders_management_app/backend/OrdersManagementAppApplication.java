package com.soumaya.orders_management_app.backend;

import com.soumaya.orders_management_app.backend.Models.User.Role;
import com.soumaya.orders_management_app.backend.Models.User.User;
import com.soumaya.orders_management_app.backend.Models.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class OrdersManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersManagementAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner createUser(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder
	){
		return args -> {

			final String username = "admin.user";
			if (!userRepository.existsByUsername(username)){
				User user = User.builder()
						.firstName("admin")
						.lastName("user")
						.username(username)
						.password(passwordEncoder.encode("admin1234"))
						.roles(Set.of(Role.ADMIN))
						.deleted(false)
						.enabled(true)
						.accountLocked(false)
						.build();

				userRepository.save(user);
			}

		};

	}
}
