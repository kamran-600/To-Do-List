package com.example.todolist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.todolist.databaseHelper.MyDatabase;
import com.example.todolist.databaseHelper.Task;
import com.example.todolist.databaseHelper.TaskDao;
import com.example.todolist.databinding.ActivityCreateTaskBinding;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity {

    ActivityCreateTaskBinding binding;
   // Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

      //  imageUri = createImageUri();


        /*ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.completed,android.R.layout.simple_spinner_dropdown_item);
        binding.isCompleted.setAdapter(arrayAdapter);*/


        if(getIntent().getStringExtra("type").equals("update")){

            binding.taskName.setText(getIntent().getStringExtra("taskName"));
            binding.taskDesc.setText(getIntent().getStringExtra("taskDesc"));
           // binding.completedLayout.setVisibility(View.VISIBLE);
           // binding.isCompleted.setText(getIntent().getStringExtra("isCompleted"));

            int id = getIntent().getIntExtra("id",0);

            binding.topAppBar.setTitle("Update Task");
            binding.addTask.setText("Update Task");
            binding.addTask.setOnClickListener(v -> {

                binding.taskName.onEditorAction(EditorInfo.IME_ACTION_DONE);
                binding.taskDesc.onEditorAction(EditorInfo.IME_ACTION_DONE); //for hide keyboard

                MyDatabase db = MyDatabase.getInstance(this);
                TaskDao taskDao = db.taskDao();

                String taskName = binding.taskName.getText().toString().trim();
                String taskDesc = binding.taskDesc.getText().toString().trim();
                String isComplete = getIntent().getStringExtra("isCompleted");

                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH)+1;
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR);
                if(hour== 0)
                    hour=12;
                final int minutes = calendar.get(Calendar.MINUTE);
                final String am_pm = calendar.get(Calendar.AM_PM)==Calendar.AM ? " AM" : " PM";

                final String currentDate = String.format(Locale.ENGLISH,"%02d/"+"%02d/",day,month)+year;
                final String currentTime = String.format(Locale.ENGLISH,"%02d:"+"%02d",hour,minutes)+am_pm;

                if(!taskName.isEmpty() && !taskDesc.isEmpty() && !isComplete.isEmpty()){
                    if(taskName.equals(getIntent().getStringExtra("taskName"))
                        && taskDesc.equals(getIntent().getStringExtra("taskDesc"))
                       /* && isComplete.equals(getIntent().getStringExtra("isCompleted"))*/){
                        onBackPressed();
                    }
                    else {
                        binding.addTask.setEnabled(false);
                        taskDao.update(new Task(id,taskName, taskDesc, currentDate, currentTime,isComplete));
                        Toast.makeText(this, "Task Created at "+currentDate+" "+currentTime, Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(CreateTaskActivity.this,MainActivity.class));
                                finishAffinity();
                            }
                        },1000);
                    }

                }
            });
        }

        else {
            binding.addTask.setOnClickListener(v -> {

                binding.taskName.onEditorAction(EditorInfo.IME_ACTION_DONE);
                binding.taskDesc.onEditorAction(EditorInfo.IME_ACTION_DONE); //for hide keyboard

                MyDatabase db = MyDatabase.getInstance(this);
                TaskDao taskDao = db.taskDao();

                String taskName = binding.taskName.getText().toString().trim();
                String taskDesc = binding.taskDesc.getText().toString().trim();

                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH)+1;
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR);
                if(hour== 0)
                    hour=12;
                final int minutes = calendar.get(Calendar.MINUTE);
                final String am_pm = calendar.get(Calendar.AM_PM)==Calendar.AM ? " AM" : " PM";

                final String currentDate = String.format(Locale.ENGLISH,"%02d/"+"%02d/",day,month)+year;
                final String currentTime = String.format(Locale.ENGLISH,"%02d:"+"%02d",hour,minutes)+am_pm;

                if(!taskName.isEmpty() && !taskDesc.isEmpty()){
                    binding.addTask.setEnabled(false);
                    taskDao.insert(new Task(taskName, taskDesc, currentDate, currentTime,"No"));
                    Toast.makeText(this, "Task Created at "+currentDate+" "+currentTime, Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(CreateTaskActivity.this,MainActivity.class));
                            finishAffinity();
                        }
                    },1000);
                }

            });
        }

    }
/*
    public Uri createImageUri(){
        File image = new File(getApplicationContext().getFilesDir(),"camera_img.png");
        return FileProvider.getUriForFile(getApplicationContext(),"com.example.todolist", image);
    }

    ActivityResultLauncher<Intent> cameraIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK && result.getData()!=null){

            }
        }
    });*/
}