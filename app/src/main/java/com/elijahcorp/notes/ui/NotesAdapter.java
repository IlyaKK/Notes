package com.elijahcorp.notes.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elijahcorp.notes.R;
import com.elijahcorp.notes.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteVh> {
    private List<Note> data = new ArrayList<>();
    private OnCardClickListener listener = null;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Note> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_note, parent, false);
        return new NoteVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVh holder, int position) {
        Note note = getOneNote(position);
        holder.titleTextView.setText(note.getTitle());
        holder.descriptionTextView.setText(note.getDescription());
        holder.createTimeTextView.setText(note.getTimeCreate());
        holder.noteCardView.setOnClickListener(l -> listener.onCardClickListener(note));
    }

    private Note getOneNote(int position){
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnCardClickListener(OnCardClickListener listener) {
        this.listener = listener;
    }

    interface OnCardClickListener{
        void onCardClickListener(Note note);
    }
}
