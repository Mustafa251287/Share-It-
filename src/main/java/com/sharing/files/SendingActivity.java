package com.sharing.files;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.net.Socket;

public class SendingActivity extends AppCompatActivity {
TextView FileName,Title;
Context con;
LinearProgressIndicator progressbar;
String ip,fileName,File_Path;
Socket s;
int port;
File FiletoSend;
String[] bundle;
long FileSize,TotalFilesended;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#329fff")));

        FileName=findViewById(R.id.FilenameTextView);
        Title=findViewById(R.id.textViewsending);
        progressbar=findViewById(R.id.linearProgressIndicator);

        con=getApplicationContext();
        bundle=getIntent().getStringExtra("alldata").split(";");

        ip=bundle[0];
        port=Integer.parseInt(bundle[1]);
        fileName=bundle[2];
        File_Path=bundle[3];

        FileName.setText(fileName);
        FiletoSend=new File(File_Path);
        FileSize=FiletoSend.length();

        sendFile();
    }




    public void runabletost(String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(con,s,Toast.LENGTH_LONG).show();
            }
        });
    }


public void sendFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    s=new Socket(ip,port);
                    DataOutputStream dout=new DataOutputStream(s.getOutputStream());
                    dout.writeUTF(fileName+";"+FileSize);
                   FileInputStream fil=new FileInputStream(FiletoSend);
                   byte[] b=new byte[1000000];
                   long i=0;
                  TotalFilesended=0;
                  while((i=fil.read(b))!=-1){

                      dout.write(b,0,(int)i);
                      TotalFilesended=TotalFilesended+i;
                     long percent=(TotalFilesended*100)/FileSize;

                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             progressbar.setProgress((int)percent);
                         }
                     });
                    }
                 fil.close();
                  dout.close();
                  s.close();

                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          Title.setText("File Sended succssfully");
                      }
                  });
                } catch (Exception e){
                    runabletost(e.toString());
                }
            }
        }).start();





 }


}