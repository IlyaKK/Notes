package com.elijahcorp.notes.domain;

import com.elijahcorp.notes.impl.NoteCRUD;

import java.util.ArrayList;
import java.util.List;

public class NoteImpl implements NoteCRUD {
    private List<Note> notes = new ArrayList<>();

    @Override
    public void createNote(Note note) {
        notes.add(note);
    }

    @Override
    public Note readNote(int idNote) {
        return notes.get(idNote);
    }

    @Override
    public List<Note> updateNote(int idNote, String newTitle, String newDescription, String timeCreate) {
        List<Note> updateNotes = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getIdNote() == idNote) {
                Note newNote = new Note(newTitle, newDescription);
                newNote.setIdNote(idNote);
                newNote.setTimeCreate(timeCreate);
                updateNotes.set(i, newNote);
            } else {
                updateNotes.set(i, notes.get(i));
            }
        }
        notes = updateNotes;
        return notes;
    }

    @Override
    public void deleteNote(int idNote) {
        List<Note> updateNotes = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getIdNote() != idNote) {
                updateNotes.set(i, notes.get(i));
            }
        }
        notes = updateNotes;
    }
}
