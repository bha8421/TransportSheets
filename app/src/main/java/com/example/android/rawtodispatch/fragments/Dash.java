package com.example.android.rawtodispatch.fragments;



import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.example.android.rawtodispatch.R;

/**
 * Created by Bharat on 7/19/2017.
 */

public class Dash extends Fragment {



    public Dash(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_dash,container,false);

        return view;
    }

    public static class DashPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference orederBy=findPreference(getString(R.string.order_by_key));
            bindPreferenceSummaryToValue(orederBy);
            Preference autoSync=findPreference(getString(R.string.auto_sync_key));
            Preference autoSyncPeriod=findPreference(getString(R.string.auto_sync_period_choose_key));
            bindPreferenceSummaryToValue(autoSyncPeriod);

        }


        public void bindPreferenceSummaryToValue(Preference preference){
            preference.setOnPreferenceChangeListener(this);
            PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String prefenceString =sharedPreferences.getString(preference.getKey(),"");
            onPreferenceChange(preference,prefenceString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String value=String.valueOf(o);
            if (preference instanceof ListPreference){
                ListPreference listPreference= (ListPreference) preference;
                int prefIndex=listPreference.findIndexOfValue(value);
                if (prefIndex>=0){
                    CharSequence[] lables=listPreference.getEntries();
                    listPreference.setSummary(lables[prefIndex]);
                }
            }else {
                preference.setSummary(value);
            }
            return true;
        }
    }

}
