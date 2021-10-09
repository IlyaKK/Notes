package com.elijahcorp.notes.ui;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elijahcorp.notes.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteVh> {
    private List<Note> data = new ArrayList<>();
    private OnCardClickListener listener = null;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Note> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteVh(parent, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVh holder, int position) {
        holder.bind(getOneNote(position));
    }

    private Note getOneNote(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnCardClickListener(OnCardClickListener listener) {
        this.listener = listener;
    }

    public int getPositionNote(Note note) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getIdNote() == note.getIdNote()) {
                return i;
            }
        }
        return 0;
    }

    interface OnCardClickListener {
        void onCardClickListener(Note note);
    }
}
