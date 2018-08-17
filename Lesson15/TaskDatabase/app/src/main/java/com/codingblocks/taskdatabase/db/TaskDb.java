package com.codingblocks.taskdatabase.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codingblocks.taskdatabase.Constants;
import com.codingblocks.taskdatabase.Constants.*;
import com.codingblocks.taskdatabase.models.Task;

import java.util.ArrayList;

import static com.codingblocks.taskdatabase.Constants.*;

public class TaskDb extends SQLiteOpenHelper {

    public TaskDb(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Called when the db was created for the first time, or its version
        //was incremented.
        //You can use this callback to create as many tables as you want
        //or fill them with some random data for the first launch of your app.
        String query = CREATE + TABLE_NAME +
                RBR + COLUMN_ID + INTEGER + PRIMARY_KEY + COMMA
                + COLUMN_TITLE + TEXT + NOT_NULL + LBR
                + TERMINATION;

        Log.e("TAG", "onCreate: SQLiteOpenHelper" + query);
        sqLiteDatabase.execSQL(query);
    }

    public long insertTask(Task task) {
//        String insertQuery = "INSERT INTO task VALUES( " +
//                task.getId() +
//                ", ); DROP TABLE task;"
//                + " );";

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_ID, task.getId());
        contentValues.put(Constants.COLUMN_TITLE, task.getTitle());

        //Store this to the database
        long position = getWritableDatabase()
                .insert(Constants.TABLE_NAME,
                        null,
                        contentValues);
        return position;
    }

    public Task getTaskWithID(Long id) {
        //return a task with the given ID
        Task task = null;

        Cursor cursor = getReadableDatabase().query(TABLE_NAME,
                null,
                COLUMN_ID + " = ?",
                new String[]{id.toString()},
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Long fetchedId = cursor.getLong(0);
            String fetchedTitle = cursor.getString(1);
            task = new Task(fetchedId, fetchedTitle);
        }

        cursor.close();

        return task;
    }

    public ArrayList<Task> getAllTasks() {

        String[] projection = new String[]{COLUMN_ID, COLUMN_TITLE};

        ArrayList<Task> tasks = new ArrayList<>();

        //Ideally this should run in a separate thread to prevent UI blocking
        Cursor c = getReadableDatabase().query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        while (c.moveToNext()) {

            Long id = c.getLong(c.getColumnIndex(COLUMN_ID.trim()));
            String title = c.getString(c.getColumnIndex(COLUMN_TITLE.trim()));

            Task task = new Task(id, title);
            tasks.add(task);
        }

        c.close();

        //return an arraylist of all the tasks stored in the db
        return tasks;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //perform table alterations here, or let the default implementation
        // happen which deletes the database and creates it again
    }
}
