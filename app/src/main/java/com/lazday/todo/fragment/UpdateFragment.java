package com.lazday.todo.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.lazday.todo.database.DatabaseService;
import com.lazday.todo.database.TaskDao;
import com.lazday.todo.database.TaskModel;
import com.lazday.todo.databinding.FragmentUpdateBinding;
import com.lazday.todo.util.DateUtil;

public class UpdateFragment extends Fragment {
    private final String TAG = "UpdateFragment";

    private FragmentUpdateBinding binding;
    private TaskModel taskDetail;
    private TaskDao database;
    private TaskModel taskUpdate = new TaskModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskDetail = (TaskModel) requireActivity().getIntent().getSerializableExtra("intent_task");
        database = DatabaseService.getInstance(requireActivity()).getDatabase().taskDao();
        Log.e("UpdateFragment", taskDetail.toString());
        setupListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        getTask();
    }

    private void setupListener(){
        binding.labelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        String datePicked = DateUtil.toString(year, monthOfYear, dayOfMonth);
                        taskUpdate.setDate( DateUtil.toLong(datePicked) );
                        binding.textDate.setText( datePicked );
                    }
                };

                DateUtil.showDialog(requireActivity(), datePicker)
                        .show();
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                taskUpdate.setTask( binding.editTask.getText().toString() );

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        database.update( taskUpdate );
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showMessage("Task Updated.");
                                requireActivity().finish();
                            }
                        });
                    }
                }).start();

            }
        });

        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { delete(); }
        });
    }

    private void getTask(){
        taskUpdate = taskDetail;
        binding.editTask.setText( taskDetail.getTask() );
        binding.textDate.setText( DateUtil.toString(taskDetail.getDate()) );
    }

    private void showMessage(String msg){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void delete(){

        AlertDialog alertDialog = new AlertDialog.Builder(requireActivity()).create();
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Delete " + taskUpdate.getTask() + "?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.delete( taskUpdate );
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage("Task Deleted.");
                                        requireActivity().finish();
                                    }
                                });
                            }
                        }).start();

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }
}