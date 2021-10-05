package com.elijahcorp.notes.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elijahcorp.notes.R;

public class NoteVh extends RecyclerView.ViewHolder {
    public NoteVh(@NonNull View itemView) {
        super(itemView);
    }

    public TextView titleTextView = itemView.findViewById(R.id.title_text_view);
    public TextView descriptionTextView = itemView.findViewById(R.id.description_text_view);
    public TextView createTimeTextView = itemView.findViewById(R.id.create_time_text_view);
}
