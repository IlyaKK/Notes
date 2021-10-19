package com.elijahcorp.notes.domain;

import android.content.Context;

import java.util.List;

public interface NotesRepo {
    void readNotes(Context context);

    List<Note> getNotes();

    void deleteNote(Context context, int position);

    void createNote(Context context, Note note);

    void updateNote(Context context, Note note);
}
