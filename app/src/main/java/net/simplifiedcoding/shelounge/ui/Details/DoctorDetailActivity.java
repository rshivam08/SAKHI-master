package net.simplifiedcoding.shelounge.ui.Details;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.simplifiedcoding.shelounge.R;
import net.simplifiedcoding.shelounge.ui.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class DoctorDetailActivity extends AppCompatActivity {
    String JOutput;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lithos.ttf");
        collapsingToolbarLayout.setExpandedTitleTypeface(font);
        collapsingToolbarLayout.setCollapsedTitleTypeface(font);
        String doc = getIntent().getStringExtra("DOCTOR");

        getSupportActionBar().setTitle(doc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint("http://ric-tiiciiitm.webhostingforstudents.com/sakhi/").build();

            net.simplifiedcoding.shelounge.DetailInterfaces.DoctorDetail api = adapter.create(net.simplifiedcoding.shelounge.DetailInterfaces.DoctorDetail.class);
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
                            JOutput = output;
                            try {

                                JSONArray Array = new JSONArray(JOutput);
                                for (int i = 0; i < Array.length(); i++) {
                                    JSONObject current = Array.getJSONObject(i);

                                    String cli_name = current.getString("Clinic_Name");
                                    String doc_name = current.getString("Doctor_Name");
                                    final String phone = current.getString("Mobile");
                                    String add = current.getString("Address");
                                    String t = current.getString("Timings");
                                    String img = current.getString("image_url");
                                    TextView address = (TextView) findViewById(R.id.address);
                                    TextView telephone = (TextView) findViewById(R.id.telephone);
                                    TextView cli = (TextView) findViewById(R.id.cli);
                                    TextView time = (TextView) findViewById(R.id.time);
                                    cli.setText(cli_name);
                                    address.setText("" + add);
                                    time.setText("" + t);
                                    telephone.setText(phone);
                                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.img_call);
                                    ImageView pic = (ImageView) findViewById(R.id.pic);
                                    fab.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                                            if (ActivityCompat.checkSelfPermission(DoctorDetailActivity.this, Manifest.permission.CALL_PHONE)
                                                    != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    ActivityCompat#requestPermissions
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for ActivityCompat#requestPermissions for more details.
                                                return;
                                            }
                                            startActivity(in);
                                        }
                                    });
                                    Glide.with(DoctorDetailActivity.this).load(img).into(pic);
                                    TextView addr = (TextView) findViewById(R.id.add);
                                    TextView no = (TextView) findViewById(R.id.no);
                                    TextView overview = (TextView) findViewById(R.id.overview);
                                    TextView timings = (TextView) findViewById(R.id.timings);
                                    Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lithos.ttf");
                                    cli.setTypeface(font);
                                    telephone.setTypeface(font);
                                    time.setTypeface(font);
                                    address.setTypeface(font);
                                    addr.setTypeface(font);
                                    no.setTypeface(font);
                                    timings.setTypeface(font);
                                    overview.setTypeface(font);
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
}
