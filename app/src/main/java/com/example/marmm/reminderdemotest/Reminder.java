package com.example.marmm.reminderdemotest;

import android.os.Parcel;
import android.os.Parcelable;

public class Reminder implements Parcelable {


    private String mReminderText;

    public Reminder(String mReminderText) {
        this.mReminderText = mReminderText;
    }


    public String getmReminderText() {
        return mReminderText;
    }

    public void setmReminderText(String mReminderText) {
        this.mReminderText = mReminderText;
    }

    @Override
    public String toString() {
        return mReminderText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mReminderText);
    }

    protected Reminder(Parcel in) {
        this.mReminderText = in.readString();
    }

    public static final Parcelable.Creator<Reminder> CREATOR = new Parcelable.Creator<Reminder>() {
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
