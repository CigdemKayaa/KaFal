package re.med.kafal;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Pc on 7.05.2018.
 */

public class UploadPhoto extends AppCompatActivity
{

    Button sendbtn;
    FileInputStream fis;
    SP sp ;


    ImageButton image1,image2,image3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo);
        image1 = (ImageButton) findViewById(R.id.image1);
        image2 = (ImageButton) findViewById(R.id.image2);
        image3 = (ImageButton) findViewById(R.id.image3);

        sp = new SP(UploadPhoto.this);
        sendbtn =(Button)findViewById(R.id.sendbtn);



        if (ContextCompat.checkSelfPermission(UploadPhoto.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UploadPhoto.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(UploadPhoto.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        if (ContextCompat.checkSelfPermission(UploadPhoto.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UploadPhoto.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(UploadPhoto.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 0);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Gelenler",sp.getImage1() + sp.getImage2() + sp.getImage3());

                if (sp.getImage1().equals("") && sp.getImage2().equals("") && sp.getImage3().equals(""))
                {
                    Toast.makeText(getApplicationContext()," Lütfen fotoğrafları yükleyiniz",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent in = new Intent(UploadPhoto.this, HomeActivity.class);
                    startActivity(in);

                }
            }
        });
    }





    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {

            case 0:
                if (resultCode == RESULT_OK) {
                    final Uri selectedImage = imageReturnedIntent.getData();
                    image1.setImageURI(selectedImage);

                    sp.setImage1("ok");
//                    sp.setProfilpicture(String.valueOf(selectedImage));
                    final String path = getRealPathFromURI(UploadPhoto.this, selectedImage);
                    final File imP = new File(Environment.getExternalStorageDirectory(), "resize.png");
                    try {

                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;

                        Bitmap b  = BitmapFactory.decodeFile(path,options);

                        Bitmap out = Bitmap.createScaledBitmap(b, 200, 200, false);

                        FileOutputStream fOut;
                        try {
                            fOut = new FileOutputStream(imP);
                            out.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                            fOut.flush();
                            fOut.close();
                            b.recycle();
                            out.recycle();
                        } catch (Exception e) {
                            Log.e("upload", "BITMAP EX :" + e.toString());

                            Toast.makeText(getApplicationContext(),"Fotoğraf boyutu yüksek", Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception eet) {
                        Log.e("upload", "IMG RESIZE ERR : " + eet);
                        Toast.makeText(getApplicationContext(),"Fotoğrafı kare olarak boyutlandırınız ", Toast.LENGTH_LONG).show();


                    }

                    Log.e("selected", path);

                    try {
                        new AsyncTask<String, String, String>() {

                            String jsonResponse;


                            int a;
                            ProgressDialog pd = new ProgressDialog(UploadPhoto.this);

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                pd.show();
                                pd.setMessage("Yükleniyor");
                            }

                            @Override
                            protected void onPostExecute(String s)

                            {
                                super.onPostExecute(s);
                                pd.cancel();


                            }

                            @Override
                            protected String doInBackground(String... params) {
                                try {
                                    Log.e("x", "Dosya Boyutu : " + imP.length());


                                    Connection con = Jsoup.connect("http://hizmetler.reeder.com.tr/med/dosyaYukle/dosyaYuklendi.php");

                                    con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*,q=0.8");
                                    con.header("Accept-Language", "en-US,en;q=0.5");
                                    con.header("Accept-Encoding", "gzip, deflate");
                                    fis = new FileInputStream(imP);

                                    con.data("name",sp.getName());
                                    con.data("dosya", "1111.jpeg", fis);

                                    con.data("tip","0");
                                    con.timeout(30000);
                                    con.method(Connection.Method.POST);
                                    jsonResponse = con.execute().parse().text();


                                } catch (IOException e) {
                                    e.printStackTrace();

                                    a=1;

                                    Log.e("x", "HATA HATA HATA : " + e.toString());
                                }

                                Log.e("f", imP.getAbsolutePath());


                                return null;
                            }
                        }.execute();


                    } catch(Exception e){

                        Log.e("e",e.toString());

                        e.printStackTrace();
                    }






        }

            case 1:
                if (resultCode == RESULT_OK) {
                    final Uri selectedImage = imageReturnedIntent.getData();
                    image2.setImageURI(selectedImage);

                    sp.setImage2("ok");

//                    sp.setProfilpicture(String.valueOf(selectedImage));
                    final String path = getRealPathFromURI(UploadPhoto.this, selectedImage);
                    final File imP = new File(Environment.getExternalStorageDirectory(), "resize.png");
                    try {

                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;

                        Bitmap b  = BitmapFactory.decodeFile(path,options);

                        Bitmap out = Bitmap.createScaledBitmap(b, 400, 400, false);

                        FileOutputStream fOut;
                        try {
                            fOut = new FileOutputStream(imP);
                            out.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                            fOut.flush();
                            fOut.close();
                            b.recycle();
                            out.recycle();
                        } catch (Exception e) {
                            Log.e("upload", "BITMAP EX :" + e.toString());

                            Toast.makeText(getApplicationContext(),"Fotoğraf boyutu yüksek", Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception eet) {
                        Log.e("upload", "IMG RESIZE ERR : " + eet);
                        Toast.makeText(getApplicationContext(),"Fotoğrafı kare olarak boyutlandırınız ", Toast.LENGTH_LONG).show();


                    }

                    Log.e("selected", path);

                    try {
                        new AsyncTask<String, String, String>() {

                            String jsonResponse;


                            int a;
                            ProgressDialog pd = new ProgressDialog(UploadPhoto.this);

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                pd.show();
                                pd.setMessage("Yükleniyor");
                            }

                            @Override
                            protected void onPostExecute(String s)

                            {
                                super.onPostExecute(s);
                                pd.cancel();


                            }

                            @Override
                            protected String doInBackground(String... params) {
                                try {
                                    Log.e("x", "Dosya Boyutu : " + imP.length());


                                    Connection con = Jsoup.connect("http://hizmetler.reeder.com.tr/med/dosyaYukle/dosyaYuklendi.php");

                                    con.userAgent("Mozilla");
                                    con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*,q=0.8");
                                    con.header("Accept-Language", "en-US,en;q=0.5");
                                    con.header("Accept-Encoding", "gzip, deflate");
                                    fis = new FileInputStream(imP);

                                    con.data("name",sp.getName());
                                    con.data("dosya", "1111.jpeg", fis);
                                    con.data("tip","1");

                                    con.timeout(30000);
                                    con.method(Connection.Method.POST);
                                    jsonResponse = con.execute().parse().text();


                                } catch (IOException e) {
                                    e.printStackTrace();

                                    a=1;

                                    Log.e("x", "HATA HATA HATA : " + e.toString());
                                }

                                Log.e("f", imP.getAbsolutePath());


                                return null;
                            }
                        }.execute();


                    } catch(Exception e){

                        Log.e("e",e.toString());

                        e.printStackTrace();
                    }




                }
                    //METHOD GELİCEK
                    break;




            case 2:
                if (resultCode == RESULT_OK) {
                    final Uri selectedImage = imageReturnedIntent.getData();
                    image3.setImageURI(selectedImage);
                    sp.setImage3("ok");
//                    sp.setProfilpicture(String.valueOf(selectedImage));
                    final String path = getRealPathFromURI(UploadPhoto.this, selectedImage);
                    final File imP = new File(Environment.getExternalStorageDirectory(), "resize.png");
                    try {

                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;

                        Bitmap b  = BitmapFactory.decodeFile(path,options);

                        Bitmap out = Bitmap.createScaledBitmap(b, 400, 400, false);

                        FileOutputStream fOut;
                        try {
                            fOut = new FileOutputStream(imP);
                            out.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                            fOut.flush();
                            fOut.close();
                            b.recycle();
                            out.recycle();
                        } catch (Exception e) {
                            Log.e("upload", "BITMAP EX :" + e.toString());

                            Toast.makeText(getApplicationContext(),"Fotoğraf boyutu yüksek", Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception eet) {
                        Log.e("upload", "IMG RESIZE ERR : " + eet);
                        Toast.makeText(getApplicationContext(),"Fotoğrafı kare olarak boyutlandırınız ", Toast.LENGTH_LONG).show();


                    }

                    Log.e("selected", path);

                    try {
                        new AsyncTask<String, String, String>() {

                            String jsonResponse;


                            int a;
                            ProgressDialog pd = new ProgressDialog(UploadPhoto.this);

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                pd.show();
                                pd.setMessage("Yükleniyor");
                            }

                            @Override
                            protected void onPostExecute(String s)

                            {
                                super.onPostExecute(s);
                                pd.cancel();


                            }

                            @Override
                            protected String doInBackground(String... params) {
                                try {
                                    Log.e("x", "Dosya Boyutu : " + imP.length());


                                    Connection con = Jsoup.connect("http://hizmetler.reeder.com.tr/med/dosyaYukle/dosyaYuklendi.php");

                                    con.userAgent("Mozilla");
                                    con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*,q=0.8");
                                    con.header("Accept-Language", "en-US,en;q=0.5");
                                    con.header("Accept-Encoding", "gzip, deflate");
                                    fis = new FileInputStream(imP);

                                    con.data("name",sp.getName());
                                    con.data("dosya", "1111.jpeg", fis);
                                    con.data("tip","2");

                                    con.timeout(30000);
                                    con.method(Connection.Method.POST);
                                    jsonResponse = con.execute().parse().text();


                                } catch (IOException e) {
                                    e.printStackTrace();

                                    a=1;

                                    Log.e("x", "HATA HATA HATA : " + e.toString());
                                }

                                Log.e("f", imP.getAbsolutePath());


                                return null;
                            }
                        }.execute();


                    } catch(Exception e){

                        Log.e("e",e.toString());

                        e.printStackTrace();
                    }




                }
                    break;


                }


    }







    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            Log.e("x", "Dosya Yolu : " + cursor.getString(column_index));
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
