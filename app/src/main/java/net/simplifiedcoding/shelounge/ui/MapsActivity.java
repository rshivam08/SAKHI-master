package net.simplifiedcoding.shelounge.ui;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import net.simplifiedcoding.shelounge.DetailInterfaces.AvgRating;
import net.simplifiedcoding.shelounge.DetailInterfaces.DoctorDetail;
import net.simplifiedcoding.shelounge.DetailInterfaces.NameToilet;
import net.simplifiedcoding.shelounge.DetailInterfaces.ShopDetail;
import net.simplifiedcoding.shelounge.Popup.ShowHudService;
import net.simplifiedcoding.shelounge.R;
import net.simplifiedcoding.shelounge.fcm.FollowingPreferenceActivity;
import net.simplifiedcoding.shelounge.fcm.FollowingPreferenceFragment;
import net.simplifiedcoding.shelounge.ui.Details.DoctorDetailActivity;
import net.simplifiedcoding.shelounge.ui.Details.ShopsDetailActivity;
import net.simplifiedcoding.shelounge.ui.Details.ToiletDetailActivity;
import net.simplifiedcoding.shelounge.ui.WomenSecurity.CheckComplaintActivity;
import net.simplifiedcoding.shelounge.ui.WomenSecurity.DialogEmergency;
import net.simplifiedcoding.shelounge.ui.WomenSecurity.PentagonActivity;
import net.simplifiedcoding.shelounge.utils.AlertDialogManager;
import net.simplifiedcoding.shelounge.utils.ApiUrl;
import net.simplifiedcoding.shelounge.utils.ConnectionDetector;
import net.simplifiedcoding.shelounge.utils.DataParser;
import net.simplifiedcoding.shelounge.utils.GPSTracker;
import net.simplifiedcoding.shelounge.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

