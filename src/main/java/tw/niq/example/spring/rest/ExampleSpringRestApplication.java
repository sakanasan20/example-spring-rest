package tw.niq.example.spring.rest;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExampleSpringRestApplication extends SpringBootServletInitializer {

	public static ApplicationContext CTX;
	
	public static void main(String[] args) {
		CTX = SpringApplication.run(ExampleSpringRestApplication.class, args);
	}
	
	/**
	 * For WAR files generation
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ExampleSpringRestApplication.class);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
