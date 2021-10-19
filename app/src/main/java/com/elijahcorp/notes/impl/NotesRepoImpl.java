package com.elijahcorp.notes.impl;

import android.content.Context;

import com.elijahcorp.notes.domain.Note;
import com.elijahcorp.notes.domain.NoteCashRepo;
import com.elijahcorp.notes.domain.NoteFileRepo;
import com.elijahcorp.notes.domain.NotesRepo;

import java.util.List;

public class NotesRepoImpl implements NotesRepo {
    private final NoteCashRepo noteCashRepo = new NoteCashImpl();
    private final NoteFileRepo noteFileRepo = new NoteFileImpl();

    @Override
    public void readNotes(Context context) {
        noteCashRepo.setNotes(noteFileRepo.getNotesFromFiles(context));
    }

    @Override
    public List<Note> getNotes() {
        return noteCashRepo.getNotes();
    }

    @Override
    public void deleteNote(Context context, int position) {
        int idDeleteNote = noteCashRepo.deleteNote(position);
        noteFileRepo.deleteNoteFile(context, idDeleteNote);
    }

    @Override
    public void createNote(Context context, Note note) {
        noteCashRepo.createNote(note);
        noteFileRepo.saveNoteToFile(context, note);
    }

    @Override
    public void updateNote(Context context, Note note) {
        noteCashRepo.updateNote(note);
        noteFileRepo.updateNoteInFile(context, note);
    }
}
