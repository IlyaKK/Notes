package com.elijahcorp.notes.domain;

import java.util.ArrayList;
import java.util.List;

public interface NoteCashRepo {
    void setNotes(List<Note> notes);

    ArrayList<Note> getNotes();

    void createNote(Note note);

    void updateNote(Note note);

    int deleteNote(int position);
}
