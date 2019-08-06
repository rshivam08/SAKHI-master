package net.simplifiedcoding.shelounge.ui.Details;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.simplifiedcoding.shelounge.DetailInterfaces.ShopDetail;
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

public class ShopsDetailActivity extends AppCompatActivity {
    String JOutput;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //String doc=getIntent().getStringExtra("NAME");
        //getSupportActionBar().setTitle(doc);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_shops_details);
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
                            JOutput = output;
                            try {

                                JSONArray Array = new JSONArray(JOutput);
                                for (int i = 0; i < Array.length(); i++) {
                                    JSONObject current = Array.getJSONObject(i);


                                    String doc_name = current.getString("shop_name");

                                    String img = current.getString("image_url");
                                    TextView name = (TextView) findViewById(R.id.name);

                                    name.setText(doc_name);
                                    Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lithos.ttf");
                                    name.setTypeface(font);
                                    ImageView imge = (ImageView) findViewById(R.id.pic);
                                    Glide.with(ShopsDetailActivity.this).load(img).into(imge);


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
