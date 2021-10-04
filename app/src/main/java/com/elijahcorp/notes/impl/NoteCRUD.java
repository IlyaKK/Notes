package com.elijahcorp.notes.impl;

import com.elijahcorp.notes.domain.Note;

import java.util.List;

public interface NoteCRUD {
    List<Note> getNotes();

    int createNote(Note note);

    Note readNote(int idNote);

    List<Note> updateNote(int idNote, String newTitle, String newDescription, String timeCreate);

    void deleteNote(int idNote);
}
