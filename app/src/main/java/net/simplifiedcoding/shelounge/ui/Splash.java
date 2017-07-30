package net.simplifiedcoding.shelounge.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import net.simplifiedcoding.shelounge.R;

public class Splash extends Activity {
    Thread splashTread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimations();
    }

    private void StartAnimations() {
        Animation anim;


        anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        // anim.reset();
        //CardView cv=(CardView)findViewById(R.id.cv);
        ImageView iv = (ImageView) findViewById(R.id.splash_img);
        // iv.clearAnimation();
        iv.startAnimation(anim);
        Animation anime = AnimationUtils.loadAnimation(this, R.anim.bottom_in);
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.startAnimation(anime);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(Splash.this, IntroActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                    Splash.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Splash.this.finish();
                }

            }
        };
        splashTread.start();

    }
}
