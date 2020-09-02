package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLJDBC {
    private final String HOST = "//ec2-54-228-250-82.eu-west-1.compute.amazonaws.com:";
    private final String PORT = "5432";
    private final String DB_NAME = "/d5h7521m977spq";
    private final String SSLMODE = "??ssl=true&sslmode=require";
    private final String CONNECTION_STRING = "jdbc:postgresql:" + HOST + PORT + DB_NAME + SSLMODE;
    private final String USER = "upvfmzwxkwssvp";
    private final String PASSWORD = "1a029fa503d76d2791558593c69d1a92f76cfb261d2a028d99902439ced533cf";
    private Connection connection = null;
    private static PostgreSQLJDBC JDBCInstance;

    static PostgreSQLJDBC getInstance(){
        if (JDBCInstance == null) JDBCInstance = new PostgreSQLJDBC();
        return JDBCInstance;
    }

    public Connection connect() {
        try {
            this.connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
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

    public Connection getConnection() {
        return connection;
    }
}