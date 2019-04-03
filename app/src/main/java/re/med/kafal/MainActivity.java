package re.med.kafal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    LayoutInflater li;
    ListView listView;
    BaseAdapter ba;
    ImageView falciresmi;
    ;
    TextView nametv;
    TextView falciismi, falcidetayi;
    SP sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = new SP(MainActivity.this);
        nametv    = (TextView) findViewById(R.id.name);
        listView  = (ListView) findViewById(R.id.list);

        nametv.setText("Sevgili " + sp.getName() + "cim istediğin bir falcı bacını seç lütfen canım");

        final String isimler[] = {"Çiğdem Bacı", "Ebru Bacı", "Melike Bacı"};
        final int resimler[] = {R.drawable.fal1, R.drawable.fal2, R.drawable.fal3};
        final String detay[] = {"Fincanın kalbini okur, isabetli yorumlar yapar",
                "Aşk ikili ve aile hayatın hakkında herşey onda",
                "İş hayatı, arkadaşlık için analiz yapar. "};

        ba = new BaseAdapter()
        {
            @Override
            public int getCount() {
                return isimler.length;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view = LayoutInflater.from(getApplicationContext()).
                        inflate(R.layout.row_list, viewGroup, false);
                falciismi = (TextView) view.findViewById(R.id.falciadi);
                falciresmi = (ImageView) view.findViewById(R.id.falciresimi);
                falcidetayi = (TextView) view.findViewById(R.id.falcidetayi);
                falcidetayi.setText(detay[i]);
                falciismi.setText(isimler[i]);
                falciresmi.setImageResource(resimler[i]);

                return view;
            }
        };

        listView.setAdapter(ba);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                String falciadi = null;
                if (i == 0)
                {
                    falciadi = "Çiğdem Baci";
                }
                else if (i == 1)
                {
                    falciadi = "Ebru Baci";
                }
                else if (i == 2)
                {
                    falciadi = "Melike Baci";
                }

                Toast.makeText(getApplicationContext(), falciadi, Toast.LENGTH_LONG).show();

                String user = getIntent().getStringExtra("user").toString();
                secim_kaydet(user, falciadi);

            }

            public void secim_kaydet(final String user, final String falci)
            {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        Intent in = new Intent(MainActivity.this, UploadPhoto.class);
                        startActivity(in);
                        Toast.makeText(getApplicationContext(), "Data gönderildi", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            Jsoup.connect("http://hizmetler.reeder.com.tr/med/insert_choice.php").data("user", sp.getName()).data("falci", falci).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
                }.execute();
            }
        });

    }


}
