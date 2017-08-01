package com.example.android.rawtodispatch;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.rawtodispatch.fragments.DatePickerFragment;
import com.example.android.rawtodispatch.services.WriteService;

import static com.example.android.rawtodispatch.data.DataContract.*;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_DATE;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_LOADING_RECEIPT;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_OTHER_CHARGES;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_QUANTITY;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_RATE;
import static com.example.android.rawtodispatch.data.DataContract.CommonColumns.COLUMN_VEHICLE;
import static com.example.android.rawtodispatch.data.DataContract.DispatchEntry.COLUMN_SOURCE;
import static com.example.android.rawtodispatch.data.DataContract.DispatchEntry.COLUMN_DESTINATION;



public class EditActivity extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener {

    private EditText eVehicle,eDate,eRate,eReceipt,eOther,eQuantity,eSource,eDestination;
    private TextInputLayout vehicleLayout;
    private Uri CONTENT_URI;
    private Button button;
    private RelativeLayout viewSD;
    private Toolbar toolbar;
    private String vehicle,rate,date,charges,receipt,quantity,source,destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        /*Binding views */
        bindViews();

        Intent intent=getIntent();
        CONTENT_URI=intent.getData();
        Log.i("URI",CONTENT_URI.toString());


        setSupportActionBar(toolbar);
        resolveToolbarTitleAndView(CONTENT_URI);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    showAlertOnBackPressed();

            }
        });

        button.setOnClickListener(this);
        eDate.setOnClickListener(this);

        vehicleLayout.getEditText().addTextChangedListener(new MyTextWatcher(eVehicle));
    }

    public void showAlertOnBackPressed(){
        getStringDataOfEditexts();
        if (TextUtils.isEmpty(vehicle)){
            finish();
        }else {
            AlertDialog.Builder alert=new AlertDialog.Builder(EditActivity.this);
            alert.setTitle(getString(R.string.alert_message));
            alert.setPositiveButton(getString(R.string.alert_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    finish();
                }
             });
            alert.setNegativeButton(getString(R.string.alert_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                }
            });
            alert.show();
        }
    }

    @Override
    public void onBackPressed() {

        showAlertOnBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.input_date:
                showDatePickerDialod();
                break;
            case R.id.submit:
                if (!validateVehicle()){
                    return;
                }
                getStringDataOfEditexts();
                saveRawOrder();
                onStartService();
                finish();
                break;
        }
    }

    public void onStartService(){
        Intent intent=new Intent(this, WriteService.class);
        intent.putExtra("sheetName",CONTENT_URI.getLastPathSegment());
        intent.putExtra("values",new String[]{vehicle,date,quantity,receipt,rate,charges,source,destination});
        startService(intent);
    }


    private void saveRawOrder() {
        if (TextUtils.isEmpty(vehicle)){
            return;
        }else {
            ContentValues values = new ContentValues();
            if (CONTENT_URI.equals(DispatchEntry.CONTENT_URI)){
                values.put(COLUMN_SOURCE,source);
                values.put(COLUMN_DESTINATION,destination);
            }
            values.put(COLUMN_VEHICLE, vehicle);
            values.put(COLUMN_RATE, rate);
            values.put(COLUMN_DATE, date);
            values.put(COLUMN_OTHER_CHARGES, charges);
            values.put(COLUMN_LOADING_RECEIPT, receipt);
            values.put(COLUMN_QUANTITY, quantity);

            Uri uri = getContentResolver().insert(CONTENT_URI, values);
            if (uri == null) {
                Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, CONTENT_URI.getLastPathSegment()+" Package Added", Toast.LENGTH_SHORT).show();
            }
        }

    }


    //Binding views with variables
     public void bindViews(){

         toolbar= (Toolbar) findViewById(R.id.toolbar);
         viewSD= (RelativeLayout) findViewById(R.id.relativeLayout_for_sd);
         vehicleLayout= (TextInputLayout) findViewById(R.id.inputLayout_vehicle);
         eVehicle= (EditText) findViewById(R.id.input_vehicle);
         eQuantity=(EditText) findViewById(R.id.input_quantity);
         eReceipt=(EditText) findViewById(R.id.input_lr);
         eRate=(EditText) findViewById(R.id.input_charges);
         eOther=(EditText) findViewById(R.id.input_other);
         eSource= (EditText) findViewById(R.id.input_source);
         eDestination= (EditText) findViewById(R.id.input_destination);
         eDate=(EditText) findViewById(R.id.input_date);
         button= (Button) findViewById(R.id.submit);
     }

     //extracting string from edittexts
     private void getStringDataOfEditexts(){
         vehicle=eVehicle.getText().toString().trim();
         rate=eRate.getText().toString().trim();
         date=eDate.getText().toString().trim();
         charges=eOther.getText().toString().trim();
         receipt=eReceipt.getText().toString().trim();
         quantity= eQuantity.getText().toString().trim();
         source=eSource.getText().toString().trim();
         destination=eDestination.getText().toString().trim();
     }

     private void showDatePickerDialod(){
         DatePickerFragment pickerFragment=new DatePickerFragment();
         pickerFragment.show(getFragmentManager(),"DatePicker");
     }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        String dateString=String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+
                String.valueOf(dayOfMonth);
        eDate.setText(dateString);
    }

    private void resolveToolbarTitleAndView(Uri CONTENT_URI){
       if (CONTENT_URI.equals(RawEntry.CONTENT_URI)){
           getSupportActionBar().setTitle(getResources().getString(R.string.edit_raw_title));
           viewSD.setVisibility(View.GONE);
       }else if (CONTENT_URI.equals(FinishEntry.CONTENT_URI)){
           getSupportActionBar().setTitle(getResources().getString(R.string.edit_finish_title));
           viewSD.setVisibility(View.GONE);

       }else if (CONTENT_URI.equals(DispatchEntry.CONTENT_URI)){
           getSupportActionBar().setTitle(getResources().getString(R.string.edit_dispatch_title));
           viewSD.setVisibility(View.VISIBLE);
       }
    }

    private class MyTextWatcher implements TextWatcher{

        private View view;

        private MyTextWatcher(View view){
            this.view=view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()){
                case R.id.input_vehicle:
                    validateVehicle();
                    break;
            }
        }
    }
    private boolean validateVehicle() {
        if (eVehicle.getText().toString().trim().isEmpty()){
            vehicleLayout.setError(getString(R.string.vehicle_edittext_error));
            vehicleLayout.setErrorEnabled(true);
            return false;
        }else {
            vehicleLayout.setErrorEnabled(false);
        }
        return true;
    }
}
