package com.example.shirin_pc.inputtime;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private EditText mEditTextInput;
    private TextView mTextViewCountDown,tv;
    private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;


    ////shirin add
    private Button button;
     ArrayList<String> reciveList;
    private Button Loop_compare;
    public String RunItem;
    public  static String temp="Shirin";
    public static String variable;
    private TextView show_task,lable_task;
    private EditText input_task;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission();
        }

          //
        show_task= (TextView) findViewById(R.id.show_task);
        lable_task=(TextView) findViewById(R.id.label_task);
        input_task= (EditText) findViewById(R.id.input_task);

        show_task.setText(input_task.getText());
        //
        mEditTextInput = (EditText) findViewById(R.id.edit_text_input);
        mTextViewCountDown = (TextView) findViewById(R.id.text_view_countdown);
        //
        button = (Button) findViewById(R.id.button);

        Loop_compare=(Button) findViewById(R.id.loop_compare);

        //
        mButtonSet = (Button) findViewById(R.id.button_set);
        mButtonStartPause = (Button) findViewById(R.id.button_start_pause);
        mButtonReset = (Button) findViewById(R.id.button_reset);


        //Recive arry list of distracted app
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        Intent intent = getIntent();
         reciveList = intent.getStringArrayListExtra("KeyList");
        Toast.makeText(MainActivity.this,reciveList.toString(), Toast.LENGTH_LONG).show();
    }
});




//Compare and act
Loop_compare.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Thread t=new Thread(){


            @Override
            public void run(){

                while(!isInterrupted()){

                    try {
                        Thread.sleep(5000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                             boolean flag_status=Compare();
                                if(flag_status ){
                                    OpenWidget();
                              Toast.makeText(MainActivity.this,"This works" ,Toast.LENGTH_LONG).show();
//                                    flag_open_distract_app=false;
                                }

                            }


                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        t.start();
    }
});





        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(MainActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });



        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               show_task.setText(input_task.getText());
                if (mTimerRunning) {

                    pauseTimer();

                } else {
                   // show_task.setText(input_task.getText());
                    startTimer();
                 ;
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        //by click on show alll arrya list of Run apps

    }


    //Compare
    public  boolean Compare(){
        boolean flag_same=false;
        String compare=Info_App_Run();
        //Toast.makeText(MainActivity.this, compare, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < reciveList.size(); i++) {
            if (compare.contains(reciveList.get(i))) {
                flag_same = true;
                break;
            }

        }
        return flag_same ;
    }

    //get app run
    public String Info_App_Run(){

        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        String sss=procInfos.get(0).processName;
        //Toast.makeText(MainActivity.this, temp, Toast.LENGTH_SHORT).show();
        if(temp.equals(sss) )
        {
            //Toast.makeText(MainActivity.this, RunItem, Toast.LENGTH_SHORT).show();
            RunItem="sh";
        }
        else
        {
            RunItem= procInfos.get(0).processName;

        }

        temp=sss;
        //Toast.makeText(MainActivity.this, "TEMP IS "+temp+"SSS is "+sss+"Runtime is: "+RunItem , Toast.LENGTH_SHORT).show();

        return RunItem;
    }
////////////////////////////////////////////////////

    public void OpenWidget(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(MainActivity.this, Widget_App_Distract.class));
            finish();
        } else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(MainActivity.this, Widget_App_Distract.class));
            finish();
        } else {
            askPermission();
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }

    }



//********************************************
    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);

    }
//Bar Menu **********************************************************************
    // Bar Menu to go other pages
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.distracted_list:
                openDistracted_list();
                //Toast.makeText(this,"Item distract  selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.help:
                Toast.makeText(this,"Item tutorial selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.exit:
                Toast.makeText(this,"Item Exit selected",Toast.LENGTH_LONG).show();

            default: return super.onOptionsItemSelected(item);
        }
    }
    // Go to distaracted page
    public void openDistracted_list() {
        Intent intent=new Intent(this,Distracted_List.class);
        startActivity(intent);
    }
    //End Go to distaracted page


    //End Go to Run_app page
    //End  bar Menu
    /////////////****************************************************

    //Timer
    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(MainActivity.this, FloatingViewService.class));


        } else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(MainActivity.this, FloatingViewService.class));

        } else {
            askPermission();

        }
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(MainActivity.this, FloatingViewService.class));


        } else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(MainActivity.this, FloatingViewService.class));

        } else {
            askPermission();
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    public void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
        variable=timeLeftFormatted;

    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
            input_task.setVisibility(View.GONE);
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");
            show_task.setText(null);


            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
                //show_task.setVisibility(View.GONE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
              input_task.setVisibility(View.VISIBLE);

            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
                //show_task.setVisibility(View.GONE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
              //  show_task.setVisibility(View.VISIBLE);

            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }
//end of timer





}
