package DAO;

import SQL.SQLDao;
import model.Entry;
import model.groups.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO  extends SQLDao<Category> implements Dao<Category> {

    CategoryDAO(){
        super("categories", new String[]{});
    }

    @Override
    protected Entry[] objectToArray(Category category) {
        Entry id = new Entry("id", Integer.toString(category.getId()));
        Entry name = new Entry("name", category.getName());
        return new Entry[]{id, name};
    }

    @Override
    public void update(Category category) throws SQLException, ClassNotFoundException, ParseException {
        updateRecord(objectToArray(category));
    }

    @Override
    public void remove(Category category) throws SQLException, ClassNotFoundException, ParseException {
        removeRecord(new Entry("id", Integer.toString(category.getId()))); }

    @Override
    public void insert(Category category) throws SQLException, ClassNotFoundException, ParseException { insertRecord(objectToArray(category)); }

    @Override
    public List<Category> getObjects(Entry entry) throws SQLException, ClassNotFoundException, ParseException {
        List<Category> categories = new ArrayList<>();
        ResultSet resultSet = getRecords(entry);
        try {
            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                categories.add(category);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return categories;
    }
}
