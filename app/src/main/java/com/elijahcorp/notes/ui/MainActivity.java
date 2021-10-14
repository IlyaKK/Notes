package com.elijahcorp.notes.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.elijahcorp.notes.R;
import com.elijahcorp.notes.domain.Note;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity implements NotesListFragment.Controller, NotesListFragment.TopAppBarListener, NoteEditFragment.Controller, NoteEditFragment.TopAppBarListener {
    private MaterialToolbar topAppBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseTopAppBar();
        launchNotesListFragment();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(NoteEditFragment.NOTE_EDIT_FRAGMENT) != null) {
            returnToBackOrSavePressed();
        } else {
            super.onBackPressed();
        }
    }

    private void initialiseTopAppBar() {
        topAppBar = findViewById(R.id.top_app_bar);
    }

    private void launchNotesListFragment() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            launchPortraitNotesList();
        } else {
            launchLandscapeNotesList();
        }
    }

    private void launchPortraitNotesList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_frame_layout, new NotesListFragment(), NotesListFragment.NOTES_LIST_FRAGMENT)
                .commit();
    }

    private void launchLandscapeNotesList() {
        if (getSupportFragmentManager().findFragmentByTag(NotesListFragment.NOTES_LIST_FRAGMENT) != null) {
            getSupportFragmentManager().popBackStack();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_frame_layout, new NotesListFragment(), NotesListFragment.NOTES_LIST_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void changeTopAppBar(String nameFragment) {
        if (nameFragment.equals(NotesListFragment.NOTES_LIST_FRAGMENT)) {
            topAppBar.setTitle(R.string.app_name);
            topAppBar.setNavigationIcon(null);
        } else {
            topAppBar.setTitle(" ");
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                topAppBar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
            } else {
                topAppBar.setNavigationIcon(R.drawable.ic_baseline_check_24);
            }
        }
        setSupportActionBar(topAppBar);
    }

    @Override
    public void displayNoteEdit(Note note) {
        launchEditNoteFragment(note);
    }

    private void launchEditNoteFragment(Note note) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            launchEditNoteFragmentPortrait(note);
        } else {
            launchEditNoteFragmentLandscape(note);
        }
    }

    private void launchEditNoteFragmentPortrait(Note note) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_frame_layout, NoteEditFragment.newInstance(note), NoteEditFragment.NOTE_EDIT_FRAGMENT)
                .addToBackStack("")
                .commit();
    }

    private void launchEditNoteFragmentLandscape(Note note) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_2_frame_layout, NoteEditFragment.newInstance(note), NoteEditFragment.NOTE_EDIT_FRAGMENT)
                .commit();
    }

    @Override
    public void initialiseNavigationIconBack() {
        topAppBar.setNavigationOnClickListener(l -> returnToBackOrSavePressed());
    }

    private void returnToBackOrSavePressed() {
        NotesListFragment notesListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentByTag(NotesListFragment.NOTES_LIST_FRAGMENT);
        NoteEditFragment noteEditFragment = (NoteEditFragment) getSupportFragmentManager().findFragmentByTag(NoteEditFragment.NOTE_EDIT_FRAGMENT);
        if (notesListFragment != null && noteEditFragment != null) {
            noteEditFragment.saveChangeNote();
            if (noteEditFragment.getArguments() != null) {
                Note note = noteEditFragment.getArguments().getParcelable(NoteEditFragment.CHANGE_NOTE_KEY);
                notesListFragment.setNoteChange(note);
            }

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                getSupportFragmentManager().popBackStack();
            } else {
                deleteOldEditFragment(noteEditFragment);
                notesListFragment.initialiseTopAppBar();
            }
        }
    }

    @Override
    public void deleteOldEditFragment(NoteEditFragment noteEditFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(noteEditFragment)
                .commit();
    }
}
