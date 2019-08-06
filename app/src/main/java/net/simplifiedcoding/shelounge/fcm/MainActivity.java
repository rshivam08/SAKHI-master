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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import net.simplifiedcoding.shelounge.R;

public class MainActivity extends AppCompatActivity {

    private static final int LOADER_ID_MESSAGES = 0;
    private static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        // Get token from the ID Service you created and show it in a log
        String token = FirebaseInstanceId.getInstance().getToken();
        String msg = getString(R.string.message_token_format, token);
        Log.d(LOG_TAG, msg);
        FirebaseMessaging.getInstance().subscribeToTopic("topic");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_following_preferences) {
            // Opens the following activity when the menu icon is pressed
            Intent startFollowingActivity = new Intent(this, FollowingPreferenceActivity.class);
            startActivity(startFollowingActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    protected void onDestroy() {
        if (FollowingPreferenceFragment.isOn == true){
            Intent i = new Intent(MainActivity.this, ShowHudService.class);
            startService(i);
            finish();
        }


        super.onDestroy();
    }*/
}
