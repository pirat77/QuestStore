package handler;

import com.sun.net.httpserver.HttpExchange;
import helper.CookieHelper;
import model.Cookie;
import model.users.User;
import service.CookieService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class CookieHandler {
    final String SESSION_COOKIE_NAME = "session_id";
    private CookieService cookieService;
    private CookieHelper cookieHelper;

    public CookieHandler(CookieService cookieService, CookieHelper cookieHelper){
        this.cookieService = cookieService;
        this.cookieHelper = cookieHelper;
    }

    public User checkCookie(HttpExchange httpExchange) {
        Optional<HttpCookie> cookieList = getSessionIdCookie(httpExchange);
        if (cookieList.isPresent()) {  // Cookie already exists
            String cookieSessionId = cookieList.get().getValue();
            cookieSessionId = cookieSessionId.substring(1, cookieSessionId.length()-1);
            if (cookieService.isCookieIsActive(cookieSessionId)){
                return cookieService.getUserByCookieSessionId(cookieSessionId);
            }
        }
        return null;
    }

    private void sendResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    public void addCookie(int userId, String sessionId) {
        Cookie cookie = new Cookie();
        cookie.setSessionId(sessionId);
        cookie.setUserId(userId);
        Date currentDate = cookieService.getCurrentDate();
        Date expireDate = cookieService.getExpireDate(currentDate);
        cookie.setExpireDate(expireDate);
        cookieService.addCookie(cookie);
    }

    public Optional<HttpCookie> getSessionIdCookie(HttpExchange httpExchange){
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        List<HttpCookie> cookies = cookieHelper.parseCookies(cookieStr);
        return cookieHelper.findCookieByName(SESSION_COOKIE_NAME, cookies);
    }

    public void setCookieNewExpireDate(String cookieSessionId) {
        cookieService.setCookieNewExpireDate(cookieSessionId);
    }


    public String generateCookieSessionId(HttpExchange httpExchange){
        return cookieService.generateCookieSessionIdAndAddToResponse(httpExchange, SESSION_COOKIE_NAME);
    }
}
