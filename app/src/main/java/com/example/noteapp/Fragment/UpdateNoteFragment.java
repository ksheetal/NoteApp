package com.example.noteapp.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteapp.Adapter.NoteViewAdapter;
import com.example.noteapp.R;
import com.example.noteapp.database.NoteModel;
import com.example.noteapp.database.dao;

import java.util.List;

import static com.example.noteapp.database.dao.USER_ID;

public class UpdateNoteFragment extends Fragment {

    EditText noteTitle;
    EditText noteDesc;

    Button updateBtn;
    Button cancelBtn;
    List<NoteModel> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_update_note, container, false);

        noteTitle = v.findViewById(R.id.noteTitle);
        noteDesc = v.findViewById(R.id.noteDesc);
        updateBtn = v.findViewById(R.id.updateNoteButton);
        cancelBtn = v.findViewById(R.id.cancelNoteButton);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();

            }
        });


        Bundle bundle = this.getArguments();
        int ID = bundle.getInt("ID");
        String NOTE_TITLE = bundle.getString("NOTE_TITLE");
        String NOTE_DESC = bundle.getString("NOTE_DESC");
        String USER_ID = bundle.getString("USER_ID");

        noteTitle.setText(NOTE_TITLE);
        noteDesc.setText(NOTE_DESC);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                noteTitle.getText().toString();
//                noteDesc.getText().toString();

                NoteModel noteModel = new NoteModel();
                noteModel.setId(ID);
                noteModel.setNoteTitle(noteTitle.getText().toString());
                noteModel.setNoteDesc(noteDesc.getText().toString());
                noteModel.setUserID(USER_ID);
                dao data = new dao(getActivity());
                try {
                    data.updateNote(noteModel);
                   // HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
                   // homeScreenFragment.refreshData();
                    Toast.makeText(view.getContext(), "Note Updated!", Toast.LENGTH_LONG).show();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("updated","updated");
                    bundle1.putString("USER_ID",noteModel.getUserID());
                    Fragment fragment = new HomeScreenFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragment.setArguments(bundle1);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Can not update note" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }


}