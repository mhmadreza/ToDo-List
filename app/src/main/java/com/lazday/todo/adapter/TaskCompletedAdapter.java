package com.lazday.todo.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lazday.todo.R;
import com.lazday.todo.database.TaskModel;
import com.lazday.todo.databinding.AdapterTaskCompletedBinding;
import com.lazday.todo.util.DateUtil;

import java.util.List;

public class TaskCompletedAdapter extends RecyclerView.Adapter<TaskCompletedAdapter.ViewHolder> {

    private List<TaskModel> listTask;
    private AdapterListener listener;

    public TaskCompletedAdapter(List<TaskModel> listTask, AdapterListener listener) {
        this.listTask   = listTask ;
        this.listener   = listener ;
    }

    @NonNull
    @Override
    public TaskCompletedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(
                AdapterTaskCompletedBinding.inflate( LayoutInflater.from(parent.getContext()), parent, false )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final TaskModel task = listTask.get(i);
        viewHolder.binding.textTask.setText(task.getTask());
        viewHolder.binding.textTask.setPaintFlags(viewHolder.binding.textTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        String date = DateUtil.toString( task.getDate());
        viewHolder.binding.textTime.setText( date );
        viewHolder.binding.textTime.setPaintFlags(viewHolder.binding.textTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        viewHolder.binding.imageCheck.setImageResource(R.drawable.ic_task_completed);
        viewHolder.binding.imageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick( task );
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTask.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterTaskCompletedBinding binding;
        public ViewHolder(AdapterTaskCompletedBinding binding) {
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
        void onClick(TaskModel task);
    }
}
