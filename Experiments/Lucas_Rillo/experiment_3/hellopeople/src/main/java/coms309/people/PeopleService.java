package coms309.people;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PeopleService {

    private final HashMap<String, Person> personList = new HashMap<>();

    public HashMap<String, Person> getAllPeople() {
        return personList;
    }

    public void addPerson(Person person) {
        personList.put(person.getFirstName(), person);
    }

    public Person getPerson(String firstName) {
        return personList.get(firstName);
    }

    public void updatePerson(String firstName, Person person) {
        personList.replace(firstName, person);
    }

    public void deletePerson(String firstName) {
        personList.remove(firstName);
    }
}
