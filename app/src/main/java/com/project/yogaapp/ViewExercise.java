package com.project.yogaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.project.yogaapp.R;
import com.project.yogaapp.Utils.Common;


public class ViewExercise extends AppCompatActivity {

    int image_id;
    String name;
    //    String desc;
    Button btnStart, btnBack;
    TextView timer, title, desc;
    ImageView detail_image;
    Boolean isRunning = false;
    SharedPreferences sharedpreferences;
    LottieAnimationView animationView;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        timer = findViewById(R.id.timer);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        sharedpreferences = getSharedPreferences(SettingPage.mypreference,
                Context.MODE_PRIVATE);
        animationView = findViewById(R.id.animationView);
        mode = sharedpreferences.getInt(SettingPage.ModeKey, 0);
        detail_image = findViewById(R.id.detailImage);
        btnStart = findViewById(R.id.btnStart);
        btnBack = findViewById(R.id.btnBack);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    btnStart.setText("DONE");

                    int timeLimit = 0;
                    if (mode == 0)
                        timeLimit = Common.TIME_LIMIT_EASY;
                    else if (mode == 1)
                        timeLimit = Common.TIME_LIMIT_MEDIUM;
                    else if (mode == 2)
                        timeLimit = Common.TIME_LIMIT_HARD;


                    new CountDownTimer(timeLimit, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                            timer.setText("" + millisUntilFinished / 1000);
                        }

                        @Override
                        public void onFinish() {
                            animationView.playAnimation();
                            btnStart.setVisibility(View.GONE);
                            btnBack.setVisibility(View.VISIBLE);
                        }
                    }.start();
                } else {
                    animationView.playAnimation();
                    btnStart.setVisibility(View.GONE);
                    btnBack.setVisibility(View.VISIBLE);
                }
                isRunning = !isRunning;
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        timer.setText("");
        if (getIntent() != null) {
            image_id = getIntent().getIntExtra("image_id", -1);
            name = getIntent().getStringExtra("name");
            detail_image.setImageResource(image_id);
            title.setText(name);
            desc.setText(getIntent().getStringExtra("desc"));
        }
    }


}
