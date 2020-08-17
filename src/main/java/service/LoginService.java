package service;

import DAO.LoginDAO;
import model.users.User;

public class LoginService {
    LoginDAO loginDAO = new LoginDAO();

    public boolean loginChecker(String login, String password){
        User user;
        user = loginDAO.getUserFromDB(login,password);
        if (user != null){
            return true;
        }
        return false;
    }

}