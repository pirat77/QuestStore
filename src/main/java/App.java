import SQL.PostgreSQLJDBC;
import com.sun.net.httpserver.HttpServer;
import handler.LoginHandler;
import handler.StudentHomepageHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws IOException {
        // create a server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Student handlers
        server.createContext("/login", new LoginHandler());
        server.createContext("/student/home", new StudentHomepageHandler());


        // creates a default executor
        server.setExecutor(null);

        // start listening
        server.start();
    }
}
