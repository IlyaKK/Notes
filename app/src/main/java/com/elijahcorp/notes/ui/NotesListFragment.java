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
import com.elijahcorp.notes.domain.NotesRepo;
import com.elijahcorp.notes.impl.NotesRepoImpl;

public class NotesListFragment extends Fragment {

    private RecyclerView notesRecycleView;
    private final NotesAdapter notesAdapter = new NotesAdapter();
    private final NotesRepo notesRepo = new NotesRepoImpl();
    private ItemTouchHelper.SimpleCallback swipeDeleteCallback;
    private Controller controller;
    public static final String NOTES_LIST_FRAGMENT = "NOTES_LIST_FRAGMENT";
    private TopAppBarListener topAppBarListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Controller) {
            controller = (Controller) context;
            topAppBarListener = (TopAppBarListener) context;
        } else {
            throw new IllegalStateException("Activity doesn't have impl NotesListFragment.Controller or NotesListFragment.TopAppBarListener interface");
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
        initialiseTopAppBar();
        fillDataFromFile();
        initialiseDeleteNoteSwipe();
        initialiseNotesRecycleView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.notes_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_note_menu) {
            initialiseAddNoteToNotesList();
            return true;
        }
        return false;
    }

    private void initialiseAddNoteToNotesList() {
        Note note = new Note("", "");
        controller.displayNoteEdit(note);
    }

    private void initialiseViews(View view) {
        notesRecycleView = view.findViewById(R.id.notes_recycle_view);
    }

    public void initialiseTopAppBar() {
        topAppBarListener.changeTopAppBar(NOTES_LIST_FRAGMENT);
    }

    private void fillDataFromFile() {
        notesRepo.readNotes(requireContext());
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
                notesRepo.deleteNote(requireContext(), viewHolder.getAdapterPosition());
                notesAdapter.setData(notesRepo.getNotes());
            }
        };
    }

    private void initialiseNotesRecycleView() {
        notesRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        notesRecycleView.setItemAnimator(new DefaultItemAnimator());
        notesRecycleView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        notesRecycleView.setAdapter(notesAdapter);
        new ItemTouchHelper(swipeDeleteCallback).attachToRecyclerView(notesRecycleView);
        notesAdapter.setData(notesRepo.getNotes());
        notesAdapter.setOnCardClickListener(this::onCardClickListener);
    }

    private void onCardClickListener(Note note) {
        controller.displayNoteEdit(note);
    }

    public void setNoteChange(Note note) {
        if (note.getTimeCreate().isEmpty()) {
            notesRepo.createNote(requireContext(), note);
        } else {
            notesRepo.updateNote(requireContext(), note);
        }
        notesAdapter.setData(notesRepo.getNotes());
    }

    interface Controller {

        void displayNoteEdit(Note note);

    }

    interface TopAppBarListener {

        void changeTopAppBar(String nameFragment);
    }

    @Override
    public void onDestroy() {
        controller = null;
        topAppBarListener = null;
        super.onDestroy();
    }
}
