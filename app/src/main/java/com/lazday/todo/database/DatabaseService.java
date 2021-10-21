package com.lazday.todo.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseService {

    private Context context;
    private static DatabaseService service;
    private DatabaseDao database;

    private DatabaseService(Context context) {
        this.context = context;
        database = Room.databaseBuilder(context, DatabaseDao.class, "LazdayTodo123.db").build();
    }

    public static synchronized DatabaseService getInstance(Context context) {
        if (service == null) {
            service = new DatabaseService(context);
        }
        return service;
    }

    public DatabaseDao getDatabase() {
        return database;
    }

}
