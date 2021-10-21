package com.lazday.todo.database;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {TaskModel.class}, version = 1, exportSchema = false)
//@TypeConverters({Converters.class})
public abstract class DatabaseDao extends RoomDatabase {
    public abstract TaskDao taskDao();
}

