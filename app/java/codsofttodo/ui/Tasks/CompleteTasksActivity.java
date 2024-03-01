package com.example.codsofttodo.ui.Tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.codsofttodo.R;
import com.example.codsofttodo.TasksAdapter;
import com.example.codsofttodo.TasksDataModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompleteTasksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;
    private ArrayList<TasksDataModel> tasksList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_tasks);

        recyclerView = findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tasks");

        tasksList = new ArrayList<>();
        tasksAdapter = new TasksAdapter(tasksList, databaseReference);
        recyclerView.setAdapter(tasksAdapter);

        // Attach a listener to read the data from Firebase
        databaseReference.orderByChild("tStatus").equalTo("Complete").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasksList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TasksDataModel tasksData = dataSnapshot.getValue(TasksDataModel.class);
                    if (tasksData != null) {
                        tasksList.add(tasksData);
                    }
                }
                tasksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });


    }
}