package net.simplifiedcoding.shelounge.ui.WomenSecurity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.shelounge.R;
import net.simplifiedcoding.shelounge.ui.CurrentLocationsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PentagonActivity extends AppCompatActivity implements SensorEventListener, LocationListener {


    //storage permission code
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    final static int PICK_CONTACT = 3;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final String TAG = "1";
    public static String[] stored_number = new String[5];
    public static String[] stored_names = new String[5];
    static double latitude;
    static double longitude;
    private final float NOISE = (float) 2.0;
    //add contacts
    int flag = 0;
    int i = 0;
    String name = null, id = null;
    String number = null;
    int counter = 0;
    String provider;
    LocationManager locationManager;
    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pentagon_activity_main);
        CurrentLocationsActivity m = new CurrentLocationsActivity();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {

            //Get the location from the given provider
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 20000, 1, this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }


        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        ImageButton camera = (ImageButton) findViewById(R.id.imageBtn);
        ImageButton call = (ImageButton) findViewById(R.id.call_btn);
        ImageButton track = (ImageButton) findViewById(R.id.log_btn);
        ImageButton audio = (ImageButton) findViewById(R.id.record_btn);
        ImageButton alarm = (ImageButton) findViewById(R.id.audio_btn);


        //add contacts
        for (int k = 0; k < 5; k++) {
            stored_names[i] = stored_number[i] = null;
        }


        loadSavedPreferences();
        ImageButton b1 = (ImageButton) findViewById(R.id.imageButton6);
        ImageButton b2 = (ImageButton) findViewById(R.id.imageButton7);
        ImageButton b3 = (ImageButton) findViewById(R.id.imageButton8);
        ImageButton b4 = (ImageButton) findViewById(R.id.imageButton9);
        ImageButton b5 = (ImageButton) findViewById(R.id.imageButton10);
        ImageButton b6 = (ImageButton) findViewById(R.id.send_msg_btn);


        b1.setOnClickListener(new btnClick());
        b2.setOnClickListener(new btnClick());
        b3.setOnClickListener(new btnClick());
        b4.setOnClickListener(new btnClick());
        b5.setOnClickListener(new btnClick());
        String location = "";


        b6.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                //	vibe.vibrate(80);

                Toast.makeText(getBaseContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
                String l = Double.toString(latitude);
                String l1 = Double.toString(longitude);
                String myMsg = "I am in an emergency situation. I need urgent help. Come and get me. My location is near : http://maps.google.com/maps?q=" + l + "," + l1;

                for (i = 0; i < 5; i++) {
                    String thenumber = stored_number[i];
                    String thename = stored_names[i];

                    if (thenumber != null && thename != null) {
                        sendMsg(thename, thenumber, myMsg);
                    }
                }

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                opencamera();


            }
        });
        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callme();
            }
        });
        track.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent map = new Intent(getApplicationContext(), CurrentLocationsActivity.class);
                startActivity(map);
            }

        });
        audio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent openaudio = new Intent(getApplicationContext(), AudioActivity.class);
                startActivity(openaudio);
                finish();


            }
        });
        alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent opensound = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(opensound);

            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {
        // Getting reference to TextView tv_longitude
        longitude = location.getLongitude();

        latitude = location.getLatitude();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void getContact() {

        Intent it = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(it, PICK_CONTACT);


    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {


        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {

            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);

                    if (c.moveToFirst()) {
                        // other data is available for the Contact. I have decided
                        // to only get the name of the Contact.
                        id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));


                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                            phones.moveToFirst();
                            number = phones.getString(phones.getColumnIndex("data1"));
                        } else {
                            number = "1234";
                        }
                        name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        first_name();

                        if (number == "1234") {
                            Toast number_not = Toast.makeText(this, "Number Not Found", Toast.LENGTH_SHORT);
                            number_not.show();
                        } else {
                            String a = Integer.toString(flag);
                            a = Integer.toString(flag);
                            savePreferences(a, name);
                            a = Integer.toString(flag + 10);
                            savePreferences(a, number);
                            stored_names[flag - 1] = name;
                            stored_number[flag - 1] = number;

                            Toast number_not = Toast.makeText(this, flag - 1 + " " + name + " " + number, Toast.LENGTH_SHORT);

                            set(flag);
                        }

                    }
                }

        }

    }

    public void first_name() {

        int len = name.length();
        String[] parts = name.split(" ");
        name = parts[0];


    }

    private void sendMsg(final String thename, final String theNumber, String myMsg) {


        String SENT = "Message Sent ";
        String DELIVERED = "Message Delivered";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);


        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(PentagonActivity.this, "Sent to" + " " + thename.toString(), Toast.LENGTH_SHORT);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Failed to" + " " + thename.toString(), Toast.LENGTH_SHORT);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No Service", Toast.LENGTH_SHORT);


                }
            }


        }, new IntentFilter(SENT));


        registerReceiver(new
                                 BroadcastReceiver() {

                                     @Override
                                     public void onReceive(Context arg0, Intent arg1) {
                                         {


                                             switch (getResultCode()) {
                                                 case Activity.RESULT_OK:
                                                     Toast.makeText(getBaseContext(), "Delivered to " + thename.toString(), Toast.LENGTH_LONG);
                                                     break;
                                                 case Activity.RESULT_CANCELED:
                                                     Toast.makeText(getBaseContext(), "deliver fail to " + thename.toString(), Toast.LENGTH_LONG);
                                                     break;


                                             }
                                         }

                                     }


                                 }, new IntentFilter(DELIVERED));


        // TODO Auto-gepnerated method stub
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(theNumber, null, myMsg, sentPI, deliveredPI);

    }

    private void set(int flagg) {
        if (flagg == 1) {
            ImageButton imageView = (ImageButton) findViewById(R.id.imageButton6);
            TextView naming = (TextView) findViewById(R.id.num_1);
            naming.setText(name);

        }
        if (flagg == 2) {
            ImageButton imageView = (ImageButton) findViewById(R.id.imageButton7);
            TextView naming = (TextView) findViewById(R.id.num_2);
            naming.setText(name);

        }
        if (flagg == 3) {
            ImageButton imageView = (ImageButton) findViewById(R.id.imageButton8);
            TextView naming = (TextView) findViewById(R.id.num_3);
            naming.setText(name);
        }
        if (flagg == 4) {
            ImageButton imageView = (ImageButton) findViewById(R.id.imageButton9);
            TextView naming = (TextView) findViewById(R.id.num_4);
            naming.setText(name);

        }
        if (flagg == 5) {
            ImageButton imageView = (ImageButton) findViewById(R.id.imageButton10);
            TextView naming = (TextView) findViewById(R.id.num_5);
            naming.setText(name);
        }
    }

    private void savePreferences(String user_id, String user_info) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();
        editor.putString(user_id, user_info);
        editor.commit();

    }

    private void loadSavedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        counter = 0;
        for (int i = 0; i < 5; i++) {
            String nam = sharedPreferences.getString(Integer.toString(i + 1), "");
            String num = sharedPreferences.getString(Integer.toString(i + 1 + 10), "");
            id = sharedPreferences.getString(Integer.toString(i + 1 + 20), "");
            name = nam;
            number = num;
            if (number != "") {
                stored_number[i] = number;
                stored_names[i] = name;
                counter++;
            }
            set(i + 1);

        }
        Toast pp = Toast.makeText(this, "no_of_contact=" + " " + counter, Toast.LENGTH_LONG);
        pp.show();

    }

    private void opencamera() {

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // startActivityForResult(intent, 10);


    }

    private void callme() {
        Intent callintent = new Intent(Intent.ACTION_CALL);
        callintent.setData(Uri.parse("tel:181"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callintent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be safely ignored for this demo
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onSensorChanged(SensorEvent event) {


        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE) deltaX = (float) 0.0;
            if (deltaY < NOISE) deltaY = (float) 0.0;
            if (deltaZ < NOISE) deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            if (deltaX > (float) 15.0 || deltaY > (float) 15.0 || deltaZ > (float) 15.0) {
                Toast.makeText(getBaseContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
                String l = Double.toString(latitude);
                String l1 = Double.toString(longitude);
                String myMsg = "I am in an emergency situation. I need urgent help. Come and get me. My location is near : http://maps.google.com/maps?q=" + l + "," + l1;

                for (i = 0; i < 5; i++) {
                    String thenumber = stored_number[i];
                    String thename = stored_names[i];

                    if (thenumber != null && thename != null) {
                        sendMsg(thename, thenumber, myMsg);

                    }
                }

            }


        }
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                ) {
                            showDialogOK("SMS and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private boolean checkAndRequestPermissions() {
        int cam = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int call_phone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int record_audio = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        int read_contacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int send = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int write = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int access = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cam != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (send != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (access != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (call_phone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (record_audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (read_contacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    class btnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageButton6:
                    flag = 1;
                    break;
                case R.id.imageButton7:
                    flag = 2;
                    break;
                case R.id.imageButton8:
                    flag = 3;
                    break;
                case R.id.imageButton9:
                    flag = 4;
                    break;
                case R.id.imageButton10:
                    flag = 5;
                    break;

            }
            getContact();

        }

    }


}
