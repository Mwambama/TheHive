package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */
public class Person {

    private String firstName;

    private String middleName;

    private String lastName;

    private String pronouns;

    private String address;

    private String telephone;

    public Person(){
        
    }

    public Person(String firstName, String middleName, String lastName, String pronouns, String address, String telephone){
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.pronouns = pronouns;
        this.address = address;
        this.telephone = telephone;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName(){ return this.middleName;}

    public void setMiddleName(String middleName){ this.middleName = middleName; }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPronouns(){ return this.pronouns; }

    public void setPronouns(String pronouns){ this.pronouns = pronouns; }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + middleName + " " + lastName
                + "\nPronouns: " + pronouns
                + "\nAddress: " + address
                + "\nTelephone: " + telephone;
    }
}
