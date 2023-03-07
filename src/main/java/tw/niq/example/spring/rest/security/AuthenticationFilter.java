package tw.niq.example.spring.rest.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tw.niq.example.spring.rest.ExampleSpringRestApplication;
import tw.niq.example.spring.rest.dto.UserDto;
import tw.niq.example.spring.rest.model.request.LoginUserRequestModel;
import tw.niq.example.spring.rest.service.UserService;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final SecurityProperties securityProperties;

	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(SecurityProperties securityProperties, AuthenticationManager authenticationManager) {
		this.securityProperties = securityProperties;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			
			// Read values from request
			LoginUserRequestModel loginUserRequestModel = 
					new ObjectMapper().readValue(request.getInputStream(), LoginUserRequestModel.class);
			
			String principal = loginUserRequestModel.getEmail();
			String credentials = loginUserRequestModel.getPassword();
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			// Prepare authentication to be verify, which will be valid if it passed the verification process
			Authentication authentication = new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
			
			/*
			 * AuthenticationManager should (optional) throw exceptions when: 
			 * - DisabledException: disabled
			 * - LockedException : locked
			 * - BadCredentialsException : incorrect credentials
			 */
			
			// call loadUserByUsername() to compare and try authentication candidate
			return authenticationManager.authenticate(authentication);
		
		} catch (IOException ex) {
			throw new RuntimeException();
		}
		
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String email = ((User) authResult.getPrincipal()).getUsername();
		UserService userService = (UserService) ExampleSpringRestApplication.CTX.getBean(UserService.class);
		UserDto userDtoFound = userService.getUserByEmail(email);
		String userId = userDtoFound.getUserId();
		
		String token = Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + securityProperties.getExpirationTime()))
				.signWith(SignatureAlgorithm.HS512, securityProperties.getTokenSecret())
				.compact();

		response.addHeader(HttpHeaders.AUTHORIZATION, SecurityBean.TOKEN_PREFIX + token);
		response.addHeader("UserId", userId);
	}
	
}
