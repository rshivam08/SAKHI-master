package net.simplifiedcoding.shelounge.ui.Details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.shelounge.DetailInterfaces.AvgRating;
import net.simplifiedcoding.shelounge.DetailInterfaces.NameToilet;
import net.simplifiedcoding.shelounge.DetailInterfaces.RetrieveFeedbacks;
import net.simplifiedcoding.shelounge.R;
import net.simplifiedcoding.shelounge.adapters.FeedbackAdapter;
import net.simplifiedcoding.shelounge.models.Feedback;
import net.simplifiedcoding.shelounge.ui.MapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class ToiletFeedbackActivity extends AppCompatActivity {
    Button fab;
    TextView name;
    RatingBar rate;
    String st;
    String JOutput;
    private RecyclerView mRecyclerView;
    private ArrayList<Feedback> data;
    private FeedbackAdapter mFeedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_detail);
        fab = (Button) findViewById(R.id.fab);
        rate = (RatingBar) findViewById(R.id.stars);
        name = (TextView) findViewById(R.id.name);
        name();
        detail();

        //Toast.makeText(ToiletFeedbackActivity.this, st, Toast.LENGTH_LONG).show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFeedbackActivity cd = new DialogFeedbackActivity(ToiletFeedbackActivity.this);
                cd.show();

            }
        });
        try {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint("http://ric-tiiciiitm.webhostingforstudents.com/sakhi/").build();
            RetrieveFeedbacks api = adapter.create(RetrieveFeedbacks.class);
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
                            // Toast.makeText(ToiletFeedbackActivity.this, output, Toast.LENGTH_LONG).show();
                            JOutput = output;

                            List<Feedback> feedbacks = new ArrayList<>();
                            try {

                                JSONArray Array = new JSONArray(JOutput);
                                // in=Array.length();
                                for (int i = 0; i < Array.length(); i++) {
                                    JSONObject current = Array.getJSONObject(i);
                                    String user = current.getString("user");
                                    String rating = current.getString("rating");
                                    String comment = current.getString("comment");
                                    String date = current.getString("date");
                                    Feedback feedback = new Feedback(user, rating, comment, date);
                                    feedbacks.add(feedback);
                                    mRecyclerView = (RecyclerView) findViewById(R.id.squawks_recycler_view);
                                    mRecyclerView.setHasFixedSize(true);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                    mRecyclerView.setLayoutManager(layoutManager);
                                    mFeedbackAdapter = new FeedbackAdapter(feedbacks);
                                    mRecyclerView.setAdapter(mFeedbackAdapter);
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

                            JOutput = output;
                            try {

                                JSONArray Array = new JSONArray(JOutput);
                                // in=Array.length();
                                for (int i = 0; i < Array.length(); i++) {
                                    JSONObject current = Array.getJSONObject(i);

                                    String rating = current.getString("rating");
                                    rate.setRating(Float.parseFloat(rating));
                                    rate.setEnabled(false);


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
                            JOutput = output;
                            try {

                                JSONArray Array = new JSONArray(JOutput);
                                for (int i = 0; i < Array.length(); i++) {
                                    JSONObject current = Array.getJSONObject(i);

                                    String named = current.getString("toilets_name");
                                    name.setText(named);


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
