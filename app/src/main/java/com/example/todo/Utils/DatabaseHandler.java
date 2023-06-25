package com.example.todo.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.Models.ToDoModel;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TAREFA = "tarefa";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE =
            "CREATE TABLE " + TODO_TABLE +
                    "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TAREFA + " TEXT, "
                    + STATUS + " INTEGER" +
                    ")"
            ;
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tabelas velhas
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Cria tabelas novamente
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTarefa(ToDoModel tarefa) {
        ContentValues cv = new ContentValues();
        cv.put(TAREFA, tarefa.getTarefa());
        cv.put(STATUS, 0);

        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTarefas() {
        List<ToDoModel> tarefas = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null && cur.moveToFirst()){
                do {
                    ToDoModel tarefa = new ToDoModel();
                    tarefa.setId(cur.getInt(cur.getColumnIndex(ID)));
                    tarefa.setTarefa(cur.getString(cur.getColumnIndex(TAREFA)));
                    tarefa.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                    tarefas.add(tarefa);
                }while(cur.moveToNext());
            }

        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return tarefas;
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateTarefa(int id, String tarefa) {
        ContentValues cv = new ContentValues();
        cv.put(TAREFA, tarefa);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteTarefa(int id) {
        db.delete(TODO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
}
