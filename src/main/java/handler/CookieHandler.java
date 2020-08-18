package handler;

import com.sun.net.httpserver.HttpExchange;
import helper.CookieHelper;
import model.Cookie;
import model.users.User;
import service.CookieService;

import java.net.HttpCookie;
import java.sql.SQLException;
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

    public User checkCookie(HttpExchange httpExchange) throws SQLException, ClassNotFoundException {
        Optional<HttpCookie> cookieList = getSessionIdCookie(httpExchange);
        if (cookieList.isPresent()) {  // Cookie already exists
            System.out.println("Cookie already exists");
            String cookieSessionId = cookieList.get().getValue();
            User userToCheck = cookieService.getUserByCookieSessionId(cookieSessionId);

            if (cookieService.checkIfCookieIsActive(cookieSessionId)){
                return userToCheck;
            }
        }
        return null;
//        else { // Create a new cookie
//            System.out.println("Cookie is being created");
//            UUID uuid = UUID.randomUUID();
//            HttpCookie cookie = new HttpCookie(SESSION_COOKIE_NAME, uuid.toString());
//            httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
//            String cookieSessionIdToAdd = uuid.toString();
//            cookieSessionIdToAdd = '"'+cookieSessionIdToAdd+'"';
//            Cookie cookieToAdd = new Cookie();
//            cookieToAdd.setSessionId(cookieSessionIdToAdd);
//            cookieToAdd.setUserId();
//            cookieService.addCookie(cookieToAdd);
//            cookieDAO.putNewCookieToDB(cookieSessionIdToAdd);
//        }
//        return null;
    }

    public void addCookie(int userId, String sessionId) throws SQLException, ClassNotFoundException {
        Cookie cookie = new Cookie();
        cookie.setUserId(userId);
        cookie.setSessionId(sessionId);
    }

    public Optional<HttpCookie> getSessionIdCookie(HttpExchange httpExchange){
        String cookieStr = httpExchange.getRequestHeaders().getFirst("Cookie");
        List<HttpCookie> cookies = cookieHelper.parseCookies(cookieStr);
        return cookieHelper.findCookieByName(SESSION_COOKIE_NAME, cookies);
    }

//    public void setCookieNewExpireDateToActiveSession(HttpExchange httpExchange) throws SQLException, ClassNotFoundException {
//        Optional<HttpCookie> cookieList = getSessionIdCookie(httpExchange);
//        String cookieSessionId = cookieList.get().getValue();
//        cookieService.setCookieNewExpireDate(cookieSessionId);
//    }
    public void setCookieNewExpireDate(String cookieSessionId) throws SQLException, ClassNotFoundException {
        cookieService.setCookieNewExpireDate(cookieSessionId);
    }

//    public void setUserIdToCookieInDB(User user, HttpExchange httpExchange) {
//        Optional<HttpCookie> cookieList = getSessionIdCookie(httpExchange);
//        String cookieSessionId = cookieList.get().getValue();
//        int studentId = user.getId();
//        cookieService.
//        cookieDAO.putUserIdToCookieInDB(studentId, cookieSessionId);
//    }
//
//    public void logout (HttpExchange httpExchange){
//        Optional<HttpCookie> cookieList = getSessionIdCookie(httpExchange);
//        String cookieSessionId = cookieList.get().getValue();
//        cookieDAO.setCookieForLogout(cookieSessionId);
//    }

    public String generateCookieSessionId(HttpExchange httpExchange){
        return cookieService.generateCookieSessionId(httpExchange, SESSION_COOKIE_NAME);
    }
}
