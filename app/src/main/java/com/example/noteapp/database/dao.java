package com.example.noteapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class dao extends SQLiteOpenHelper {

    public static final String NOTE_TABLE = "NOTE_TABLE";
    public static final String NOTE_TITLE = "NOTE_TITLE";
    public static final String NOTE_DESC = "NOTE_DESC";
    public static final String ID = "ID";
    public static final String USER_ID = "USER_ID";

    public dao(@Nullable Context context) {
        super(context, "note.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NOTE_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_TITLE + " TEXT, " + NOTE_DESC + " TEXT, " + USER_ID + " TEXT )";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(NoteModel noteModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE_TITLE, noteModel.getNoteTitle());
        cv.put(NOTE_DESC, noteModel.getNoteDesc());
        cv.put(USER_ID, noteModel.getUserID());

        long insert = sqLiteDatabase.insert(NOTE_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<NoteModel> getAllNotes(String userId) {
        List<NoteModel> res = new ArrayList<>();
        String queryString = "SELECT * FROM " + NOTE_TABLE + " WHERE " + USER_ID + " = " + "'"+ userId + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                int noteId = cursor.getInt(0);
                String noteTitle = cursor.getString(1);
                String noteDesc = cursor.getString(2);
                String userID = cursor.getString(3);

                NoteModel noteModel = new NoteModel(noteId, noteTitle, noteDesc, userID);
                res.add(noteModel);

            } while (cursor.moveToNext());
        } else {

        }
        cursor.close();
        db.close();
        return res;
    }

    public Boolean deleteNote(int noteId){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + NOTE_TABLE + " WHERE " + ID + " = " + noteId;
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }

    }
}
