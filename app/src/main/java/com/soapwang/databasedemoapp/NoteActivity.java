package com.soapwang.databasedemoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {
    public static final int NEW_RECORD = 0;
    public static final int EXIST_RECORD = 1;

    private int state;
    private String noteId;
    private boolean changed;
    private Button doneButton;
    private Button discardButton;
    private TextView titleView;
    private TextView contentView;
    private String title="";
    private String content="";
    private String notice0;
    private String notice1;
    private String notice2;
    private String notice3;
    private String notice4;
    private MyDB myDB;
    private ListActivity listActivity;


    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changed = true;
        notice0 = this.getString(R.string.no_title);
        notice1 = this.getString(R.string.discard_notice);
        notice2 = this.getString(R.string.ok);
        notice3 = this.getString(R.string.cancel);
        notice4 = getString(R.string.saved);
        myDB = new MyDB(this);
        setContentView(R.layout.activity_main);
        noteId = getIntent().getStringExtra("id");
        if(noteId.equals("new note"))
            state = NEW_RECORD;
        else
            state = EXIST_RECORD;
        titleView = (TextView) findViewById(R.id.titleText);
        contentView = (TextView) findViewById(R.id.contentText);
        fillText();
        doneButton = (Button) findViewById(R.id.button2);
        discardButton = (Button) findViewById(R.id.button1);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    insertToDB();
                    Toast.makeText(NoteActivity.this, notice4, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    NoteActivity.this.finish();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleView.getText().equals("") && contentView.getText().equals(""))
                    changed = false;
                if(changed) {
                    showExitConfirm();
                } else {
                    NoteActivity.this.finish();
                }
            }
        });
    }

    public void showExitConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
        builder.setMessage(notice1);
        builder.setPositiveButton(notice2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                NoteActivity.this.finish();
            }
        });
        builder.setNegativeButton(notice3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public void onBackPressed()
    {
        if(titleView.getText().equals("") && contentView.getText().equals(""))
            changed = false;
        if(changed) {
            showExitConfirm();
        } else {
            NoteActivity.this.finish();
        }
    }

    public void insertToDB() {
        myDB.open();
        title = titleView.getText().toString();
        if(title.trim().equals(""))
            title = "No title";
        content = contentView.getText().toString();
        if(state == NEW_RECORD)
            myDB.insertNote(title, content);
        else
            myDB.updateNote(noteId, title, content);
        myDB.close();
    }

    private void fillText() {
        if(state == EXIST_RECORD) {
            myDB.open();
            Cursor c = myDB.queryNote(noteId);
            if(c != null && c.getCount() > 0) {
                c.moveToFirst();
                title = c.getString(c.getColumnIndex(Constants.TITLE_NAME));
                content = c.getString(c.getColumnIndex(Constants.CONTENT_NAME));
                titleView.setText(title);
                contentView.setText(content);
            }
            if(c != null)
                c.close();
            myDB.close();
        } else {
            titleView.setText("");
            contentView.setText("");
        }
    }
}