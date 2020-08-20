package DAO;

import SQL.SQLDao;
import model.Entry;
import model.users.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO  extends SQLDao<User> implements Dao<User> {

    public UserDAO(){
        super("users", new String[]{"id", "login", "password", "first_name", "last_name", "is_active", "user_type_id", "student_id"});
    }

    @Override
    protected Entry[] objectToArray(User user) {
        Entry id = new Entry("id", Integer.toString(user.getId()));
        Entry login = new Entry("login", user.getLogin());
        Entry password = new Entry("password", user.getPassword());
        Entry first_name = new Entry("first_name", user.getFirstName());
        Entry last_name = new Entry("last_name", user.getLastName());
        Entry is_active = new Entry("is_active", Boolean.toString(user.isActive()));
        Entry student_id = new Entry("student_id", Integer.toString(user.getStudentId()));
        String user_type_id_string = "";
        switch (user.getClass().getSimpleName()){
            case "Student":
                user_type_id_string = "3";
                break;
            case "Mentor":
                user_type_id_string = "2";
                break;
            case "Admin":
                user_type_id_string = "1";
                break;
        }
        Entry user_type_id = new Entry("user_type_id", user_type_id_string);
        return new Entry[]{id, login, password, first_name, last_name, is_active, user_type_id, student_id};
    }

    @Override
    public void update(User user) {
        updateRecord(objectToArray(user));
    }

    @Override
    public void remove(User user) {
        removeRecord(new Entry("id", Integer.toString(user.getId()))); }

    @Override
    public void insert(User user) { insertRecord(objectToArray(user)); }

    @Override
    public List<User> getObjects(Entry entry) {
        List<User> users = new ArrayList<>();
        ResultSet resultSet = getRecords(entry);
        try {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setActive(resultSet.getBoolean("is_active"));
                user.setUserTypeId(resultSet.getInt("user_type_id"));
                user.setStudentId(resultSet.getInt("student_id"));
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }
}
