package com.example.kimja.a12th_class_networking;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main2Activity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        editText = (EditText)findViewById(R.id.editText);
        textView = (TextView)findViewById(R.id.textView);
    }
    public void onClick(View v){
        if(v.getId() == R.id.next){
            Intent ab  = new Intent(Main2Activity.this,Main3Activity.class);
            startActivity(ab);
        } else {
            myThread.start();
        }

    }

    Handler myHandler = new Handler();
    Thread myThread = new Thread() {
        @Override
        public void run() {
            URL url = null;
            try {
                url = new URL(editText.getText().toString());

                HttpURLConnection urlConnection =
                        (HttpURLConnection) url.openConnection();

                if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK ) {
                    final String data = readData(urlConnection.getInputStream());
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(data);
                        }
                    });
                    urlConnection.disconnect();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    };
    String readData(InputStream is){
        String data = "";
        Scanner s = new Scanner(is);
        while(s.hasNext()) data += s.nextLine() + "\n";
        s.close();
        return data;
    }
}
