/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package net.simplifiedcoding.shelounge.fcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import net.simplifiedcoding.shelounge.Popup.ShowHudService;
import net.simplifiedcoding.shelounge.R;


public class FollowingPreferenceFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String LOG_TAG = FollowingPreferenceFragment.class.getSimpleName();
    public static boolean isOn;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Add visualizer preferences, defined in the XML file in res->xml->preferences_squawker
        addPreferencesFromResource(R.xml.following);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);



        // Preference preference = findPreference(key);
        // FirebaseMessaging.getInstance().subscribeToTopic(key);
        if (preference != null && preference instanceof SwitchPreferenceCompat) {
            // Get the current state of the switch preference
             boolean on = sharedPreferences.getBoolean(key, false);

            isOn = on;

            if (on) {
                // Subscribe
                FirebaseMessaging.getInstance
                        ().subscribeToTopic(key);
                Log.d(LOG_TAG, "Subscribing to " + key);
                if (key.equals("popup")) {
                    Toast.makeText(FollowingPreferenceFragment.this.getActivity(), "Toggle Clicled Again", Toast.LENGTH_SHORT).show();

                    int permissionCheck = ContextCompat.checkSelfPermission(FollowingPreferenceFragment.this.getActivity(), android.Manifest.permission.SYSTEM_ALERT_WINDOW);
                    if (permissionCheck==0){
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            // Show alert dialog to the user saying a separate permission is needed
                            // Launch the settings activity if the user prefers
                            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            startActivity(myIntent);
                        }
                    }



                    Intent i = new Intent(FollowingPreferenceFragment.this.getActivity(), ShowHudService.class);
                    getActivity().startService(i);
                    FollowingPreferenceFragment.this.getActivity().finish();
                }
            } else {
                // Un-subscribe
                FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
                Log.d(LOG_TAG, "Un-subscribing to " + key);
            }


        }



    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the shared preference change listener

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the shared preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
