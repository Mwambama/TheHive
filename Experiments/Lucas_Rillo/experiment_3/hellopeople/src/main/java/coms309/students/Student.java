package coms309.students;

import coms309.people.Person;

public class Student extends Person {

    protected String university;

    public Student(){}

    public Student(String firstName,
                   String lastName,
                   String address,
                   String telephone,
                   String university
    ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.university = university;
    }

    public String getUniversity() {
        return this.university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.university;
    }
}
