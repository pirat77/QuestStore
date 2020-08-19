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
        buildQueryString();
    }

    private void buildQueryString(){
        createColumnsString();
        createInsertString();
        createRemoveString();
        createSelectString();
    }

    private void createSelectString(){ this.selectString = "SELECT * FROM " + this.tableName + " WHERE %s LIKE ?"; }

    private void createRemoveString(){ this.removeString = "DELETE FROM " + this.tableName + "  WHERE Id =  ? "; }

    private void createColumnsString(){
        StringBuilder columns = new StringBuilder(" ( ");https://app.lucidchart.com/documents/edit/ea779a82-10df-49fe-b716-38780decb62c/0_0?beaconFlowId=6B2A9512D3130D31#?folder_id=home&browser=icon
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

    protected ResultSet executeQuery(String query, String[] parameters) throws SQLException, ClassNotFoundException {
        ResultSet resultSet;
        this.connection = JDBCInstance.connect();
        createStatement(query);
        updateParameters(parameters);
        this.statement.execute();
        resultSet = this.statement.getResultSet();
        this.connection.close();
        return resultSet;
    }

    private void createStatement(String query) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        statement = connection.prepareStatement(query);
    }

    private void updateParameters(String[] parameters) throws SQLException {
        for (int i = 1; i<=parameters.length; i++){
            this.statement.setString(i, parameters[i-1]);
        }
    }

    protected void updateRecord(String[] newValues) throws SQLException, ClassNotFoundException {
        String id = newValues[0];
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        for (String column : columnNames) { query.append(column).append(" = ?"); }
        query.append(" WHERE Id = ").append(id).append(";");
        executeQuery(query.toString(), newValues);
    }

    protected void removeRecord(String id) throws SQLException, ClassNotFoundException {
        executeQuery(this.removeString, new String[]{this.tableName, id});
    }

    protected void insertRecord(String[] values) throws SQLException, ClassNotFoundException {
        executeQuery(this.insertString, values);
    }

    protected  ResultSet getRecords(String column, String value) throws SQLException, ClassNotFoundException {
        String[] parameters = {value};
        String searchQuery = String.format(this.selectString, column);
        return executeQuery(searchQuery, parameters);
    }

    protected abstract String[] objectToArray(T t);
}