package com.soapwang.databasedemoapp;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListView noteList;
    private MyDB myDB;
    private ArrayList<MyNote> noteArrayList;
    private NoteAdapter myAdapter;
    private Cursor c;
    private boolean newNote;
    private String noteId;
    private MyNote listItem;
    private AlertDialog dialog;
    String notice0;
    String notice1;
    String notice2;
    String myUri = "content://com.soapwang.databasedemoapp/notes";
    Uri CONTENT_URI = Uri.parse(myUri);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        noteList = (ListView) findViewById(R.id.listView);
        //setSupportActionBar(toolbar);
        notice0 = getString(R.string.delete_confirm);
        notice1 = getString(R.string.ok);
        notice2 = getString(R.string.cancel);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote = true;
                startNoteActivity(newNote);
            }
        });
        myAdapter = new NoteAdapter(this);
        noteArrayList = new ArrayList<MyNote>();
        myDB = new MyDB(this);
        myDB.open();
        refreshList();
        myAdapter.setNoteList(noteArrayList);
        noteList.setAdapter(myAdapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listItem = (MyNote) noteList.getAdapter().getItem(i);
                noteId = listItem.getId();
                newNote = false;
                startNoteActivity(newNote);
            }
        });
        noteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                listItem = (MyNote) noteList.getAdapter().getItem(i);
                noteId = listItem.getId();
                deleteConfirm(noteId, i);
                return true;
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }
    //temporary solution
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

    public void deleteConfirm(String id, final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(notice0);
        builder.setPositiveButton(notice1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noteArrayList.remove(index);
                int result = myDB.deleteNote(noteId);
                if(result > 0)
                    Toast.makeText(ListActivity.this, "The note was deleted.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ListActivity.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                myAdapter.notifyDataSetChanged();
                noteList.invalidate();
            }
        });
        builder.setNegativeButton(notice2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    protected void onStop() {
        super.onStop();
        if(!c.isClosed())
            c.close();
    }

    protected  void onDestroy() {
        super.onDestroy();
        myDB.close();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                refreshList();
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    public void startNoteActivity(boolean isNew) {
        if(isNew) {
            Intent i = new Intent(ListActivity.this, NoteActivity.class);
            i.putExtra("id", "new note");
            startActivityForResult(i, 1);
        } else {
            Intent i = new Intent(ListActivity.this, NoteActivity.class);
            i.putExtra("id", noteId);
            startActivityForResult(i, 1);
        }
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
//        Uri baseUri;
//        if (mCurFilter != null) {
//            baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
//                    Uri.encode(mCurFilter));
//        } else {
//            baseUri = Contacts.CONTENT_URI;
//        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
//        String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
//                + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
//                + Contacts.DISPLAY_NAME + " != '' ))";
//        return new CursorLoader(getActivity(), baseUri,
//                CONTACTS_SUMMARY_PROJECTION, select, null,
//                Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        return null;
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        // Swap the new cursor in.  (The framework will take care of closing the
//        // old cursor once we return.)
//        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
//        // This is called when the last Cursor provided to onLoadFinished()
//        // above is about to be closed.  We need to make sure we are no
//        // longer using it.
//        myAdapter.swapCursor(null);
    }
}
