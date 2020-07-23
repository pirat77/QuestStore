package DAO;

import SQL.SQLDao;
import model.Quest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestDAO  extends SQLDao<Quest> implements Dao<Quest> {

    QuestDAO(){
        super("quests", new String[]{"id", "name", "description", "value", "category_id"});
    }

    @Override
    protected String[] objectToArray(Quest quest) {
        String id = Integer.toString(quest.getId());
        String name = quest.getName();
        String description = quest.getDescription();
        String value = Integer.toString(quest.getValue());
        String category_id = Integer.toString(quest.getCategoryId());
        return new String[]{id, name, description, value, category_id};
    }

    @Override
    public void update(Quest quest) {
        updateRecord(objectToArray(quest));
    }

    @Override
    public void remove(Quest quest) {
        removeRecord(Integer.toString(quest.getId()));
    }

    @Override
    public void insert(Quest quest) {
        updateRecord(objectToArray(quest));
    }

    @Override
    public List<Quest> getObjects(String columnName, String columnValue) {
        List<Quest> users = new ArrayList<>();
        ResultSet resultSet = getRecords(columnName, columnValue);
        try {
            while (resultSet.next()) {
                Quest quest = new Quest();
                quest.setId(resultSet.getInt("id"));
                quest.setName(resultSet.getString("name"));
                quest.setDescription(resultSet.getString("description"));
                quest.setValue(resultSet.getInt("value"));
                quest.setCategoryId(resultSet.getInt("category_id"));
                users.add(quest);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }
}
