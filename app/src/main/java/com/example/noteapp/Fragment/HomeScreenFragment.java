package com.example.noteapp.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.noteapp.MainActivity;
import com.example.noteapp.Adapter.NoteViewAdapter;
import com.example.noteapp.R;
import com.example.noteapp.database.NoteModel;
import com.example.noteapp.database.dao;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class HomeScreenFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {


    Button logoutBtn;
    SharedPreferences sharedPreferences;
    TextView userName, userEmail, userId;
    ImageView profileImage;
    RecyclerView noteRecyclerView;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    FloatingActionButton addFragmentBtn;
    EditText noteTitle;
    EditText noteDes;
    Button addNoteBtn;
    String myValue;
    RecyclerView itemRecyclerView;
    NoteViewAdapter adapterforItem;
    private String userIdValue;

    private List<NoteModel> res;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);

        Bundle bundle = this.getArguments();

        try{
         myValue = bundle.getString("uuid");
         String update = bundle.getString("updated");
         if(update.equals("updated")){
             myValue = bundle.getString("USER_ID");
             dao db = new dao(getActivity());
             res = db.getAllNotes(myValue);
             adapterforItem = new NoteViewAdapter(res, getContext());
             itemRecyclerView = v.findViewById(R.id.note_recyclerView);
             itemRecyclerView.setAdapter(adapterforItem);
             adapterforItem.notifyDataSetChanged();
         }
       }catch (Exception e){

       }

        logoutBtn = (Button) v.findViewById(R.id.logoutBtn);
        userName = (TextView) v.findViewById(R.id.name);
        userEmail = (TextView) v.findViewById(R.id.email);
        userId = (TextView) v.findViewById(R.id.userId);
        profileImage = (ImageView) v.findViewById(R.id.profileImage);
        noteRecyclerView = v.findViewById(R.id.note_recyclerView);
        //addFragmentBtn = v.findViewById(R.id.floatingActionButton);

        dao db = new dao(getActivity());
        res = db.getAllNotes(myValue);
        LinearLayoutManager layoutManagerForItems = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView itemRecyclerView = v.findViewById(R.id.note_recyclerView);
        itemRecyclerView.setLayoutManager(layoutManagerForItems);
        adapterforItem = new NoteViewAdapter(res, getContext());
        itemRecyclerView.setAdapter(adapterforItem);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this::onConnectionFailed)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.clear();
//                                    editor.commit();
                                    gotoMainActivity();
                                } else {
                                    Toast.makeText(getContext(), "Session not close", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


        noteTitle = v.findViewById(R.id.noteTitle);
        noteDes = v.findViewById(R.id.noteDesc);
        addNoteBtn = v.findViewById(R.id.addNoteButton);

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteModel noteModel;

                // validation Checks
                if(noteTitle.getText().toString().isEmpty() || noteDes.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please Enter Details",Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    noteModel = new NoteModel(1, noteTitle.getText().toString(), noteDes.getText().toString(), userId.getText().toString());
                    noteDes.setText("");
                    noteTitle.setText("");
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error ", Toast.LENGTH_LONG).show();
                    noteModel = new NoteModel(-1, "", "", "");
                }
                dao database = new dao(getActivity());
                boolean success = database.addOne(noteModel);
                if (success== true){
                    Toast.makeText(view.getContext(), "Note Added!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(view.getContext(), "Failed to Add note!", Toast.LENGTH_LONG).show();
                }
                List<NoteModel> res = db.getAllNotes(userId.getText().toString());
                NoteViewAdapter adapterforItem = new NoteViewAdapter(res, getContext());
                itemRecyclerView.setAdapter(adapterforItem);
            }
        });
        return v;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            userIdValue = result.getSignInAccount().getId();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            userName.setText("Hello, " + account.getDisplayName().split( " ")[0]);
            userEmail.setText(account.getEmail());
            userId.setText(account.getId());
            userIdValue = account.getId();
            try {
                Glide.with(getActivity()).load(account.getPhotoUrl()).into(profileImage);
            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), "image not found", Toast.LENGTH_LONG).show();
            }

        } else {
            gotoMainActivity();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }

    public void refreshData(){
        dao db = new dao(getActivity());
        res = db.getAllNotes(myValue);
        adapterforItem.notifyDataSetChanged();

    }

    @Override
    public void onResume(){
        super.onResume();
        refreshData();
    }
    private void gotoMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }
}