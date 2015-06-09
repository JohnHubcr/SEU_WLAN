package com.yuanzifu.helloworld;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


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
                NameValuePair pair1 = new BasicNameValuePair("username", login_name);
                NameValuePair pair2 = new BasicNameValuePair("password", login_password);
                List list = new ArrayList();
                list.add(pair1);
                list.add(pair2);
                try {
                    HttpEntity httpEntity = new UrlEncodedFormEntity(list);//使用编码构建Post实体
                    HttpPost post = new HttpPost(util.getPOST_URL());
                    post.setEntity(httpEntity);//设置Post实体
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(post);//执行Post方法
                    String resultString = EntityUtils.toString(response.getEntity());
                    show_textvied.setText(resultString);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

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
}
