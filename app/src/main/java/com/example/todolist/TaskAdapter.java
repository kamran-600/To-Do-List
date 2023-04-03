package com.example.todolist;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.example.todolist.databaseHelper.MyDatabase;
import com.example.todolist.databaseHelper.Task;
import com.example.todolist.databaseHelper.TaskDao;
import com.example.todolist.databinding.BottomSheetBinding;
import com.example.todolist.databinding.SingleRowTaskBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ListAdapter<Task,TaskAdapter.Viewholder> {
    public TaskAdapter() {
        super(diffCallback);
    }
    private static final DiffUtil.ItemCallback<Task> diffCallback = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId()== newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SingleRowTaskBinding binding = SingleRowTaskBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
           Task task = getItem(position);
           holder.binding.tName.setText(task.getTaskName());
           holder.binding.desc.setText(task.getTaskDesc());
           holder.binding.postedDate.setText(task.getDate());
           holder.binding.time.setText(task.getTime());
           if(task.getCompleted().equals("Yes"))
               holder.binding.lottie.setVisibility(View.VISIBLE);
           else{
               //holder.binding.lottie.clearAnimation();
               holder.binding.lottie.setVisibility(View.GONE);
           }
           holder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext(),R.style.bottom_sheet_theme);
                   BottomSheetBinding sheetBinding = BottomSheetBinding.inflate(LayoutInflater.from(v.getContext()));
                   bottomSheetDialog.setContentView(sheetBinding.getRoot());
                   bottomSheetDialog.show();
                   if(task.getCompleted().equals("Yes"))
                       sheetBinding.yesButton.setChecked(true);
                   else sheetBinding.noButton.setChecked(true);

                   MyDatabase db = MyDatabase.getInstance(v.getContext());
                   TaskDao taskDao = db.taskDao();

                   sheetBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                       @Override
                       public void onCheckedChanged(RadioGroup group, int checkedId) {
                           if(checkedId == sheetBinding.yesButton.getId()) {

                               taskDao.update(new Task(task.getId(),task.getTaskName(),task.getTaskDesc(),task.getDate(),task.getTime(), "Yes"));
                               holder.binding.lottie.setVisibility(View.VISIBLE);
                               new Handler().postDelayed(new Runnable() {
                                   @Override
                                   public void run() {
                                       bottomSheetDialog.dismiss();
                                   }
                               },300);

                           }
                           else {

                               taskDao.update(new Task(task.getId(),task.getTaskName(),task.getTaskDesc(),task.getDate(),task.getTime(), "No"));
                               holder.binding.lottie.setVisibility(View.GONE);
                               new Handler().postDelayed(new Runnable() {
                                   @Override
                                   public void run() {
                                       bottomSheetDialog.dismiss();
                                   }
                               },300);
                           }
                       }
                   });
                   return true;
               }
           });

    }

    /*ArrayList<Task> list;

    public TaskAdapter(ArrayList<Task> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TaskAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleRowTaskBinding binding = SingleRowTaskBinding.inflate(inflater,parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.Viewholder holder, int position) {
            Task task = list.get(position);
            holder.binding.tName.setText(task.getTaskName());
            holder.binding.desc.setText(task.getTaskDesc());
            holder.binding.postedDate.setText(task.getDate());
            holder.binding.time.setText(task.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }*/

    public static class Viewholder extends RecyclerView.ViewHolder {

        SingleRowTaskBinding binding;
        public Viewholder(@NonNull SingleRowTaskBinding binding1) {
            super(binding1.getRoot());
            this.binding = binding1;
        }
    }

}
