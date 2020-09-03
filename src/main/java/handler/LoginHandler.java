package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.users.User;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import service.LoginService;

import java.io.*;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {
    private final LoginService loginService;
    CookieHandler cookieHandler;
    User user;

    public LoginHandler(CookieHandler cookieHandler) {
        this.loginService = LoginService.getInstance();
        this.cookieHandler = cookieHandler;
    }

    public LoginHandler(CookieHandler cookieHandler, LoginService loginService) {
        this.loginService = loginService;
        this.cookieHandler = cookieHandler;
    }

    private static Map<String, String> parseFormData(String formData) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
            map.put(keyValue[0], value);
        }
        return map;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        user = cookieHandler.checkCookie(httpExchange);
        if (user != null) {
            redirectToUserSpecificPage(httpExchange);
            return;
        }
        String method = httpExchange.getRequestMethod();

        if (method.equals("POST")) {
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map<String, String> inputs = parseFormData(formData);
            String login = inputs.get("login");
            String password = inputs.get("password");
            if (loginService.checkUser(login, password)) {
                user = loginService.getUser(login);
                System.out.println("You logged in");

                String cookieSessionId = cookieHandler.generateCookieSessionId(httpExchange);
                HttpCookie cookie = new HttpCookie("session_id", cookieSessionId);
                httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());

                cookieHandler.addCookie(user.getId(), cookieSessionId);


                redirectToUserSpecificPage(httpExchange);
            } else {
                getResponse("templates/loginpage.twig", true, httpExchange);
            }
        }
        if (method.equals("GET")) {
            getResponse("templates/loginpage.twig", false, httpExchange);
        }
        if(method.equals("POST")) {
            getResponse("templates/loginpage.twig", true, httpExchange);
        }

    }

    private void redirectToUserSpecificPage(HttpExchange httpExchange) throws IOException {
        if (user.getUserTypeId().equals(3)) {
            httpExchange.getResponseHeaders().add("Location", "/student/home");
            httpExchange.sendResponseHeaders(303, 0);
        } else if (user.getUserTypeId().equals(2)) {
            httpExchange.getResponseHeaders().add("Location", "/mentor/home");
            httpExchange.sendResponseHeaders(303, 0);
        } else if (user.getUserTypeId().equals(1)) {
            httpExchange.getResponseHeaders().add("Location", "/admin/home");
            httpExchange.sendResponseHeaders(303, 0);
        }
    }

    private void getResponse(String path, boolean isWrongInput, HttpExchange httpExchange) throws IOException {
        String response = modelResponse(path, isWrongInput);
        sendResponse(response, httpExchange);
    }

    private void sendResponse(String response, HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String modelResponse(String path, boolean isWrongInput) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(path);
        JtwigModel model = JtwigModel.newModel();
        if (isWrongInput) {
            String wrongInputText = "<p>Incorrect login or password</p>";
            //todo - fix twig modelling
            model.with("wrongInputText", wrongInputText);
        }
        return template.render(model);
    }
}
