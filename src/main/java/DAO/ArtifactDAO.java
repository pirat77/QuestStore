package DAO;

import SQL.SQLDao;
import model.elements.Artifact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtifactDAO  extends SQLDao<Artifact> implements Dao<Artifact> {

    ArtifactDAO(){
        super("artifacts", new String[]{"id", "name", "description", "value", "category_id"});
    }

    @Override
    protected String[] objectToArray(Artifact artifact) {
        String id = Integer.toString(artifact.getId());
        String name =  artifact.getName();
        String description = artifact.getDescription();
        String value = Integer.toString(artifact.getValue());
        String category_id = Integer.toString(artifact.getCategoryId());
        return new String[]{id, name, description, value, description, value, category_id};
    }

    @Override
    public void update(Artifact artifact) throws SQLException, ClassNotFoundException {
        updateRecord(objectToArray(artifact));
    }

    @Override
    public void remove(Artifact artifact) throws SQLException, ClassNotFoundException { removeRecord(Integer.toString(artifact.getId())); }

    @Override
    public void insert(Artifact artifact) throws SQLException, ClassNotFoundException { insertRecord(objectToArray(artifact)); }

    @Override
    public List<Artifact> getObjects(String columnName, String columnValue) throws SQLException, ClassNotFoundException {
        List<Artifact> artifacts = new ArrayList<>();
        ResultSet resultSet = getRecords(columnName, columnValue);
        try {
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
