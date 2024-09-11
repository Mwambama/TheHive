package coms309.raters;


import java.util.HashMap;

/**
 * Provides the Definition/Structure for a profile
 *
 * @author Fadahunsi Adeife
 */

public class Profile {

    private String name;

    private int age;

    private String citizenship;

    private HashMap<String, Integer> countryratings = new HashMap<>();
    public Profile(){
    }

    public Profile(String name, int age, String citizenship){
        this.name = name;
        this.age = age;
        this.citizenship = citizenship;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) { this.name = name; }

    public int getAge() { return this.age; }

    public void setAge(int age){ this.age = age; }

    public String getCitizenship() { return this.citizenship; }

    public void setCitizenship(String citizenship){ this.citizenship = citizenship; }

    public void addCountryRating(String country, Integer rating){
        countryratings.put(country, rating);
    }
    public int getCountryRating(String country){
        return countryratings.get(country);
    }

    public HashMap<String, Integer> getAllCountryRatings(){
        return countryratings;
    }

    @Override
    public String toString() {
        return this.name + " is " + this.age + " years old from " + this.citizenship + " and has rated " + this.countryratings.size() + " countries.";
    }
}
