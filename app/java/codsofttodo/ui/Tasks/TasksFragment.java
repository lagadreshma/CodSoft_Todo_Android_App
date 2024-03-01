package com.example.codsofttodo.ui.Tasks;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.codsofttodo.AddTaskFormActivity;
import com.example.codsofttodo.NotesDataModel;
import com.example.codsofttodo.R;
import com.example.codsofttodo.TasksAdapter;
import com.example.codsofttodo.TasksDataModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TasksAdapter tasksAdapter;
    private ArrayList<TasksDataModel> tasksList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Button addTaskButton, allTasksButton, completeTasksButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        addTaskButton = view.findViewById(R.id.addTaskButton);
        allTasksButton = view.findViewById(R.id.allTaskButton);
        completeTasksButton = view.findViewById(R.id.completeTaskButton);

        recyclerView = view.findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireContext(), AddTaskFormActivity.class);
                startActivity(intent);

            }
        });

        allTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireContext(), AllTasksActivity.class);
                startActivity(intent);

            }
        });

        completeTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireContext(), CompleteTasksActivity.class);
                startActivity(intent);

            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tasks");

        tasksList = new ArrayList<>();
        tasksAdapter = new TasksAdapter(tasksList, databaseReference);
        recyclerView.setAdapter(tasksAdapter);


        String todaysDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        // Attach a listener to read the data from Firebase
        databaseReference.orderByChild("tDueDate").equalTo(todaysDate).addValueEventListener(new ValueEventListener() {
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



        return view;
    }

}