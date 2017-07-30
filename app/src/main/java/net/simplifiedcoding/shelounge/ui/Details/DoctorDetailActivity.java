package net.simplifiedcoding.shelounge.ui.Details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);


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
                                    TextView name = (TextView) findViewById(R.id.name);
                                    TextView address = (TextView) findViewById(R.id.address);
                                    TextView cli = (TextView) findViewById(R.id.cli);
                                    TextView time = (TextView) findViewById(R.id.time);
                                    name.setText(doc_name);
                                    cli.setText(cli_name);
                                    address.setText("ADDRESS: " + add);
                                    time.setText("TIMINGS: " + t);
                                    ImageView imge = (ImageView) findViewById(R.id.imge);
                                    CardView cv = (CardView) findViewById(R.id.cv);
                                    cv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                                            startActivity(in);
                                        }
                                    });
                                    Glide.with(DoctorDetailActivity.this).load(img).into(imge);


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
