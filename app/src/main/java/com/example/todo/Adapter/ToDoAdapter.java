package com.example.todo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.AddNewTarefa;
import com.example.todo.MainActivity;
import com.example.todo.Models.ToDoModel;
import com.example.todo.R;
import com.example.todo.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(MainActivity activity, DatabaseHandler db) {
        this.activity = activity;
        this.db = db;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tarefa_layout, parent, false);

        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();

        final ToDoModel item = todoList.get(position);
        holder.tarefa.setText(item.getTarefa());
        holder.tarefa.setChecked(toBoolean(item.getStatus()));
        holder.tarefa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.updateStatus(item.getId(), isChecked ? 1 : 0);
            }
        });
    }

    private boolean toBoolean(int number) {
        return number != 0;
    }

    public void setTarefas(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("tarefa",item.getTarefa());
        AddNewTarefa fragment = new AddNewTarefa();
        fragment.setArguments(bundle);

        fragment.show(activity.getSupportFragmentManager(), AddNewTarefa.TAG);
    }

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTarefa(item.getId());

        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox tarefa;

        ViewHolder(View view) {
            super(view);
            tarefa = view.findViewById(R.id.todoCheckBox);
        }
    }
}
