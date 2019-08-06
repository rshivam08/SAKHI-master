package net.simplifiedcoding.shelounge.ui.Details;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.shelounge.DetailInterfaces.InsertFeedback;
import net.simplifiedcoding.shelounge.R;
import net.simplifiedcoding.shelounge.ui.MapsActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tjuhi on 6/1/2017.
 */

public class DialogFeedbackActivity extends Dialog implements View.OnClickListener {
    public static final String ROOT_URL = "http://ric-tiiciiitm.webhostingforstudents.com/sakhi";
    public Activity c;
    public Dialog d;
    public RatingBar mRate;
    // public EditText mComment;
    public Button mDone, mCancel;

    public DialogFeedbackActivity(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_layout);
        mRate = (RatingBar) findViewById(R.id.rate);
        LayerDrawable stars = (LayerDrawable) mRate.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        //  mComment = (EditText) findViewById(R.id.feed);
        mDone = (Button) findViewById(R.id.done);
        TextView feed = (TextView) findViewById(R.id.feedback);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/lithos.ttf");
        feed.setTypeface(font);
        // mCancel = (Button) findViewById(R.id.cancel);
        mDone.setOnClickListener(this);
//        mCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:

                insertUser();
                dismiss();
                break;


        }

    }

    private void insertUser() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL).build();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");

        String strDate = mdformat.format(calendar.getTime());
        Toast.makeText(getContext(), strDate, Toast.LENGTH_LONG).show();
        InsertFeedback api = adapter.create(InsertFeedback.class);
        api.insertUser(

                "Anonymous",
                mRate.getRating(),
                // mComment.getText().toString(),
                strDate
                ,
                MapsActivity.mChosenLoc,

                new Callback<Response>() {

                    @Override
                    public void success(Response result, Response response) {
                        BufferedReader reader = null;
                        String output = "";
                        try {
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Toast.makeText(getContext(), output, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
