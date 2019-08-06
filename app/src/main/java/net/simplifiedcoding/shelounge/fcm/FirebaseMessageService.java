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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.simplifiedcoding.shelounge.R;

import java.util.Map;


public class FirebaseMessageService extends FirebaseMessagingService {

    private static final String JSON_KEY_AUTHOR = "author";
    private static final String JSON_KEY_MESSAGE = "message";

    private static final int NOTIFICATION_MAX_CHARACTERS = 30;
    private static String LOG_TAG = FirebaseMessageService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {
            Log.d(LOG_TAG, "Message data payload: " + data);

            // Send a notification that you got a new message
            sendNotification(data);

        }
    }

    private void sendNotification(Map<String, String> data) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("txt1", data.get("extra1"));
        intent.putExtra("txt2", data.get("extra2"));
        intent.putExtra("txt3", data.get("extra3"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String author = data.get(JSON_KEY_AUTHOR);
        String message = data.get(JSON_KEY_MESSAGE);

        if (message.length() > NOTIFICATION_MAX_CHARACTERS) {
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(String.format(getString(R.string.notification_message), author))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}