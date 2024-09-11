package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DetailsController {

    @GetMapping("/{name}/details")
    public String details(
            @PathVariable String name
    ) {
        return "This page contains details about the user " + name;
    }
}
