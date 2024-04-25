package com.sharing.files;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.Unbreakable.codes.Mustafacodes;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SendActivity extends AppCompatActivity {
Context con;
    AlertDialog.Builder alertbuilder;
    AlertDialog alert;
    ArrayList<String> arrayList;
    ArrayAdapter<String> aa;
    File[] filesarray;
    String path;
    String temppath;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        try {
            ActionBar ab=getSupportActionBar();
            con=getApplicationContext();
            ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303030")));

            lv=findViewById(R.id.list);
            arrayList=new ArrayList<String>();
             con=getApplicationContext();
            getFileName();
        } catch (Exception e){
            tost(e.toString());
        }


    }

    public void getFileName(){
        new Thread(){
            public void run(){
                path= Environment.getExternalStorageDirectory().toString();
createdialog();
                filesarray=new File(path).listFiles();

                for(File f:filesarray){
                    arrayList.add(f.getName());
                }
                aa=new ArrayAdapter<String>(con,R.layout.custom,arrayList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            lv.setAdapter(aa);
                        } catch (Exception e){
                            tost(e.toString());
                        }

                    }
                });
            }
        }.start();

  lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
          new Thread(){
              public void run(){
                  String filename=(String)adapterView.getItemAtPosition(i);
                  temppath=path+"/"+filename;

                      File test=new File(temppath);
                      if(test.isDirectory()){
                          path=temppath;
                          for(File ft:filesarray){
                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      aa.remove(ft.getName());
                                  }
                              });
                          }

                         filesarray=test.listFiles();

                          for (File ft:filesarray) {
                              runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      aa.add(ft.getName());
                                  }
                              });
                          }

                      } else {
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {

                                  alert.show();
                              }
                          });



                      }



              }
          }.start();





      }
  });
    }



    public void tost(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    public void runaabletost(final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tost(str);
            }
        });
    }

@Override
public void onBackPressed(){
if(path.equals(Environment.getExternalStorageDirectory().toString())){
    try{
        startActivity(new Intent(this,MainActivity.class));
        finish();
    } catch (Exception e){
       tost(e.toString());
    }

} else {
    new Thread(){
        public void run(){
            try{
                File ft=new File(path).getParentFile();
                path=ft.getPath();
                //runaabletost("Externel storage dir path"+Environment.getExternalStorageDirectory().toString()+": Normal file path "+path);
                for(File fa:filesarray){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            aa.remove(fa.getName());
                        }
                    });
                }
                filesarray=ft.listFiles();

                for(File fb:filesarray){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            aa.add(fb.getName());
                        }
                    });
                }
            } catch (Exception e){
                runaabletost(e.toString());
            }



        }
    }.start();



}

    }

    public void createdialog(){
        alertbuilder=new AlertDialog.Builder(SendActivity.this);
        alertbuilder.setMessage("You Want to Send This File?");
        alertbuilder.setCancelable(false);
        alertbuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           dialogInterface.cancel();
           int per=checkCallingOrSelfPermission(Manifest.permission.CAMERA);
           if(per!=0){
               ActivityCompat.requestPermissions(SendActivity.this,new String[]{Manifest.permission.CAMERA},101);
           }
           scancode();
            }
        });
        alertbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertbuilder.setTitle("Dont Take Me As Joke!");
        try{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert=alertbuilder.create();

                }
            });

        } catch (Exception e){
            runaabletost(e.toString());
        }


    }

    public void scancode(){


        ScanOptions options=new ScanOptions();
        options.setPrompt("Scan The Receiver's QR-Code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(capture.class);
        res.launch(options);
    }

    ActivityResultLauncher<ScanOptions> res=registerForActivityResult(new ScanContract(),result -> {
        if(result.getContents()!=null){


            new Thread(){
                public void start(){
                    String[] test=temppath.split("/");
                    String res= new Mustafacodes().CodeToString(result.getContents());
                    Intent i=new Intent(SendActivity.this,SendingActivity.class);
                    res=res+";"+test[test.length-1]+";"+temppath;
                    i.putExtra("alldata",res);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                      startActivity(i);
                        }
                    });
                }
            }.start();


        }
    });


}