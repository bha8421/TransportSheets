package com.example.android.rawtodispatch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.rawtodispatch.data.DataContract.RawEntry;
import com.example.android.rawtodispatch.data.DataContract.FinishEntry;
import com.example.android.rawtodispatch.data.DataContract.DispatchEntry;
/**
 * Created by Bharat on 7/19/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="myexcel.db";

    private static final int DATABASE_VERSION=1;

    private static final String SQL_CREATE_RAW_TABLE="CREATE TABLE "+RawEntry.TABLE_NAME+
            "("+ RawEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + RawEntry.COLUMN_VEHICLE+" TEXT NOT NULL,"
            +RawEntry.COLUMN_QUANTITY+" INTEGER,"
            +RawEntry.COLUMN_RATE+" INTEGER,"
            +RawEntry.COLUMN_LOADING_RECEIPT+" TEXT,"
            +RawEntry.COLUMN_OTHER_CHARGES+" INTEGER,"
            +RawEntry.COLUMN_DATE+" TEXT)";

    private static final String SQL_CREATE_FINISH_TABLE="CREATE TABLE "+FinishEntry.TABLE_NAME+
            "("+ FinishEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FinishEntry.COLUMN_VEHICLE+" TEXT NOT NULL,"
            +FinishEntry.COLUMN_QUANTITY+" INTEGER,"
            +FinishEntry.COLUMN_RATE+" INTEGER,"
            +FinishEntry.COLUMN_LOADING_RECEIPT+" TEXT,"
            +FinishEntry.COLUMN_OTHER_CHARGES+" INTEGER,"
            +FinishEntry.COLUMN_DATE+" TEXT)";

    private static final String SQL_CREATE_DISPATCH_TABLE="CREATE TABLE "+DispatchEntry.TABLE_NAME+
            "("+ DispatchEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DispatchEntry.COLUMN_VEHICLE+" TEXT NOT NULL,"
            +DispatchEntry.COLUMN_QUANTITY+" INTEGER,"
            +DispatchEntry.COLUMN_RATE+" INTEGER,"
            +DispatchEntry.COLUMN_LOADING_RECEIPT+" TEXT,"
            +DispatchEntry.COLUMN_OTHER_CHARGES+" INTEGER,"
            +DispatchEntry.COLUMN_DATE+" TEXT,"
            +DispatchEntry.COLUMN_SOURCE+" TEXT,"
            +DispatchEntry.COLUMN_DESTINATION+" TEXT)";


    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_RAW_TABLE);
            sqLiteDatabase.execSQL(SQL_CREATE_FINISH_TABLE);
            sqLiteDatabase.execSQL(SQL_CREATE_DISPATCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RawEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FinishEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DispatchEntry.TABLE_NAME);

        // create new tables
        onCreate(sqLiteDatabase);
    }
}
