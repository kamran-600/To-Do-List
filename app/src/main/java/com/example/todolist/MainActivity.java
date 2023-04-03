package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.todolist.databaseHelper.MyDatabase;
import com.example.todolist.databaseHelper.Task;
import com.example.todolist.databaseHelper.TaskDao;
import com.example.todolist.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<Task> list;
    TaskAdapter adapter;
    TaskDao taskDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(this,CreateTaskActivity.class);
            intent.putExtra("type","insert");
            startActivity(intent);
        });



        MyDatabase db = MyDatabase.getInstance(this);
        taskDao = db.taskDao();
        list = (ArrayList<Task>) taskDao.getTasks();
        taskDao.getUpdatedTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                list = (ArrayList<Task>) tasks;
                adapter.submitList(list);
            }
        });
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        adapter = new TaskAdapter();
        adapter.submitList(list);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        binding.recyclerview.addItemDecoration(decoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerview);


    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;// it is for drag;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task task = list.get(position);

            switch (direction){

                case ItemTouchHelper.LEFT :

                    taskDao.delete(task);
                    list.remove(position);
                    adapter.notifyItemRemoved(position);
                    Snackbar.make(binding.recyclerview, "Deleted "+task.getTaskName(),Snackbar.LENGTH_LONG)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(getColor(R.color.teal_800))
                            .setActionTextColor(getColor(R.color.white))
                            .setAction("Undo",v -> {
                               taskDao.insert(task);
                               list.add(position,task);
                               adapter.notifyItemInserted(position);
                            }).show();

                    break;

                case ItemTouchHelper.RIGHT:

                    Intent intent = new Intent(MainActivity.this,CreateTaskActivity.class);
                    intent.putExtra("type", "update");
                    intent.putExtra("id",task.getId());
                    intent.putExtra("taskName", task.getTaskName());
                    intent.putExtra("taskDesc", task.getTaskDesc());
                    intent.putExtra("isCompleted",task.getCompleted());
                    startActivity(intent);
                    adapter.notifyItemChanged(position);

                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeLeftActionIcon(R.drawable.delete)
                        .addSwipeLeftBackgroundColor(Color.RED)
                            .addSwipeRightActionIcon(R.drawable.edit)
                                    .addSwipeRightBackgroundColor(R.color.teal_700)
                                                    .setActionIconTint(Color.WHITE)
                                                            .create().decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}