package service;

import DAO.UserDAO;
import model.users.User;

import java.util.List;

public class LoginService {
    UserDAO userDAO;
    static LoginService loginServiceInstance;

    LoginService(){
        this.userDAO = new UserDAO();
    }

    static public LoginService getInstance(){
        if (loginServiceInstance != null) return loginServiceInstance;
        else loginServiceInstance = new LoginService();
        return loginServiceInstance;
    }

    public boolean checkUser(String login, String password) {
        List<User> users = userDAO.getObjects("login", login);
        if (users.size() != 0) {
            User user = userDAO.getObjects("login", login).get(0);
            if (user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public User getUser(String login) {
        User user = userDAO.getObjects("login", login).get(0);
        return user;
    }

}