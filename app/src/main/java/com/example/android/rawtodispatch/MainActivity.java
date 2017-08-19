package com.example.android.rawtodispatch;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.*;

import com.example.android.rawtodispatch.Helper.*;
import com.example.android.rawtodispatch.data.DataContract;
import com.example.android.rawtodispatch.fragments.*;

import android.content.pm.PackageManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private TabLayout tabLayout;

    private ViewPager viewPager;

    private int[] icons={
      R.drawable.raw, R.drawable.finish,R.drawable.vehicle};

    private int[] iconsSelect={
           R.drawable.raw_select,R.drawable.finish_select,R.drawable.vehicle_select};

    private static final String LOG_TAG=MainActivity.class.getSimpleName();
    private FloatingActionButton floatingActionButton;

    private Uri CONTENT_URI= DataContract.RawEntry.CONTENT_URI;

    String[] permissionsRequired=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.GET_ACCOUNTS};

    private SharedPreferences preferences;

    private boolean sentToSettings = false;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;

    private static final int REQUEST_PERMISSION_SETTING = 101;

    private View view;

    private SharedPreferences sharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout= (TabLayout) findViewById(R.id.tabs);
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        floatingActionButton= (FloatingActionButton) findViewById(R.id.fab);

        view=findViewById(R.id.main_layout);

        preferences=getSharedPreferences("permissionStatus",MODE_PRIVATE);
        sharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        getSupportActionBar().setTitle(CONTENT_URI.getLastPathSegment().toUpperCase());
        preferences.registerOnSharedPreferenceChangeListener(this);
        /*Setting up viewpager with fragments*/
        setUpViewPager(viewPager);

        /*passing viewpager to tablayout*/
        tabLayout.setupWithViewPager(viewPager);

        setUpTabIcon();



        /*Setting on TabSelectedLitner to change the color of the tab and modifying the CONTENT_URI*/
        tabLayout.setOnTabSelectedListener(this);

        setPreferenceFileName();
        Log.v(LOG_TAG,"On Create completed");
    }

    private void setPreferenceFileName(){

        SharedPreferences.Editor editor=sharedPreference.edit();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(Calendar.getInstance().getTime());
        String fileName=getResources().getString(R.string.app_name)+"-"+month_name;
        editor.putString(getString(R.string.file_name),fileName);
        editor.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.settings_option){
            startActivity(new Intent(this,UtilitiesActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*After clicking floating button checking permissions to read and write the data from storage */
                if(ActivityCompat.checkSelfPermission(MainActivity.this,permissionsRequired[0])!=PackageManager.PERMISSION_GRANTED
                        ||ActivityCompat.checkSelfPermission(MainActivity.this,permissionsRequired[1])!=PackageManager.PERMISSION_GRANTED){

                    //requesting permission
                    requestRequiresPermissions();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(permissionsRequired[0],true);
                    editor.commit();
                }else {
                    //You already have the permission just call startEditActivity()
                    startEditActivity();
                }
            }


        });
        Log.v(LOG_TAG,"On start completed");
    }

    /*Calling EditActivity*/
    private void startEditActivity(){
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.setData(CONTENT_URI);
        startActivity(intent);
    }




    /*Setting up the tabs Icon*/
    private void setUpTabIcon() {
        tabLayout.getTabAt(0).setIcon(iconsSelect[0]);
        tabLayout.getTabAt(1).setIcon(icons[1]);
        tabLayout.getTabAt(2).setIcon(icons[2]);

    }

    /*Adding fragments to FragmentViewPagerFragment and settings adpter to viewpager */
    private void setUpViewPager(ViewPager viewPager) {
        FragementViewPagerAdapter adapter =new FragementViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(getString(R.string.raw_fragment), new Raw());
        adapter.addFragment(getString(R.string.finish_fragment),new Finished());
        adapter.addFragment(getString(R.string.dispatch_fragment),new Dispatch());
        viewPager.setAdapter(adapter);
    }



    //Requesting Permission at the runtime
    private void requestRequiresPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permissionsRequired[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permissionsRequired[1])){
            //Show Information about why you need the permission
           //AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            showSnackBarToRequestPermission();

        }else if (preferences.getBoolean(permissionsRequired[0],false)){

            //Previously Permission Request was cancelled with 'Dont Ask Again',
            // Redirect to Settings after showing Information about why you need the permission
            Snackbar.make(view,getString(R.string.snack_bar_go_to_the_settings_message),Snackbar.LENGTH_INDEFINITE).
                    setAction(getString(R.string.snack_bar_ok_button), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sentToSettings=true;
                            Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri=Uri.fromParts("package",getPackageName(),null);
                            intent.setData(uri);
                            startActivityForResult(intent,REQUEST_PERMISSION_SETTING);
                            Toast.makeText(MainActivity.this, getString(R.string.snack_bar_go_to_the_settings_message), Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
        }

    }

    //SnackBar to proceed requesting the permissions
    private void showSnackBarToRequestPermission(){
        Snackbar.make(view,getString(R.string.snack_bar_permission_request_message),Snackbar.LENGTH_INDEFINITE).
                setAction(getString(R.string.snack_bar_ok_button), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MainActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                }).show();
    }


    /*Callback from RequestPermisssionResult */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allGranted=false;
            for (int i=0 ; i<permissions.length ; i++){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    allGranted=true;
                }else {
                    allGranted=false;
                    break;
                }
            }
            if (allGranted){
                startEditActivity();
            }else if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permissionsRequired[1])){

                showSnackBarToRequestPermission();
            } else {
                Toast.makeText(getBaseContext(),getString(R.string.snack_bar_unable_to_get_permission),Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Got result from settings page regarding permissions
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this,permissionsRequired[1]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                startEditActivity();
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int i=tab.getPosition();
        tab.setIcon(iconsSelect[i]);
        switch (i){
            case 0:
                CONTENT_URI= DataContract.RawEntry.CONTENT_URI;
                getSupportActionBar().setTitle(CONTENT_URI.getLastPathSegment().toUpperCase());
                break;
            case 1:
                CONTENT_URI= DataContract.FinishEntry.CONTENT_URI;
                getSupportActionBar().setTitle(CONTENT_URI.getLastPathSegment().toUpperCase());
                break;
            case 2:
                CONTENT_URI= DataContract.DispatchEntry.CONTENT_URI;
                getSupportActionBar().setTitle(CONTENT_URI.getLastPathSegment().toUpperCase());
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int i=tab.getPosition();
        tab.setIcon(icons[i]);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.file_name))){
            deleteTables();

        }
    }

    private void deleteTables() {
        getContentResolver().delete(DataContract.RawEntry.CONTENT_URI,null,null);
        getContentResolver().delete(DataContract.FinishEntry.CONTENT_URI,null,null);
        getContentResolver().delete(DataContract.DispatchEntry.CONTENT_URI,null,null);

    }
}
