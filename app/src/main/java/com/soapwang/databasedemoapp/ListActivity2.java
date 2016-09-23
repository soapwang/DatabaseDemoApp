package com.soapwang.databasedemoapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListActivity2 extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    SimpleCardViewAdapter mAdapter;
    ArrayList<MyNote> noteArrayList;
    MyDB myDB;
    MyNote listItem;
    Cursor c;
    String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SimpleCardViewAdapter(this);
        noteArrayList = new ArrayList<MyNote>();
        myDB = new MyDB(this);
        myDB.open();
        refreshList();
        mAdapter.setNoteList(noteArrayList);
        mAdapter.setOnItemClickListener(new SimpleCardViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                noteId = noteArrayList.get(position).getId();
                startNoteActivity(false);
            }
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(ListActivity2.this, position + " long click", Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNoteActivity(true);
            }
        });
    }

    public void refreshList() {
        c = myDB.getNote();
        noteArrayList.clear();
        if(c.moveToFirst()) {
            do {
                String id = c.getString(c.getColumnIndex(Constants.KEY_ID));
                String title = c.getString(c.getColumnIndex(Constants.TITLE_NAME));
                String content = c.getString(c.getColumnIndex(Constants.CONTENT_NAME));
                DateFormat dateFormat = DateFormat.getDateInstance();
                String date = dateFormat.format(new Date(c.getLong(c.getColumnIndex(Constants.DATE_NAME
                ))).getTime());
                MyNote temp = new MyNote(id, title, content, date);
                noteArrayList.add(temp);
            } while (c.moveToNext());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                refreshList();
                mAdapter.swap(noteArrayList);
            }
        }
    }

    public void startNoteActivity(boolean isNew) {
        if(isNew) {
            Intent i = new Intent(ListActivity2.this, NoteActivity.class);
            i.putExtra("id", "new note");
            startActivityForResult(i, 1);
        } else {
            Intent i = new Intent(ListActivity2.this, NoteActivity.class);
            i.putExtra("id", noteId);
            startActivityForResult(i, 1);
        }
    }
}
