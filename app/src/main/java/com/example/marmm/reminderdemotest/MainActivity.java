package com.example.marmm.reminderdemotest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.marmm.reminderdemotest.NumbersApiService.BASE_URL;

public class MainActivity extends AppCompatActivity implements ReminderAdapter.ReminderClickListener{


    private List<Reminder> mReminders;
    private EditText mNewReminderText;

    private TextView mQuoteTextView;

    private ReminderAdapter mAdapter;

    private RecyclerView mRecyclerView;

    //Constants used when calling the update activity
    public static final String EXTRA_REMINDER = "Reminder";
    public static final int REQUESTCODE = 1234;
    private int mModifyPosition;


    static AppDatabase db;

    public final static int TASK_GET_ALL_REMINDERS = 0;
    public final static int TASK_DELETE_REMINDER = 1;
    public final static int TASK_UPDATE_REMINDER = 2;
    public final static int TASK_INSERT_REMINDER = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQuoteTextView= findViewById(R.id.quote_message);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        mNewReminderText = findViewById(R.id.editText_main);

        requestData();

        mReminders = new ArrayList<>();

        db = AppDatabase.getInstance(this);

        new ReminderAsyncTask(TASK_GET_ALL_REMINDERS).execute();

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

//                    mReminders.add(newReminder);


//                    db.reminderDao().insertReminders(newReminder);

                    new ReminderAsyncTask(TASK_INSERT_REMINDER).execute(newReminder);

//Tell the adapter that the data set has been modified: the screen will be refreshed.


                    //Initialize the EditText for the next item

                    mNewReminderText.setText("");

                } else {

                    //Show a message to the user if the textfield is empty

                    Snackbar.make(view, "Please enter some text in the textfield", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }

            }
        });

        /*
Add a touch helper to the RecyclerView to recognize when a user swipes to delete a list entry.
An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
and uses callbacks to signal when a user is performing these actions.
*/
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        //mReminders.remove(position);
             //           db.reminderDao().deleteReminders(mReminders.get(position));
                        new ReminderAsyncTask(TASK_DELETE_REMINDER).execute(mReminders.get(position));
                        updateUI();
                        //mAdapter.notifyItemRemoved(position);
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


    }

    public void setQuoteTextView(String quoteMessage) {
        mQuoteTextView.setText(quoteMessage);
    }


    public void onReminderDbUpdated(List list) {
        mReminders = list;
        updateUI();
    }


    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new ReminderAdapter(this, mReminders);
            mRecyclerView.setAdapter(mAdapter);

        } else {
            mAdapter.swapList(mReminders);
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

    @Override
    public void reminderOnClick(int i) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        mModifyPosition = i;
        intent.putExtra(EXTRA_REMINDER,  mReminders.get(i));
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                Reminder updatedReminder = data.getParcelableExtra(MainActivity.EXTRA_REMINDER);
                // New timestamp: timestamp of update
                mReminders.set(mModifyPosition, updatedReminder);
                updateUI();
            }
        }
    }



    public class ReminderAsyncTask extends AsyncTask<Reminder, Void, List<Reminder>> {

        private int taskCode;

        public ReminderAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List<Reminder> doInBackground(Reminder... reminders) {
            switch (taskCode) {
                case TASK_DELETE_REMINDER:
                    db.reminderDao().deleteReminders(reminders[0]);
                    break;
                case TASK_UPDATE_REMINDER:
                    db.reminderDao().updateReminders(reminders[0]);
                    break;
                case TASK_INSERT_REMINDER:
                    db.reminderDao().insertReminders(reminders[0]);
                    break;
            }

            //To return a new list with the updated data, we get all the data from the database again.
            return db.reminderDao().getAllReminders();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onReminderDbUpdated(list);
        }

    }



    private void requestData()

    {
            NumbersApiService service = NumbersApiService.retrofit.create(NumbersApiService.class);
            Calendar calendar = Calendar.getInstance();

            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);



            Call<DayQuoteItem> call = service.getTodaysQuote(month, dayOfMonth);


            call.enqueue(new Callback<DayQuoteItem>() {
                @Override
                public void onResponse(Call<DayQuoteItem> call, Response<DayQuoteItem> response) {
                    DayQuoteItem dayQuoteItem = response.body();
                    setQuoteTextView(dayQuoteItem.getText());
                }

                @Override
                public void onFailure(Call<DayQuoteItem> call, Throwable t) {

                }
            });


    }

}
