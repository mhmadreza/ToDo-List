package com.lazday.todo.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lazday.todo.R;
import com.lazday.todo.database.TaskModel;
import com.lazday.todo.databinding.AdapterTaskBinding;
import com.lazday.todo.util.DateUtil;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private final String TAG = "TaskAdapter";

    private List<TaskModel> listTask;
    private AdapterListener listener;

    public TaskAdapter(List<TaskModel> listTask, AdapterListener listener) {
        this.listTask   = listTask ;
        this.listener   = listener ;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(
                AdapterTaskBinding.inflate( LayoutInflater.from(parent.getContext()), parent, false )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final TaskModel task = listTask.get(i);
        viewHolder.binding.textTask.setText(task.getTask());
        viewHolder.binding.textTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDetail( task );
            }
        });
        String date = DateUtil.toString( task.getDate() );
        viewHolder.binding.textTime.setText( date );

        viewHolder.binding.imageCheck.setImageResource(R.drawable.ic_task);
        viewHolder.binding.imageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCompleted( task );
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTask.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterTaskBinding binding;
        public ViewHolder(AdapterTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setData(List<TaskModel> data) {
        listTask.clear();
        listTask.addAll(data);
        notifyDataSetChanged();
    }

    public interface AdapterListener {
        void onCompleted(TaskModel task);
        void onDetail(TaskModel task);
    }
}
