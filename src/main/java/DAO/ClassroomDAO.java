package DAO;
import SQL.SQLDao;
import model.groups.Classroom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassroomDAO  extends SQLDao<Classroom> implements Dao<Classroom> {

    ClassroomDAO(){
        super("classrooms", new String[]{});
    }

    @Override
    protected String[] objectToArray(Classroom classroom) {
        String id = Integer.toString(classroom.getId());
        String name = classroom.getName();
        return new String[]{id, name};
    }

    @Override
    public void update(Classroom classroom) {
        updateRecord(objectToArray(classroom));
    }

    @Override
    public void remove(Classroom classroom) { removeRecord(Integer.toString(classroom.getId())); }

    @Override
    public void insert(Classroom classroom) { insertRecord(objectToArray(classroom)); }

    @Override
    public List<Classroom> getObjects(String columnName, String columnValue) {
        List<Classroom> classrooms = new ArrayList<>();
        ResultSet resultSet = getRecords(columnName, columnValue);
        try {
            while (resultSet.next()) {
                Classroom classroom = new Classroom();
                classroom.setId(resultSet.getInt("id"));
                classroom.setName(resultSet.getString("name"));
                classrooms.add(classroom);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return classrooms;
    }
}
