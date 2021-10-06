package com.elijahcorp.notes.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
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
    private final String CHANGE_NOTE_KEY = "change_note_key";
    private ActivityResultLauncher<Intent> editNoteActivityLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseViews();
        initialiseTopAppBar();
        fillExampleData();
        initialiseGetChangeNote();
        initialiseNotesRecycleView();
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
            initialiseAddNoteToNotesList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialiseAddNoteToNotesList() {
        Note note = new Note("", "");
        Intent intent = new Intent(this, NoteEditActivity.class);
        intent.putExtra(CHANGE_NOTE_KEY, note);
        editNoteActivityLaunch.launch(intent);
    }

    private void initialiseViews() {
        topAppBar = findViewById(R.id.top_app_bar);
        notesRecycleView = findViewById(R.id.notes_recycle_view);
    }

    private void initialiseTopAppBar() {
        setSupportActionBar(topAppBar);
    }

    private void fillExampleData() {
        notesCRUD.createNote(new Note("Заголовок 1", "jjjjjjjfpiwejfipjkoko[wekf[okq[epfko[koekf[okjkgjkerl"));
        notesCRUD.createNote(new Note("Заголовок 2", "jjjjjjjfpiwejfipjkoko[wekf[okq[epfko[koekf[olkwjef"));
        notesCRUD.createNote(new Note("Заголовок 3", "jjjjjjjfpiwejfipjkoko[wekf[okq[epfko[koekf[oaiwjfio;"));
        notesCRUD.createNote(new Note("Заголовок 4", "jjjjjjjfpiwejfipjkoko[wekf[okq[epfko[koekf[oliwrjfoiqehr;erouteup9rogh"));
    }

    private void initialiseGetChangeNote() {
        editNoteActivityLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Note changeNote = data.getParcelableExtra(CHANGE_NOTE_KEY);
                    notesCRUD.updateNote(changeNote);
                    notesAdapter.setData(notesCRUD.getNotes());
                }
            } else if (result.getResultCode() == Activity.RESULT_FIRST_USER) {
                Intent data = result.getData();
                if (data != null) {
                    Note newNote = data.getParcelableExtra(CHANGE_NOTE_KEY);
                    notesCRUD.createNote(newNote);
                    notesAdapter.setData(notesCRUD.getNotes());
                }
            }
        });
    }

    private void initialiseNotesRecycleView() {
        notesRecycleView.setLayoutManager(new LinearLayoutManager(this));
        notesRecycleView.setAdapter(notesAdapter);
        notesAdapter.setData(notesCRUD.getNotes());
        notesAdapter.setOnCardClickListener(this::onCardClickListener);
    }

    private void onCardClickListener(Note note) {
        Intent intent = new Intent(this, NoteEditActivity.class);
        intent.putExtra(CHANGE_NOTE_KEY, note);
        editNoteActivityLaunch.launch(intent);
    }
}