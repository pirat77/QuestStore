package DAO;

import SQL.SQLDao;
import model.Cookie;
import model.Entry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CookieDAO extends SQLDao<Cookie> implements Dao<Cookie> {
    public CookieDAO() {
        super("cookies", new String[]{"session_id", "expire_date", "user_id"});
    }

    @Override
    protected Entry[] objectToArray(Cookie cookie) {
        String pattern = "yyyyMMdd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Entry session_id = new Entry("session_id", cookie.getSessionId());
        Entry expire_date = new Entry("expire_date", dateFormat.format(cookie.getExpireDate()));
        Entry user_id = new Entry("user_id", String.valueOf(cookie.getUserId()));
        return new Entry[]{session_id, expire_date, user_id};
    }


    @Override
    public void update(Cookie cookie) {
        updateRecord(objectToArray(cookie));
    }

    @Override
    public void remove(Cookie cookie) {
        removeRecord(new Entry("id", cookie.getSessionId()));
    }

    @Override
    public void insert(Cookie cookie) {
        insertRecord(objectToArray(cookie));
    }

    @Override
    public List<Cookie> getObjects(Entry entry) {
        List<Cookie> cookies = new ArrayList<>();
        ResultSet resultSet = getRecords(entry);
        try {
            while (resultSet.next()) {
                Cookie cookie = new Cookie();
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
