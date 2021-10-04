package com.elijahcorp.notes.domain;

import com.elijahcorp.notes.impl.NoteCRUD;

import java.util.ArrayList;
import java.util.List;

public class NoteImpl implements NoteCRUD {
    private List<Note> cashNotes = new ArrayList<>();
    private int counterId = 0;

    @Override
    public List<Note> getNotes() {
        return cashNotes;
    }

    @Override
    public int createNote(Note note) {
        int newId = ++counterId;
        note.setIdNote(newId);
        cashNotes.add(note);
        return newId;
    }

    @Override
    public Note readNote(int idNote) {
        return cashNotes.get(idNote);
    }

    @Override
    public List<Note> updateNote(int idNote, String newTitle, String newDescription, String timeCreate) {
        List<Note> updateNotes = new ArrayList<>();
        for (int i = 0; i < cashNotes.size(); i++) {
            if (cashNotes.get(i).getIdNote() == idNote) {
                Note newNote = new Note(newTitle, newDescription);
                newNote.setIdNote(idNote);
                newNote.setTimeCreate(timeCreate);
                updateNotes.set(i, newNote);
            } else {
                updateNotes.set(i, cashNotes.get(i));
            }
        }
        cashNotes = updateNotes;
        return cashNotes;
    }

    @Override
    public void deleteNote(int idNote) {
        List<Note> updateNotes = new ArrayList<>();
        for (int i = 0; i < cashNotes.size(); i++) {
            if (cashNotes.get(i).getIdNote() != idNote) {
                updateNotes.set(i, cashNotes.get(i));
            }
        }
        cashNotes = updateNotes;
    }
}
