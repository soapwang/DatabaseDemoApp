package com.soapwang.databasedemoapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Acer on 2016/7/1.
 */

public class NoteAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<MyNote> noteArrayList;
    private MyDB myDB;
    View v;

    public NoteAdapter(Context context) {
        inflater = LayoutInflater.from(context);
//        noteList = new ArrayList<MyNote>();
//        getdata();
//        myDB = new MyDB(context);
//        myDB.open();
    }


    public View getView(int arg0, View arg1, ViewGroup arg2) {
        final ViewHolder holder;
        v = arg1;
        if((v == null) || (v.getTag() == null)) {
            v = inflater.inflate(R.layout.note_row_layout, null);
            holder = new ViewHolder();
            holder.t = (TextView)v.findViewById(R.id.row_title);
            holder.d = (TextView)v.findViewById(R.id.row_date);
            v.setTag(holder);
        } else {
            holder = (ViewHolder)v.getTag();
        }
        holder.myNote = getItem(arg0); //the list item seems passed at here
        holder.t.setText(holder.myNote.getTitle());
        holder.d.setText(holder.myNote.getDate());
        v.setTag(holder);
        return v;
    }

    public long getItemId(int i) {
        return i;
    }

    public MyNote getItem(int i) {
        return noteArrayList.get(i);
    }

    public int getCount() {
        return noteArrayList.size();
    }

    public void setNoteList(ArrayList<MyNote> n) {
        noteArrayList = n;
    }

    public class ViewHolder {
        MyNote myNote;
        TextView t;
        TextView d;
    }
}