package certis.CertisHomepage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class CertisHomepageApplication {

	public static void main(String[] args) {
		SpringApplication.run(CertisHomepageApplication.class, args);
	}

	@GetMapping
	public String index(){
		return "introduce";
	}

	@GetMapping("/login")
	public String login(){
		return "login";
	}

	@GetMapping("/register")
	public String register(){
		return "register";
	}
}
