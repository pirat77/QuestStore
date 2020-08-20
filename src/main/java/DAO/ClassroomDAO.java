package DAO;

import SQL.SQLDao;
import model.Entry;
import model.groups.Classroom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ClassroomDAO  extends SQLDao<Classroom> implements Dao<Classroom> {

    ClassroomDAO(){
        super("classrooms", new String[]{});
    }

    @Override
    protected Entry[] objectToArray(Classroom classroom) {
        Entry id = new Entry("id", Integer.toString(classroom.getId()));
        Entry name = new Entry("name", classroom.getName());
        return new Entry[]{id, name};
    }

    @Override
    public void update(Classroom classroom) throws SQLException, ClassNotFoundException, ParseException {
        updateRecord(objectToArray(classroom));
    }

    @Override
    public void remove(Classroom classroom) throws SQLException, ClassNotFoundException, ParseException {
        removeRecord(new Entry("id", Integer.toString(classroom.getId()))); }

    @Override
    public void insert(Classroom classroom) throws SQLException, ClassNotFoundException, ParseException { insertRecord(objectToArray(classroom)); }

    @Override
    public List<Classroom> getObjects(Entry entry) throws SQLException, ClassNotFoundException, ParseException {
        List<Classroom> classrooms = new ArrayList<>();
        ResultSet resultSet = getRecords(entry);
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
