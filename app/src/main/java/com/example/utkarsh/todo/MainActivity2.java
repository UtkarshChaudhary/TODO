package com.example.utkarsh.todo;

/**
 * Created by Utkarsh on 7/16/2017.
 */
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

public class MainActivity2 extends AppCompatActivity {
    public static final String pos="1";
    int iduse;
    EditText date;
    EditText time;
    long edate;
    String category="personal";
    RadioGroup categoryRadioGroup;
    RadioGroup priorityRadioGroup;

    String priority="low";
    RadioButton low;
    RadioButton high;
    RadioButton medium;
    RadioButton personal;
    RadioButton travel;
    RadioButton business;
    RadioButton food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        low=(RadioButton)findViewById(R.id.Low);
        medium=(RadioButton)findViewById(R.id.medium);
        high=(RadioButton)findViewById(R.id.high);
        personal=(RadioButton)findViewById(R.id.personal);
        travel=(RadioButton)findViewById(R.id.travel);
        business=(RadioButton)findViewById(R.id.business);
        food=(RadioButton)findViewById(R.id.food);
        Intent i=getIntent();
        final EditText title=(EditText) findViewById(R.id.title);
        final EditText notes=(EditText)findViewById(R.id.notes);
        categoryRadioGroup=(RadioGroup) findViewById(R.id.category);
        priorityRadioGroup=(RadioGroup)findViewById(R.id.priority);
        time=(EditText) findViewById(R.id.time);
        date=(EditText) findViewById(R.id.date);
        final int myid =i.getIntExtra(MainActivity2.pos,-1);
        iduse=myid;
        if(myid==-1) {
            FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();

                    DetailsOpenHelper detailsOpenHelper =new DetailsOpenHelper(MainActivity2.this);
                    SQLiteDatabase db = detailsOpenHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(DetailsOpenHelper.EXPENSE_TITLE, title.getText().toString());
                    cv.put(DetailsOpenHelper.EXPENSE_DESC, notes.getText().toString());
                    cv.put(DetailsOpenHelper.EXPENSE_DATE, date.getText().toString());
                    cv.put(DetailsOpenHelper.EXPENSE_TIME, time.getText().toString());
                    cv.put(DetailsOpenHelper.EXPENSE_CATEGORY,category);
                    cv.put(DetailsOpenHelper.EXPENSE_PRIORITY,priority);
                    db.insert(DetailsOpenHelper.EXPENSE_TABLE_NAME, null, cv);

                    setAlarm();
                    setResult(1, intent);
                    finish();
                }
            });
        }
        else
        {
            DetailsOpenHelper detailsOpenHelper =new DetailsOpenHelper(MainActivity2.this);
            SQLiteDatabase sqLiteDatabase = detailsOpenHelper.getReadableDatabase();
            Cursor cursor=sqLiteDatabase.query(DetailsOpenHelper.EXPENSE_TABLE_NAME,null,DetailsOpenHelper.EXPENSE_ID+ "=" +myid,null,null,null,null);
            cursor.moveToNext();
            title.setText(cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_TITLE)));
            notes.setText(cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_DESC)));
            date.setText(cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_DATE)));
            time.setText(cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_TIME)));
             priority=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_PRIORITY));
            category=cursor.getString(cursor.getColumnIndex(DetailsOpenHelper.EXPENSE_CATEGORY));
            if(priority.equals("low")){
                low.setChecked(true);
            }else if(priority.equals("medium")){
               medium.setChecked(true);
            }else if(priority.equals("high")){
          high.setChecked(true);
        }

        if(category.equals("food")){
            food.setChecked(true);
        }else if(category.equals("personal")){
            personal.setChecked(true);
        }else if(category.equals("travel")){
            travel.setChecked(true);
        }else if(category.equals("business")){
            business.setChecked(true);
        }
            FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    DetailsOpenHelper detailsOpenHelper = new DetailsOpenHelper(MainActivity2.this);
                    SQLiteDatabase db = detailsOpenHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(DetailsOpenHelper.EXPENSE_TITLE, title.getText().toString());
                    cv.put(DetailsOpenHelper.EXPENSE_DESC, notes.getText().toString());
                    cv.put(DetailsOpenHelper.EXPENSE_DATE, date.getText().toString());
                    cv.put(DetailsOpenHelper.EXPENSE_TIME, time.getText().toString());
                    cv.put(DetailsOpenHelper.EXPENSE_PRIORITY,priority);
                    cv.put(DetailsOpenHelper.EXPENSE_CATEGORY,category);
                    db.update(DetailsOpenHelper.EXPENSE_TABLE_NAME,cv,DetailsOpenHelper.EXPENSE_ID + "=" + myid,null);
                    intent.putExtra(DetailsOpenHelper.EXPENSE_ID,myid);
                    setAlarm();
                    setResult(2, intent);
                    finish();
                }
            });
        }
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker timePicker=new TimePicker(MainActivity2.this);
                int hourOfDay=timePicker.getHour();
                int minute=timePicker.getMinute();
                boolean is24HourView=timePicker.is24HourView();
                showTimePicker(MainActivity2.this,hourOfDay, minute,is24HourView);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);
                int day=calendar.get(Calendar.DATE);
                showDatePicker(MainActivity2.this,year,month,day);
            }
        });

    }
    public void setAlarm()
    {
        AlarmManager am=(AlarmManager) MainActivity2.this.getSystemService(ALARM_SERVICE);
        Intent i=new Intent(MainActivity2.this,MyReceiver.class);
        PendingIntent pendingIntent=(PendingIntent) PendingIntent.getBroadcast(MainActivity2.this,1,i,0);
        Calendar calendar=Calendar.getInstance();
        long mdate=calendar.get(Calendar.DATE);
        String[] myuse=date.getText().toString().split("/");

        TimePicker timePicker2=new TimePicker(MainActivity2.this);
        long hour=timePicker2.getHour();
        long minute=timePicker2.getMinute();
        String[] use=time.getText().toString().split(":");
        long myhour=Long.parseLong(use[0])-hour;
        long myminute=Long.parseLong(use[1])-minute;
        long mydate= Long.parseLong(myuse[2])- mdate;

        long alarmtime=0;
        if(mydate!=0)
        {
            alarmtime=(mydate*24*60*60*1000);
        }
        if(myhour!=0)
        {
            alarmtime=(myhour*60*60*1000)+alarmtime;
        }
        if(myminute!=0)
        {
            alarmtime=(myminute*60*1000)+alarmtime;
        }
        am.set(AlarmManager.RTC,System.currentTimeMillis()+alarmtime,pendingIntent);
    }
    public void showTimePicker(Context context,int hourOfDay,
                               int minute,
                               boolean is24HourView)
    {
        TimePickerDialog timePickerDialog=new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                time.setText(Integer.toString(i)+":"+ Integer.toString(i1));
            }
        }, hourOfDay, minute, is24HourView);
        timePickerDialog.show();
    }
    public void showDatePicker(Context context, final int year, final int month, final int day)
    {
        DatePickerDialog datePickerDialog=new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar=Calendar.getInstance();
                calendar.set(year,month,day);
                edate=calendar.getTime().getTime();
                Log.i("epoch",Long.toString(edate));
                date.setText(i + "/" + (i1 + 1) + "/" + i2);
            }
        },year,month,day);
        datePickerDialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity2menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.remove)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("DETELE");
            builder.setMessage("Are You Sure You Want To Delete");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DetailsOpenHelper detailsOpenHelper =new DetailsOpenHelper(MainActivity2.this);
                    SQLiteDatabase sqLiteDatabase = detailsOpenHelper.getReadableDatabase();
                    sqLiteDatabase.delete(DetailsOpenHelper.EXPENSE_TABLE_NAME,DetailsOpenHelper.EXPENSE_ID+ "=" +iduse,null);
                    setResult(3);
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

   public void priorityRadioButtonClicked(View view){
       int id=view.getId();

       if(id==R.id.Low){
         priority="low";
       }else if(id==R.id.high){
         priority="high";
       }else if(id==R.id.medium){
         priority="medium";
       }
    }
    public void categoryRadioButtonClicked(View view){
    int id=view.getId();

        if(id==R.id.food){
         category="food";
        }else if(id==R.id.business){
          category="business";
        }else if(id==R.id.personal){
           category="personal";
        }else if(id==R.id.travel){
         category="travel";
        }
    }
}


