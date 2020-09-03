package service;

import DAO.CookieDAO;
import DAO.UserDAO;
import com.sun.net.httpserver.HttpExchange;
import model.Cookie;
import model.Entry;
import model.users.User;

import java.net.HttpCookie;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CookieService {
    private CookieDAO cookieDAO;
    private UserDAO userDAO;

    public CookieService(CookieDAO cookieDAO, UserDAO userDAO){
        this.cookieDAO = cookieDAO;
        this.userDAO = userDAO;
    }

    public boolean isCookieIsActive(String cookieSessionId) {
        List<Cookie> cookies = cookieDAO.getObjects(new Entry("session_id", cookieSessionId));
        if(cookies.size() == 0) return false;
        Cookie cookie =  cookies.get(0);
        Date currentDate = getCurrentDate();
        return (cookie.getExpireDate().getTime() - currentDate.getTime()) >= 0;
    }

    public Date getCurrentDate() {
        return new Date(Calendar.getInstance().getTime().getTime());
    }

    public Date getExpireDate (Date currentDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 1);
        return new Date(calendar.getTimeInMillis());
    }

    public String formatTime(Date date){
        String pattern = "dd/MM/yyyy HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public void setCookieNewExpireDate(String cookieSessionId) {
        Date currentDate = getCurrentDate();
        Date expireDate = getExpireDate(currentDate);

        Cookie cookie = cookieDAO.getObjects(new Entry("session_id", cookieSessionId)).get(0);
        cookie.setExpireDate(expireDate);
        cookieDAO.update(cookie);
    }

    public User getUserByCookieSessionId(String cookieSessionId) {
        Cookie cookie =  cookieDAO.getObjects(new Entry("session_id", cookieSessionId)).get(0);
        String userId = String.valueOf(cookie.getUserId());
        User user = userDAO.getObjects(new Entry("id", userId)).get(0);
        return user;
    }

    public void addCookie(Cookie cookie) {
        cookieDAO.insert(cookie);
    }

    public String generateCookieSessionIdAndAddToResponse(HttpExchange httpExchange, String sessionCookieName) {
        UUID uuid = UUID.randomUUID();
        HttpCookie cookie = new HttpCookie(sessionCookieName, uuid.toString());
        httpExchange.getResponseHeaders().add("Set-Cookie", cookie.toString());
        return uuid.toString();
    }
}