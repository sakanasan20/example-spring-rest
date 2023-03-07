package tw.niq.example.spring.rest;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExampleSpringRestApplication {

	public static ApplicationContext CTX;
	
	public static void main(String[] args) {
		CTX = SpringApplication.run(ExampleSpringRestApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
