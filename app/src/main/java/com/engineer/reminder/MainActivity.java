package com.engineer.reminder;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String[] from = { RemindersDbAdapter.COL_ID, RemindersDbAdapter.COL_CONTENT, RemindersDbAdapter.COL_IMPORTANT};
        RemindersDbAdapter db = new RemindersDbAdapter(this);
        db.open();
        db.createReminder("Act like nothing happened", false);
        Cursor cursor = db.fetchAllReminders();
        RemindersSimpleCursorAdapter adapter = new RemindersSimpleCursorAdapter(this,
                R.layout.dialog_custom, cursor, from, null, 0);

    }
}
