package model;

public abstract class Element {
    private int id;
    private String name;
    private String description;
    private int value;
    private int categoryId;

    public Element(int id, String name, String description, int value, int categoryId){

        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.categoryId = categoryId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getValue() {
        return this.value;
    }

    public int getCategoryId() {
        return this.categoryId;
    }
}
