package com.example.marmm.reminderdemotest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<Reminder> mReminders;
    private ArrayAdapter mAdapter;
    private ListView mListView;
    private EditText mNewReminderText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mListView = findViewById(R.id.listView_main);
        mNewReminderText = findViewById(R.id.editText_main);

        mReminders = new ArrayList<>();


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mReminders.remove(position);
                updateUI();
                return true;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//Get the user text from the textfield

                String text = mNewReminderText.getText().toString();
                Reminder newReminder = new Reminder(text);

//Check if some text has been added

                if (!(TextUtils.isEmpty(text))) {

                    //Add the text to the list (datamodel)

                    mReminders.add(newReminder);


//Tell the adapter that the data set has been modified: the screen will be refreshed.
                updateUI();


                    //Initialize the EditText for the next item

                    mNewReminderText.setText("");

                } else {

                    //Show a message to the user if the textfield is empty

                    Snackbar.make(view, "Please enter some text in the textfield", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }

            }
        });
    }


    private void updateUI() {

        if (mAdapter == null) {

            mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mReminders);

            mListView.setAdapter(mAdapter);

        } else {

            mAdapter.notifyDataSetChanged();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
