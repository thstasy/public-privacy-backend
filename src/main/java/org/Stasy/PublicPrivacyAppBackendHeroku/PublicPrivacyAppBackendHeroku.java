package org.Stasy.PublicPrivacyAppBackendHeroku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PublicPrivacyAppBackendHeroku {

	public static void main(String[] args) {
		try {
			SpringApplication.run(PublicPrivacyAppBackendHeroku.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//public static void main(String[] args) {
		//SpringApplication.run(PublicPrivacyAppBackendHeroku.class, args);
	//}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
