package com.example.todotasks.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotasks.AddNewTask;
import com.example.todotasks.MainActivity;
import com.example.todotasks.R;
import com.example.todotasks.Utils.DataBaseHelper;
import com.example.todotasks.model.ToDoModel;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
private List<ToDoModel>mList;
private MainActivity activity;
private DataBaseHelper mDB;

    public ToDoAdapter(DataBaseHelper dataBaseHelper,MainActivity activity) {
        this.activity = activity;
        this.mDB = dataBaseHelper;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.task,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item=mList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        if (toBoolean(item.getStatus())) {
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.line.setVisibility(View.GONE);
        }
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if (isChecked){
                  mDB.upDateStatus(item.getId(),1);
               holder.line.setVisibility(View.VISIBLE);
              }else {
                  mDB.upDateStatus(item.getId(),0);
                  holder.line.setVisibility(View.GONE);

              }
            }
        });

    }
public boolean toBoolean (int num){
        return num!=0;

}

public Context getContext (){
        return activity;
}
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setTasks(List<ToDoModel>mList){
        this.mList=mList;
        notifyDataSetChanged();

    }
    public void deleteTask(int position){
        ToDoModel item=mList.get(position);
        mDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item=mList.get(position);
        Bundle bundle=new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());

        AddNewTask task=new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(),task.getTag());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
         View line;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox=itemView.findViewById(R.id.mcheckbox);
            line=itemView.findViewById(R.id.line_view);
        }
    }
}
