package tw.niq.example.spring.rest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurity {
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		
		http.authenticationManager(authenticationManager);
		
		http.csrf().disable()
			.authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/users/check").permitAll()
			.anyRequest().authenticated();
		
		return http.build();
	}
	
}
