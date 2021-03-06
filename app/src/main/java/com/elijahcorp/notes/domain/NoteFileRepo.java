package com.elijahcorp.notes.domain;

import android.content.Context;

import java.util.List;

public interface NoteFileRepo {
    void saveNoteToFile(Context context, Note note);

    void updateNoteInFile(Context context, Note note);

    void deleteNoteFile(Context context, int idNote);

    List<Note> getNotesFromFiles(Context context);
}
