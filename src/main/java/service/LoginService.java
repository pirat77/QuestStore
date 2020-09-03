package service;

import DAO.UserDAO;
import model.Entry;
import model.users.User;

import java.util.List;

public class LoginService {
    UserDAO userDAO;
    static LoginService loginServiceInstance;

    LoginService(){
        this.userDAO = new UserDAO();
    }

    LoginService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    static public LoginService getInstance(){
        if (loginServiceInstance != null) return loginServiceInstance;
        else loginServiceInstance = new LoginService();
        return loginServiceInstance;
    }

    static public LoginService getTestInstance(UserDAO userDAO) {
        return new LoginService(userDAO);
    }

    public boolean checkUser(String login, String password) {
        List<User> users = userDAO.getObjects(new Entry("login", login));
        if (users.size() != 0) {
            User user = users.get(0);
            if (user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public User getUser(String login) {
        User user = userDAO.getObjects(new Entry("login", login)).get(0);
        return user;
    }
}