package com.example.kimja.a12th_class_networking;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class Main4Activity extends AppCompatActivity {
    TextView text;
    EditText id, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        text = (TextView) findViewById(R.id.text);
        id = (EditText) findViewById(R.id.id);
        password = (EditText) findViewById(R.id.password);
    }


    public void onClick(View v) {
        if (id.getText().length()<0 && password.getText().length()<0) {
            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
        } else {
            thread.start();
        }
    }

    Handler handler = new Handler();
    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                URL url = new
                        URL("http://jerry1004.dothome.co.kr/info/login.php");
                HttpURLConnection httpURLConnection =
                        (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                String postData = "userid=" + URLEncoder.encode(id.getText().toString())
                        + "&password=" + URLEncoder.encode(password.getText().toString());
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                InputStream inputStream;
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    inputStream = httpURLConnection.getInputStream();
                else
                    inputStream = httpURLConnection.getErrorStream();


                final String result = loginResult(inputStream);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(result.equals("FAIL"))
                            text.setText("로그인이 실패했습니다 .");
                        else
                            text.setText(result + "님 로그인 성공");
                    }
                });
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String loginResult(InputStream inputStream) {
            InputStreamReader inputStreamReader = null;
            StringBuilder sb = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString().trim();
        }
    };



}
