package net.simplifiedcoding.shelounge.ui.WomenSecurity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.simplifiedcoding.shelounge.R;

import java.io.File;
import java.io.IOException;

public class AudioActivity extends AppCompatActivity {


    private static final String LOG_TAG = "AudioActivity Recording";
    ImageButton mStart, mStop;
    TextView mStatus;
    private MediaRecorder recorder = null;
    private int opFormats[] = {MediaRecorder.OutputFormat.MPEG_4,
            MediaRecorder.OutputFormat.THREE_GPP
    };
    private int curFormat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        mStart = (ImageButton) findViewById(R.id.recordbtn1);
        mStop = (ImageButton) findViewById(R.id.log_btn);
        mStatus = (TextView) findViewById(R.id.num_1);

        mStop.setEnabled(false);
        mStart.setOnClickListener(new btnClick());
        mStop.setOnClickListener(new btnClick());
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, PentagonActivity.class));
        finish();


    }

    void startRecording() {
        getFilePath();
        try {
            recorder = new MediaRecorder();
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(opFormats[curFormat]);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(getFilePath());

            mStatus = (TextView) findViewById(R.id.num_1);

            recorder.prepare();

            recorder.start();

            mStart.setEnabled(false);
            mStatus.setText("Recording Started");
            mStop.setEnabled(true);
        } catch (IOException e) {
            Log.e(LOG_TAG, "repare() failed");
        }


    }

    /**
     * @return
     */
    private String getFilePath() {


        String filePath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filePath, "PentagonLog");
        if (!file.exists())
            file.mkdir();

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3");

    }

    public void stopRecording() {
        // TODO Auto-generated method stub
        mStatus.setText("");
        Toast.makeText(getApplicationContext(), getFilePath().toString(), Toast.LENGTH_LONG).show();
        SharedPreferences share = getSharedPreferences("ScreenCast", Context.MODE_PRIVATE);
        String no = (share.getString("mobile", ""));

        String valueofpathh = getFilePath().toString();
        File filee = new File(valueofpathh);
        Uri uri = Uri.parse("file://" + filee.getAbsolutePath());
        String s = PentagonActivity.stored_number[0].substring(0, 1);
        if (PentagonActivity.stored_number[0].contains("+91")) {
            PentagonActivity.stored_number[0].replace("+", "");
        } else if (s.equals("0")) {
            s = "91";
            PentagonActivity.stored_number[0] = s + PentagonActivity.stored_number[0].substring(1, PentagonActivity.stored_number[0].length());


        } else {
            PentagonActivity.stored_number[0] = "91" + PentagonActivity.stored_number[0];
        }


        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("audio/*");
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        sendIntent.setPackage("com.whatsapp");
        sendIntent.putExtra("jid", PentagonActivity.stored_number[0] + "@s.whatsapp.net");

        startActivity(sendIntent);
        try {
            if (recorder != null) {
                //recorder.stop();
                recorder.reset();
                recorder.release();

                recorder = null;


            } else {

                Toast tt = Toast.makeText(this, "No Recording Started", Toast.LENGTH_SHORT);
                tt.show();
            }
            mStart.setEnabled(true);
            mStop.setEnabled(false);
        } catch (Exception e) {
            Toast ttt = Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT);
            ttt.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.audio, menu);
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

    class btnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recordbtn1:
                    startRecording();
                    break;
                case R.id.log_btn:
                    stopRecording();
                    break;

            }

        }

    }
}
