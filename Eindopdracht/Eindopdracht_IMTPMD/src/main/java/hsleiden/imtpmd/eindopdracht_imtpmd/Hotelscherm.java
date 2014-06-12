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
    int fake;

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

        //De buttons worden actief gemaakt, d.m.v. het aanroepen van
        //diens methode
        buttonListener();

        //De Bestemmingsnaam TextView met MainActivity.gekozenBestemming als waarde
        bestemmingnaam = String.valueOf(MainActivity.gekozenBestemming);
        TextView naam = (TextView) findViewById(R.id.bestemmingnaam);
        naam.setText(bestemmingnaam);

        //Het aantal sterren met MainActivity.sterren als waarde
        bestemmingsterren = String.valueOf(MainActivity.sterren);
        TextView sterren = (TextView) findViewById(R.id.sterren);
        sterren.setText(bestemmingsterren);

        //De hotel beschrijving met MainActivity.info als waarde
        bestemminginfo = String.valueOf(MainActivity.info);
        TextView info = (TextView) findViewById(R.id.info);
        info.setText(bestemminginfo);

        //De prijs van het hotel per ticket met MainActivity.prijs als waarde
        TextView prijs = (TextView) findViewById(R.id.prijs);
        prijs.setText(MainActivity.prijs);

        preferences = this.getSharedPreferences("gegevens", 0);
        this.aantalTickets = preferences.getString("Aantal", "");
        setString();
    }

    //De string voor het in te vullen aantal tickets wordt gevuld
    //met de public String aantalTickets
    public void setString()
    {
        EditText aantalEditTekst = (EditText) this.findViewById(R.id.aantalticketsedittekst);
        aantalEditTekst.setText(aantalTickets);
    }

    //Deze methode geeft de 2 buttons functionaliteit mee
    //BoekingButton opent het boekingscherm en voert de methode setAantal uit
    public void buttonListener()
    {
        BoekingButton = (Button) findViewById(R.id.boekingbutton);
        BoekingButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setAantal();
                Intent intent = new Intent(getApplicationContext(), Boekingscherm.class);
                startActivity(intent);
            }
        });

        AnnuleerButton = (Button) findViewById(R.id.annuleerbutton);
        AnnuleerButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                //als er op de annuleerknop wordt gedrukt wordt het hoofdscherm weer getoond
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setAantal()
    {
        //Het aantal tickets en de hotelnaam worden opgeslagen
        //Via het boekingscherm kunnen de 2 objecten worden opgehaald
        EditText aantalEditTekst = (EditText) this.findViewById(R.id.aantalticketsedittekst);
        this.aantalTickets = aantalEditTekst.getText().toString();

        Editor editor = preferences.edit();
        editor.putString("Aantal", aantalTickets);
        editor.putString("Hotel", MainActivity.gekozenBestemming);
        editor.commit();
    }
}