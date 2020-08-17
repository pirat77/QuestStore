package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.users.User;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.io.OutputStream;

public class StudentHomepageHandler implements HttpHandler {
    User user = null;
    //CookieHandler cookieHandler = new CookieHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
//        user = cookieHandler.cookieChecker(httpExchange);
//        if(user == null || user.getUserType() != 1){
//            httpExchange.getResponseHeaders().set("Location", "/login");
//            httpExchange.sendResponseHeaders(303, 0);
//        }

//        String method = httpExchange.getRequestMethod();
//        StudentDAO studentDAO = new StudentDAO();
//        int coins = studentDAO.getStudentCoins(user.getId());
        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")){
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/student/homepage.twig");
            JtwigModel model = JtwigModel.newModel();
            response = template.render(model);

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