import static net.simplifiedcoding.shelounge.R.id.map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private static final int LOADER_ID_MESSAGES = 0;
    private static String LOG_TAG = MapsActivity.class.getSimpleName();

    final int REQUEST_READ_PHONE_STATE=2;

    String imei_num;
    SharedPreferences preferences;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final String TAG = "1";
    public static String mChosenLoc;
    final String JOutput = "";
    public int flag = 3;
    ProgressDialog p;
    ArrayList<LatLng> MarkerPoints;
    GPSTracker gps;
    double latitude;
    double longitude;
    PrefManager pref;
    String JSON_String;
    RatingBar rate;
    String cli_name, doc_name;
    Button mReviewBtn, mDetailBtn;
    String json_string0, json_string1, json_string2;
    JSONObject jsonObject, JO;
    String shop_name;
    JSONArray jsonArray;
    TextView txtUninstall;
    TextView txtOpen;
    ImageView medical_shops, female_doctors, see_toilets, woman;
    TextView txtBackup, txtDetail;
    TextView mTitle;
    private GoogleMap mMap, mMap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.map_tool);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setIcon(R.drawable.toilet_small);
        mTitle = (TextView) findViewById(R.id.toolbar_text);
        mTitle.setText("TOILETS");

        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lithos.ttf");
        mTitle.setTypeface(font);

        FollowingPreferenceFragment.isOn = true;

        MarkerPoints = new ArrayList<>();
        woman = (ImageView) findViewById(R.id.woman);
        medical_shops = (ImageView) findViewById(R.id.medical_shop);
        female_doctors = (ImageView) findViewById(R.id.doctors);
        see_toilets = (ImageView) findViewById(R.id.toilets);

        pref = new PrefManager(getApplicationContext());
        pref.setEvent("0");
        preferences=getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Registered",false);
        editor.apply();
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent ws = new Intent(MapsActivity.this, PentagonActivity.class);
                startActivity(ws);*/

                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                } else {
                    //TODO

if (preferences.getBoolean("Registered",false)){
    DialogEmergency cd = new DialogEmergency(MapsActivity.this);
    cd.show();
}
else {
     Intent ws = new Intent(MapsActivity.this, PentagonActivity.class);
                startActivity(ws);
}

                }



            }
        });
        medical_shops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle.setText("MEDICAL SHOPS");
                getSupportActionBar().setIcon(R.drawable.medical_small);
                flag = 1;


                AlertDialogManager alert = new AlertDialogManager();
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    alert.showAlertDialog(MapsActivity.this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    // stop executing code by return
                    return;
                } else {
                    getLatLong();
                    mMap.clear();
                    System.out.println(json_string1);
                    try {
                        JO = new JSONObject(json_string0);
                        jsonArray = JO.optJSONArray("medical_shops_json");
                        int count = 0;
                        String name, lat, lng;

                        while (count < jsonArray.length()) {

                            try {
                                JO = jsonArray.getJSONObject(count);
                                name = JO.getString("shop_name");
                                lat = JO.getString("latitude");
                                lng = JO.getString("longitude");
                                LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        LatLng sydney = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(sydney).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                        LatLngBounds GWALIOR = new LatLngBounds(new LatLng(26.104292, 78.111943), new LatLng(26.320862, 78.270518));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GWALIOR.getCenter(), Float.parseFloat("10.7")));
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(26.2183, 78.1828))
                                .radius(10000)
                                .strokeColor(Color.BLACK)
                                .fillColor(0x42ffff00));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        female_doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle.setText("FEMALE DOCTORS");
                getSupportActionBar().setIcon(R.drawable.aid_small);
                flag = 2;

                AlertDialogManager alert = new AlertDialogManager();
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    alert.showAlertDialog(MapsActivity.this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    // stop executing code by return
                    return;
                } else {
                    getLatLong();
                    mMap.clear();
                    System.out.println(json_string2);
                    try {
                        JO = new JSONObject(json_string2);
                        jsonArray = JO.optJSONArray("female_json");
                        int count = 0;
                        String name, lat, lng;

                        while (count < jsonArray.length()) {

                            try {
                                JO = jsonArray.getJSONObject(count);
                                name = JO.getString("Clinic_Name");
                                lat = JO.getString("Latitude");
                                lng = JO.getString("Longitude");
                                LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        LatLng sydney = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(sydney).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                        LatLngBounds GWALIOR = new LatLngBounds(new LatLng(26.104292, 78.111943), new LatLng(26.320862, 78.270518));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GWALIOR.getCenter(), Float.parseFloat("10.7")));
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(26.2183, 78.1828))
                                .radius(10000)
                                .strokeColor(Color.BLACK)
                                .fillColor(0x42ffff00));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
                /*pref.setEvent("1");
                new BackGroundTask().execute();*/


        });

        see_toilets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle.setText("TOILETS");
                getSupportActionBar().setIcon(R.drawable.emergency_small);
                flag = 3;

                AlertDialogManager alert = new AlertDialogManager();
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    alert.showAlertDialog(MapsActivity.this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    // stop executing code by return
                    return;
                } else {
                    getLatLong();
                    mMap.clear();
                    System.out.println(json_string1);
                    try {
                        JO = new JSONObject(json_string1);
                        jsonArray = JO.optJSONArray("toilets_json");
                        int count = 0;
                        String name, lat, lng, sno;

                        while (count < jsonArray.length()) {
                            try {
                                JO = jsonArray.getJSONObject(count);
                                sno = JO.getString("S_NO");
                                name = JO.getString("toilets_name");
                                lat = JO.getString("latitude");
                                lng = JO.getString("longitude");
                                LatLng gwalior = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                                mMap.addMarker(new MarkerOptions().position(gwalior).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                                //

                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                        LatLng gwalior = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(gwalior).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                        LatLngBounds GWALIOR = new LatLngBounds(new LatLng(26.104292, 78.111943), new LatLng(26.320862, 78.270518));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GWALIOR.getCenter(), Float.parseFloat("10.7")));
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(26.2183, 78.1828))
                                .radius(10000)
                                .strokeColor(Color.BLACK)
                                .fillColor(0x42ffff00));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment.getMapAsync(this);

    }

    public static String getDeviceId(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

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

    protected void onDestroy() {
        if (FollowingPreferenceFragment.isOn == true) {
            Intent i = new Intent(MapsActivity.this, ShowHudService.class);
            startService(i);
            finish();
        }
        super.onDestroy();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap1 = googleMap;

        mMap.setBuildingsEnabled(false);

        // Setting onclick event listener for the map

        mMap1.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                //  Take some action here

                LatLng point = marker.getPosition();


                // Already two locations
                if (MarkerPoints.size() > 1) {
                    MarkerPoints.clear();
                    //     mMap1.clear();
                }


                // Adding new item to the ArrayList
                MarkerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();
                MarkerOptions options1 = new MarkerOptions();
                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (MarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                getLatLong();
                LatLng a = new LatLng(latitude, longitude);
                MarkerPoints.add(a);
                options1.position(a);
                if (MarkerPoints.size() == 2) {
                    options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // Add new marker to the Google Map Android API V2
                mMap1.addMarker(options);
                mMap1.addMarker(options1);

                // Checks, whether start and end locations are captured
                if (MarkerPoints.size() >= 2) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap1.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap1.animateCamera(CameraUpdateFactory.zoomTo(11));
                    String[] lat = options.getPosition().toString().split(",");
                    lat[1] = lat[1].replace(")", "");
                    mChosenLoc = lat[1];
                    showSheet();
                }

                return true;
            }

        });

        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());


        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MapsActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        } else
            new BackGroundTask().execute();

    }

    private void showSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_1, null);
        view.setAlpha(Float.parseFloat("0.8"));
        txtBackup = (TextView) view.findViewById(R.id.txt_backup);

        txtOpen = (TextView) view.findViewById(R.id.textView);
        rate = (RatingBar) view.findViewById(R.id.ratingBar);
        txtUninstall = (TextView) view.findViewById(R.id.txt_uninstall);
        LayerDrawable stars = (LayerDrawable) rate.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        txtOpen.setTextColor(getResources().getColor(R.color.textTitleColor));
        txtUninstall.setTextColor(getResources().getColor(R.color.textTitleColor));
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lithos.ttf");
        txtBackup.setTypeface(font);
        txtOpen.setTypeface(font);
        txtUninstall.setTypeface(font);
