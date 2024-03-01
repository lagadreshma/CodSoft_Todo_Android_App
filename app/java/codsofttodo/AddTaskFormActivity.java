package com.example.codsofttodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.codsofttodo.ui.Notes.AddNoteFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskFormActivity extends AppCompatActivity {

    TextInputEditText title, subtask, duedate, duetime;
    Button setDueDateButton, setDueTimeButton, addTaskButton;
    SwitchMaterial switchToggleBtn;
    DatePickerDialog datePicker;
    TimePickerDialog timePickerDialog;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String tDueDateCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_form);

        title = findViewById(R.id.editTaskTitle);
        subtask = findViewById(R.id.editTaskSubtask);
        duedate = findViewById(R.id.editTaskDueDate);
        duetime = findViewById(R.id.editTaskDueTime);
        switchToggleBtn = findViewById(R.id.switchReminder);

        setDueDateButton = findViewById(R.id.editDateButton);
        setDueTimeButton = findViewById(R.id.editTimeButton);
        addTaskButton = findViewById(R.id.addTaskButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tasks");

        tDueDateCalendar = getIntent().getStringExtra("selectedDate");
        duedate.setText(tDueDateCalendar);

        setDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(AddTaskFormActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                duedate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        setDueTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AddTaskFormActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        duetime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minutes, true);

                timePickerDialog.show();
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateInput()){
                    saveData();
                }

            }
        });


    }

    private boolean validateInput() {
        String taskTitleText = title.getText().toString().trim();
        if (taskTitleText.isEmpty()) {
            title.setError("Task title cannot be empty");
            return false;
        }
        // Add more validation as needed
        return true;
    }


    public void saveData() {
        String taskTitle = title.getText().toString().trim();
        String taskSubtask = subtask.getText().toString().trim();
        String taskDueDate = duedate.getText().toString().trim();
        String taskDueTime = duetime.getText().toString().trim();
        boolean taskReminder = switchToggleBtn.isChecked();

        // Ensure proper date format
        SimpleDateFormat dateFormatterInput = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        SimpleDateFormat dateFormatterOutput = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date dueDate = dateFormatterInput.parse(taskDueDate);
            taskDueDate = dateFormatterOutput.format(dueDate); // Reformat date string
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing due date", Toast.LENGTH_SHORT).show();
            return;
        }

        String taskId = databaseReference.push().getKey();
        String taskAddedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String taskStatus = "inComplete";

        TasksDataModel tasksDataModel = new TasksDataModel(taskId, taskTitle, taskSubtask, taskAddedDate, taskDueDate, taskDueTime, taskReminder, taskStatus);
        databaseReference.child(taskId).setValue(tasksDataModel).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Task Added.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace(); // Log the error in detail for debugging
                    }
                });
    }


}