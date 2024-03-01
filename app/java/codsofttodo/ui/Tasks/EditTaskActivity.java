package com.example.codsofttodo.ui.Tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.codsofttodo.AddTaskFormActivity;
import com.example.codsofttodo.MainActivity;
import com.example.codsofttodo.R;
import com.example.codsofttodo.TasksDataModel;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {

    private TextInputEditText tTitle, tSubtitle, tDueDate, tDueTime;
    Button dueDateBtn, dueTimeBtn;
    SwitchMaterial remainder;
    Button updateTaskBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String tid;
    DatePickerDialog datePicker;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        tTitle = findViewById(R.id.editTaskTitle);
        tSubtitle = findViewById(R.id.editTaskSubtask);
        tDueDate = findViewById(R.id.editTaskDueDate);
        tDueTime = findViewById(R.id.editTaskDueTime);
        remainder = findViewById(R.id.switchReminder);
        dueDateBtn = findViewById(R.id.editDateButton);
        dueTimeBtn = findViewById(R.id.editTimeButton);
        updateTaskBtn = findViewById(R.id.updateTaskButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Tasks");

        getTaskDetails();

        dueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(EditTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tDueDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        dueTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(EditTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tDueTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minutes, true);

                timePickerDialog.show();
            }
        });

        updateTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateTask();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });

    }

    private void getTaskDetails(){

        Intent intent = getIntent();
        if(intent != null){
            tid = intent.getStringExtra("tid");
        }

        DatabaseReference taskRef = databaseReference.child(tid);
        taskRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    TasksDataModel task = snapshot.getValue(TasksDataModel.class);
                    if(task != null){
                        tTitle.setText(task.gettTitle());
                        tSubtitle.setText(task.gettSubtask());
                        tDueDate.setText(task.gettDueDate());
                        tDueTime.setText(task.gettDueTime());
                        remainder.setChecked(task.isRemainderSet()); // Set the state of the switch
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to get Task Details ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                error.toException().printStackTrace();
            }
        });


    }

    private void updateTask() {
        String taskTitle = tTitle.getText().toString();
        String taskSubtask = tSubtitle.getText().toString();
        String taskDueDate = tDueDate.getText().toString();
        String taskDueTime = tDueTime.getText().toString();
        boolean isRemainderSet = remainder.isChecked(); // Use isChecked() to get the state

        if (taskTitle.isEmpty() || taskSubtask.isEmpty() || taskDueDate.isEmpty() || taskDueTime.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference reference = databaseReference.child(tid);
        reference.child("tTitle").setValue(taskTitle);
        reference.child("tSubtask").setValue(taskSubtask);
        reference.child("tDueDate").setValue(taskDueDate);
        reference.child("tDueTime").setValue(taskDueTime);
        reference.child("remainderSet").setValue(isRemainderSet); // Corrected field name

        Toast.makeText(getApplicationContext(), "Task Updated", Toast.LENGTH_SHORT).show();
    }

}