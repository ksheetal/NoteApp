package com.example.noteapp.database;

public class NoteModel {

    private int id;
    private String noteTitle;
    private String noteDesc;
    private String userID;

    public NoteModel(int id, String noteTitle, String noteDesc, String userID) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteDesc = noteDesc;
        this.userID = userID;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public NoteModel() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDesc() {
        return noteDesc;
    }

    public void setNoteDesc(String noteDesc) {
        this.noteDesc = noteDesc;
    }
}
