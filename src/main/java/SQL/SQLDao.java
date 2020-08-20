package SQL;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    protected boolean[] isDateFlag;
    protected boolean[] isIntFlag;

    public SQLDao(String tableName, String[] columnNames) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        updateFlags();
        JDBCInstance = PostgreSQLJDBC.getInstance();
        buildQueryString();
    }

    private void updateFlags(){
        this.isIntFlag = new boolean[columnNames.length];
        this.isDateFlag = new boolean[columnNames.length];
        for (int index = 0; index < columnNames.length; index++) {
            this.isIntFlag[index] = !columnNames[index].contains("session_id") && columnNames[index].contains("id");
            System.out.print(this.columnNames[index] + " IntFLAG: " + isDateFlag[index] + " |");
            this.isDateFlag[index] = columnNames[index].contains("date");
            System.out.print(this.columnNames[index] + " DateFLAG: " + isDateFlag[index]);
            System.out.println(" ");
        }
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
        StringBuilder columns = new StringBuilder(" ( " + columnNames[0]); //https://app.lucidchart.com/documents/edit/ea779a82-10df-49fe-b716-38780decb62c/0_0?beaconFlowId=6B2A9512D3130D31#?folder_id=home&browser=icon
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
        try{
            this.connection = JDBCInstance.connect();
            createStatement(query);
            updateParameters(parameters);
            this.statement.execute();
            resultSet = this.statement.getResultSet();
            this.connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //todo exeptions
        return resultSet;
    }

    private void createStatement(String query) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        statement = connection.prepareStatement(query);
    }

    private void updateParameters(String[] parameters) throws SQLException {
        for (int i = 1; i<=parameters.length; i++){
            if (!isIntFlag[i-1] && !isDateFlag[i-1]){
                this.statement.setString(i, parameters[i-1]);
            }
            if(isIntFlag[i-1]){
                System.out.println("PARAMETRY:" + parameters[i-1]);
                this.statement.setInt(i, Integer.parseInt(parameters[i-1]));
            }
            if(isDateFlag[i-1]){
                this.statement.setDate(i, parseDate(parameters[i-1]));
            }
            System.out.println(statement.toString());
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

    protected  ResultSet getRecords(String column, String value) {
        String[] parameters = {value};
        String searchQuery = String.format(this.selectString, column);
        System.out.println(searchQuery);
        return executeQuery(searchQuery, parameters);
    }

    protected abstract String[] objectToArray(T t);


    private java.sql.Date parseDate(String date){
        String pattern = "yyyyMMdd HH:mm:ss";
        Date returnDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
             returnDate = dateFormat.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return parseDate(returnDate);
    }

    private java.sql.Date parseDate(Date date){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return java.sql.Date.valueOf(localDate);
    }
}