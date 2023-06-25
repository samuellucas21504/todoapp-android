package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todo.Adapter.ToDoAdapter;
import com.example.todo.Models.ToDoModel;
import com.example.todo.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tarefasRV;
    private ToDoAdapter tarefasAdapter;
    private DatabaseHandler db;
    private FloatingActionButton fab;

    private List<ToDoModel> tarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        tarefasRV = findViewById(R.id.tarefasRV);
        tarefasRV.setLayoutManager(new LinearLayoutManager(this));
        tarefasAdapter = new ToDoAdapter(this, db);
        tarefasRV.setAdapter(tarefasAdapter);

        fab = findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new RecyclerItemTouchHelper(tarefasAdapter));
        itemTouchHelper.attachToRecyclerView(tarefasRV);

        tarefas = db.getAllTarefas();
        Collections.reverse(tarefas);

        tarefasAdapter.setTarefas(tarefas);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTarefa.newInstance().show(getSupportFragmentManager(), AddNewTarefa.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        tarefas = db.getAllTarefas();
        Collections.reverse(tarefas);
        tarefasAdapter.setTarefas(tarefas);
        tarefasAdapter.notifyDataSetChanged();
    }
}