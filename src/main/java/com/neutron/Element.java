package com.neutron;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Element {
    private final StringProperty id;
    private final StringProperty link;
    private final StringProperty  date;
    public Element() {
        this.id = new SimpleStringProperty("");
        this.link = new SimpleStringProperty("");
        this.date = new SimpleStringProperty("");
    }

    public Element(String link, String date) {
        this.link = new SimpleStringProperty(link);
        this.date = new SimpleStringProperty(date);
        this.id = new SimpleStringProperty("");
    }

    public Element(Integer id, String link, String date) {
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.link = new SimpleStringProperty(link);
        this.date = new SimpleStringProperty(date);
    }

    public String getId(){
        return id.get();
    }

    public void setId(String fId){
        id.set(fId);
    }

    public String getLink() {
        return link.get();
    }

    public StringProperty linkProperty() {
        return link;
    }

    public void setLink(String link) {
        this.link.set(link);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    @Override
    public String toString() {
        return "Element{" +
                "id=" + id +
                ", link =" + link +
                ", date=" + date +
                '}';
    }
}
