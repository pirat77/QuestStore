package DAO;

import SQL.SQLDao;
import model.Entry;
import model.elements.Quest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestDAO  extends SQLDao<Quest> implements Dao<Quest> {

    public QuestDAO(){
        super("quests", new String[]{"id", "name", "description", "value", "category_id"});
    }

    @Override
    protected Entry[] objectToArray(Quest quest) {
        Entry id =  new Entry("id", Integer.toString(quest.getId()));
        Entry name = new Entry("name", quest.getName());
        Entry description = new Entry("description", quest.getDescription());
        Entry value = new Entry("value", Integer.toString(quest.getValue()));
        Entry category_id = new Entry("category_id", Integer.toString(quest.getCategoryId()));
        return new Entry[]{id, name, description, value, category_id};
    }

    @Override
    public void update(Quest quest) {
        updateRecord(objectToArray(quest));
    }

    @Override
    public void remove(Quest quest) {
        removeRecord(new Entry("id", Integer.toString(quest.getId())));
    }

    @Override
    public void insert(Quest quest) { insertRecord(objectToArray(quest)); }

    @Override
    public List<Quest> getObjects(Entry entry){
        List<Quest> users = new ArrayList<>();

        try {
            ResultSet resultSet = getRecords(entry);
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
