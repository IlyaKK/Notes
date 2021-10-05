package com.elijahcorp.notes.impl;

import android.annotation.SuppressLint;

import com.elijahcorp.notes.domain.Note;
import com.elijahcorp.notes.domain.NoteCRUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class NoteImpl implements NoteCRUD {
    private List<Note> cashNotes = new ArrayList<>();
    private int counterId = 0;

    private String initCreateTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = new Date();
        return formatter.format(date);
    }

    @Override
    public ArrayList<Note> getNotes() {
        return new ArrayList<>(cashNotes);
    }

    @Override
    public int createNote(Note note) {
        int newId = ++counterId;
        note.setIdNote(newId);
        note.setTimeCreate(initCreateTime());
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
                Note newNote = cashNotes.get(i);
                newNote.setTitle(newTitle);
                newNote.setDescription(newDescription);
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
                updateNotes.add(cashNotes.get(i));
            }
        }
        cashNotes = updateNotes;
    }
}
