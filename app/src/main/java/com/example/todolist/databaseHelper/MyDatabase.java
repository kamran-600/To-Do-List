package com.example.todolist.databaseHelper;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Task.class,exportSchema = false,version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public static final String DB_NAME = "task_db";
    public static MyDatabase instance;

    public static synchronized MyDatabase getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context,MyDatabase.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract TaskDao taskDao();
}
