import com.sun.net.httpserver.HttpServer;
import handler.CookieHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AppTest {
    @Mock
    static HttpServer server;
    static CookieHandler cookieHandler;

    @Test
    void should_ReturnExecuteServerMethods_WhenAppSetPathsCalled() {
        App.setPaths(cookieHandler, server);
        Mockito.verify(server, times(9)).createContext(anyString(), any());
        Mockito.verify(server, times(1)).setExecutor(any());
        Mockito.verify(server, times(1)).start();
    }
}
