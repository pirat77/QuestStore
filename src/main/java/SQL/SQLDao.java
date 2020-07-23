package SQL;

import java.sql.*;

public abstract class SQLDao<T> {
    protected Connection connection;
    protected PreparedStatement statement;
    protected String[] columnNames;
    protected String tableName;
    protected PostgreSQLJDBC JDBCInstance;

    SQLDao(String tableName, String[] columnNames) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        JDBCInstance = PostgreSQLJDBC.getInstance();
        connection = JDBCInstance.connect();
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
            System.out.println("Call sqldblite engineers, there is nothing you can do :)");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't connect to database, check it's availability or call support");
        }
        return resultSet;
    }

    private void createStatement(String query) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        statement = connection.prepareStatement(query);
    }

    private void updateParameters(String[] parameters) throws SQLException {
        for (int i = 1; i<= parameters.length; i++){
            this.statement.setString(i, parameters[i]);
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
        String query = "DELETE FROM ?  WHERE Id =  ? ";
        executeQuery(query, new String[]{this.tableName, id});
    }

    protected void insertRecord(String[] values) {
        String columnsString = " ( " + String.join(", " , columnNames) + " ) ";
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + columnsString + " VALUES ( ? ");
        for (int i=1; i<columnNames.length; i++){ query.append(", ?"); }
        query.append(")");
        executeQuery(query.toString(), values);
    }

    protected  ResultSet getRecords(String column, String value){
        String query = "SELECT * FROM ? WHERE ? LIKE ?";
        String[] parameters = {this.tableName, column, value};
        return executeQuery(query, parameters);
    }

    protected abstract String[] objectToArray(T t);
}