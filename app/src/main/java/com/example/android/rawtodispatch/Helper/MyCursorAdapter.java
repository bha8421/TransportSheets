package com.example.android.rawtodispatch.Helper;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.rawtodispatch.R;

import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_DATE;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_VEHICLE;

/**
 * Created by Bharat on 7/19/2017.
 */

public class MyCursorAdapter extends CursorAdapter {

    private TextView vehicleTv,dateTv;

    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView vehicleTV= (TextView) view.findViewById(R.id.vehicle);
        TextView dateTV= (TextView) view.findViewById(R.id.date);
        String vehicle=cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE));
        String date=cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        vehicleTV.setText(vehicle);
        dateTV.setText(date);

    }
}
