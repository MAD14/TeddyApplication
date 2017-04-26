package it.polito.teddyapplication;

/**
 * Created by Utente on 03/04/2017.
 */

public class ExpenseItem {

    private String name;
    private String value; //double
    private String description;
    private String author;

    public ExpenseItem(String name, String value, String description,String author) {
        this.name = name;
        this.value = value;
        this.description = description;
        this.author=author;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {return value;}

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() { return description;}

    public void setDescription(String description) { this.description = description;}

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
