package com.example.android.rawtodispatch.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.android.rawtodispatch.Helper.MyCursorAdapter;
import com.example.android.rawtodispatch.R;
import com.example.android.rawtodispatch.data.DataContract;
import android.content.SharedPreferences;
import android.widget.Toast;

import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_DATE;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_VEHICLE;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns._ID;

/**
 * Created by Bharat on 7/19/2017.
 */

public class Raw extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private MyCursorAdapter cursorAdapter;
    private static final int CONTENT_LOADER=0;
    private ListView listView;
    private View emptyView;
    private Uri CONTENT_URI= DataContract.RawEntry.CONTENT_URI;

    private static final String LOG_TAG=Raw.class.getSimpleName();

    private SharedPreferences preferences;

    public Raw(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG,"On create fragment method");

        preferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.registerOnSharedPreferenceChangeListener(this);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragement_raw,container,false);
        listView=view.findViewById(R.id.list);

        emptyView=view.findViewById(R.id.empty_view);


        Log.v(LOG_TAG,"On createView metod of fragment");
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
        }else if (key.equals(getString(R.string.auto_sync_period_choose_key))){
            Toast.makeText(getContext(), "Autosync changes to: "+preferences.getString(getString(R.string.auto_sync_period_choose_key),
                    getString(R.string.auto_sync_period_choose_default)), Toast.LENGTH_SHORT).show();
        }else if (key.equals(getString(R.string.auto_sync_key))){
            Toast.makeText(getContext(), "AutoSync checkbox clicked "+preferences.getBoolean(getString(R.string.auto_sync_key
            ),Boolean.valueOf(getString(R.string.auto_sync_dafault_value))), Toast.LENGTH_SHORT).show();
        }
    }
}
