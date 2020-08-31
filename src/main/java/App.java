import DAO.CookieDAO;
import DAO.UserDAO;
import com.sun.net.httpserver.HttpServer;
import handler.CookieHandler;
import handler.FileHandler;
import handler.LoginHandler;
import handler.student.StudentPagesHandler;
import helper.CookieHelper;
import service.CookieService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws IOException {
        UserDAO userDAO = new UserDAO();
        CookieDAO cookieDAO = new CookieDAO();
        CookieService cookieService = new CookieService(cookieDAO, userDAO);
        CookieHelper cookieHelper = new CookieHelper();
        CookieHandler cookieHandler = new CookieHandler(cookieService, cookieHelper);

        // create a server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        setPaths(cookieHandler, server);
    }

    static void setPaths(CookieHandler cookieHandler, HttpServer server) {
        server.createContext("/static", new FileHandler());
        server.createContext("/images", new FileHandler());
        server.createContext("/login", new LoginHandler(cookieHandler));

        // Student handlers -> should be created only if student logs in
        Stream.of("artifact", "home", "quest", "raid", "student", "store").forEach(str -> server.createContext(
                "/student/" + str, new StudentPagesHandler(str)));

        // creates a default executor
        server.setExecutor(null);
        // start listening
        server.start();
    }
}
