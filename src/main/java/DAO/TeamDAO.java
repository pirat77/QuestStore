package DAO;

import SQL.SQLDao;
import model.groups.Team;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO  extends SQLDao<Team> implements Dao<Team> {

    TeamDAO(){
        super("teams", new String[]{"id", "name"});
    }

    @Override
    protected String[] objectToArray(Team team) {
        String id = Integer.toString(team.getId());
        String name = team.getName();
        return new String[]{id, name};
    }

    @Override
    public void update(Team team) throws SQLException, ClassNotFoundException { updateRecord(objectToArray(team)); }

    @Override
    public void remove(Team team) throws SQLException, ClassNotFoundException { removeRecord(Integer.toString(team.getId())); }

    @Override
    public void insert(Team team) throws SQLException, ClassNotFoundException { insertRecord(objectToArray(team)); }

    @Override
    public List<Team> getObjects(String columnName, String columnValue) throws SQLException, ClassNotFoundException {
        List<Team> teams = new ArrayList<>();
        ResultSet resultSet = getRecords(columnName, columnValue);
        try {
            while (resultSet.next()) {
                Team team = new Team();
                team.setId(resultSet.getInt("id"));
                team.setName(resultSet.getString("name"));
                teams.add(team);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return teams;
    }
}