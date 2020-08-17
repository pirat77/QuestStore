package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import service.LoginService;


import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        LoginService loginService = new LoginService();
        //CookieHandler cookieHandler = new CookieHandler();

        String method = httpExchange.getRequestMethod();
        String response = "";

        if (method.equals("GET")){
            JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/loginpage.twig");
            JtwigModel model = JtwigModel.newModel();
            response = template.render(model);

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map inputs = parseFormData(formData);

            String login = inputs.get("login").toString();
            String password = inputs.get("password").toString();

            if (loginService.loginChecker(login, password)) {
                System.out.println("you logged in");
                //cookieHandler.setCookieNewExpireDateToActiveSession(httpExchange);
                //cookieHandler.setUserIdToCookieInDB(user, httpExchange);
                httpExchange.getResponseHeaders().add("Location", "/student/homepage");
                httpExchange.sendResponseHeaders(303, 0);
            }


//            httpExchange.getResponseHeaders().add("Location", "/logout");
//            httpExchange.sendResponseHeaders(303, response.getBytes().length);
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