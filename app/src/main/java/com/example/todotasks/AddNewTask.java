package com.example.todotasks;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todotasks.Adapter.ToDoAdapter;
import com.example.todotasks.Utils.DataBaseHelper;
import com.example.todotasks.model.ToDoModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

 public static final String TAG="AddNewTask";
 private EditText mEditText;
 private Button mSaveBtn;
 private DataBaseHelper mDBH;
 public static AddNewTask newInstance(){
     return new AddNewTask();
 }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.add_new_task,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditText=view.findViewById(R.id.edittext);
        mSaveBtn=view.findViewById(R.id.button_save);
        mDBH=new DataBaseHelper(getActivity());


        boolean isUpdate=false;
        Bundle bundle=getArguments();
        if (bundle !=null){
            isUpdate=true;
            String task=bundle.getString("task");
            mEditText.setText(task);
            if (task.length()>0){
                mSaveBtn.setEnabled(false);
            }
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             if (s.toString().equals("")){
                 mSaveBtn.setEnabled(false);
                 mSaveBtn.setBackgroundColor(Color.GRAY);
             }else {
                 mSaveBtn.setEnabled(true);
                 mSaveBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

             }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
             final boolean finalIsUpdate=isUpdate;
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=mEditText.getText().toString();
                  if (finalIsUpdate){
                      mDBH.updateTask( bundle.getInt("id"),text);
                  }
                  else {
                      ToDoModel item =new ToDoModel();
                      item.setTask(text);
                      item.setStatus(0);
                      mDBH.insertTask(item);
                  }
                  dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity=getActivity();
        if (activity instanceof OnDialogeCloseListener){
            ((OnDialogeCloseListener)activity).onDialogClose(dialog);
        }
    }
}
