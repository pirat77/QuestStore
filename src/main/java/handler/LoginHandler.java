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
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {
    CookieHandler cookieHandler;
    User user;

    public LoginHandler(CookieHandler cookieHandler){
        this.cookieHandler = cookieHandler;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        user = cookieHandler.checkCookie(httpExchange);
        if(user != null){
            checkUser(httpExchange);
        }

        String method = httpExchange.getRequestMethod();

        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map inputs = parseFormData(formData);
            String login = inputs.get("login").toString();
            String password = inputs.get("password").toString();
            if (LoginService.getInstance().checkUser(login, password)) {
                user = LoginService.getInstance().getUser(login);
                System.out.println("You logged in");

                String cookieSessionId = cookieHandler.generateCookieSessionId(httpExchange);
                HttpCookie cookie = new HttpCookie("session_id", cookieSessionId);
                httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());

                cookieHandler.addCookie(user.getId(), cookieSessionId);

                checkUser(httpExchange);
            }
            else{
                String wrongInputText = "<p>Incorrect login or password</p>";
                getResponse("templates.loginpage/twig", wrongInputText, httpExchange);

            }
        }

        if (method.equals("GET")){
            getResponse("templates/loginpage.twig", "", httpExchange);
        }

        if(method.equals("POST")) {
            String wrongInputText = "<p>Incorrect login or password</p>";
            getResponse("templates.loginpage/twig", wrongInputText, httpExchange);
        }

    }

    private void checkUser(HttpExchange httpExchange) throws IOException {
        if(user.getUserTypeId().equals(3)){
            httpExchange.getResponseHeaders().add("Location", "/student/home");
            httpExchange.sendResponseHeaders(303, 0);
        }
        else if (user.getUserTypeId().equals(2)){
            httpExchange.getResponseHeaders().add("Location", "/mentor/home");
            httpExchange.sendResponseHeaders(303, 0);
        }
        else if (user.getUserTypeId().equals(1)){
            httpExchange.getResponseHeaders().add("Location", "/admin/home");
            httpExchange.sendResponseHeaders(303, 0);
        }
    }

    private void getResponse(String path, String wrongInputText, HttpExchange httpExchange) throws IOException {
        String response = modelResponse(path, wrongInputText);
        sendResponse(response, httpExchange);
    }

    private void sendResponse(String response, HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String modelResponse(String path, String wrongInputText){
        JtwigTemplate template = JtwigTemplate.classpathTemplate(path);
        JtwigModel model = JtwigModel.newModel();
        model.with("wrongInputText", wrongInputText);
        return template.render(model);
    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }
}