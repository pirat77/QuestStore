package SQL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class PostgreSQLJDBCTest {

    private PostgreSQLJDBC postgreSQLJDBC;

    @BeforeEach
    public void setUp() {
        postgreSQLJDBC = PostgreSQLJDBC.getInstance();
    }

    @AfterEach
    public void tearDown() {
        postgreSQLJDBC = null;
    }

    @Test
    public void testIsInstanceIsCreated() {
        assertNotEquals(null, postgreSQLJDBC);
    }

    @Test
    public void testShouldReturnPostgreSQLJDBCconnection() {
//        testIsPostgreSQLJDBCMethodConnection_IsValid()
        postgreSQLJDBC.connect();
        assertNotEquals(null, postgreSQLJDBC.getConnection());
    }

    @Test
    public void testShouldReturnPostgreSQLJDBCConnectionClosed() throws SQLException {
//        testIsPostgreSQLJDBCMethodDisconnect_IsValid()
        postgreSQLJDBC.connect();
        postgreSQLJDBC.disconnect();
        assertTrue(postgreSQLJDBC.getConnection().isClosed());
    }
}