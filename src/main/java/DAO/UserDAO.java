package DAO;
import SQL.SQLDao;
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
    protected String[] objectToArray(User user) {
        String id = Integer.toString(user.getId());
        String login = user.getLogin();
        String password = user.getPassword();
        String first_name = user.getFirstName();
        String last_name = user.getLastName();
        String is_active = Boolean.toString(user.isActive());
        String student_id = Integer.toString(user.getStudentId());
        String user_type_id = "";
        switch (user.getClass().getSimpleName()){
            case "Studnet":
                user_type_id = "3";
                break;
            case "Mentor":
                user_type_id = "2";
                break;
            case "Admin":
                user_type_id = "1";
                break;
        }
        return new String[]{id, login, password, first_name, last_name, is_active, user_type_id, student_id};
    }

    @Override
    public void update(User user) {
        updateRecord(objectToArray(user));
    }

    @Override
    public void remove(User user) { removeRecord(Integer.toString(user.getId())); }

    @Override
    public void insert(User user) { insertRecord(objectToArray(user)); }

    @Override
    public List<User> getObjects(String columnName, String columnValue) {
        List<User> users = new ArrayList<>();
        ResultSet resultSet = getRecords(columnName, columnValue);
        try {
            while (resultSet.next()) {
                User user = User.getUserByType(resultSet.getInt("user_type_id"));
                user.setId(resultSet.getInt("id"));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setActive(resultSet.getBoolean("isActive"));
                user.setStudentId(resultSet.getInt("student_id"));
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }
}
