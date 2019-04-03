package re.med.kafal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pc on 9.05.2018.
 */

public class HomeActivity extends AppCompatActivity 
{
    SP sp;

    AlarmManager alarmManager;
    List<String> imagelist;
    ListView list;
    ImageView iv1,iv2,iv3;
    BaseAdapter ba ;
    private           FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button oku = (Button)findViewById(R.id.oku);

        oku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        {


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        iv1=(ImageView) findViewById(R.id.iv1);

        iv2=(ImageView) findViewById(R.id.iv2);
        iv3=(ImageView) findViewById(R.id.iv3);


        list = (ListView) findViewById(R.id.list);

        imagelist= new ArrayList<String>();
        sp  = new SP(HomeActivity.this);


        falcek();

    }}
    public void falcek()
    {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);




                    String newimage1= "http://hizmetler.reeder.com.tr/med/dosyaYukle/"+imagelist.get(0).toString();
                    Picasso.get().load(newimage1).into(iv1);

                String newimage2= "http://hizmetler.reeder.com.tr/med/dosyaYukle/"+imagelist.get(1).toString();
                Picasso.get().load(newimage2).into(iv2);

                String newimage3= "http://hizmetler.reeder.com.tr/med/dosyaYukle/"+imagelist.get(2).toString();
                Picasso.get().load(newimage3).into(iv3);

                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent i = new Intent(HomeActivity.this,Okundu.class);
                PendingIntent pi = PendingIntent.getActivity(HomeActivity.this,0,i,0);

                alarmManager.set(AlarmManager.ELAPSED_REALTIME, 10000, pi);


            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    String response = Jsoup.connect("http://hizmetler.reeder.com.tr/med/display_fals.php").data("name",sp.getName()).get().text();

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        String image = jo.getString("image");
                        imagelist.add(image);

                    }
                    Log.e("response",response);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
