package com.example.codsofttodo.ui.Notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.codsofttodo.NotesAdapter;
import com.example.codsofttodo.NotesDataModel;
import com.example.codsofttodo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private Button addNoteBtn;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    ArrayList<NotesDataModel> notesList;

    FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    ValueEventListener eventListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        addNoteBtn = view.findViewById(R.id.addNoteBtn);

        recyclerView = view.findViewById(R.id.noteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new NotesFragment(), R.id.addNote);

            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Notes");

        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList);
        recyclerView.setAdapter(notesAdapter);

        // Attach a listener to read the data from Firebase
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NotesDataModel notesData = dataSnapshot.getValue(NotesDataModel.class);
                    if (notesData != null) {
                        notesList.add(notesData);
                    }
                }
                notesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment, int destinationId) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(destinationId);
    }

}