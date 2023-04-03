package com.example.todolist.databaseHelper;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("select * from tasks")
    LiveData<List<Task>> getUpdatedTasks();

    @Query("select * from tasks")
    List<Task> getTasks();
}
