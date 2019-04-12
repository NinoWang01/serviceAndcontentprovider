package com.example.myapplication;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;

    private MyService2.DownLoadBind downLoadBinder;
    private MyReceiver2 myReceiver2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setContentProvider();
    }

    private void setContentProvider() {
        /**
         * 对user表进行操作
         */

        // 设置URI
        Uri uri_user = Uri.parse("content://cn.scu.myprovider/user");

        // 插入表中数据
        ContentValues values = new ContentValues();
        values.put("_id", 3);
        values.put("name", "Iverson");


        // 获取ContentResolver
        ContentResolver resolver =  getContentResolver();
        // 通过ContentResolver 根据URI 向ContentProvider中插入数据
        resolver.insert(uri_user,values);

        // 通过ContentResolver 向ContentProvider中查询数据
        Cursor cursor = resolver.query(uri_user, new String[]{"_id","name"}, null, null, null);
        while (cursor.moveToNext()){
            System.out.println("query book:" + cursor.getInt(0) +" "+ cursor.getString(1));
            // 将表中数据全部输出
        }
        cursor.close();
        // 关闭游标

        /**
         * 对job表进行操作
         */
        // 和上述类似,只是URI需要更改,从而匹配不同的URI CODE,从而找到不同的数据资源
        Uri uri_job = Uri.parse("content://cn.scu.myprovider/job");

        // 插入表中数据
        ContentValues values2 = new ContentValues();
        values2.put("_id", 3);
        values2.put("job", "NBA Player");

        // 获取ContentResolver
        ContentResolver resolver2 =  getContentResolver();
        // 通过ContentResolver 根据URI 向ContentProvider中插入数据
        resolver2.insert(uri_job,values2);

        // 通过ContentResolver 向ContentProvider中查询数据
        Cursor cursor2 = resolver2.query(uri_job, new String[]{"_id","job"}, null, null, null);
        while (cursor2.moveToNext()){
            System.out.println("query job:" + cursor2.getInt(0) +" "+ cursor2.getString(1));
            // 将表中数据全部输出
        }
        cursor2.close();
        // 关闭游标
    }

    private void initView() {
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn5 = findViewById(R.id.button5);
        btn6 = findViewById(R.id.button6);
        btn7 = findViewById(R.id.button7);
        btn8 = findViewById(R.id.button8);
        btn9 = findViewById(R.id.button9);


        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent intent_start = new Intent(this, MyService1.class);
                startService(intent_start);

                break;
            case R.id.button2:
                Intent intent_stop = new Intent(this, MyService1.class);
                stopService(intent_stop);
                break;
            case R.id.button3:
                Intent intent_start_bind = new Intent(this, MyService2.class);
                bindService(intent_start_bind, connection, BIND_AUTO_CREATE);
                break;
            case R.id.button4:
                unbindService(connection);
                break;
            case R.id.button5:
                MyIntentService myIntentService = new MyIntentService();
                myIntentService.startActionBaz(this, "aaaaa", "111111");
                myIntentService.startActionFoo(this, "bbbbb", "222222");
                break;
            case R.id.button6:
                Intent intent = new Intent(this, MyIntentService.class);
                stopService(intent);
                break;
            case R.id.button7://静态

                break;
            case R.id.button8://动态
                myReceiver2 = new MyReceiver2();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.aaaaaaaaaa");
                intentFilter.setPriority(101);
                //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
                registerReceiver(myReceiver2, intentFilter);
                break;
            case R.id.button9:
                Intent intent9 = new Intent();
                intent9.setAction("android.net.conn.aaaaaaaaaa");
                //Intent intent = new Intent("com,example.mymessage");
                //也可以像注释这样写
                sendBroadcast(intent9);//发送标准广播
//                sendOrderedBroadcast(intent9, null);//发送有序广播
                // 意思就是发送值为com.example.mymessage的这样一条广播
                break;
        }
    }


    private ServiceConnection connection = new ServiceConnection() {
        /**
         * 服务解除绑定时候调用
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

        /**
         * 绑定服务的时候调用
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            //myService=((DownLoadBinder) service).
            downLoadBinder = (MyService2.DownLoadBind) service;

            //回调
            MyService2 myService2 = downLoadBinder.getService();
            myService2.setCallBack(new MyService2.CallBack() {
                @Override
                public void newData(String newData) {
                    Log.d("---------------", "newData:" + newData);
                }
            });

            /*
             * 调用DownLoadBinder的方法实现参数的传递
             */
            downLoadBinder.myTest("test传到service");
        }
    };
}
