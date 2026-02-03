package com.lautaro.taskmanager.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.lautaro.taskmanager.R;

public class AddTaskDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_DESC = "arg_desc";
    private static final String ARG_POSITION = "arg_position";

    private int taskPosition = -1;



    public static AddTaskDialogFragment newInstance(
            String title,
            String description,
            int position
    ) {
        AddTaskDialogFragment fragment = new AddTaskDialogFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);

        args.putInt(ARG_POSITION, position);

        fragment.setArguments(args);
        return fragment;
    }




    public interface OnTaskSavedListener {
        void onTaskSaved(String title, String description, int position);
    }

    private OnTaskSavedListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnTaskSavedListener) {
            listener = (OnTaskSavedListener) context;
        }
        else {
            throw new RuntimeException(
                    context.toString() + " must implement OnTaskSavedListener"
            );

        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){



        LayoutInflater inflater= requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_task,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view);
        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etDescription = view.findViewById(R.id.etDescription);
        Button btnAddTask = view.findViewById(R.id.btnAddTask);
        if (getArguments() != null) {

            String existingTitle = getArguments().getString(ARG_TITLE);
            String existingDesc = getArguments().getString(ARG_DESC);

            taskPosition = getArguments().getInt(ARG_POSITION, -1);

            etTitle.setText(existingTitle);
            etDescription.setText(existingDesc);

            if (taskPosition != -1) {
                btnAddTask.setText("Guardar cambios");
            }
        }



        btnAddTask.setOnClickListener(v -> {

            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (title.isEmpty()) {
                etTitle.setError("El nombre es obligatorio");
                return;
            }

            listener.onTaskSaved(title, description, taskPosition);
            dismiss();
        });


        return builder.create();
    }
}
