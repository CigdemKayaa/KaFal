package re.med.kafal;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Pc on 25.04.2018.
 */

public class LoginActivity extends AppCompatActivity

{
    SP sp;
    EditText usernameEt;
    Spinner relationSp, jobSp;
    Button datePickerBtn, btnOk;
    RadioGroup genderRg;
    String date;
    int idx;
    String name, relation, job, gender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEt = (EditText) findViewById(R.id.usernameEt);
        relationSp = (Spinner) findViewById(R.id.relationSpinner);
        jobSp = (Spinner) findViewById(R.id.jobSpinner);
        datePickerBtn = (Button) findViewById(R.id.btnDatePicker);
        btnOk = (Button) findViewById(R.id.btnOK);
        genderRg = (RadioGroup) findViewById(R.id.genderRadioGroup);
        String jobstatus[] = {"Seçim Yapınız", "Çalışmıyor", "Çalışıyor", "Öğrenci", "İş Arıyor"};
        String relationstatus[] = {"Seçim Yapınız", "İlişkisi yok", "İlişkisi var", "Nişanlı", "Evli", "Dul", "Karışık", "Flörtte", "Platonik", "Mucize Bekliyor"};
         sp = new SP(LoginActivity.this);

         if (!sp.getName().equals(""))
         {
             Intent in = new Intent(LoginActivity.this,MainActivity.class);
             in.putExtra("user",sp.getName());
             startActivity(in);
         }

         //android.R.layout.spinner...

        ArrayAdapter adapter =
                new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.text, jobstatus);

        ArrayAdapter
                adapter1 = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.text, relationstatus);

        jobSp.setAdapter(adapter);
        relationSp.setAdapter(adapter1);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (jobSp.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Lütfen Mesleğinizi Seçiniz", Toast.LENGTH_LONG).show();
                }
                if (relationSp.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Lütfen İlişki durumu Seçiniz", Toast.LENGTH_LONG).show();

                }
                name = usernameEt.getText().toString();


                if (datePickerBtn.getText().equals("Seç"))

                {
                    Toast.makeText(getApplicationContext(),"Bir Tarih seçiniz",Toast.LENGTH_LONG).show();
                }

                if (name.equals("")) {
                    usernameEt.setError("İsim zorunludur");
                }

                else
                {
                    kullanici_kaydet();

                }

            }
        });


        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button chooseBtn;
                final DatePicker datePicker;
                final Dialog dialog = new Dialog(LoginActivity.this); // Context, this, etc.
                dialog.setContentView(R.layout.dialog_demo);

                chooseBtn =(Button) dialog.findViewById(R.id.chooseBtn);

                datePicker = (DatePicker) dialog.findViewById(R.id.dp);

                chooseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final int year =datePicker.getYear();
                        final int month = datePicker.getMonth();
                        final int day = datePicker.getDayOfMonth();
                         date = year+"-"+month+"-"+day;

                         Log.e("date",date);
                        Toast.makeText(getApplicationContext(),"Bu tarihi seçtiniz"+date,Toast.LENGTH_LONG).show();

                        dialog.dismiss();
                        datePickerBtn.setText(date);
                    }
                });





                dialog.setTitle("Tarih seçiniz");
                dialog.show();

            }
        });


    }

    public void kullanici_kaydet()
    {

        int radioButtonID = genderRg.getCheckedRadioButtonId();
        View radioButton = genderRg.findViewById(radioButtonID);
          idx = genderRg.indexOfChild(radioButton);


        name = usernameEt.getText().toString();

        relation = relationSp.getSelectedItem().toString();
        job = jobSp.getSelectedItem().toString();

             if (idx == 0)

                 {
                    gender = "Kadın";
                }
                if (idx == 1)
                {
                    gender = "Erkek";
                }
                if (idx == -1)
                {
                    Toast.makeText(getApplicationContext(), "Cinsiyet seçmek zorunludur", Toast.LENGTH_SHORT).show();
                }




        new AsyncTask<String, String, String>() {
             int error;

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (error==1)
                {
                    Toast.makeText(getApplicationContext(),"Data Gönderilemedi",Toast.LENGTH_LONG).show();

                }
                else
                {
                    sp.setName(name);
                    sp.setGender(gender);
                    sp.setRelation(relation);
                    sp.setBirthdate(datePickerBtn.getText().toString());
                    sp.setJob(job);

                    Toast.makeText(getApplicationContext(), "Data Gönderildi", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(LoginActivity.this,MainActivity.class);

                    in.putExtra("user",name);
                    startActivity(in);

                }   }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


                Log.e("data-controlü","name "+ name + " job"+ job + " relation "+ relation + " gender"+ gender +"date" + date);
            }

            @Override
            protected String doInBackground(String... strings) {

                //$query2 =  mysqli_query($cnn, "INSERT INTO user (name,job,relation,birthdate,gender,create_date) VALUES('".$name ."','".$job."','".$relation."','".$birthdate."','".$gender."','".$bugun."')");

                try
                {
                    Jsoup.connect("http://hizmetler.reeder.com.tr/med/register_user_fal.php").data("name",name).data("job",job).data("relation",relation).data("birhdate",date).data("gender",gender).get();
                }
                catch (IOException e)
                {

                    error=1;
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }


}
