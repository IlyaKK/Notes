package com.elijahcorp.notes.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elijahcorp.notes.R;
import com.elijahcorp.notes.domain.Note;
import com.elijahcorp.notes.domain.NoteCashRepo;
import com.elijahcorp.notes.domain.NoteFileRepo;
import com.elijahcorp.notes.impl.NoteFileImpl;
import com.elijahcorp.notes.impl.NoteImpl;
import com.google.android.material.appbar.MaterialToolbar;

public class NotesListActivity extends AppCompatActivity {
    private MaterialToolbar topAppBar;
    private RecyclerView notesRecycleView;
    private final NotesAdapter notesAdapter = new NotesAdapter();
    private final NoteCashRepo notesCashRepo = new NoteImpl();
    private final NoteFileRepo noteFileRepo = new NoteFileImpl();
    private ActivityResultLauncher<Intent> editNoteActivityLaunch;
    private ItemTouchHelper.SimpleCallback swipeDeleteCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseViews();
        initialiseTopAppBar();
        fillDataFromFile();
        initialiseGetChangeNote();
        initialiseDeleteNoteSwipe();
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
        NoteEditActivity.launchEditNoteActivity(editNoteActivityLaunch, this, note);
    }

    private void initialiseViews() {
        topAppBar = findViewById(R.id.top_app_bar);
        notesRecycleView = findViewById(R.id.notes_recycle_view);
    }

    private void initialiseTopAppBar() {
        setSupportActionBar(topAppBar);
    }

    private void fillDataFromFile() {
        notesCashRepo.setNotes(noteFileRepo.getNotesFromFiles(this));
    }

    private void initialiseGetChangeNote() {
        editNoteActivityLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            switch (result.getResultCode()) {
                case Activity.RESULT_OK:
                    Intent data = result.getData();
                    if (data != null) {
                        Note changeNote = data.getParcelableExtra(NoteEditActivity.CHANGE_NOTE_KEY);
                        notesAdapter.setData(notesCashRepo.updateNote(changeNote));
                        noteFileRepo.updateNoteInFile(this, changeNote);
                        notesRecycleView.scrollToPosition(notesAdapter.getPositionNote(changeNote));
                    }
                    break;
                case Activity.RESULT_FIRST_USER:
                    data = result.getData();
                    if (data != null) {
                        Note newNote = data.getParcelableExtra(NoteEditActivity.CHANGE_NOTE_KEY);
                        if (!(newNote.getTitle().isEmpty() && newNote.getDescription().isEmpty())) {
                            int idNote = notesCashRepo.createNote(newNote);
                            notesAdapter.setData(notesCashRepo.getNotes());
                            noteFileRepo.saveNoteToFile(this, notesCashRepo.readNote(idNote));
                            notesRecycleView.scrollToPosition(notesAdapter.getItemCount() - 1);
                        }
                    }
                    break;
            }
        });
    }

    private void initialiseDeleteNoteSwipe() {
        swipeDeleteCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    getDefaultUIUtil().onSelected(((NoteVh) viewHolder).getForegroundNoteCardView());
                }
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDrawOver(c, recyclerView, ((NoteVh) viewHolder).getForegroundNoteCardView(), dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(((NoteVh) viewHolder).getForegroundNoteCardView());
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDraw(c, recyclerView, ((NoteVh) viewHolder).getForegroundNoteCardView(), dX, dY,
                        actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int idDeleteNote = notesCashRepo.deleteNote(viewHolder.getAdapterPosition());
                noteFileRepo.deleteNoteFile(NotesListActivity.this, idDeleteNote);
                notesAdapter.setData(notesCashRepo.getNotes());
            }
        };
    }

    private void initialiseNotesRecycleView() {
        notesRecycleView.setLayoutManager(new LinearLayoutManager(this));
        notesRecycleView.setItemAnimator(new DefaultItemAnimator());
        notesRecycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesRecycleView.setAdapter(notesAdapter);
        new ItemTouchHelper(swipeDeleteCallback).attachToRecyclerView(notesRecycleView);
        notesAdapter.setData(notesCashRepo.getNotes());
        notesAdapter.setOnCardClickListener(this::onCardClickListener);
    }

    private void onCardClickListener(Note note) {
        NoteEditActivity.launchEditNoteActivity(editNoteActivityLaunch, this, note);
    }
}