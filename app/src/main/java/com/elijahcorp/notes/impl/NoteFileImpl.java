package com.elijahcorp.notes.impl;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import com.elijahcorp.notes.domain.Note;
import com.elijahcorp.notes.domain.NoteFileRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class NoteFileImpl implements NoteFileRepo {
    @Override
    public void saveNoteToFile(Context context, Note note) {
        ObjectMapper om = new ObjectMapper();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                context.openFileOutput(String.format("%s.txt", note.getIdNote()), MODE_PRIVATE)))) {
            bw.write(om.writeValueAsString(note));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateNoteInFile(Context context, Note note) {
        saveNoteToFile(context, note);
    }

    @Override
    public void deleteNoteFromFile(Context context, int idNote) {
        String[] filesNotes = context.fileList();
        for (String fileNote : filesNotes) {
            if (fileNote.equals(String.format("%s.txt", idNote))) {
                context.deleteFile(fileNote);
            }
        }
    }

    @Override
    public List<Note> getNotesFromFiles(Context context) {
        List<Note> notes = new ArrayList<>();
        String[] filesNotes = context.fileList();
        for (String fileName : filesNotes) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    context.openFileInput(fileName)))) {
                String str;
                while ((str = br.readLine()) != null) {
                    JSONObject jsonRequest = new JSONObject(str);
                    Note note = new Note(jsonRequest.getInt("idNote"), jsonRequest.getString("title"),
                            jsonRequest.getString("description"), jsonRequest.getString("timeCreate"));
                    notes.add(note);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        return notes;
    }
}
