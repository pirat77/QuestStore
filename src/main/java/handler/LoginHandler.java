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
import java.sql.SQLException;
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
        try {
            user = cookieHandler.checkCookie(httpExchange);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(user != null){
            if(user.getUserTypeId().equals(3)){
                httpExchange.getResponseHeaders().add("Location", "/student/home");
                httpExchange.sendResponseHeaders(303, 0);
            }
            else if(user.getUserTypeId().equals(2)){
                httpExchange.getResponseHeaders().add("Location", "/mentor/home");
                httpExchange.sendResponseHeaders(303, 0);
            }
            else if(user.getUserTypeId().equals(1)){
                httpExchange.getResponseHeaders().add("Location", "/admin/home");
                httpExchange.sendResponseHeaders(303, 0);
            }
        }

        String method = httpExchange.getRequestMethod();
        String response;

        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map inputs = parseFormData(formData);
            String login = inputs.get("login").toString();
            String password = inputs.get("password").toString();
            try {
                if (LoginService.getInstance().checkUser(login, password)) {
                    user = LoginService.getInstance().getUser(login);
                    System.out.println("You logged in");

                    String cookieSessionId = cookieHandler.generateCookieSessionId(httpExchange);
                    HttpCookie cookie = new HttpCookie("session_id", cookieSessionId);
                    httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());

                    cookieHandler.addCookie(user.getId(), cookieSessionId);
                    cookieHandler.setCookieNewExpireDate(cookieSessionId);
//                    System.out.println("getting user");
//                    user = cookieHandler.checkCookie(httpExchange);
                    System.out.println("User type id: " + user.getUserTypeId());
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
                else{
                    JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/loginpage.twig");
                    JtwigModel model = JtwigModel.newModel();
                    response = template.render(model);

                    httpExchange.sendResponseHeaders(200, response.length());
                    OutputStream os = httpExchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (method.equals("GET")){
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/loginpage.twig");
            JtwigModel model = JtwigModel.newModel();
            response = template.render(model);

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        if(method.equals("POST")) {
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/loginpage.twig");
            JtwigModel model = JtwigModel.newModel();
            String wrongInputText = "<p>Wrong login or password</p>";
            model.with("wrongInputText", wrongInputText);
            response = template.render(model);

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

    }

    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }
}