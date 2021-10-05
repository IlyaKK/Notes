package com.elijahcorp.notes.domain;

import java.util.ArrayList;
import java.util.List;

public interface NoteCRUD {
    ArrayList<Note> getNotes();

    int createNote(Note note);

    Note readNote(int idNote);

    List<Note> updateNote(Note note);

    void deleteNote(int idNote);
}
