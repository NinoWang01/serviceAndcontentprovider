package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService2 extends Service {
    private DownLoadBind downLoadBind = new DownLoadBind();
    CallBack callBack;

    public class DownLoadBind extends Binder{

        public MyService2 getService(){
            return MyService2.this;
        }

        public void myTest(String test) {
            Log.d("---------------", "myTest"+test);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(5000);
                        callBack.newData("newData");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }

    public MyService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("---------------", "onBind-bind");
        return downLoadBind;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("---------------", "onCreate-bind");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("---------------", "onDestory-bind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("---------------", "onUnbind-bind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("------------------", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    public static interface CallBack{
        void newData(String newData);
    }

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }
}
