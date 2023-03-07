package tw.niq.example.spring.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tw.niq.example.spring.rest.controller.UserController;
import tw.niq.example.spring.rest.service.UserService;

@EnableWebSecurity
@Configuration
public class WebSecurity {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

		/*
		 * AuthenticationManager
		 */
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		
		authenticationManagerBuilder
			.userDetailsService(userService)
			.passwordEncoder(bCryptPasswordEncoder);

		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		
		/*
		 * AuthenticationFilter
		 */
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(securityProperties, authenticationManager);
		authenticationFilter.setFilterProcessesUrl(securityProperties.getLoginUrl());
		
		/*
		 * AuthorizationFilter
		 */
		AuthorizationFilter authorizationFilter = new AuthorizationFilter(securityProperties, authenticationManager);
		
		/*
		 * SecurityFilterChain
		 */
		http.authenticationManager(authenticationManager);
		http.addFilter(authenticationFilter);
		http.addFilter(authorizationFilter);

		http.authorizeHttpRequests((authorize) -> authorize
			.requestMatchers(PathRequest.toH2Console()).permitAll()
			.requestMatchers(HttpMethod.GET, UserController.PATH + "/check").permitAll()
			.requestMatchers(HttpMethod.POST, securityProperties.getLoginUrl()).permitAll()
			.anyRequest().authenticated());
		
		http.csrf().disable();
		http.headers().frameOptions().sameOrigin().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		SecurityFilterChain securityFilterChain = http.build();
		
		return securityFilterChain;
	}
	
}
