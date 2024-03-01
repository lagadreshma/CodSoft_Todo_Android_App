package com.example.codsofttodo.ui.Notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.codsofttodo.NotesDataModel;
import com.example.codsofttodo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteFragment extends Fragment {

    TextInputEditText title, subPoint, desc;
    Button addNote;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        title = view.findViewById(R.id.editTextTitle);
        subPoint = view.findViewById(R.id.editTextSubPoints);
        desc = view.findViewById(R.id.editTextDescription);
        addNote = view.findViewById(R.id.buttonAddNote);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Notes");

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateInput()){
                    saveData();
                }

            }
        });

        return view;

    }

    private boolean validateInput(){

        String noteTitle = title.getText().toString().trim();
        String noteSubpoint = subPoint.getText().toString().trim();
        String noteDesc = desc.getText().toString().trim();

        if(noteTitle.isEmpty()){
            title.setError("Please Enter Note Title");
            return false;
        }

        if(noteDesc.isEmpty()){
            desc.setError("Please Enter Note Description");
            return false;
        }

        if(noteSubpoint.isEmpty()){
            subPoint.setError("Please Enter Note Subpoint");
            return false;
        }

        return true;

    }

    public void saveData(){

        String noteTitle = title.getText().toString().trim();
        String noteSubpoint = subPoint.getText().toString().trim();
        String noteDesc = desc.getText().toString().trim();

        String noteId = databaseReference.push().getKey();
        String noteAddDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        NotesDataModel notesDataModel = new NotesDataModel(noteId, noteTitle, noteSubpoint, noteDesc, noteAddDate);
        databaseReference.child(noteId).setValue(notesDataModel).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(requireContext(), "Notes Saved.", Toast.LENGTH_SHORT).show();

                            replaceFragment(new AddNoteFragment(), R.id.nav_notes);

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace(); // Log the error in detail for debugging
                    }
                });
    }

    private void replaceFragment(Fragment fragment, int destinationId) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(destinationId);
    }

}