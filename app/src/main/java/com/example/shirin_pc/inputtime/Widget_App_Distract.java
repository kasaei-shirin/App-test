package com.example.shirin_pc.inputtime;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.List;

public class Widget_App_Distract extends Service {


    //shirin add
    private Button closewidget, closeapp;
    ///

    private WindowManager mWindowManager;
    private View mFloatingView;


    private View expandedView;

    public Widget_App_Distract() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.distract_app_widget_, null);

        //setting the layout parameters
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        //getting the collapsed and expanded view from the floating view

        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);

        //adding click listener to close button and expanded view
        // mFloatingView.findViewById(R.id.btn_Yes).setOnClickListener(this);
        // expandedView.setOnClickListener(this);


        //close the widget
        closewidget = (Button) mFloatingView.findViewById(R.id.btn_Yes);
        closewidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });

        //////////////////////////////

        //close app
        closeapp = (Button) mFloatingView.findViewById(R.id.btn_No);
        closeapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // String b= Run();
                //android.os.Process.killProcess(android.os.Process.myPid());
                //  Toast.makeText(Widget_App_Distract.this,b, Toast.LENGTH_LONG).show();
                // killAppBypackage(b);
//                Intent intent = new Intent(this,MainActivity.class);
//
//                startActivity(intent);
                //Run();
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                stopSelf();
            }
        });


        //adding an touchlistener to make drag movement of the floating widget
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget

                        expandedView.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    //    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.layoutExpanded:
//
//                //switching views
//
//                expandedView.setVisibility(View.GONE);
//                break;
//
////            case R.id.buttonClose:
////                //closing the widget
////                stopSelf();
////                break;
//        }
//    }
    private void killAppBypackage(String packageTokill) {

        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);


        ActivityManager mActivityManager = (ActivityManager) Widget_App_Distract.this.getSystemService(Context.ACTIVITY_SERVICE);

        String myPackage = getApplicationContext().getPackageName();

        for (ApplicationInfo packageInfo : packages) {

            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }
            if (packageInfo.packageName.equals(myPackage)) {
                continue;
            }
            if (packageInfo.packageName.equals(packageTokill)) {
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);
            }

        }

    }

    public void Run() {
        ActivityManager mActivityManager = (ActivityManager) Widget_App_Distract.this.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        // String s=procInfos.get(0).processName;
        mActivityManager.killBackgroundProcesses(procInfos.get(0).processName);
//        android.os.Process.killProcess(Integer.parseInt(procInfos.get(0).processName));
        // ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        //am.killBackgroundProcesses(procInfos.get(0).processName);

//return s;


    }


}



