package com.elijahcorp.notes.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elijahcorp.notes.R;
import com.google.android.material.card.MaterialCardView;

public class NoteVh extends RecyclerView.ViewHolder {
    public TextView titleTextView;
    public TextView descriptionTextView;
    public TextView createTimeTextView;
    public MaterialCardView foregroundNoteCardView;
    public MaterialCardView backgroundCardView;

    public NoteVh(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.title_text_view);
        descriptionTextView = itemView.findViewById(R.id.description_text_view);
        createTimeTextView = itemView.findViewById(R.id.create_time_text_view);
        foregroundNoteCardView = itemView.findViewById(R.id.foreground_note_card_view);
        backgroundCardView = itemView.findViewById(R.id.background_card_view);
    }
}
