package re.med.kafal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Pc on 9.05.2018.
 */


public class SP
{
    SharedPreferences sp; //preferences referansı
    SharedPreferences.Editor edit;  //preferences editor nesnesi referansı .prefernces nesnesine veri ekleyip cıkarmak için
    Context c;
    public SP(Context c)
    {
        this.c = c;
        sp = PreferenceManager.getDefaultSharedPreferences(c);//preferences objesi
        edit = sp.edit(); //aynı şekil editor nesnesi oluşturuluyor


    }

    //Burda sharedPreferencas üzerine kayıtlı login değerini alıyoruz.
    //Login değeri doğru giriş yapıldığında veya kayıt olduğunda true olarak kaydedilir
    //Amacı ise kullanıcı uygulamadan cıkarken direk çıkıs demeden cıktıysa yanı direk home veya back tusuyla uygulamadan çıktıysa
    //Geri geldiğinde tekrar giriş bilgilerini istemeden anasayfaya yönlendiriyoruz
    //Bu değer ancak anasayfa üzerinde cıkış butonuna basılırsa diğer bilgiler silinmeden bu değer false yapılır
    //ve uygulamaya tekrar girildiğinde kayıt olurken kullandığı şifre ve emaili ister



    public void reset()
    {
        edit.clear().commit();


    }

    public void setName(String name)
    {
        edit.putString("name",name).commit();
    }

    public String getName()
    {
        return sp.getString("name","");
    }

    public void setJob(String job)
    {
        edit.putString("job",job).commit();
    }

    public String getJob()
    {
        return sp.getString("job","");
    }


    public void setGender(String gender)
    {
        edit.putString("gender",gender).commit();
    }

    public String getGender()
    {
        return sp.getString("gender","");
    }

    public void setRelation(String relation)
    {
        edit.putString("relation",relation).commit();
    }

    public String getRelation()
    {
        return sp.getString("relation","");
    }
    public void setBirthdate(String birthdate)
    {
        edit.putString("birthdate",birthdate).commit();
    }

    public String getBirtdate()
    {
        return sp.getString("birthdate","");
    }
    public void clear()

    {
        edit.clear().commit();
    }

    public void setImage1(String name)
    {
        edit.putString("image",name).commit();
    }

    public String getImage1()
    {
        return sp.getString("image","");
    }


    public void setImage2(String name)
    {
        edit.putString("image2",name).commit();
    }

    public String getImage2()
    {
        return sp.getString("image2","");
    }


    public void setImage3(String name)
    {
        edit.putString("image3",name).commit();
    }

    public String getImage3()
    {

        return sp.getString("image3","");
    }



}