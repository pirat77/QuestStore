package DAO;

import SQL.SQLDao;
import model.Entry;
import model.groups.Team;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO  extends SQLDao<Team> implements Dao<Team> {

    TeamDAO(){
        super("teams", new String[]{"id", "name"});
    }

    @Override
    protected Entry[] objectToArray(Team team) {
        Entry id = new Entry("id", Integer.toString(team.getId()));
        Entry name = new Entry("name", team.getName());
        return new Entry[]{id, name};
    }

    @Override
    public void update(Team team) throws SQLException, ClassNotFoundException, ParseException { updateRecord(objectToArray(team)); }

    @Override
    public void remove(Team team) throws SQLException, ClassNotFoundException, ParseException {
        removeRecord(new Entry("id", Integer.toString(team.getId()))); }

    @Override
    public void insert(Team team) throws SQLException, ClassNotFoundException, ParseException { insertRecord(objectToArray(team)); }

    @Override
    public List<Team> getObjects(Entry entry) throws SQLException, ClassNotFoundException, ParseException {
        List<Team> teams = new ArrayList<>();
        ResultSet resultSet = getRecords(entry);
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