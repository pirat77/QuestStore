package SQL;

import java.sql.*;

public abstract class SQLDao<T> {
    protected Connection connection;
    protected PreparedStatement statement;
    protected String[] columnNames;
    protected String tableName;
    protected PostgreSQLJDBC JDBCInstance;
    protected String columnsString;
    protected String insertString;
    protected String removeString;
    protected String selectString;

    public SQLDao(String tableName, String[] columnNames) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        JDBCInstance = PostgreSQLJDBC.getInstance();
        connection = JDBCInstance.connect();
        buildQueryString();
    }

    private void buildQueryString(){
        createColumnsString();
        createInsertString();
        createRemoveString();
        createSelectString();
    }

    private void createSelectString(){ this.selectString = "SELECT * FROM ? WHERE ? LIKE ?"; }

    private void createRemoveString(){ this.removeString = "DELETE FROM ?  WHERE Id =  ? "; }

    private void createColumnsString(){
        StringBuilder columns = new StringBuilder(" ( ");
        for (int i=1; i<columnNames.length; i++) { columns.append(", " + columnNames[i]); }
        columns.append(" ) ");
        this.columnsString = columns.toString();
    }

    private void createInsertString() {
        StringBuilder query = new StringBuilder("INSERT INTO " + this.tableName + this.columnsString + " VALUES ( ? ");
        for (int i = 1; i < columnNames.length; i++) { query.append(", ?"); }
        query.append(")");
        this.insertString = query.toString();
    }

    protected ResultSet executeQuery(String query, String[] parameters) {
        ResultSet resultSet = null;
        try {
            createStatement(query);
            updateParameters(parameters);
            this.statement.execute();
            resultSet = this.statement.getResultSet();
            this.connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Call postgreSQL engineers, there is nothing you can do :)");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't connect to database, check it's availability or call support");
        }
        return resultSet;
    }

    private void createStatement(String query) throws ClassNotFoundException, SQLException {
        //TODO: solve redshift problem with prepared statements on postgresql
        Class.forName("org.postgresql.Driver");
        statement = connection.prepareStatement(query);
    }

    private void updateParameters(String[] parameters) throws SQLException {
        for (int i = 1; i<=parameters.length; i++){
            this.statement.setString(i, parameters[i-1]);
        }
    }

    protected void updateRecord(String[] newValues) {
        String id = newValues[0];
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        for (String column : columnNames) { query.append(column).append(" = ?"); }
        query.append(" WHERE Id = ").append(id).append(";");
        executeQuery(query.toString(), newValues);
    }

    protected void removeRecord(String id) {
        executeQuery(this.removeString, new String[]{this.tableName, id});
    }

    protected void insertRecord(String[] values) {
        executeQuery(this.insertString, values);
    }

    protected  ResultSet getRecords(String column, String value){
        String[] parameters = {this.tableName, column, value};
        return executeQuery(this.selectString, parameters);
    }

    protected abstract String[] objectToArray(T t);
}