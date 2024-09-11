package coms309.raters;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.HashMap;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Fadahunsi Adeife
 */

@RestController
public class RatersController {

    // Note that there is only ONE instance of PeopleController in 
    // Springboot system.
    HashMap<String, Profile> profilesList = new HashMap<>();
    @GetMapping("/profiles")
    public HashMap<String, Profile> getAllProfiles() {
        return profilesList;
    }

    @DeleteMapping("/profiles")
    public String deleteProfile() {
        profilesList.clear();
        return "Successfully deleted all profiles";
    }

    @PostMapping("/profile")
    public String createProfile(@RequestBody Profile account) {
        System.out.println(account);
        profilesList.put(account.getName(), account);
        return "New rater: "+ account.getName() + " Profile Saved";
    }

    @GetMapping("/profile/{Name}")
    public Profile getProfile(@PathVariable String Name) {
        return profilesList.get(Name);
    }

    @DeleteMapping("/profile/{Name}")
    public HashMap<String, Profile> deleteProfile(@PathVariable String Name) {
        profilesList.remove(Name);
        return profilesList;
    }

    @PutMapping("/profiles/{Name}")
    public Profile updatePerson(@PathVariable String Name, @RequestBody Profile p) {
        profilesList.replace(Name, p);
        return profilesList.get(Name);
    }

    @PostMapping("/profile/{Name}/ratings")
    public String rateCountry(@PathVariable String Name, @RequestBody Country country) {
        profilesList.get(Name).addCountryRating(country.getCountry(), country.getRating());
        return "Successfully rated " + country.getCountry() + " as " + country.getRating() + "/10";
    }

    @GetMapping("/profiles/{Name}/ratings")
    public HashMap<String, Integer> getAllUserRatings(@PathVariable String Name) {
        return profilesList.get(Name).getAllCountryRatings();
    }


}

