package it.polito.teddyapplication;

/**
 * Created by Utente on 03/04/2017.
 */

public class DebitsItem {

    private String name;
    private String value;

    public DebitsItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
