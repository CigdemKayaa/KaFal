package re.med.kafal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Pc on 21.05.2018.
 */

public  class Okundu extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.okundu);
        Toast.makeText(getApplicationContext(),"Falınız okundu",Toast.LENGTH_LONG).show();



    }
}
