package service;

import DAO.UserDAO;
import model.Entry;
import model.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    private String password = "jasio";
    private String login = "jasio";
    private LoginService loginService;
//    @Mock
    private Entry properEntry;
    private Entry invalidEntry;
//    @Mock
    private User user;

    @Mock
    private UserDAO mockUserDAO;

    @BeforeEach
    public void setUp() {
//        mockUserDAO = new UserDAO();
//        mockUserDAO = mock(UserDAO.class);
        properEntry = new Entry("login","jasio");
        invalidEntry = new Entry("login","invalidLogin");
//        mockEntry = mock(Entry.class);
//        when(mockEntry.getColumnValue()).thenReturn("login");
//        when(mockEntry.getColumnName()).thenReturn("jasio");
//        mockEntry.setColumnValue("login");
//        mockEntry.setColumnName("jasio");

//        mockUser = new User();
//        mockUser = mock(User.class);
        user = new User();
        user.setId(1);
        user.setLogin("jasio");
        user.setPassword("jasio");
        user.setFirstName("jan");
        user.setLastName("mikos");
        user.setStudentId(1);
        user.setActive(true);
        user.setUserTypeId(3);

        List<User> listWithExpectedUser = Collections.singletonList(user);
        when(mockUserDAO.getObjects(properEntry)).thenReturn(listWithExpectedUser);
//        when(mockUserDAO.getObjects(any()).thenReturn(Collections.emptyList()));
//        when(mockUserDAO.getObjects(invalidEntryLogin)).thenReturn(Collections.emptyList());
//        loginService = new LoginService(mockUserDAO);
        loginService = LoginService.getTestInstance(mockUserDAO);
    }

    @Test
    public void testShouldReturnUser() {
        User actualUser = loginService.getUser(login);
        User expectedUser = user;
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testShouldReturnTrue_WhenUserIsValid() {
//        when(user.getPassword()).thenReturn(password);
        assertTrue(loginService.checkUser(user.getLogin(), user.getPassword()));
    }

    //TODO in progress ,dolozyc casea z roznymi Entry przy tej samej metodzie (mockUserDao.getobjects), dla ponizszych 2 testow kiedy podane beda niewlasciwe dane
    @Test
    public void testShouldReturnFalse_WhenUserIsAbsent() {
//        when(mockUser.getPassword()).thenReturn(password);
//        when(user.getLogin()).thenReturn(login);
//        when(mockUserDAO.getObjects(invalidEntry)).thenReturn(Collections.emptyList());
        assertFalse(loginService.checkUser(login, password));
    }

    @Test
    public void testShouldReturnFalse_WhenPasswordIsIncorrect() {
//        when(user.getPassword()).thenReturn(password);
//        when(mockUser.getLogin()).thenReturn(login);
        assertFalse(loginService.checkUser(login, "incorrectPassword"));
    }
}