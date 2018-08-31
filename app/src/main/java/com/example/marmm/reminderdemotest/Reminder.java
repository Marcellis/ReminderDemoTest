package com.example.marmm.reminderdemotest;

public class Reminder {


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
}
