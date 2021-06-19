package com.example.noteapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Fragment.UpdateNoteFragment;
import com.example.noteapp.R;
import com.example.noteapp.database.NoteModel;
import com.example.noteapp.database.dao;

import java.util.ArrayList;
import java.util.List;

public class NoteViewAdapter extends RecyclerView.Adapter<NoteViewAdapter.ViewHolder> {

    private List<NoteModel> notes = new ArrayList<>();
    private Context mContext;

    public NoteViewAdapter(List<NoteModel> notes, Context mContext) {
        this.notes = notes;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(notes.get(position).getNoteTitle());
        holder.noteDesc.setText(notes.get(position).getNoteDesc());
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao data = new dao(view.getContext());
                try {
                    data.deleteNote(notes.get(position).getId());
                    notes.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(view.getContext(), "Note Deleted!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Please Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle bundle = new Bundle();
                bundle.putInt("ID",notes.get(position).getId());
                bundle.putString("NOTE_TITLE",notes.get(position).getNoteTitle());
                bundle.putString("NOTE_DESC",notes.get(position).getNoteDesc());
                bundle.putString("USER_ID",notes.get(position).getUserID());

                Fragment myFragment = new UpdateNoteFragment();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

//                NoteModel noteModel = new NoteModel();
//                noteModel.setId(notes.get(position).getId());
//                noteModel.setNoteTitle("Hard Coded Title");
//                noteModel.setNoteDesc("Hard Coded Desc");
//                noteModel.setUserID(notes.get(position).getUserID());
//                dao data = new dao(view.getContext());
//                try {
//                    data.updateNote(noteModel);
//                    notes.remove(position);
//                    notes.add(position,noteModel);
//                    notifyDataSetChanged();
//                } catch (Exception e) {
//                    Toast.makeText(view.getContext(), "Can not update note", Toast.LENGTH_LONG).show();
//                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView noteDesc;
        ImageView deleteImg;
        ImageView editImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView);
            noteDesc = itemView.findViewById(R.id.textView2);
            deleteImg = itemView.findViewById(R.id.imageView);
            editImageView = itemView.findViewById(R.id.editImageView);
        }
    }
}
