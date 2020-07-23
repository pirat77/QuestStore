package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLJDBC {
    private Connection connection = null;

    public Connection connect() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://ec2-54-228-250-82.eu-west-1.compute.amazonaws.com:5432/d5h7521m977spq??ssl=true&sslmode=require",
                    "upvfmzwxkwssvp", "1a029fa503d76d2791558593c69d1a92f76cfb261d2a028d99902439ced533cf");
            System.out.println("Opened database successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return connection;
    }

    public void disconnect(){
        try {
            connection.close();
            if(connection.isClosed()){
                System.out.println("DB Closed");
            }else {
                System.out.println("Cant't Close DB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}