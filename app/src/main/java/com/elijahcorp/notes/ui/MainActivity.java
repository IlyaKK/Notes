package com.elijahcorp.notes.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.elijahcorp.notes.R;
import com.elijahcorp.notes.domain.Note;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity implements NotesListFragment.Controller, NoteEditFragment.Controller {
    private MaterialToolbar topAppBar;
    private NotesListFragment notesListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseTopAppBar();
        launchNotesListFragment();
    }

    private void initialiseTopAppBar() {
        topAppBar = findViewById(R.id.top_app_bar);
        setSupportActionBar(topAppBar);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(NoteEditFragment.NOTE_EDIT_FRAGMENT) != null) {
            NoteEditFragment noteEditFragment = (NoteEditFragment) getSupportFragmentManager().findFragmentByTag(NoteEditFragment.NOTE_EDIT_FRAGMENT);
            if (noteEditFragment != null) {
                noteEditFragment.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void launchNotesListFragment() {
        notesListFragment = new NotesListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_frame_layout, notesListFragment, NotesListFragment.NOTES_LIST_FRAGMENT)
                .commit();
    }

    @Override
    public void launchEditNoteScreen(Note note) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_frame_layout, NoteEditFragment.newInstance(note, topAppBar), NoteEditFragment.NOTE_EDIT_FRAGMENT)
                .addToBackStack("")
                .commit();
        topAppBar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        topAppBar.setTitle("");
    }

    @Override
    public void returnNewNote(Note note) {
        notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentByTag(NotesListFragment.NOTES_LIST_FRAGMENT);
        if (notesListFragment != null) {
            getSupportFragmentManager().popBackStack();
            notesListFragment.setNewNote(note);
            topAppBar.setTitle(getString(R.string.app_name));
            topAppBar.setNavigationIcon(null);
        }
    }

    @Override
    public void returnChangedNote(Note note) {
        notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentByTag(NotesListFragment.NOTES_LIST_FRAGMENT);
        if (notesListFragment != null) {
            getSupportFragmentManager().popBackStack();
            notesListFragment.setChangedNote(note);
            topAppBar.setTitle(getString(R.string.app_name));
            topAppBar.setNavigationIcon(null);
        }
    }
}
