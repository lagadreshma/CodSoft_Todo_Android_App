package com.example.codsofttodo.ui.Notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.codsofttodo.NotesDataModel;
import com.example.codsofttodo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditNoteActivity extends AppCompatActivity {

    private TextInputEditText noteTitle, noteSubpoint, noteDesc;
    private Button updateNoteBtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String nid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        noteTitle = findViewById(R.id.editTextTitle);
        noteSubpoint = findViewById(R.id.editTextSubPoints);
        noteDesc = findViewById(R.id.editTextDescription);

        updateNoteBtn = findViewById(R.id.buttonUpdateNote);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Notes");

        getNoteDetails();

        updateNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateNote();

            }
        });

    }

    private void getNoteDetails(){

        Intent intent = getIntent();
        if(intent != null){
            nid = intent.getStringExtra("nid");
        }

        DatabaseReference noteRef = databaseReference.child(nid);
        noteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    NotesDataModel note = snapshot.getValue(NotesDataModel.class);
                    if(note != null){
                        noteTitle.setText(note.getnTitle());
                        noteSubpoint.setText(note.getnSubpoint());
                        noteDesc.setText(note.getnDesc());
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Failed to get Note Details ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                error.toException().printStackTrace();

            }
        });

    }

    private void updateNote(){

        String nTitle = noteTitle.getText().toString();
        String nSubpoint = noteSubpoint.getText().toString();
        String nDesc = noteDesc.getText().toString();

        DatabaseReference reference = databaseReference.child(nid);
        reference.child("nTitle").setValue(nTitle);
        reference.child("nSubPoint").setValue(nSubpoint);
        reference.child("nDesc").setValue(nDesc);

        Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();

    }

}