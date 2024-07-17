package certis.CertisHomepage.web.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/home")
    public String Home(){
        return "home";
    }



}
