package com.engineer.reminder;

import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.amitshekhar.DebugDB;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String[] from = { RemindersDbAdapter.COL_ID, RemindersDbAdapter.COL_CONTENT, RemindersDbAdapter.COL_IMPORTANT};
        RemindersDbAdapter db = new RemindersDbAdapter(this);
        db.open();
        //db.createReminder("Act like nothing happened", false);
        DebugDB.getAddressLog();
        Cursor cursor = db.fetchAllReminders();
        RemindersSimpleCursorAdapter adapter = new RemindersSimpleCursorAdapter(this,
                R.layout.reminder_row, cursor, new String[] {RemindersDbAdapter.COL_CONTENT}, new int[]{R.id.textView}
                , 0);
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }
}
