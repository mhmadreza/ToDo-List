package com.lazday.todo.activity;

import androidx.appcompat.app.AlertDialog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;

import com.lazday.todo.R;
import com.lazday.todo.adapter.TaskAdapter;
import com.lazday.todo.adapter.TaskCompletedAdapter;
import com.lazday.todo.database.DatabaseService;
import com.lazday.todo.database.TaskDao;
import com.lazday.todo.database.TaskModel;
import com.lazday.todo.databinding.ActivityTaskBinding;
import com.lazday.todo.databinding.DialogAddBinding;
import com.lazday.todo.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends BaseActivity {
    private final String TAG = "TaskActivity";

    private ActivityTaskBinding binding;
    private TaskDao database;
    private TaskModel taskSelected = new TaskModel();
    private List<TaskModel> listTask = new ArrayList<>();
    private List<TaskModel> listTaskCompleted = new ArrayList<>();
    private TaskAdapter taskAdapter;
    private TaskCompletedAdapter taskCompletedAdapter;
    Long dateSelected = 0L;
    private Long dateStart = DateUtil.toLong(DateUtil.today());
    private Long dateEnd = DateUtil.toLong(DateUtil.nextWeek());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = DatabaseService.getInstance(this).getDatabase().taskDao();
        setupList();
        setupListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTask();
    }

    private void setupList(){
        taskAdapter = new TaskAdapter(listTask, new TaskAdapter.AdapterListener() {
            @Override
            public void onCompleted(TaskModel data) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        taskSelected = data;
                        taskSelected.setCompleted( true );
                        database.update( taskSelected );
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getTask();
                            }
                        });
                    }
                }).start();

            }
            @Override
            public void onDetail(TaskModel data) {
                startActivity(
                        new Intent(TaskActivity.this, EditActivity.class)
                                .putExtra("intent_task", data)
                );
            }
        });
        binding.listTask.setAdapter( taskAdapter );
        taskCompletedAdapter = new TaskCompletedAdapter(listTaskCompleted, new TaskCompletedAdapter.AdapterListener() {
            @Override
            public void onClick(TaskModel data) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        taskSelected = data;
                        taskSelected.setCompleted( false );
                        database.update( taskSelected );
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getTask();
                            }
                        });
                    }
                }).start();

            }
        });
        binding.listTaskCompleted.setAdapter( taskCompletedAdapter );
    }

    private void setupListener(){
        binding.imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(TaskActivity.this, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_new:
                                addDialog();
                                break;
                            case R.id.action_delete:
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        database.deleteCompleted();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                getTask();
                                            }
                                        });
                                    }
                                }).start();
                                break;
                            case R.id.action_delete_all:
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        database.deleteAll();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                getTask();
                                            }
                                        });
                                    }
                                }).start();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.menu_task);
                popupMenu.show();
            }
        });

        binding.textCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.listTaskCompleted.getVisibility() == View.GONE) {
                    binding.listTaskCompleted.setVisibility( View.VISIBLE );
                    binding.imageArrow.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    binding.listTaskCompleted.setVisibility( View.GONE );
                    binding.imageArrow.setImageResource(R.drawable.ic_arrow_right);
                }
            }
        });
    }

    private void addDialog(){

        AlertDialog dialog = new AlertDialog
                .Builder(TaskActivity.this)
                .create();
        DialogAddBinding bindingDialog =
                DialogAddBinding.inflate(getLayoutInflater());
        dialog.setView(bindingDialog.getRoot());
        dialog.setCancelable(true);
        dialog.show();

        bindingDialog.textDate.setText( DateUtil.today() );
        dateSelected = DateUtil.toLong( DateUtil.today() );

        bindingDialog.labelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        String datePicked = DateUtil.toString(year, monthOfYear, dayOfMonth);
                        dateSelected = DateUtil.toLong( datePicked );
                        bindingDialog.textDate.setText( datePicked );
                    }
                };

                DateUtil.showDialog(TaskActivity.this, datePicker)
                        .show();
            }
        });

        bindingDialog.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                TaskModel task = new TaskModel();
                task.setTask( bindingDialog.editTask.getText().toString() );
                task.setCompleted( false );
                task.setDate( dateSelected );

                new Thread(() -> {
                    database.insert(task);
                    runOnUiThread(TaskActivity.this::getTask);
                }).start();

            }
        });
    }

    private void getTask(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                listTask = database.taskAll(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "listTask " + listTask.size());
                        taskAdapter.setData( listTask );
                        if (listTask.size() == 0 ) binding.textAlert.setVisibility(View.VISIBLE);
                        else binding.textAlert.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                listTaskCompleted = database.taskAll(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "listTaskCompleted " + listTaskCompleted.size());
                        taskCompletedAdapter.setData( listTaskCompleted );
                        if (listTaskCompleted.size() == 0 ) {
                            binding.textCompleted.setVisibility(View.GONE);
                            binding.imageArrow.setVisibility(View.GONE);
                        } else {
                            binding.textCompleted.setVisibility(View.VISIBLE);
                            binding.imageArrow.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();
    }
}