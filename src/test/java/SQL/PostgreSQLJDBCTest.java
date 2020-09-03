package SQL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * In PostgreSQLJDBCTest a correct connection to the database is being checked,
 * as well as its disconnection. Tests cover 75% of the code.
 *
 * The code refactor contained only a small code extension that was meant to be testable.
 */
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