package DAO;

import SQL.SQLDao;
import model.groups.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO  extends SQLDao<Category> implements Dao<Category> {

    CategoryDAO(){
        super("categories", new String[]{});
    }

    @Override
    protected String[] objectToArray(Category category) {
        String id = Integer.toString(category.getId());
        String name =  category.getName();
        return new String[]{id, name};
    }

    @Override
    public void update(Category category) throws SQLException, ClassNotFoundException {
        updateRecord(objectToArray(category));
    }

    @Override
    public void remove(Category category) throws SQLException, ClassNotFoundException { removeRecord(Integer.toString(category.getId())); }

    @Override
    public void insert(Category category) throws SQLException, ClassNotFoundException { insertRecord(objectToArray(category)); }

    @Override
    public List<Category> getObjects(String columnName, String columnValue) throws SQLException, ClassNotFoundException {
        List<Category> categories = new ArrayList<>();
        ResultSet resultSet = getRecords(columnName, columnValue);
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
