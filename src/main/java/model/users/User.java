package model.users;

public abstract class User {
    private int id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Integer studentId;
    private boolean isActive;

    //TODO : create usertype enum

    public static User getUserByType(int userType){
        switch (userType){
            case 1: return new Admin();
            case 2: return new Mentor();
            case 3: return new Student();
        }
        return new Student();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public boolean isActive() { return this.isActive; }

    public void setActive(boolean active) { isActive = active; }
}
