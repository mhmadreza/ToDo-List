package com.lazday.todo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lazday.todo.util.DateUtil;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM TaskModel WHERE completed=:completed ORDER BY date ASC")
    List<TaskModel> taskAll(Integer completed);

    @Query("SELECT * FROM TaskModel WHERE completed=:completed AND date=:date ")
    List<TaskModel> taskDate(Integer completed, Long date);

    @Query("SELECT COUNT(id) FROM TaskModel WHERE date=:date ")
    Integer taskCount(Long date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TaskModel taskModel);

    @Update
    void update(TaskModel taskModel);

    @Delete
    void delete(TaskModel taskModel);

    @Query("DELETE FROM TaskModel WHERE completed=1")
    void deleteCompleted();

    @Query("DELETE FROM TaskModel")
    void deleteAll();

}
