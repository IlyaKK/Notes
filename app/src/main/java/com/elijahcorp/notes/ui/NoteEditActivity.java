package com.elijahcorp.notes.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elijahcorp.notes.R;
import com.elijahcorp.notes.domain.Note;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class NoteEditActivity extends AppCompatActivity {
    private TextInputEditText titleEditText;
    private TextInputEditText descriptionEditText;
    private MaterialToolbar editNoteTopAppBar;
    private final String CHANGE_NOTE_KEY = "change_note_key";
    private Note note;
    private boolean isEmptyNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        initViews();
        getNoteFromNotesList();
        fillEditTexts();
        setSupportActionBar(editNoteTopAppBar);
        initialiseNavigationIconOnClick();
    }

    @Override
    public void onBackPressed() {
        returnToNotesListActivity();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notes_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.change_create_time && !isEmptyNote) {
            initialiseChangeCreateTimeDataPicker();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialiseChangeCreateTimeDataPicker() {
        AtomicReference<String> data = new AtomicReference<>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            Date date = formatter.parse(note.getTimeCreate());
            if (date != null) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText(R.string.select_data_create)
                        .setSelection(date.getTime())
                        .build();

                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText(R.string.select_time_created)
                        .setHour(Integer.parseInt(note.getTimeCreate().split(" ", 2)[1].split(":", 2)[0]))
                        .setMinute(Integer.parseInt(note.getTimeCreate().split(" ", 2)[1].split(":", 2)[1]))
                        .build();

                datePicker.show(getSupportFragmentManager(), "data picker");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(selection);
                    timePicker.show(getSupportFragmentManager(), "time picker");
                    data.set(format.format(calendar.getTime()));
                });

                timePicker.addOnPositiveButtonClickListener((l) -> {
                    @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute());
                    data.set(data.get() + " " + time);
                    note.setTimeCreate(data.get());
                });
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        editNoteTopAppBar = findViewById(R.id.edit_note_top_app_bar);
    }

    private void getNoteFromNotesList() {
        Intent intent = getIntent();
        note = intent.getParcelableExtra(CHANGE_NOTE_KEY);
    }

    private void fillEditTexts() {
        if (note.getTitle().isEmpty() && note.getDescription().isEmpty()) {
            isEmptyNote = true;
        } else {
            titleEditText.setText(note.getTitle());
            descriptionEditText.setText(note.getDescription());
        }
    }

    private void initialiseNavigationIconOnClick() {
        editNoteTopAppBar.setNavigationOnClickListener(l -> {
            returnToNotesListActivity();
            finish();
        });
    }

    private void returnToNotesListActivity() {
        if (titleEditText.getText() != null && descriptionEditText.getText() != null && !isEmptyNote) {
            note.setTitle(titleEditText.getText().toString());
            note.setDescription(descriptionEditText.getText().toString());
            Intent intent = new Intent();
            intent.putExtra(CHANGE_NOTE_KEY, note);
            setResult(RESULT_OK, intent);
        } else if (titleEditText.getText() != null && descriptionEditText.getText() != null && isEmptyNote) {
            note.setTitle(titleEditText.getText().toString());
            note.setDescription(descriptionEditText.getText().toString());
            Intent intent = new Intent();
            intent.putExtra(CHANGE_NOTE_KEY, note);
            setResult(RESULT_FIRST_USER, intent);
        }
    }
}