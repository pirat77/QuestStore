import DAO.UserDAO;
import SQL.PostgreSQLJDBC;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        System.out.println("Duzo by gadac");
        try {
            System.out.println((new UserDAO()).getObjects("login", "%"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
