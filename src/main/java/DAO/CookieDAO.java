package DAO;

import SQL.SQLDao;
import model.Cookie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CookieDAO extends SQLDao<Cookie> implements Dao<Cookie> {
    public CookieDAO() {
        super("cookies", new String[]{"session_id", "expire_date", "user_id"});
    }

    @Override
    protected String[] objectToArray(Cookie cookie) {
        String pattern = "yyyyMMdd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return new String[]{cookie.getSessionId(), dateFormat.format(cookie.getExpireDate()), String.valueOf(cookie.getUserId())};
    }


    @Override
    public void update(Cookie cookie) {
        updateRecord(objectToArray(cookie));
    }

    @Override
    public void remove(Cookie cookie) {
        removeRecord(cookie.getSessionId());
    }

    @Override
    public void insert(Cookie cookie) {
        insertRecord(objectToArray(cookie));
    }

    @Override
    public List<Cookie> getObjects(String columnName, String columnValue) {
        List<Cookie> cookies = new ArrayList<>();
        ResultSet resultSet = getRecords(columnName, columnValue);
        try {
            while (resultSet.next()) {
                Cookie cookie = null;
                cookie.setSessionId(resultSet.getString("session_id"));
                cookie.setExpireDate(resultSet.getDate("expire_date"));
                cookie.setUserId(resultSet.getInt("user_id"));
                cookies.add(cookie);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cookies;
    }

}
