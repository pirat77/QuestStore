package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import model.users.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.LoginService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginHandlerTest {
    @Mock
    static CookieHandler cookieHandler;
    @Mock
    static LoginService loginService;
    @Mock
    static HttpExchange httpExchange;
    @Mock
    static OutputStream outputStream;
    @InjectMocks
    static LoginHandler loginHandler;

    static Headers headers;
    static User user;

    void setGeneralMocks() {
        when(cookieHandler.checkCookie(httpExchange)).thenReturn(user);
        when(httpExchange.getResponseHeaders()).thenReturn(headers);
    }

    @BeforeEach
    void setUp() {
        user = UserFactory.getStudent();
        headers = new Headers();
        loginHandler = new LoginHandler(cookieHandler, loginService);
    }

    @AfterEach
    void tearDown() {
        user = null;
        headers = null;
        loginHandler = null;
    }

    @Test
    void should_RedirectToStudent_WhenMethodIsGetAndStudentInCookie() throws IOException {
        UserFactory.setStudent();
        should_RedirectToUserSpecificPage_WhenMethodIsGetAndUserInCookie("/student/home");
    }

    @Test
    void should_RedirectToMentor_WhenMethodIsGetAndMentorInCookie() throws IOException {
        UserFactory.setMentor();
        should_RedirectToUserSpecificPage_WhenMethodIsGetAndUserInCookie("/mentor/home");
    }

    @Test
    void should_RedirectToAdmin_WhenMethodIsGetAndAdminInCookie() throws IOException {
        UserFactory.setAdmin();
        should_RedirectToUserSpecificPage_WhenMethodIsGetAndUserInCookie("/admin/home");
    }

    void should_RedirectToUserSpecificPage_WhenMethodIsGetAndUserInCookie(String path) throws IOException {
        setGeneralMocks();

        loginHandler.handle(httpExchange);

        assertAll(
                () -> assertEquals(path, headers.get("Location").get(0)),
                () -> verify(httpExchange, times(1)).sendResponseHeaders(303, 0)
        );
    }

    @Test
    void should_SendResponse_WhenMethodIsGetAndNoUserInCookie() throws IOException {
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(cookieHandler.checkCookie(httpExchange)).thenReturn(user);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);
        when(cookieHandler.checkCookie(httpExchange)).thenReturn(null);

        user = null;
        loginHandler.handle(httpExchange);

        assertAll(
                () -> verify(httpExchange, times(1)).getRequestMethod(),
                () -> verify(httpExchange, times(1)).sendResponseHeaders(200, 656),
                () -> verify(outputStream, times(1)).write(any()),
                () -> verify(outputStream, times(1)).close()
        );
    }

    @Test
    void should_RedirectToStudent_WhenMethodIsPostAndStudentCredentials() throws IOException {
        setGeneralMocks();
        when(httpExchange.getRequestMethod()).thenReturn("POST");
        when(loginService.getUser(any())).thenReturn(user);
        when(httpExchange.getRequestBody()).thenReturn(new ByteArrayInputStream("login=jasio&password=jasio".getBytes()));
        when(loginService.checkUser(anyString(), any())).thenReturn(true);
        when(cookieHandler.generateCookieSessionId(any())).thenReturn("someSessionId");
        when(cookieHandler.checkCookie(httpExchange)).thenReturn(null);

        loginHandler.handle(httpExchange);

        assertAll(
                () -> assertEquals("session_id=\"someSessionId\"", headers.get("Set-Cookie").get(0)),
                () -> assertEquals("/student/home", headers.get("Location").get(0)),
                () -> verify(cookieHandler, times(1)).generateCookieSessionId(httpExchange),
                () -> verify(httpExchange, times(2)).getResponseHeaders(),
                () -> verify(cookieHandler, times(1)).addCookie(1, "someSessionId"),
                () -> verify(httpExchange, times(1)).sendResponseHeaders(303, 0)
        );
    }

    @Test
    void should_ReloadLoginPage_WhenMethodIsPostAndCredentialsInvalid() throws IOException {
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(cookieHandler.checkCookie(httpExchange)).thenReturn(user);
        when(cookieHandler.checkCookie(httpExchange)).thenReturn(null);
        when(httpExchange.getRequestMethod()).thenReturn("POST");
        when(httpExchange.getRequestBody()).thenReturn(new ByteArrayInputStream("login=jasio&password=jasio".getBytes()));
        when(loginService.checkUser(anyString(), any())).thenReturn(false);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        loginHandler.handle(httpExchange);

        assertAll(
                () -> verify(httpExchange, times(1)).sendResponseHeaders(200, 690),
                () -> verify(outputStream, times(1)).write(any()),
                () -> verify(outputStream, times(1)).close()
        );
    }

    //=========== private methods ===========

    @Test
    void should_RenderResponse_WhenCalledWithLoginPageTwigAndValidInput() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String expected = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>HackNet Login</title>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"../static/css/loginstyle.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"container\">\n" +
                "    <div id=\"window\">\n" +
                "        <form method=\"POST\">\n" +
                "            <p>Welcome to HackNet</p>\n" +
                "            <p>>provide Your data:</p>\n" +
                "            <p>>USERNAME:</p>\n" +
                "            <p>><input type=\"text\" name=\"login\"></p>\n" +
                "            <p>>PASSWORD:</p>\n" +
                "            <p>><input type=\"password\" name=\"password\"></p>\n" +
                "            <p>><button class=\"button\" value=\"submit\">submit</button></p>\n" +
                "            \n" +
                "        </form>\n" +
                "\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        should_RenderResponse_WhenCalledWithLoginPageTwig(expected, false);
    }

    void should_RenderResponse_WhenCalledWithLoginPageTwig(String expected, boolean isWrongInput) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method modelResponse = LoginHandler.class.getDeclaredMethod("modelResponse", String.class, boolean.class);
        modelResponse.setAccessible(true);
        String actual = (String) modelResponse.invoke(loginHandler, "templates/loginpage.twig", isWrongInput);

        assertEquals(expected, actual);
    }

    @Test
    void should_RenderResponse_WhenCalledWithLoginPageTwigAndInvalidInput() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String expected = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>HackNet Login</title>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"../static/css/loginstyle.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"container\">\n" +
                "    <div id=\"window\">\n" +
                "        <form method=\"POST\">\n" +
                "            <p>Welcome to HackNet</p>\n" +
                "            <p>>provide Your data:</p>\n" +
                "            <p>>USERNAME:</p>\n" +
                "            <p>><input type=\"text\" name=\"login\"></p>\n" +
                "            <p>>PASSWORD:</p>\n" +
                "            <p>><input type=\"password\" name=\"password\"></p>\n" +
                "            <p>><button class=\"button\" value=\"submit\">submit</button></p>\n" +
                "            <p>Incorrect login or password</p>\n" +
                "        </form>\n" +
                "\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        should_RenderResponse_WhenCalledWithLoginPageTwig(expected, true);
    }

    @Test
    void should_ReturnStringMap_WhenCalledWithValidLoginAndPassword() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String formData = "login=jasio&password=jasio";
        Map<String, String> expected = new HashMap<>();
        expected.put("password", "jasio");
        expected.put("login", "jasio");

        should_ReturnStringMap_WhenCalledWithLoginAndPasswordString(expected, formData);
    }

    void should_ReturnStringMap_WhenCalledWithLoginAndPasswordString(Map<String, String> expected, String formData) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method modelResponse = LoginHandler.class.getDeclaredMethod("parseFormData", String.class);
        modelResponse.setAccessible(true);
        Object actual = modelResponse.invoke(loginHandler, formData);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Not implemented functionality")
    @Disabled
    void should_ReturnStringMap_WhenCalledWithAmpersandInPassword() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String formData = "login=jasio&password=ja&sio";
        Map<String, String> expected = new HashMap<>();
        expected.put("password", "ja&sio");
        expected.put("login", "jasio");

        should_ReturnStringMap_WhenCalledWithLoginAndPasswordString(expected, formData);
    }

    @Test
    @DisplayName("Not implemented functionality")
    @Disabled
    void should_ReturnStringMap_WhenCalledWithEqualsInPassword() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String formData = "login=jasio&password=ja=sio";
        Map<String, String> expected = new HashMap<>();
        expected.put("password", "ja=sio");
        expected.put("login", "jasio");

        should_ReturnStringMap_WhenCalledWithLoginAndPasswordString(expected, formData);
    }

    static class UserFactory {
        static User getStudent() {
            User user = new User();
            user.setId(1);
            user.setUserTypeId(3);
            return user;
        }

        static void setStudent() {
            user.setUserTypeId(3);
        }

        static void setMentor() {
            user.setUserTypeId(2);
        }

        static void setAdmin() {
            user.setUserTypeId(1);
        }
    }
}
