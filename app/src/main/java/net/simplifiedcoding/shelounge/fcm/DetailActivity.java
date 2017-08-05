package net.simplifiedcoding.shelounge.fcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.simplifiedcoding.shelounge.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);

        TextView txt = (TextView) findViewById(R.id.txt);
        TextView txt2 = (TextView) findViewById(R.id.txt2);
        ImageView img = (ImageView) findViewById(R.id.img);

        String s =  getIntent().getStringExtra("txt1");
       // Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

        String s1 =getIntent().getStringExtra("txt2");
        String s2 =getIntent().getStringExtra("txt3");

        txt.setText(s);
        txt2.setText(s1);

        Glide.with(this).load(s2).into(img);
    }
}
