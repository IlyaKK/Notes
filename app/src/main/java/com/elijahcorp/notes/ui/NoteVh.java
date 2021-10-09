package com.elijahcorp.notes.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elijahcorp.notes.R;
import com.elijahcorp.notes.domain.Note;
import com.google.android.material.card.MaterialCardView;

public class NoteVh extends RecyclerView.ViewHolder {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView createTimeTextView;
    private MaterialCardView foregroundNoteCardView;
    private Note note;

    public NoteVh(@NonNull ViewGroup parent, NotesAdapter.OnCardClickListener listener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.one_note, parent, false));
        initialiseViews();
        foregroundNoteCardView.setOnClickListener(l -> listener.onCardClickListener(note));
    }

    private void initialiseViews() {
        titleTextView = itemView.findViewById(R.id.title_text_view);
        descriptionTextView = itemView.findViewById(R.id.description_text_view);
        createTimeTextView = itemView.findViewById(R.id.create_time_text_view);
        foregroundNoteCardView = itemView.findViewById(R.id.foreground_note_card_view);
    }

    public void bind(Note note) {
        this.note = note;
        titleTextView.setText(note.getTitle());
        descriptionTextView.setText(note.getDescription());
        createTimeTextView.setText(note.getTimeCreate());
    }

    public MaterialCardView getForegroundNoteCardView() {
        return foregroundNoteCardView;
    }
}
