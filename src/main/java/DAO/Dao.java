package DAO;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface Dao<T> {

    void update(T object) throws SQLException, ClassNotFoundException, ParseException;

    void remove(T object) throws SQLException, ClassNotFoundException, ParseException;

    void insert(T object) throws SQLException, ClassNotFoundException, ParseException;

    List<T> getObjects(String columnName, String columnValue) throws SQLException, ClassNotFoundException, ParseException;
}
