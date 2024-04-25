package com.sharing.files;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
boolean permissiongranted=false;
int this_android_version=Build.VERSION.SDK_INT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#329fff")));

if(!checkper()){

    new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
            takepermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    },2500);

} else {
File dir=new File(Environment.getExternalStorageDirectory(),"ShareIt");
dir.mkdir();
File dir1=new File(dir.getAbsolutePath()+"/Documents");
dir1.mkdir();
    permissiongranted=true;
}

    }

    private boolean checkper(){
if(this_android_version>=Build.VERSION_CODES.R){

    if(Environment.isExternalStorageManager()){

        return true;
    } else {
        return false;
    }
} else {
    int read= checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    int write=checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    return read== PackageManager.PERMISSION_GRANTED && write==PackageManager.PERMISSION_GRANTED;
}

    }




    private void takepermission(String[] permissions){
if(this_android_version>=Build.VERSION_CODES.R){
    try{
        //Intent i=new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        //i.addCategory(Intent.CATEGORY_DEFAULT);
        //i.setData(Uri.parse(String.format("package:%s",getApplicationContext(),getPackageName())));
        //startActivityForResult(i,100);

        Intent i=new Intent();
        i.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        startActivityForResult(i,100);
    } catch(Exception e){
        tost(e.toString());
    }


} else {
ActivityCompat.requestPermissions(this,permissions,102);
}
    }

    private void tost(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int request,int result,Intent i) {

        super.onActivityResult(request, result, i);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R && request==100){
          if(Environment.isExternalStorageManager()){
              tost("Storage Permission granted");
              permissiongranted=true;
          } else {
              tost("Storage Permission Denied");
              permissiongranted=false;
          }

        }

    }

    public void onRequestPermissionsResult(int request,String[] per,int[] results) {

        super.onRequestPermissionsResult(request, per, results);
        if(request==102 && results.length>0){
            for(int r:results){
                if(r==PackageManager.PERMISSION_GRANTED){

                } else {
                   tost("You have Rejected Some of the Permissions");
                    permissiongranted=false;
                    return;
                }

            }
            tost("All Storage Permission Granted");
           permissiongranted=true;
        }

    }


  public void click(View v){
       if(v==findViewById(R.id.img1)){
if(permissiongranted){
    Intent i=new Intent(MainActivity.this,SendActivity.class);
   startActivity(i);
} else {
    tost("All Permissions are not Granted");
    return;
}

       } else if(v==findViewById(R.id.img2)){
startActivity(new Intent(this,ReceiveActivity.class));



       }

  }
}