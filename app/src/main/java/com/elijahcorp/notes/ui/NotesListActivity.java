package com.elijahcorp.notes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.elijahcorp.notes.R;
import com.elijahcorp.notes.domain.Note;
import com.elijahcorp.notes.domain.NoteCRUD;
import com.elijahcorp.notes.impl.NoteImpl;
import com.google.android.material.appbar.MaterialToolbar;

public class NotesListActivity extends AppCompatActivity {
    private MaterialToolbar topAppBar;
    private RecyclerView notesRecycleView;
    private final NotesAdapter notesAdapter = new NotesAdapter();
    private final NoteCRUD notesCRUD = new NoteImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseViews();
        initialiseTopAppBar();
        fillExampleData();
        initialiseNotesRecycleView();
    }

    private void fillExampleData() {
        notesCRUD.createNote(new Note("Заголовок 1", "jjjjjjjfpiwejfipjkoko[wekf[okq[epfko[koekf[okjkgjkerl"));
        notesCRUD.createNote(new Note("Заголовок 2", "jjjjjjjfpiwejfipjkoko[wekf[okq[epfko[koekf[olkwjef"));
        notesCRUD.createNote(new Note("Заголовок 3", "jjjjjjjfpiwejfipjkoko[wekf[okq[epfko[koekf[oaiwjfio;"));
        notesCRUD.createNote(new Note("Заголовок 4", "jjjjjjjfpiwejfipjkoko[wekf[okq[epfko[koekf[oliwrjfoiqehr;erouteup9rogh"));
    }

    private void initialiseViews() {
        topAppBar = findViewById(R.id.top_app_bar);
        notesRecycleView = findViewById(R.id.notes_recycle_view);
    }

    private void initialiseTopAppBar() {
        setSupportActionBar(topAppBar);
    }

    private void initialiseNotesRecycleView() {
        notesRecycleView.setLayoutManager(new LinearLayoutManager(this));
        notesRecycleView.setAdapter(notesAdapter);
        notesAdapter.setData(notesCRUD.getNotes());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_note_menu) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}