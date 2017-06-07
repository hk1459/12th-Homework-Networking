package com.example.kimja.a12th_class_networking;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    String serverIP = "172.17.67.53";
    int serverPort = 200;
    String msg = "";
    EditText e1;
    public void onClick(View v){
        msg =  e1.getText().toString();
        myThread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = (EditText)findViewById(R.id.etmsg);
    }
    Handler myHandler = new Handler();
    Thread myThread = new Thread(){
        @Override
        public void run() {
            try {
                Socket aSocket = new Socket(serverIP, serverPort);
//                System.out.println("Client] 서버 접속");

                Scanner s = new Scanner(System.in);
//                System.out.println("서버에 보낼 데이터를 입력하세요 ");
                String data = s.next();

                ObjectOutputStream outstream = new ObjectOutputStream(aSocket.getOutputStream());
                outstream.writeObject(data);
                outstream.flush();
//                System.out.println("Client] 전송데이터 : " + data);

                ObjectInputStream instream = new ObjectInputStream(aSocket.getInputStream());
                final Object obj = instream.readObject();
//                System.out.println("Client] 받은데이터 : " + obj);

                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                (String)obj,Toast.LENGTH_SHORT).show();
                    }
                });
                
                aSocket.close();
//                System.out.println("Client] 서버 접속 중단");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
}
