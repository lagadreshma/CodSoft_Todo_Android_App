package com.example.codsofttodo.ui.Calendar;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.codsofttodo.AddTaskFormActivity;
import com.example.codsofttodo.R;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private Button addTaskButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        addTaskButton = view.findViewById(R.id.addTaskButton);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the selected date
                String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;

                // Pass the selected date to the AddTaskFormActivity
                Intent intent = new Intent(requireContext(), AddTaskFormActivity.class);
                intent.putExtra("selectedDate", selectedDate);
                startActivity(intent);
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddTaskFormActivity without a specific date
                startActivity(new Intent(requireContext(), AddTaskFormActivity.class));
            }
        });



        return view;

    }

}