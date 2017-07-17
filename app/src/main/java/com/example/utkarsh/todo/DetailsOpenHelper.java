package com.example.utkarsh.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jayant Phor on 7/16/2017.
 */

public class DetailsOpenHelper extends SQLiteOpenHelper {

    public static final String EXPENSE_TABLE_NAME="Expense";
    public static final String EXPENSE_ID="_id";
    public static final String EXPENSE_TITLE="Title";
    public static final String EXPENSE_DESC ="Notes";
    public static final String EXPENSE_DATE="Date";
    public static final String EXPENSE_TIME="Time";
    public static final String EXPENSE_PRIORITY="Priority";
    public static final String EXPENSE_CATEGORY="category";
    public static final String TAG="openHelper";

    public DetailsOpenHelper(Context context) {
        super(context,"Details.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query="create table " + EXPENSE_TABLE_NAME + " ( " + EXPENSE_ID + " integer primary key autoincrement, " + EXPENSE_TITLE +
                " text, " + EXPENSE_DESC + " text, " + EXPENSE_DATE + " text, " + EXPENSE_TIME + " text, " + EXPENSE_PRIORITY + " text, " + EXPENSE_CATEGORY + " text);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
