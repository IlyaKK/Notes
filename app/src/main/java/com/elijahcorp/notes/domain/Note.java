package com.elijahcorp.notes.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private int idNote;
    private String title;
    private String description;
    private String timeCreate;

    public Note(int idNote, String title, String description, String timeCreate){
        this.idNote = idNote;
        this.title = title;
        this.description = description;
        this.timeCreate = timeCreate;
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Note(Parcel in) {
        idNote = in.readInt();
        title = in.readString();
        description = in.readString();
        timeCreate = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public void setTimeCreate(String timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdNote() {
        return idNote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idNote);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(timeCreate);
    }
}
