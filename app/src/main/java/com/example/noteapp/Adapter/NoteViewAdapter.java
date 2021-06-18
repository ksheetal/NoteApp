package com.example.noteapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;
import com.example.noteapp.database.NoteModel;
import com.example.noteapp.database.dao;

import java.util.ArrayList;
import java.util.List;

public class NoteViewAdapter extends RecyclerView.Adapter<NoteViewAdapter.ViewHolder>{

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao data = new dao(view.getContext());
                data.deleteNote(notes.get(position).getId());
                notes.remove(position);
                notifyItemRemoved(position);

            }
        });
        holder.editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "edit image clicked",Toast.LENGTH_LONG).show();

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
