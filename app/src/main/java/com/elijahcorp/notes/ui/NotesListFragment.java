package com.elijahcorp.notes.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class NotesListFragment extends Fragment {

    private RecyclerView notesRecycleView;
    private final NotesAdapter notesAdapter = new NotesAdapter();
    private final NoteCashRepo notesCashRepo = new NoteImpl();
    private final NoteFileRepo noteFileRepo = new NoteFileImpl();
    private ItemTouchHelper.SimpleCallback swipeDeleteCallback;
    private Controller controller;
    public static final String NOTES_LIST_FRAGMENT = "NOTES_LIST_FRAGMENT";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Controller) {
            controller = (Controller) context;
        } else {
            throw new IllegalStateException("Activity doesn't have impl NotesListFragment.Controller interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initialiseViews(view);
        fillDataFromFile();
        initialiseDeleteNoteSwipe();
        initialiseNotesRecycleView();
    }

    private void initialiseViews(View view) {
        notesRecycleView = view.findViewById(R.id.notes_recycle_view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.notes_menu, menu);
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
        controller.launchEditNoteScreen(note);
    }

    private void fillDataFromFile() {
        notesCashRepo.setNotes(noteFileRepo.getNotesFromFiles(requireContext()));
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
                noteFileRepo.deleteNoteFile(requireContext(), idDeleteNote);
                notesAdapter.setData(notesCashRepo.getNotes());
            }
        };
    }

    private void initialiseNotesRecycleView() {
        notesRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        notesRecycleView.setItemAnimator(new DefaultItemAnimator());
        notesRecycleView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        notesRecycleView.setAdapter(notesAdapter);
        new ItemTouchHelper(swipeDeleteCallback).attachToRecyclerView(notesRecycleView);
        notesAdapter.setData(notesCashRepo.getNotes());
        notesAdapter.setOnCardClickListener(this::onCardClickListener);
    }

    private void onCardClickListener(Note note) {
        controller.launchEditNoteScreen(note);
    }

    public void setNewNote(Note note) {
        notesCashRepo.createNote(note);
        noteFileRepo.saveNoteToFile(requireContext(), note);
        notesAdapter.setData(notesCashRepo.getNotes());
    }

    public void setChangedNote(Note note) {
        notesCashRepo.updateNote(note);
        noteFileRepo.updateNoteInFile(requireContext(), note);
    }

    interface Controller {
        void launchEditNoteScreen(Note note);
    }
}
