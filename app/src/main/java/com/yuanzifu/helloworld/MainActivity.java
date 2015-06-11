package com.yuanzifu.helloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;

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

    Util util = new Util();

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
                /*get name and password*/
                String login_name = name_edittext.getText().toString();
                String login_password = passwort_edittext.getText().toString();
                //Toast.makeText(getApplicationContext(), login_name+"-"+login_password, Toast.LENGTH_LONG).show();

                /*sent post to server*/
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "220131440");
                params.put("password", "574423");
                String result = HttpUtils.sendPostMessage(params, "utf-8");
                System.out.println("System-test");
                System.out.println("System-" + result);
                HttpUtils.main(null);

                show_textvied.setText(result);

                showImage();


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
}
