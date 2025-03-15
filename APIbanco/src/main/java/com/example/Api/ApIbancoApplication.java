package com.example.Api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ApIbancoApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(ApIbancoApplication.class, args);
		/*BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode("882710225532883C");
		System.out.println("Hash de la contraseña ingresada: " + hashedPassword);	*/
	}
	

}