//        txtDetail.setTypeface(font);

        final Dialog mBottomSheetDialog = new Dialog(MapsActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        showDetails();


        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (flag) {
                    case 1:
                        mBottomSheetDialog.dismiss();
                        Intent intentShop = new Intent(MapsActivity.this, ShopsDetailActivity.class);
                        //intentShop.putExtra("NAME",doc_name);

                        startActivity(intentShop);

                        break;
                    case 2:
                        mBottomSheetDialog.dismiss();
                        Intent intent = new Intent(MapsActivity.this, DoctorDetailActivity.class);
                        intent.putExtra("CLINIC", cli_name);
                        intent.putExtra("DOCTOR", doc_name);
                        startActivity(intent);
                        break;
                    case 3:
                        mBottomSheetDialog.dismiss();
                        startActivity(new Intent(MapsActivity.this, ToiletDetailActivity.class));
                }
            }
        });


    }

    private void showDetails() {

        switch (flag) {
            case 1:
//                txtDetail.setVisibility(View.INVISIBLE);
                try {
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint("http://ric-tiiciiitm.webhostingforstudents.com/sakhi/").build();

                    ShopDetail api = adapter.create(ShopDetail.class);
                    api.where(MapsActivity.mChosenLoc
                            , new retrofit.Callback<retrofit.client.Response>() {
                                @Override
                                public void success(retrofit.client.Response response, retrofit.client.Response response2) {
                                    BufferedReader reader = null;
                                    String output = "";
                                    try {
                                        reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                                        //Reading the output in the string
                                        output = reader.readLine();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //JOutput=output;
                                    try {

                                        JSONArray Array = new JSONArray(output);
                                        for (int i = 0; i < Array.length(); i++) {
                                            JSONObject current = Array.getJSONObject(i);


                                            doc_name = current.getString("shop_name");
                                            txtBackup.setText(doc_name);


                                        }
                                    } catch (JSONException e) {
                                        Log.e("QueryUtils", "Problem parsing the JSON results", e);
                                    }

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                } catch (Exception e) {

                }
                break;
            case 2:
//                txtDetail.setVisibility(View.VISIBLE);
                try {
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint("http://ric-tiiciiitm.webhostingforstudents.com/sakhi/").build();

                    DoctorDetail api = adapter.create(DoctorDetail.class);
                    api.where(MapsActivity.mChosenLoc
                            , new retrofit.Callback<retrofit.client.Response>() {
                                @Override
                                public void success(retrofit.client.Response response, retrofit.client.Response response2) {
                                    BufferedReader reader = null;
                                    String output = "";
                                    try {
                                        reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                                        //Reading the output in the string
                                        output = reader.readLine();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //JOutput=output;
                                    try {

                                        JSONArray Array = new JSONArray(output);
                                        for (int i = 0; i < Array.length(); i++) {
                                            JSONObject current = Array.getJSONObject(i);


                                            cli_name = current.getString("Clinic_Name");
                                            doc_name = current.getString("Doctor_Name");

                                            txtBackup.setText(cli_name);
                                            txtBackup.append(" " + doc_name);


                                        }
                                    } catch (JSONException e) {
                                        Log.e("QueryUtils", "Problem parsing the JSON results", e);
                                    }

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                } catch (Exception e) {

                }
                break;
            case 3:
                txtUninstall.setVisibility(View.VISIBLE);
                rate.setVisibility(View.VISIBLE);
                txtOpen.setVisibility(View.VISIBLE);
                rate.setEnabled(false);
                name();
                detail();
        }

    }

    private void detail() {
        try {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint("http://ric-tiiciiitm.webhostingforstudents.com/sakhi/").build();
            AvgRating api = adapter.create(AvgRating.class);
            api.where(MapsActivity.mChosenLoc
                    , new retrofit.Callback<retrofit.client.Response>() {
                        @Override
                        public void success(retrofit.client.Response response, retrofit.client.Response response2) {
                            BufferedReader reader = null;
                            String output = "";
                            try {
                                reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                                //Reading the output in the string
                                output = reader.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            try {

                                JSONArray Array = new JSONArray(output);
                                // in=Array.length();
                                for (int i = 0; i < Array.length(); i++) {
                                    JSONObject current = Array.getJSONObject(i);

                                    String rating = current.getString("rating");
                                    String len = current.getString("len");
                                    rate.setRating(Float.parseFloat(rating));
                                    txtUninstall.setText(len);


                                }
                            } catch (JSONException e) {
                                Log.e("QueryUtils", "Problem parsing the JSON results", e);
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void name() {
        try {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint("http://ric-tiiciiitm.webhostingforstudents.com/sakhi/").build();

            NameToilet api = adapter.create(NameToilet.class);
            api.where(MapsActivity.mChosenLoc
                    , new retrofit.Callback<retrofit.client.Response>() {
                        @Override
                        public void success(retrofit.client.Response response, retrofit.client.Response response2) {
                            BufferedReader reader = null;
                            String output = "";
                            try {
                                reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                                //Reading the output in the string
                                output = reader.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {

                                JSONArray Array = new JSONArray(output);
                                for (int i = 0; i < Array.length(); i++) {
                                    JSONObject current = Array.getJSONObject(i);

                                    String named = current.getString("toilets_name");
                                    txtBackup.setText(named);


                                }
                            } catch (JSONException e) {
                                Log.e("QueryUtils", "Problem parsing the JSON results", e);
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {

        }
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    imei_num = getDeviceId(getApplicationContext());

                    DialogEmergency cd = new DialogEmergency(MapsActivity.this);
                    cd.show();

                }
                break;

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
                            //proceed with logic by disabling the related features or quit the app.
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

    public void getLatLong() {
        gps = new GPSTracker(MapsActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap1.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    class BackGroundTask extends AsyncTask<Void, Void, String> {

        // params,progress,result
        String json_url[] = new String[3];

        @Override
        protected void onPreExecute() {

            // handled by UI threads
            json_url[0] = ApiUrl.MEDICAL_SHOPS_URL;
            json_url[1] = ApiUrl.TOILET_URL;
            json_url[2] = ApiUrl.FEMALE_DOCTORS_URL;

            p = new ProgressDialog(MapsActivity.this);
            p.setMessage("Loading...");
            p.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }// can be used for displaying progress bars

        @Override
        protected String doInBackground(Void... voids) {
            // this carries out the background task
            // StringBuilder stringBuilder = new StringBuilder();
            try {

                int i = 0;
                while (i < 3) {
                    StringBuilder stringBuilder = new StringBuilder();
                    URL url = new URL(json_url[i]);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


                    while ((JSON_String = bufferedReader.readLine()) != null) {
                        stringBuilder.append(JSON_String + "\n");
                    }
                    if (i == 0) {
                        json_string0 = stringBuilder.toString().trim();
                    }
                    if (i == 1) {
                        json_string1 = stringBuilder.toString().trim();
                    }

                    if (i == 2) {
                        json_string2 = stringBuilder.toString().trim();
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    i++;
                }


                return json_string0;    // trim deletes white space
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            switch (IntroActivity.a) {
                case 1:
                    mTitle.setText("TOILETS");
                    getSupportActionBar().setIcon(R.drawable.toilet_small);
                    try {
                        JO = new JSONObject(json_string1);
                        jsonArray = JO.optJSONArray("toilets_json");
                        int count = 0;
                        String name = "", lat, lng, sno;

                        while (count < jsonArray.length()) {
                            //  JSONObject JO = null;
                            try {
                                JO = jsonArray.getJSONObject(count);
                                sno = JO.getString("S_NO");
                                //name = JO.getString("toilets_name");
                                lat = JO.getString("latitude");
                                lng = JO.getString("longitude");
                                LatLng gwalior = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                                mMap.addMarker(new MarkerOptions().position(gwalior).title(sno).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                                //

                                count++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (JSONException e) {

                    }
                    getLatLong();
                    LatLng gwaliort = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(gwaliort).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    LatLngBounds GWALIORT = new LatLngBounds(new LatLng(26.104292, 78.111943), new LatLng(26.320862, 78.270518));

                    p.dismiss();


                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GWALIORT.getCenter(), Float.parseFloat("10.7")));
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(26.2183, 78.1828))
                            .radius(10000)
                            .strokeColor(Color.BLACK)
                            .fillColor(0x42ffff00));

                    if (pref.getEvent().equals("1")) {
                        Intent i = new Intent(MapsActivity.this, CheckComplaintActivity.class);
                        startActivity(i);
                    }

                    break;
                case 4:
                    startActivity(new Intent(MapsActivity.this, PentagonActivity.class));
                    break;
                case 3:
                    mTitle.setText("FEMALE DOCTORS");
                    getSupportActionBar().setIcon(R.drawable.aid_small);
                    //getSupportActionBar().setTitle("FEMALE DOCTORS");
                    flag = 2;

                    AlertDialogManager alert = new AlertDialogManager();
                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                    // Check if Internet present
                    if (!cd.isConnectingToInternet()) {
                        // Internet Connection is not present
                        alert.showAlertDialog(MapsActivity.this,
                                "Internet Connection Error",
                                "Please connect to working Internet connection", false);
                        // stop executing code by return
                        return;
                    } else {
                        getLatLong();
                        mMap.clear();
                        System.out.println(json_string2);
                        try {
                            JO = new JSONObject(json_string2);
                            jsonArray = JO.optJSONArray("female_json");
                            int count = 0;
                            String name, lat, lng;

                            while (count < jsonArray.length()) {

                                try {
                                    JO = jsonArray.getJSONObject(count);
                                    name = JO.getString("Clinic_Name");
                                    lat = JO.getString("Latitude");
                                    lng = JO.getString("Longitude");
                                    LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                    mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

                                    count++;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {

                        }
                        getLatLong();
                        LatLng gwalior = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(gwalior).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                        LatLngBounds GWALIOR = new LatLngBounds(new LatLng(26.104292, 78.111943), new LatLng(26.320862, 78.270518));

                        p.dismiss();


                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GWALIOR.getCenter(), Float.parseFloat("10.7")));
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(26.2183, 78.1828))
                                .radius(10000)
                                .strokeColor(Color.BLACK)
                                .fillColor(0x42ffff00));

                        if (pref.getEvent().equals("1")) {
                            Intent i = new Intent(MapsActivity.this, CheckComplaintActivity.class);
                            startActivity(i);
                        }


                    }
                    break;
                case 2:
                    // getSupportActionBar().setTitle("MEDICAL SHOPS");
                    mTitle.setText("MEDICAL SHOPS");
                    getSupportActionBar().setIcon(R.drawable.medical_small);
                    flag = 1;


                    AlertDialogManager alertm = new AlertDialogManager();
                    ConnectionDetector cdm = new ConnectionDetector(getApplicationContext());

                    // Check if Internet present
                    if (!cdm.isConnectingToInternet()) {
                        // Internet Connection is not present
                        alertm.showAlertDialog(MapsActivity.this,
                                "Internet Connection Error",
                                "Please connect to working Internet connection", false);
                        // stop executing code by return
                        return;
                    } else {
                        getLatLong();
                        mMap.clear();
                        System.out.println(json_string1);
                        try {
                            JO = new JSONObject(json_string0);
                            jsonArray = JO.optJSONArray("medical_shops_json");
                            int count = 0;
                            String name, lat, lng;

                            while (count < jsonArray.length()) {

                                try {
                                    JO = jsonArray.getJSONObject(count);
                                    name = JO.getString("shop_name");
                                    lat = JO.getString("latitude");
                                    lng = JO.getString("longitude");
                                    LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                    mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                    count++;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {

                        }
                        getLatLong();
                        LatLng gwalior = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(gwalior).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                        LatLngBounds GWALIOR = new LatLngBounds(new LatLng(26.104292, 78.111943), new LatLng(26.320862, 78.270518));

                        p.dismiss();


                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GWALIOR.getCenter(), Float.parseFloat("10.7")));
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(26.2183, 78.1828))
                                .radius(10000)
                                .strokeColor(Color.BLACK)
                                .fillColor(0x42ffff00));

                        if (pref.getEvent().equals("1")) {
                            Intent i = new Intent(MapsActivity.this, CheckComplaintActivity.class);
                            startActivity(i);
                        }

                    }


            }

        }
    }
}