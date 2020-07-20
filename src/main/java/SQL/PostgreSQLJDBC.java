package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLJDBC {
    private Connection connection = null;

    public Connection connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
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