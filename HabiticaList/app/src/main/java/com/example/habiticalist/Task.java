/*
*   Task.java
*
*   This class provides a basic task object.
 */
package com.example.habiticalist;

public class Task {
    String name;
    String id;

    //TODO date is currently being abused to handle streaks as well. This needs to be changed.
    String date;

    public Task(){

    }
    public Task(String name, String id, String date){
        this.name = name;
        this.id = id;
        this.date = date;
    }

    public void setName(String name) {this.name = name;}
    public void setDate(String date) {this.date = date;}
    public void setId(String id) {this.id = id;}

    public String getId() {return id; }
    public String getDate() {return date;}
    public String getName() {return name;}
}
