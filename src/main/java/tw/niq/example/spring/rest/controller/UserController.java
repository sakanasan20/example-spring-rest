package tw.niq.example.spring.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.PATH)
public class UserController {

	public static final String PATH = "/users";
	
	@GetMapping("/check")
	public String check() {
		return PATH + " is working...";
	}
	
}
