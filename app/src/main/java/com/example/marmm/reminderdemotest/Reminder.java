package com.example.marmm.reminderdemotest;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "reminder")
public class Reminder implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private Long id;


    @ColumnInfo(name = "remindertext")
    private String mReminderText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReminderText() {
        return mReminderText;
    }

    public void setmReminderText(String mReminderText) {
        this.mReminderText = mReminderText;
    }

    public Reminder(String mReminderText) {

        this.mReminderText = mReminderText;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.mReminderText);
    }

    protected Reminder(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mReminderText = in.readString();
    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel source) {
            return new Reminder(source);
        }

        @Override
        public Reminder[] newArray(int size) {
            return new Reminder[size];
        }
    };
}
