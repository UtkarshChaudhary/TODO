package com.example.utkarsh.todo;


import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView mylistview;
    ArrayList<Details> detailsArrayList;
    MyAdapter adapter;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailsArrayList=new ArrayList<>();
        mylistview=(ListView)findViewById(R.id.mylistview);
        adapter=new MyAdapter(MainActivity.this,detailsArrayList);
        updateDetailsList();
        mylistview.setAdapter(adapter);
        mylistview.setNestedScrollingEnabled(true);
        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                Details details=detailsArrayList.get(i);
                intent.putExtra(MainActivity2.pos,details.id);
                pos=i;
                startActivityForResult(intent,1);

            }
        });
        mylistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int position, long l) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("DELETE");
                builder.setMessage("Are You Sure You Want To Delete");
                final int iduse=detailsArrayList.get(position).id;
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DetailsOpenHelper detailsOpenHelper =new DetailsOpenHelper(MainActivity.this);
                        SQLiteDatabase sqLiteDatabase = detailsOpenHelper.getReadableDatabase();
                        sqLiteDatabase.delete(DetailsOpenHelper.EXPENSE_TABLE_NAME,DetailsOpenHelper.EXPENSE_ID+ "=" +iduse,null);
                        detailsArrayList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
//        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Notes");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,MainActivity2.class);
                startActivityForResult(i,1);
            }
        });
    }
    public void updateDetailsList()
    {
        DetailsOpenHelper detailsOpenHelper=new DetailsOpenHelper(this);
        detailsArrayList.clear();
        SQLiteDatabase database=detailsOpenHelper.getReadableDatabase();
        Cursor cursor=database.query(DetailsOpenHelper.EXPENSE_TABLE_NAME,null,null,null,null,null,null,null);
        while(cursor.moveToNext())
        {
            String title=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_TITLE));
            String date=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_DATE));
            String time=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_TIME));
            String desc=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_DESC));
            String category=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_CATEGORY));
            String priority=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_PRIORITY));
            int id=cursor.getInt(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_ID));
            Details details=new Details(id,title,date,time,desc,priority,category);
            detailsArrayList.add(details);
            adapter.notifyDataSetChanged();
        }

    }
    public void changeinlist(int myid)
    {
        DetailsOpenHelper detailsOpenHelper=new DetailsOpenHelper(this);
        SQLiteDatabase database=detailsOpenHelper.getReadableDatabase();
        Details details=detailsArrayList.get(pos);
        Cursor cursor=database.query(DetailsOpenHelper.EXPENSE_TABLE_NAME,null,DetailsOpenHelper.EXPENSE_ID+ "=" + myid,null,null,null,null,null);
        cursor.moveToNext();
        details.title=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_TITLE));
        details.date=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_DATE));
        details.time=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_TIME));
        details.desc=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_DESC));
        details.id=cursor.getInt(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_ID));
        details.category=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_CATEGORY));
        details.priority=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_PRIORITY));
        adapter.notifyDataSetChanged();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1)
        {
            if(resultCode==1)
            {
                updateDetailsList();
            }
            else if(resultCode==2)
            {
                int myid=data.getIntExtra(DetailsOpenHelper.EXPENSE_ID,-1);
                changeinlist(myid);
            }
            else if(resultCode==3)
            {
                detailsArrayList.remove(pos);
                adapter.notifyDataSetChanged();
            }
            else if(resultCode==RESULT_CANCELED)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.add)
        {
            Intent i=new Intent(MainActivity.this,MainActivity2.class);
            startActivityForResult(i,1);
        }
        if(id==R.id.removeAll){
            DetailsOpenHelper detailsOpenHelper=new DetailsOpenHelper(this);
            detailsArrayList.clear();
            SQLiteDatabase database=detailsOpenHelper.getWritableDatabase();
            database.delete(DetailsOpenHelper.EXPENSE_TABLE_NAME,null,null);
            detailsArrayList.clear();
            adapter.notifyDataSetChanged();
        }
        if(id==R.id.emailus)
        {
            Intent i=new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:utkarshobyan@gmail.com"));
            if(i.resolveActivity(getPackageManager())!=null)
            {
                startActivity(i);
            }
        }
        if(id==R.id.contactus)
        {
            Intent i=new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:9811370534"));
            if(i.resolveActivity(getPackageManager())!=null)
            {
                startActivity(i);
            }
        }
        if(id==R.id.aboutus)
        {
            Intent i=new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http:www.codingninjas.in"));
            if(i.resolveActivity(getPackageManager())!=null)
            {
                startActivity(i);
            }
        }
        return true;
    }
}