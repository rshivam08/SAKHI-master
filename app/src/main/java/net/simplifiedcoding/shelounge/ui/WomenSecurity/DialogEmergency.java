package net.simplifiedcoding.shelounge.ui.WomenSecurity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import net.simplifiedcoding.shelounge.DetailInterfaces.InsertAPI;
import net.simplifiedcoding.shelounge.R;
import net.simplifiedcoding.shelounge.ui.MapsActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Shivam on 8/4/2017.
 */

public class DialogEmergency extends Dialog implements View.OnClickListener {

    public static final String ROOT_URL = "http://ric-tiiciiitm.webhostingforstudents.com/sakhi/";

    SharedPreferences preferences;

    TextInputEditText name, age, email, phone_num, aadhar_num;
    RadioButton male, female;
    Button done;

    String gender;



    String imei_num;

    public Activity c;

    public DialogEmergency(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_emergency);


        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        name = (TextInputEditText) findViewById(R.id.name_et);
        age = (TextInputEditText) findViewById(R.id.age_et);
        email = (TextInputEditText) findViewById(R.id.email_et);
        phone_num = (TextInputEditText) findViewById(R.id.phone_et);
        aadhar_num = (TextInputEditText) findViewById(R.id.aadhar_et);

        male = (RadioButton) findViewById(R.id.radio_male);
        female = (RadioButton) findViewById(R.id.radio_female);

      //  female.setChecked(true);

        if (female.isChecked()){
            gender = "Female";
        } else {
            gender = "Male";
        }

        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                if (name.equals("") || age.equals("") || email.equals("") || phone_num.equals("") || aadhar_num.equals("")){
                    Toast.makeText(getContext(),"All Fields are Mandatory", Toast.LENGTH_SHORT).show();

                }else {
                    imei_num = MapsActivity.getDeviceId(getContext());
                    Log.e("MainActivity", imei_num );
                    insertUser();
                    preferences = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("Registered",true);
                    editor.apply();
                    getContext().startActivity(new Intent(getContext(), PentagonActivity.class));
                    dismiss();
                }
        }
    }

    private void insertUser(){
        RestAdapter adapter=new RestAdapter.Builder()
                .setEndpoint(ROOT_URL).build();
        /*Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");

        String strDate = mdformat.format(calendar.getTime());
        Toast.makeText(getContext(),strDate,Toast.LENGTH_LONG).show();*/

        InsertAPI api=adapter.create(InsertAPI.class);
        api.insertUser(
                name.getText().toString(),
                age.getText().toString(),
                gender,
                email.getText().toString(),
                phone_num.getText().toString(),
                aadhar_num.getText().toString(),
                imei_num,

                new Callback<Response>() {

                    @Override
                    public void success(Response result, Response response) {
                        BufferedReader reader = null;
                        String output = "";
                        try{
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), output, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
