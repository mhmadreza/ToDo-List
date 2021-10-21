package com.lazday.todo.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lazday.todo.R;
import com.lazday.todo.database.TaskModel;
import com.lazday.todo.databinding.FragmentDetailBinding;
import com.lazday.todo.util.DateUtil;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    private TaskModel taskModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskModel = (TaskModel) requireActivity().getIntent().getSerializableExtra("intent_task");
        Log.e("DetailFragment", taskModel.toString());
        setupListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        getTask();
    }

    private void setupListener(){
        binding.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment
                        .findNavController(DetailFragment.this)
                        .navigate(R.id.action_detailFragment_to_updateFragment);
            }
        });
    }

    private void getTask(){
        binding.textTask.setText( taskModel.getTask() );
        binding.textDate.setText( DateUtil.toString(taskModel.getDate()) );
    }
}