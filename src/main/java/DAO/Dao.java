package DAO;
import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {

    void update(T object) throws SQLException, ClassNotFoundException;

    void remove(T object) throws SQLException, ClassNotFoundException;

    void insert(T object) throws SQLException, ClassNotFoundException;

    List<T> getObjects(String columnName, String columnValue) throws SQLException, ClassNotFoundException;
}
