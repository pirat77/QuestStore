import DAO.UserDAO;
import SQL.PostgreSQLJDBC;

public class App {
    public static void main(String[] args) {
        System.out.println("Duzo by gadac");
        System.out.println((new UserDAO()).getObjects("id", "%"));
    }
}
