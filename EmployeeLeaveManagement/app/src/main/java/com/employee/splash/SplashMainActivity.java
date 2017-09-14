package com.employee.splash;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.employee.main.MainActivity;

import main.employee.com.employeeleavemanagement.R;

public class SplashMainActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);

        Handler handler = new Handler();
        Runnable obj = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }
        };
        handler.postDelayed(obj, 3000);

    }

    @Override
    protected void onStart() {
        super.onStart();

        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(ivLogo, "alpha",0.2f, 2f,0.2f,2f);
        alphaAnimation.setDuration(3000);

        ObjectAnimator alphaAnimation1 = ObjectAnimator.ofFloat(tvWelcome, "alpha",0.2f,1.5f,0.2f,1.5f);
        alphaAnimation1.setDuration(3000);

        AnimatorSet set = new AnimatorSet();
        set.play(alphaAnimation).with(alphaAnimation1);
        set.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();



    }
    @Override
    protected void onPause() {

        super.onPause();
        finish();

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

}
