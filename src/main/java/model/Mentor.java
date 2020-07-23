package model;

public class Mentor extends User {

    public Mentor(int id, String login, String password, String firstName, String lastName, Integer studentId) {
        super(id, login, password, firstName, lastName);
    }
}
