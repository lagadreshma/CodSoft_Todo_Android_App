package com.example.codsofttodo;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codsofttodo.ui.Notes.EditNoteActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private final ArrayList<NotesDataModel> notesList;

    public NotesAdapter(ArrayList<NotesDataModel> notesList){
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note_card, parent, false);
        return new NotesAdapter.NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesViewHolder holder, int position) {

        NotesDataModel notesData = notesList.get(position);

        holder.nDate.setText(notesData.getnAddDate());
        holder.nTitle.setText(notesData.getnTitle());
        holder.nSubpoint.setText(notesData.getnSubpoint());
        holder.nDesc.setText(notesData.getnDesc());


        // delete note
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Note.")
                        .setMessage("Are you sure want to delete?")
                        .setIcon(R.drawable.delete)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String nid = notesList.get(holder.getAdapterPosition()).getnId();

                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notes");

                                reference.child(nid).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(v.getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(v.getContext(), "Error deleting item", Toast.LENGTH_SHORT).show();
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

        // update note
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nid = notesList.get(holder.getAdapterPosition()).getnId();
                Log.d("NotesAdapter", "Update Button Clicked - Note ID : " + nid);

                Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
                intent.putExtra("nid", nid);
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {

        TextView nTitle, nDate, nSubpoint, nDesc;
        Button editBtn, deleteBtn;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            nTitle = itemView.findViewById(R.id.noteTitle);
            nDate = itemView.findViewById(R.id.noteAddDate);
            nSubpoint = itemView.findViewById(R.id.noteSubPoint);
            nDesc = itemView.findViewById(R.id.noteDesc);

            editBtn = itemView.findViewById(R.id.noteEdit);
            deleteBtn = itemView.findViewById(R.id.noteDelete);

        }
    }
}
