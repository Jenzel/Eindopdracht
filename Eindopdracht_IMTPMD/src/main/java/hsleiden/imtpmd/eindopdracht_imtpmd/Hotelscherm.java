package hsleiden.imtpmd.eindopdracht_imtpmd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Hotelscherm extends Activity
{
    //De buttons op het Hotelscherm
    Button BoekingButton;
    Button AnnuleerButton;

    //Het aantal tickets dat bij het Boekingscherm scherm gebruikt
    //gaat worden, vandaar dat het een public String is
    public String aantalTickets;

    SharedPreferences preferences;

    //De strings die gevuld worden met informatie over het hotel
    private String bestemmingnaam, bestemmingsterren, bestemminginfo;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_layout);
    }
}