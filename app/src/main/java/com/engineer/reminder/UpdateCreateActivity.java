package com.engineer.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);
        final RemindersDbAdapter db = new RemindersDbAdapter(this);
        db.open();
        Intent intent = getIntent();
        Integer remID = intent.getIntExtra("reminderID", -1);
        Button button = (Button) findViewById(R.id.create_button);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        final EditText data = (EditText) findViewById(R.id.editText);
        if(remID == -1)
        {
            //That means create.
            button.setText("Create");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean important = checkBox.isChecked();
                    if(data.getText().toString().equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "Cannot add an empty reminder",Toast.LENGTH_LONG).show();
                        return;
                    }
                    db.createReminder(data.getText().toString(), important);
                    Toast.makeText(getApplicationContext(), "Created Reminder Successfully", Toast.LENGTH_LONG);
                }
            });
        }
        else {
            final Reminder reminder = db.fetchReminderById(remID);
            button.setText("Update");
            data.setText(reminder.getContent());
            checkBox.setChecked(reminder.getImportant() == 1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(data.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Cannot add an empty reminder",Toast.LENGTH_LONG).show();
                        return;
                    }
                    reminder.setContent(data.getText().toString());
                    if(checkBox.isChecked()){ reminder.setImportant(1); }else{
                        reminder.setImportant(0);
                    }
                    db.updateReminder(reminder);
                    Toast.makeText(getApplicationContext(), "Updated Reminder Successfully", Toast.LENGTH_LONG);
                }
            });
        }

    }

}
