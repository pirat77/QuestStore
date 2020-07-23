package model;

public class Student extends User {
    private int wallet;
    public Student(int id, String login, String password, String firstName, String lastName, int wallet) {
        super(id, login, password, firstName, lastName);
        this.wallet = wallet;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}
