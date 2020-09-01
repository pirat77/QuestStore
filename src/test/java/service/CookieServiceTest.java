package service;

import DAO.CookieDAO;
import DAO.UserDAO;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import model.Cookie;
import model.Entry;
import model.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CookieServiceTest {

    private CookieDAO cookieDAO;
    private UserDAO userDAO;
    private CookieService cookieService;

    @BeforeEach
    public void setup() {
        cookieDAO = mock(CookieDAO.class);
        userDAO = mock(UserDAO.class);
        cookieService = new CookieService(cookieDAO, userDAO);
    }

    @Test
    public void should_ReturnTrue_WhenIsCookieIsActiveGetsValidCookieSessionId() {
        Cookie cookie = mock(Cookie.class);
        when(cookie.getExpireDate()).thenReturn(new Date(System.currentTimeMillis() + 50000L));
        when(cookieDAO.getObjects(any(Entry.class)))
                .thenReturn(Collections.singletonList(cookie));
        assertTrue(cookieService.isCookieIsActive("true"));
    }

    @Test
    public void should_ReturnFalse_WhenIsCookieIsActiveGetsInvalidCookieSessionId() {
        when(cookieDAO.getObjects(any(Entry.class))).thenReturn(Collections.emptyList());
        assertFalse(cookieService.isCookieIsActive("false"));
    }

    @Test
    public void should_ReturnNotNull_WhenInvokeGetCurrentDate() {
        assertNotNull(cookieService.getCurrentDate());
    }

    @Test
    public void should_ReturnFutureDate_WhenInvokeGetExpireDate() {
        Date date = new Date(System.currentTimeMillis());
        Date expireDate = cookieService.getExpireDate(date);
        assertTrue(date.before(expireDate));
    }

    @Test
    public void should_ReturnProperFormatTimeString_WhenInvokeFormatTime() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String formattedDate = String.format("%02d/%02d/%d %02d:%02d:%02d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
        assertEquals(formattedDate, cookieService.formatTime(date));
    }

    @Test
    public void should_ThrowIndexOutOfBoundException_WhenInvokeSetCookieNewExpireDateWithNoCookieToSet() {
        assertThrows(IndexOutOfBoundsException.class, () -> cookieService.setCookieNewExpireDate("seesionId"));
    }

    @Test
    public void should_InvokeCookieDaoUpdate_WhenInvokeSetCookieNewExpireDate() {
        Cookie cookie = mock(Cookie.class);
        when(cookieDAO.getObjects(any(Entry.class)))
                .thenReturn(Collections.singletonList(cookie));
        cookieService.setCookieNewExpireDate("seesionId");
        verify(cookieDAO, times(1)).getObjects(any(Entry.class));
        verify(cookie, times(1)).setExpireDate(any(Date.class));
        verify(cookieDAO, times(1)).update(any(Cookie.class));
    }

    @Test
    public void should_ThrowIndexOutOfBoundException_WhenInvokedGetUserBySessionIdWithoutUsers() {
        assertThrows(IndexOutOfBoundsException.class,
                () -> cookieService.getUserByCookieSessionId("invalidId"));
    }

    @Test
    public void should_ReturnedUserIsNotNull_WhenInvokeGetUserByCookieSessionId() {
        Cookie cookie = mock(Cookie.class);
        when(cookie.getUserId()).thenReturn(1234);
        when(cookieDAO.getObjects(any(Entry.class)))
                .thenReturn(Collections.singletonList(cookie));
        when(userDAO.getObjects(any(Entry.class)))
                .thenReturn(Collections.singletonList(mock(User.class)));
        assertNotNull(cookieService.getUserByCookieSessionId("cookieSessionId"));
    }


    @Test
    public void should_InvokeCookieDaoInsert_WhenInvokeAddCookie() {
        cookieService.addCookie(mock(Cookie.class));
        verify(cookieDAO).insert(any(Cookie.class));
    }

    @Test
    public void should_GenerateAndAddCookieSession_WhenInvokeGenerateCookieSessionIdAndAddToResponse() {
        HttpExchange httpExchange = mock(HttpExchange.class);
        Headers headers = spy(Headers.class);
        when(httpExchange.getResponseHeaders()).thenReturn(headers);
        String uuid = cookieService.generateCookieSessionIdAndAddToResponse(httpExchange, "sessionCookieName");
        assertNotNull(uuid);
        assertNotNull(headers.getFirst("Set-Cookie"));
    }

}