package com.example.tk_2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearlayout;
    private Toolbar toolbar;
    private RecyclerView recyclerview;
    private NavigationView navigationview;
    private DrawerLayout drawerlayut;
    private RelecrAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                List<javaBean.DataBean> list = (List<javaBean.DataBean>) msg.obj;
                adapter.addData(list);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        navigationview = (NavigationView) findViewById(R.id.navigationview);
        drawerlayut = (DrawerLayout) findViewById(R.id.drawerlayut);
        initView();
        initData();
        initRecler();
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                String result = initGson();
                List<javaBean.DataBean> data = new Gson().fromJson(result, javaBean.class).getData();
                Message message = new Message();
                message.obj = data;
                message.what = 1;
                handler.sendMessage(message);
            }
        }.start();
    }

    private String initGson() {
        try {
            URL url = new URL("http://www.qubaobei.com/ios/cf/dish_list.php?stage_id=1&limit=20&page=1");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int lengh;
            byte[] bytes = new byte[1024 * 4];
            while ((lengh = in.read(bytes)) != -1) {
                out.write(bytes, 0, lengh);
            }
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void initRecler() {
        List<javaBean.DataBean> list = new ArrayList<>();
        adapter = new RelecrAdapter(list, this);
        recyclerview.setAdapter(adapter);
        View view = LayoutInflater.from(this).inflate(R.layout.pop_item, null);
        View bt_queren = view.findViewById(R.id.bt_queren);
        View bt_quxiao = view.findViewById(R.id.bt_quxiao);
        bt_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "不可能取消", Toast.LENGTH_SHORT).show();
            }
        });
        bt_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "12";
                String name = "notification";

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 72, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification nu = new NotificationCompat.Builder(MainActivity.this, id)
                        .setContentTitle("我的收藏")
                        .setContentText("1234667789")
                        .setSmallIcon(R.color.colorAccent)
                        .setContentIntent(pendingIntent)
                        .build();

                notificationManager.notify(100, nu);
            }
        });
        final PopupWindow popupWindow = new PopupWindow(view, 500, 300);
        adapter.setOnClick(new RelecrAdapter.OnClick() {
            @Override
            public void onClick(View v, int i) {
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                popupWindow.setOutsideTouchable(true);

                popupWindow.showAtLocation(linearlayout, Gravity.CENTER, 0, 0);
            }
        });
    }

    private void initView() {
        toolbar.setTitle("标题");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayut, toolbar, R.string.open, R.string.close);
        drawerlayut.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        drawerlayut.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                LinearLayout id = findViewById(R.id.linearlayout);
                int right = drawerlayut.getRight();
                id.setX(right);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
}
