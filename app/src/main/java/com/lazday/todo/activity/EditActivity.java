package com.lazday.todo.activity;


import android.os.Bundle;
import com.lazday.todo.databinding.ActivityEditBinding;

public class EditActivity extends BaseActivity {

    private ActivityEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}