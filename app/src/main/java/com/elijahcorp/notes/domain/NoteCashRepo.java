package com.elijahcorp.notes.domain;

import java.util.ArrayList;
import java.util.List;

public interface NoteCashRepo {
    void setNotes(List<Note> notes);

    ArrayList<Note> getNotes();

    int createNote(Note note);

    Note readNote(int idNote);

    List<Note> updateNote(Note note);

    int deleteNote(int position);
}
