package com.yuanzifu.helloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;


import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    Button login_button = null;
    EditText name_edittext = null;
    EditText passwort_edittext = null;
    TextView show_textvied = null;
    final static String TAG = "seu";
    private static final int COMPLETED = 0;
    private static final int BEGIN = 1;

    Util util = new Util();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                String result = (String) msg.obj;
                String new_result = StringEscapeUtils.unescapeJava(result);
                result = UnicodeUtil.decodeUnicode(new_result);

                try {
                    JSONObject jo = new JSONObject(result);
                    if (jo.has("success")) {
                        show_textvied.setText("success info:" + result);
                    } else {
                        show_textvied.setText("error info:" + result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (msg.what == BEGIN) {
                show_textvied.setText("begining");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* get component*/
        login_button = (Button) findViewById(R.id.login);
        name_edittext = (EditText) findViewById(R.id.editText2);
        passwort_edittext = (EditText) findViewById(R.id.editText);
        show_textvied = (TextView) findViewById(R.id.show);

        /*set login button click listener*/
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_textvied.setText("begining");
                Log.v(TAG, "begining");

                /*get name and password*/
                String login_name = name_edittext.getText().toString();
                String login_password = passwort_edittext.getText().toString();
                Log.v(TAG, login_name + "-" + login_password);

                /*new Thread to send post*/
                WorkThread doPostThread = new WorkThread();
                doPostThread.setUsername(login_name);
                doPostThread.setPassword(login_password);
                doPostThread.start();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showImage() {
        ImageView iv = new ImageView(this);
        iv.setBackgroundColor(0xFFFFFFFF);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iv.setLayoutParams(new Gallery.LayoutParams(40, 40));
        downloadAndShowInternetFile("http://www.twicular.com/images/top_07.png", iv);
        this.setContentView(iv);
    }

    void downloadAndShowInternetFile(String url, ImageView iv) {
        URL internetUrl = null;
        try {
            internetUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) internetUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            Bitmap bmImg = BitmapFactory.decodeStream(is);
            iv.setImageBitmap(bmImg);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }

    //工作线程
    private class WorkThread extends Thread {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public void run() {
            /*sent post to server*/
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);
            String result = HttpUtils.sendPostMessage(params, "utf-8");
            Log.v(TAG, "System-test-logcat");
            Log.v(TAG, result);

            //处理完成后给handler发送消息
            Message msg = new Message();
            msg.what = COMPLETED;
            msg.obj = result;
            handler.sendMessage(msg);
        }
    }
}
