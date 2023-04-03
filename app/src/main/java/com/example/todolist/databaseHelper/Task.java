package com.example.todolist.databaseHelper;

import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "taskName")
    private String taskName;
    @ColumnInfo(name = "taskDesc")
    private String taskDesc;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "completed")
    private String completed;


    //for update purpose


    public Task(int id, String taskName, String taskDesc, String date, String time, String completed) {
        this.id = id;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.date = date;
        this.time = time;
        this.completed = completed;
    }

    // for insert purpose
    @Ignore
    public Task(String taskName, String taskDesc, String date, String time, String completed) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.date = date;
        this.time = time;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return getId() == task.getId() && getTaskName().equals(task.getTaskName()) && getTaskDesc().equals(task.getTaskDesc()) && getDate().equals(task.getDate()) && getTime().equals(task.getTime()) && getCompleted().equals(task.getCompleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTaskName(), getTaskDesc(), getDate(), getTime(), getCompleted());
    }
}
