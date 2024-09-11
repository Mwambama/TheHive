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

    @GetMapping("/{name}/details")
    public String details(
            @PathVariable String name
    ) {
        return "This page contains details about " + name;
    }
}
