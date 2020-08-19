package service;

import DAO.UserDAO;
import model.users.User;

import java.sql.SQLException;
import java.util.List;

public class LoginService {
    private UserDAO userDAO;

    public LoginService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public boolean checkUser(String login, String password) throws SQLException, ClassNotFoundException {
        List<User> users = userDAO.getObjects("login", login);
        System.out.println(users.get(0).getFirstName());
        User userLogin = userDAO.getObjects("login", login).get(0);
        User userPassword = userDAO.getObjects("password", password).get(0);
        if (users.size() != 0 && userLogin.getId() == userPassword.getId()){
            return true;
        }
        return false;
    }

}