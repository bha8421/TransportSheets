package com.example.android.rawtodispatch.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.rawtodispatch.Helper.MyCursorAdapter;
import com.example.android.rawtodispatch.R;
import com.example.android.rawtodispatch.data.DataContract;

import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_DATE;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_VEHICLE;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns._ID;

/**
 * Created by Bharat on 7/19/2017.
 */

public class Dispatch extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private MyCursorAdapter cursorAdapter;
    private static final int CONTENT_LOADER=2;
    private ListView listView;
    private View emptyView;
    private Uri CONTENT_URI= DataContract.DispatchEntry.CONTENT_URI;

    private SharedPreferences preferences;

    public Dispatch(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragement_raw,container,false);
        listView=view.findViewById(R.id.list);

        emptyView=view.findViewById(R.id.empty_view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setEmptyView(emptyView);
        cursorAdapter=new MyCursorAdapter(getContext(),null);
        listView.setAdapter(cursorAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(CONTENT_LOADER,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        preferences=PreferenceManager.getDefaultSharedPreferences(getContext());

        String sortOrder= DataContract.CommonColumns._ID+" "+preferences.getString(
                getString(R.string.order_by_key),
                getString(R.string.order_by_default)
        );

        String[] projection = {
                _ID,
                COLUMN_VEHICLE,
                COLUMN_DATE
        };



        return new CursorLoader(getContext(),
                CONTENT_URI,
                projection,
                null,null,sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLoaderManager().destroyLoader(CONTENT_LOADER);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.order_by_key))){
            cursorAdapter.swapCursor(null);
            getLoaderManager().restartLoader(CONTENT_LOADER,null,this);
        }
    }
}

