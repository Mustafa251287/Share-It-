package com.sharing.files;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Unbreakable.codes.Mustafacodes;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Random;


public class ReceiveActivity extends AppCompatActivity {
    ImageView imgv;
    Context con;
    ServerSocket ss;
    Socket s;
int QR_CODE_HIEGHT=500;
String Filename;
DataInputStream din;
TextView tv,filen;
LinearProgressIndicator lin;
String[] bundle;
long FileSize,Totalsend,percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#329fff")));

        imgv = findViewById(R.id.img);
        tv=findViewById(R.id.textView);
        filen=findViewById(R.id.Filenametv);
        lin=findViewById(R.id.Progessbar);
        filen.setVisibility(View.INVISIBLE);
        lin.setVisibility(View.INVISIBLE);

        con = getApplicationContext();
        getIpAddressAndGenerateQrCode();
    }

    public InetAddress getLocalAddress() {
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while (b.hasMoreElements()) {
                for (InterfaceAddress f : b.nextElement().getInterfaceAddresses()) {
                    if (f.getAddress().isSiteLocalAddress())
                        return f.getAddress();
                }

            }

        } catch (Exception e) {
            runtost(e.toString());
        }
        return null;
    }

    public void runtost(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(con, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void tost(String s){
        Toast.makeText(con,s,Toast.LENGTH_LONG).show();
    }

    public void getIpAddressAndGenerateQrCode() {

        new Thread() {
            public void run() {
                String str = getLocalAddress().toString();
                String ip=str.substring(1);

                int port=new Random().nextInt(9000);
if(port<1080){
    port+=1000;
}

                Bitmap test=StringToQrCode(new Mustafacodes().StringToCodes(ip+";"+port));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgv.setImageBitmap(test);
                    }
                });
                //String Filename;
                try {
                    ss = new ServerSocket(port);
                    s = ss.accept();
                } catch(Exception e) {
                   Intent i=new Intent(ReceiveActivity.this,ReceiveActivity.class);
                   startActivity(i);
                   finish();
                }

                    try {
                    din=new DataInputStream(s.getInputStream());
                    Filename=din.readUTF();
                    bundle=Filename.split(";");
                    Filename=bundle[0];
                    FileSize=Long.parseLong(bundle[1]);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imgv.setVisibility(View.INVISIBLE);
                            tv.setText("Plz Wait While Receiving File is In Progress");
                            filen.setText(Filename);
                            lin.setVisibility(View.VISIBLE);
                              filen.setVisibility(View.VISIBLE);
                        }
                    });


                } catch (Exception e){
                    runtost(e.toString());
                    Filename=null;
                }


                try{
                    FileOutputStream fout=new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/ShareIt/Documents",Filename));
                    byte[] b=new byte[1000000];
                    long i=0;
                    Totalsend=0;
                    while((i=din.read(b))!=-1){
                        fout.write(b,0,(int)i);
                        Totalsend=Totalsend+i;
                        percent=(Totalsend*100)/FileSize;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lin.setProgress((int)percent);
                            }
                        });

                    }
                    fout.close();
                    din.close();
                    s.close();
                    ss.close();
                    //runtost("File Successfully Received");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("File Successfully Received");
                        }
                    });
                } catch(Exception e){
                    runtost(e.toString());
                }


            }
        }.start();

    }

    public Bitmap StringToQrCode(String str){
        BitMatrix bitmatrix;
try{
    bitmatrix=new MultiFormatWriter().encode(str, BarcodeFormat.DATA_MATRIX.QR_CODE,QR_CODE_HIEGHT,QR_CODE_HIEGHT,null);

} catch(Exception e){
    runtost(e.toString());
    return null;
}
int height= bitmatrix.getHeight();
int width= bitmatrix.getWidth();
int[] pixels=new int[height * width];

for(int y=0;y<height;y++){
int offset=y*width;

for(int x=0;x<width;x++){
    pixels[offset+x]=bitmatrix.get(x,y)?getResources().getColor(R.color.black):getResources().getColor(R.color.white);
}

}
Bitmap bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_4444);
bitmap.setPixels(pixels,0,500,0,0,width,height);
return bitmap;
    }

}