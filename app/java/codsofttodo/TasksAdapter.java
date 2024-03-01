package com.example.codsofttodo;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codsofttodo.ui.Notes.EditNoteActivity;
import com.example.codsofttodo.ui.Tasks.EditTaskActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private final ArrayList<TasksDataModel> tasksList;
    DatabaseReference databaseReference;
    public TasksAdapter(ArrayList<TasksDataModel> tasksList, DatabaseReference databaseReference){
        this.tasksList = tasksList;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public TasksAdapter.TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_task_card, parent, false);
        return new TasksAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.TasksViewHolder holder, int position) {

        TasksDataModel tasksData = tasksList.get(position);

        holder.tTitle.setText(tasksData.gettTitle());
        holder.tdueDate.setText(tasksData.gettDueDate());
        holder.taddedDate.setText(tasksData.gettAddedDate());
        holder.tStatus.setChecked("Complete".equals(tasksData.gettStatus()));

        // Checkbox click listener to update task status
        holder.tStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String taskId = tasksData.gettId();
                databaseReference.child(taskId).child("tStatus").setValue(isChecked ? "Complete" : "inComplete")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(buttonView.getContext(), "Task status updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(buttonView.getContext(), "Failed to update task status", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Failed to update task status", e);
                            }
                        });
            }
        });

        // delete task
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Task.")
                        .setMessage("Are you sure want to delete?")
                        .setIcon(R.drawable.delete)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String tid = tasksList.get(holder.getAdapterPosition()).gettId();

                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tasks");

                                reference.child(tid).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(v.getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(v.getContext(), "Error deleting Task", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                dialog.dismiss();
//
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        });

                builder.show();

            }
        });

        // update task
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tid = tasksList.get(holder.getAdapterPosition()).gettId();
                Log.d("TasksAdapter", "Update Button Clicked - Task ID : " + tid);

                Intent intent = new Intent(v.getContext(), EditTaskActivity.class);
                intent.putExtra("tid", tid);
                v.getContext().startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public class TasksViewHolder extends RecyclerView.ViewHolder {

        TextView tdueDate, tTitle, taddedDate;
        CheckBox tStatus;
        ImageButton editBtn, deleteBtn;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            tdueDate = itemView.findViewById(R.id.taskDueDate);
            tTitle = itemView.findViewById(R.id.taskTitle);
            taddedDate = itemView.findViewById(R.id.taskAddedDate);
            tStatus = itemView.findViewById(R.id.taskCheckboxButton);

            editBtn = itemView.findViewById(R.id.taskEditBtn);
            deleteBtn = itemView.findViewById(R.id.taskDeleteBtn);

        }
    }
}
