package com.example.todotasks.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todotasks.model.ToDoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase dp;
    private static final String DATABASE_NAME="TODO_DATABASE";
    private static final String TABLE_NAME="TODO_TABLE";
    private static final String col_1="ID";
    private static final String col_2="TASK";
    private static final String col_3="STATUS";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF  NOT EXISTS "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT ,TASK TEXT ,STATUS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    public void insertTask(ToDoModel model){
        dp=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(col_2,model.getTask());
        values.put(col_3,0);
        dp.insert(TABLE_NAME,null,values);


    }
    public void updateTask(int id, String task){
        dp=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(col_2,task);
        dp.update(TABLE_NAME,values,"ID=?",new String[]{String.valueOf(id)});


    }
    public void upDateStatus(int id ,int status){
        dp=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(col_3,status);
        dp.update(TABLE_NAME,values,"ID=?",new String[]{String.valueOf(id)});

    }

    public void deleteTask(int id){
        dp=this.getWritableDatabase();
        dp.delete(TABLE_NAME,"ID=?",new String[]{String.valueOf(id)});
    }
    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks() {
        dp = this.getReadableDatabase();
        Cursor cursor = null;
        List<ToDoModel> list = new ArrayList<>();

        try {
            cursor = dp.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ToDoModel task = new ToDoModel();
                    task.setId(cursor.getInt(cursor.getColumnIndex(col_1)));
                    task.setTask(cursor.getString(cursor.getColumnIndex(col_2)));
                    task.setStatus(cursor.getInt(cursor.getColumnIndex(col_3)));
                    list.add(task);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // Handle exceptions and log errors here
            e.printStackTrace();
            Log.d("DatabasegetAllTasks", Objects.requireNonNull(e.getMessage()));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dp.close(); // Close the database
        }
        return list;
    }}
