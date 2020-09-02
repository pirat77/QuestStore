package model;

import java.util.Objects;

public class Entry {
    private String columnName;
    private String columnValue;

    public Entry(String columnValue){
        this.columnValue = columnValue;
    }

    public Entry(String columnName, String columnValue){
        this.columnName = columnName;
        this.columnValue = columnValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;
        Entry entry = (Entry) o;
        return Objects.equals(getColumnName(), entry.getColumnName()) &&
                Objects.equals(getColumnValue(), entry.getColumnValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColumnName(), getColumnValue());
    }
}
