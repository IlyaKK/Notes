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

    private void initialiseTopAppBar() {
        topAppBar = findViewById(R.id.top_app_bar);
    }

    private void launchNotesListFragment() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            launchPortraitNotesList();
        }
    }

    private void launchPortraitNotesList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_frame_layout, new NotesListFragment())
                .commit();
    }

    private void launchEditNoteFragment(Note note) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            launchEditNoteFragmentPortrait(note);
        }
    }

    private void launchEditNoteFragmentPortrait(Note note) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_frame_layout, NoteEditFragment.newInstance(note))
                .addToBackStack("")
                .commit();
    }

    @Override
    public void changeTopAppBar(String nameFragment) {
        if (nameFragment.equals(NotesListFragment.NOTES_LIST_FRAGMENT)) {
            topAppBar.setTitle(R.string.app_name);
            topAppBar.setNavigationIcon(null);
        } else {
            topAppBar.setTitle(" ");
            topAppBar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        }
        setSupportActionBar(topAppBar);
    }

    @Override
    public void displayNoteEdit(Note note) {
        launchEditNoteFragment(note);
    }

    @Override
    public void initialiseNavigationIconCallBack() {
        topAppBar.setNavigationOnClickListener(l -> getSupportFragmentManager().popBackStack());
    }
}
