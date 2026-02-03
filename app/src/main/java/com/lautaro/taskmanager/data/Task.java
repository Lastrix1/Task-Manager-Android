package com.lautaro.taskmanager.data;

public class Task {
    private int id;
    private String title;
    private String description;
    private boolean state;

    public Task(int id,String title,String description){
        this.description=description;
        this.id=id;
        state=false;
        this.title=title;
    }
    public Task(String title,String description){
        this.description=description;
        state=false;
        this.title=title;
        id=0;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isState() {
        return state;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
