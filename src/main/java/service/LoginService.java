package service;

import DAO.UserDAO;
import model.users.User;

import java.sql.SQLException;
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

    public boolean checkUser(String login, String password) throws SQLException, ClassNotFoundException {
        List<User> users = userDAO.getObjects("login", login);
        System.out.println("User login from db: " + users.get(0).getLogin());
        if (users.size() != 0) {
            User user = userDAO.getObjects("login", login).get(0);
            System.out.println(user.getFirstName() + " tries to log in");
            if (user.getPassword().equals(password)){
                return true;
            }
        }
        System.out.println("False");
        return false;
    }

}