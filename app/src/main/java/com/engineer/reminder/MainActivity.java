package com.engineer.reminder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    final RemindersDbAdapter db = new RemindersDbAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String[] from = { RemindersDbAdapter.COL_ID, RemindersDbAdapter.COL_CONTENT, RemindersDbAdapter.COL_IMPORTANT};
        db.open();
        //db.createReminder("hello", true);
        DebugDB.getAddressLog();
        final Cursor cursor = db.fetchAllReminders();
        RemindersSimpleCursorAdapter adapter = new RemindersSimpleCursorAdapter(this,
                R.layout.reminder_row, cursor, new String[] {RemindersDbAdapter.COL_CONTENT}, new int[]{R.id.textView}
                , 0);
        listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateCreateActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton deleteButton = (FloatingActionButton)  findViewById(R.id.deleteFab);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllReminders();
                Toast.makeText(getApplicationContext(), "You've deleted all reminders :(", Toast.LENGTH_SHORT);
                notifyListUpdate();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                PopupMenu menu = new PopupMenu(getContext(), view.findViewById(R.id.textView));
                menu.getMenuInflater().inflate(R.menu.reminder_clicked, menu.getMenu());
                cursor.moveToPosition(i);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.menu_delete:
                                //Delete the thingy.
                                Reminder reminder = db.fetchReminderById(cursor.getInt(0));
                                db.deleteReminderById(reminder.getId());
                                notifyListUpdate();
                                return true;
                            case R.id.menu_update:
                                //Update the reminder.
                                Intent intent = new Intent(MainActivity.this, UpdateCreateActivity.class);
                                intent.putExtra("reminderID", cursor.getInt(0));
                                startActivity(intent);
                                return true;
                        }
                        return false;
                    }
                });
                menu.show();

            }
        });
    }

    protected Context getContext()
    {
        return this;
    }

    private void notifyListUpdate()
    {
        ((RemindersSimpleCursorAdapter)listView.getAdapter()).changeCursor(db.fetchAllReminders());
    }
}
