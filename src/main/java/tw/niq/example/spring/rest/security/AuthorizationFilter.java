package tw.niq.example.spring.rest.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tw.niq.example.spring.rest.ExampleSpringRestApplication;
import tw.niq.example.spring.rest.dto.UserDto;
import tw.niq.example.spring.rest.service.UserService;

public class AuthorizationFilter extends BasicAuthenticationFilter {
	
	private final SecurityProperties securityProperties;

	public AuthorizationFilter(SecurityProperties securityProperties, AuthenticationManager authenticationManager) {
		super(authenticationManager);
		this.securityProperties = securityProperties;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String requestHeaderToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		String requestHeaderUserId = request.getHeader("UserId");
		
		// Credentials not contained
		if (requestHeaderToken == null || !requestHeaderToken.startsWith(SecurityBean.TOKEN_PREFIX) || requestHeaderToken == null) {
			chain.doFilter(request, response);
			return;
		}
		
		String tokenToVerify = requestHeaderToken.replace(SecurityBean.TOKEN_PREFIX, "");
		
		// Parsing token to get contained user ID
		String userIdToVerify = Jwts.parser()
				.setSigningKey(securityProperties.getTokenSecret())
				.parseClaimsJws(tokenToVerify)
				.getBody()
				.getSubject();

		Authentication authentication = null;
		
		// Matching user ID
		if (userIdToVerify != null && userIdToVerify.equals(requestHeaderUserId)) {
			UserService userService = (UserService) ExampleSpringRestApplication.CTX.getBean(UserService.class);
			UserDto userDtoFound = userService.getUserByUserId(requestHeaderUserId);
			String email = userDtoFound.getEmail();
			
			authentication = new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);
	}

}
