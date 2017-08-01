package com.example.android.rawtodispatch.data;

/**
 * Created by Bharat on 7/20/2017.
 */
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.rawtodispatch.data.DataContract.RawEntry;
import com.example.android.rawtodispatch.data.DataContract.FinishEntry;
import com.example.android.rawtodispatch.data.DataContract.DispatchEntry;

public class MyContentProvider extends ContentProvider {

    private DatabaseHelper dbHelper;

    public static final String LOG_TAG=MyContentProvider.class.getSimpleName();// Tag for Log Messages

    private static final int RAWS=100;

    private static final int RAWS_ID=101;

    private static final int FINISH=200;

    private static final int FINISH_ID=201;

    private static final int DISPATCH=300;

    private static final int DISPATCH_ID=301;

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY,DataContract.PATH_RAW,RAWS);
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY,DataContract.PATH_RAW+"/#",RAWS_ID);

        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY,DataContract.PATH_FINISH,FINISH);
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY,DataContract.PATH_FINISH+"/#",FINISH_ID);

        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY,DataContract.PATH_DISPATCH,DISPATCH);
        sUriMatcher.addURI(DataContract.CONTENT_AUTHORITY,DataContract.PATH_DISPATCH+"/#",DISPATCH_ID);
    }


    @Override
    public boolean onCreate() {
        dbHelper=new DatabaseHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        Cursor cursor;

        int match=sUriMatcher.match(uri);

        switch (match){
            case RAWS:
                cursor=database.query(RawEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case RAWS_ID:
                selection= RawEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query(RawEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case FINISH:
                cursor=database.query(FinishEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case FINISH_ID:
                selection= FinishEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query(FinishEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case DISPATCH:
                cursor=database.query(DispatchEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case DISPATCH_ID:
                selection= DispatchEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query(DispatchEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri: "+uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match=sUriMatcher.match(uri);
        switch (match){
            case RAWS:
                return RawEntry.CONTENT_LIST_TYPE;
            case RAWS_ID:
                return RawEntry.CONTENT_ITEM_TYPE;
            case FINISH:
                return FinishEntry.CONTENT_LIST_TYPE;
            case FINISH_ID:
                return FinishEntry.CONTENT_ITEM_TYPE;
            case DISPATCH:
                return DispatchEntry.CONTENT_LIST_TYPE;
            case DISPATCH_ID:
                return DispatchEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unkwon Uri "+uri+ " with match "+match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match=sUriMatcher.match(uri);
        switch (match){
            case RAWS:
                return insertData(uri,RawEntry.TABLE_NAME,contentValues);
            case FINISH:
                return insertData(uri,FinishEntry.TABLE_NAME,contentValues);
            case DISPATCH:
                return insertData(uri,DispatchEntry.TABLE_NAME,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for "+uri);
        }
    }

    private Uri insertData(Uri uri, String tableName, ContentValues contentValues) {
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        long id=database.insert(tableName,null,contentValues);
        if (id==-1){
            Log.e(LOG_TAG,"Failed to insert a row for uri:"+uri+" table:"+tableName);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
