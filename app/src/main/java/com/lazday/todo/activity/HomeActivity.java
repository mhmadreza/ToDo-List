package com.lazday.todo.activity;

import androidx.appcompat.app.AlertDialog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.lazday.todo.R;
import com.lazday.todo.adapter.TaskAdapter;
import com.lazday.todo.adapter.TaskCompletedAdapter;
import com.lazday.todo.database.DatabaseService;
import com.lazday.todo.database.TaskDao;
import com.lazday.todo.database.TaskModel;
import com.lazday.todo.databinding.ActivityHomeBinding;
import com.lazday.todo.databinding.CustomHeaderBinding;
import com.lazday.todo.databinding.DialogAddBinding;
import com.lazday.todo.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends BaseActivity {
    private final String TAG = "HomeActivity";

    private ActivityHomeBinding binding;
    private CustomHeaderBinding bindingHeader;
    private TaskDao database;
    private TaskModel taskSelected = new TaskModel();
    private List<TaskModel> listTask = new ArrayList<>();
    private List<TaskModel> listTaskCompleted = new ArrayList<>();
    private TaskAdapter taskAdapter;
    private TaskCompletedAdapter taskCompletedAdapter;
    Long dateSelected = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        bindingHeader = binding.header;
        setContentView(binding.getRoot());
        database = DatabaseService.getInstance(this).getDatabase().taskDao();
        setupList();
        setupListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getHeader();
        getTask();

        //        String today = String.valueOf( new Date().getTime() )
        Log.e(TAG, "testDate " + DateUtil.toLong("22/3/2021"));
        Log.e(TAG, "today " + new Date().getTime());

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
                        new Intent(HomeActivity.this, EditActivity.class)
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
        binding.textAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, TaskActivity.class));
            }
        });

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog = new AlertDialog
                        .Builder(HomeActivity.this)
                        .create();
                DialogAddBinding bindingDialog =
                        DialogAddBinding.inflate(getLayoutInflater());
                dialog.setView(bindingDialog.getRoot());
                dialog.setCancelable(true);
                dialog.show();

                bindingDialog.textDate.setText( DateUtil.today() );
                dateSelected = DateUtil.toLong( DateUtil.today() );
                Log.e("dateSelected", dateSelected.toString());

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

                        DateUtil.showDialog(HomeActivity.this, datePicker)
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

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                database.insert(task);
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

    private void getTask(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                listTask = database.taskDate(0, DateUtil.toLong( DateUtil.today() ) );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "listTask " + listTask.size());
                        taskAdapter.setData( listTask );
                        if (listTask.size() == 0 ) {
                            binding.textAlert.setText("Tidak ada tugas hari ini");
                            binding.textAlert.setVisibility(View.VISIBLE);
                        }
                        else binding.textAlert.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                listTaskCompleted = database.taskCompletedToday( DateUtil.toLong( DateUtil.today() ) );
                listTaskCompleted = database.taskDate( 1, DateUtil.toLong( DateUtil.today() ) );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        taskCompletedAdapter.setData( listTaskCompleted );
                        bindingHeader.textTaskCompleted.setText( listTaskCompleted.size() + "/" );

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer count = database.taskCount( DateUtil.toLong( DateUtil.today() ) );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("count",count + "" );
                        bindingHeader.textTask.setText( count + "" );
                    }
                });
            }
        }).start();
    }

    private void getHeader(){
        bindingHeader.textToday.setText( DateUtil.today() );
    }

}