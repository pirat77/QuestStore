package DAO;

import SQL.SQLDao;
import model.Entry;
import model.elements.Artifact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtifactDAO  extends SQLDao<Artifact> implements Dao<Artifact> {

    public ArtifactDAO(){
        super("artifacts", new String[]{"id", "name", "description", "value", "category_id"});
    }

    @Override
    protected Entry[] objectToArray(Artifact artifact) {
        Entry id = new Entry("id", Integer.toString(artifact.getId()));
        Entry name =  new Entry("name", artifact.getName());
        Entry description = new Entry("description", artifact.getDescription());
        Entry value =  new Entry("value", Integer.toString(artifact.getValue()));
        Entry category_id = new Entry(Integer.toString(artifact.getCategoryId()));
        return new Entry[]{id, name, description, value, description, value, category_id};
    }

    @Override
    public void update(Artifact artifact) {
        updateRecord(objectToArray(artifact));
    }

    @Override
    public void remove(Artifact artifact) { removeRecord(new Entry("id", Integer.toString(artifact.getId()))); }

    @Override
    public void insert(Artifact artifact) { insertRecord(objectToArray(artifact)); }

    @Override
    public List<Artifact> getObjects(Entry entry) {
        List<Artifact> artifacts = new ArrayList<>();

        try {
            ResultSet resultSet = getRecords(entry);
            while (resultSet.next()) {
                Artifact artifact = new Artifact();
                artifact.setId(resultSet.getInt("id"));
                artifact.setName(resultSet.getString("name"));
                artifact.setDescription(resultSet.getString("description"));
                artifact.setValue(resultSet.getInt("value"));
                artifact.setCategoryId(resultSet.getInt("category_id"));
                artifacts.add(artifact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return artifacts;
    }
}
