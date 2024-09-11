package coms309;

import org.springframework.web.bind.annotation.*;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/{name}")
    public String welcome(
          @PathVariable String name,
          @RequestParam(required = false) String major
    ) {
        String response = "Hello and welcome to COMS 309: " + name;

        if (major != null) {
            response += ", " + major + " major";
        }
        return response;
    }
}
