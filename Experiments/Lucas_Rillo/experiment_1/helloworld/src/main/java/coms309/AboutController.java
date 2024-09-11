package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AboutController {

    @GetMapping("/coms309/about")
    public String about() {
        return "This is the first Spring Boot tutorial for COMS 309!";
    }
}
