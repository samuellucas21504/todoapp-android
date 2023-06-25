package com.example.todo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.todo.Models.ToDoModel;
import com.example.todo.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

public class AddNewTarefa extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private EditText newTarefaTxt;
    private Button newTarefaSaveButton;
    private DatabaseHandler db;

    public static AddNewTarefa newInstance() {
        return new AddNewTarefa();
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nova_tarefa, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTarefaTxt = getView().findViewById(R.id.novaTarefaTxt);
        newTarefaSaveButton = getView().findViewById(R.id.novaTarefaBtn);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if(bundle != null) {
            isUpdate = true;
            String tarefa = bundle.getString("tarefa");
            newTarefaTxt.setText(tarefa);
            assert tarefa != null;
            if (tarefa.length() > 0) {
                newTarefaSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            }
        }
        newTarefaTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTarefaSaveButton.setEnabled(false);
                    newTarefaSaveButton.setTextColor(Color.GRAY);
                }
                else {
                    newTarefaSaveButton.setEnabled(true);
                    newTarefaSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        boolean finalIsUpdate = isUpdate;

        newTarefaSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTarefaTxt.getText().toString();
                if(finalIsUpdate){
                    db.updateTarefa(bundle.getInt("id"), text);
                }
                else {
                    ToDoModel tarefa = new ToDoModel();
                    tarefa.setTarefa(text);
                    tarefa.setStatus(0);
                    db.insertTarefa(tarefa);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
