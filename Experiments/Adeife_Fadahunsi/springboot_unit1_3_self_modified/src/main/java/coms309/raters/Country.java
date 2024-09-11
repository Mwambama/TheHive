package coms309.raters;

public class Country {
    private String country;
    private int rating;

    public Country(){}

    public Country(String country, int rating){
        this.country = country;
        this.rating = rating;
    }

    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return country;
    }

    public void setRating(int rating){
        this.rating = rating;
    }
    public int getRating(){
        return rating;
    }

}
