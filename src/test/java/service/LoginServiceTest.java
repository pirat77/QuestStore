package service;

import DAO.UserDAO;
import model.Entry;
import model.users.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    private String password = "jasio";
    private String login = "jasio";
    private LoginService loginService;
    private static Entry properEntry;
    private static Entry invalidEntry;
    private static User user;

    @Mock
    private UserDAO mockUserDAO;

    @BeforeAll
    static void setUp() {
        properEntry = new Entry("login","jasio");
        invalidEntry = new Entry("login","invalidLogin");
        user = new User();
        user.setId(1);
        user.setLogin("jasio");
        user.setPassword("jasio");
        user.setFirstName("jan");
        user.setLastName("mikos");
        user.setStudentId(1);
        user.setActive(true);
        user.setUserTypeId(3);
    }

    @BeforeEach
    public void set() {
        loginService = LoginService.getTestInstance(mockUserDAO);
    }

    @Test
    public void testShouldReturnUser() {
        List<User> listWithExpectedUser = Collections.singletonList(user);
        when(mockUserDAO.getObjects(properEntry)).thenReturn(listWithExpectedUser);
        User actualUser = loginService.getUser(login);
        User expectedUser = user;
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testShouldReturnTrue_WhenUserIsValid() {
        List<User> listWithExpectedUser = Collections.singletonList(user);
        when(mockUserDAO.getObjects(properEntry)).thenReturn(listWithExpectedUser);
        assertTrue(loginService.checkUser(user.getLogin(), user.getPassword()));
    }

    @Test
    public void testShouldReturnFalse_WhenUserIsAbsent() {
        when(mockUserDAO.getObjects(invalidEntry)).thenReturn(Collections.emptyList());
        assertFalse(loginService.checkUser("invalidLogin", password));
    }

    @Test
    public void testShouldReturnFalse_WhenPasswordIsIncorrect() {
        List<User> listWithExpectedUser = Collections.singletonList(user);
        when(mockUserDAO.getObjects(properEntry)).thenReturn(listWithExpectedUser);
        assertFalse(loginService.checkUser(login, "incorrectPassword"));
    }
}