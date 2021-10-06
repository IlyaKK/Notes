package com.elijahcorp.notes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.elijahcorp.notes.R;
import com.elijahcorp.notes.domain.Note;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class NoteEditActivity extends AppCompatActivity {
    private TextInputEditText titleEditText;
    private TextInputEditText descriptionEditText;
    private MaterialToolbar editNoteTopAppBar;
    private final String CHANGE_NOTE_KEY = "change_note_key";
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        initViews();
        getNoteFromNotesList();
        fillEditTexts();
        setSupportActionBar(editNoteTopAppBar);
        initialiseNavigationIconOnClick();
    }

    @Override
    public void onBackPressed() {
        returnToNotesListActivity();
        super.onBackPressed();
    }

    private void initViews() {
        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        editNoteTopAppBar = findViewById(R.id.edit_note_top_app_bar);
    }

    private void getNoteFromNotesList() {
        Intent intent = getIntent();
        note = intent.getParcelableExtra(CHANGE_NOTE_KEY);
    }

    private void fillEditTexts() {
        titleEditText.setText(note.getTitle());
        descriptionEditText.setText(note.getDescription());
    }

    private void initialiseNavigationIconOnClick() {
        editNoteTopAppBar.setNavigationOnClickListener(l -> {
            returnToNotesListActivity();
            finish();
        });
    }

    private void returnToNotesListActivity() {
        if (titleEditText.getText() != null && descriptionEditText.getText() != null) {
            note.setTitle(titleEditText.getText().toString());
            note.setDescription(descriptionEditText.getText().toString());
            Intent intent = new Intent();
            intent.putExtra(CHANGE_NOTE_KEY, note);
            setResult(RESULT_OK, intent);
        }
    }
}