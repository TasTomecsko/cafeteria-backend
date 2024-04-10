package com.tastomecsko.cafeteria;

import com.tastomecsko.cafeteria.entities.User;
import com.tastomecsko.cafeteria.entities.enums.Role;
import com.tastomecsko.cafeteria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CafeteriaApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(CafeteriaApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	public void run(String... args) {
		if(userRepository.findAll().isEmpty()) {
			User user = new User();

			user.setEmail("user@test.te");
			user.setFirstName("user");
			user.setLastName("user");
			user.setRole(Role.USER);
			user.setPassword(new BCryptPasswordEncoder().encode("pass"));

			userRepository.save(user);

			User admin = new User();

			admin.setEmail("admin@test.te");
			admin.setFirstName("admin");
			admin.setLastName("admin");
			admin.setRole(Role.ADMIN);
			admin.setPassword(new BCryptPasswordEncoder().encode("admin"));

			userRepository.save(admin);
		}
	}

}
