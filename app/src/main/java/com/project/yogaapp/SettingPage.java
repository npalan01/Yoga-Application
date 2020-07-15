package com.project.yogaapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.project.yogaapp.R;

import java.util.Calendar;
import java.util.Date;

public class SettingPage extends AppCompatActivity {
    Button btnSave;
    RadioButton rdiEasy, rdiMedium, rdiHard;
    RadioGroup radioGroup;
    ToggleButton switchAlarm;
    TimePicker timePicker;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "yoga-mode-pref";
    public static final String ModeKey = "modeKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        btnSave = findViewById(R.id.btnSave);
        radioGroup = findViewById(R.id.rdiGroup);
        rdiEasy = findViewById(R.id.rdiEasy);
        rdiMedium = findViewById(R.id.rdiMedium);
        rdiHard = findViewById(R.id.rdiHard);

        switchAlarm = findViewById(R.id.switchAlarm);

        timePicker = findViewById(R.id.timePicker);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        int mode = sharedpreferences.getInt(ModeKey, 0);
        setRadioButton(mode);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveWorkoutMode();
                saveAlarm(switchAlarm.isChecked());
                showAlert(SettingPage.this);
            }


        });
    }

    private void saveAlarm(boolean checked) {

        if (checked) {
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent;
            PendingIntent pendingIntent;

            intent = new Intent(SettingPage.this, AlarmNotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            //set time to alarm
            Calendar calendar = Calendar.getInstance();
            Date toDay = Calendar.getInstance().getTime();
            calendar.set(toDay.getYear(), toDay.getMonth(), toDay.getDay(), timePicker.getHour(), timePicker.getMinute());

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("DEBUG", "Alarm will wake At: " + timePicker.getHour() + ":" + timePicker.getMinute());
        } else {
            //cancel alarm
            Intent intent = new Intent(SettingPage.this, AlarmNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
        }
    }

    private void saveWorkoutMode() {
        int selectedID = radioGroup.getCheckedRadioButtonId();
        if (selectedID == rdiEasy.getId())
            saveSharedPref(0);
        else if (selectedID == rdiMedium.getId())
            saveSharedPref(1);
        else if (selectedID == rdiHard.getId())
            saveSharedPref(2);
    }

    private void saveSharedPref(int mode) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(ModeKey, mode);
        editor.commit();
    }

    private void setRadioButton(int mode) {
        if (mode == 0)
            radioGroup.check(R.id.rdiEasy);
        else if (mode == 1)
            radioGroup.check(R.id.rdiMedium);
        else if (mode == 2)
            radioGroup.check(R.id.rdiHard);

    }

    private void showAlert(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Save Successful !!!");
        alertDialog.setMessage("Your mode setting was saved sucessfully.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();
        // Get the alert dialog buttons reference
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        // Change the alert dialog buttons text and background color
        positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));
        positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"));
    }
}
