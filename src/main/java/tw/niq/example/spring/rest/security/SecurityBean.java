package tw.niq.example.spring.rest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityBean {
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	@Bean
	public SecurityProperties securityProperties() {
		return new SecurityProperties();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
