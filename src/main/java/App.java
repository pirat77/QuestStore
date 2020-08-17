import com.sun.net.httpserver.HttpServer;
import handler.CookieHandler;
import handler.LoginHandler;
import handler.StaticHandler;
import handler.student.StudentPagesHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws IOException {
        // create a server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/static", new StaticHandler());
        server.createContext("/cookie", new CookieHandler());

        server.createContext("/login", new LoginHandler());

        // Student handlers -> should be created only if student logs in

        Stream.of("artifact", "home", "quest", "raid", "student", "store").forEach(str -> server.createContext(
                "/student/" + str, new StudentPagesHandler(str)));

        // creates a default executor
        server.setExecutor(null);

        // start listening
        server.start();
    }
}
