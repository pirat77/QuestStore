import com.sun.net.httpserver.HttpServer;
import handler.CookieHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppTest {
    @Mock
    static HttpServer server;
    static CookieHandler cookieHandler;

    @Test
    void should_ExecuteServerMethods_WhenAppSetPathsCalled() {
        App.setPaths(cookieHandler, server);
        assertAll(
                () -> verify(server, times(1)).createContext(eq("/static"), any()),
                () -> verify(server, times(1)).createContext(eq("/images"), any()),
                () -> verify(server, times(1)).createContext(eq("/login"), any()),
                () -> verify(server, times(1)).createContext(eq("/student/artifact"), any()),
                () -> verify(server, times(1)).createContext(eq("/student/home"), any()),
                () -> verify(server, times(1)).createContext(eq("/student/quest"), any()),
                () -> verify(server, times(1)).createContext(eq("/student/raid"), any()),
                () -> verify(server, times(1)).createContext(eq("/student/student"), any()),
                () -> verify(server, times(1)).createContext(eq("/student/store"), any()),
                () -> verify(server, times(1)).setExecutor(any()),
                () -> verify(server, times(1)).start()
        );
    }
}
