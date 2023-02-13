package org.example;

public class ToDo {
    private int id;
    private String task;
    private boolean done;

    public ToDo(int id, String task, boolean done) {
        this.id = id;
        this.task = task;
        this.done = done;
    }

    public ToDo(String task, boolean done) {
        this.task = task;
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
