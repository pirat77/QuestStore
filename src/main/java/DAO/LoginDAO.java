package DAO;

import model.users.Student;
import model.users.User;

import java.util.List;

public class LoginDAO implements Dao {
    @Override
    public void update(Object object) {

    }

    @Override
    public void remove(Object object) {

    }

    @Override
    public void insert(Object object) {

    }

    @Override
    public List getObjects(String columnName, String columnValue) {
        return null;
    }

    public User getUserFromDB(String login, String password){
        //TODO
        User user = new Student();
        return user;
    }
}
