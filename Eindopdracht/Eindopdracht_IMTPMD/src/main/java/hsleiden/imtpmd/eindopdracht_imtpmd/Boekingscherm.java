package hsleiden.imtpmd.eindopdracht_imtpmd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Boekingscherm extends Activity
{
    //De buttons op het Hotelscherm
    Button BoekingButton;
    Button AnnuleerButton;

    //Strings voor het weergeven van hotelinformatie
    private String hotelinformatie, hotelsetter, hotelnaam;

    //Strings voor het kunnen invullen van persoonlijke gegevens
    private String naam, adres, telnr, email, postcode, woonplaats;

    //String voor het aantal tickets om de berekening
    //van het totaalbedrag toe te passen
	public String aantalTickets;

	SharedPreferences preferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boeking_layout);

        //De buttons worden actief gemaakt, d.m.v. het aanroepen van
        //diens methode
		buttonListener();

		//De preferences worden geladen en standaardwaarden toegekend aan de strings
		preferences = this.getSharedPreferences("gegevens", 0);
		this.naam = preferences.getString("Naam", " ");
		this.adres = preferences.getString("Adres", " ");
		this.postcode = preferences.getString("Postcode", " ");
		this.woonplaats = preferences.getString("Plaats", " ");
		this.telnr = preferences.getString("Telefoonnummer", " ");
		this.email = preferences.getString("Email", " ");
		this.aantalTickets = preferences.getString("Aantal", " ");
		this.hotelnaam = preferences.getString("Hotel", " ");

        //Methode voor het initieren van de strings
		setString();

		//Controle voor verbinding met internet
		final ConnectivityManager connectivity =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo network = connectivity.getActiveNetworkInfo();
		if (network != null && network.isConnected())
		{}
		else
		{
			//Het bericht wanneer er geen verbinding mogelijk is. De BoekingButton wordt op non-actief gezet.
			Toast.makeText(getApplicationContext(), "Er is geen verbinding met internet." + "\n" +  "Het boeken van hotels is niet mogelijk.", Toast.LENGTH_LONG).show();
			BoekingButton.setEnabled(false);
		}
	}
	
	//Alle string worden ingevuld met de bijbehorende waarden
	public void setString()
	{
        //De naam van de stad
        hotelsetter = String.valueOf(MainActivity.gekozenStad);
        TextView stadnaam = (TextView) findViewById(R.id.hotelsetter);
        stadnaam.setText(hotelsetter);

        //De naam van het hotel
        hotelnaam = String.valueOf(MainActivity.gekozenBestemming);
        TextView hotel = (TextView) findViewById(R.id.hotelnaam);
        hotel.setText(hotelnaam);

        //De beschrijving van het hotel
        hotelinformatie = String.valueOf(MainActivity.info);
        TextView hotelinfo = (TextView) findViewById(R.id.hotelinformatie);
        hotelinfo.setText(hotelinformatie);

        //Het aantal tickets inclusief een integer om een getal te maken van de string
        TextView aantal = (TextView) findViewById( R.id.info);
        aantal.setText( "Aantal tickets: " + this.aantalTickets);
        int ticketAantal = Integer.parseInt(this.aantalTickets);

        //prijs wordt berekend d.m.v. het opgegeven aantal uit Hotelscherm
        //en de prijswaarde vanuit MainActivity
        int prijs = Integer.parseInt(MainActivity.prijswaarde);

        //Het totaalbedrag wordt berekend aan de hand van
        //de prijswaarde uit MainActivity en aantalTickets uit Hotelscherm
        int totaalbedrag;
        totaalbedrag = ticketAantal * prijs;

        //Het totaalbedrag wordt ingevuld in de TextView
        TextView totaal = (TextView) findViewById(R.id.totaal);
        totaal.setText("" + totaalbedrag + " euro");

        //Alle tekstvelden voor persoonlijke informatie
        //Naam
		EditText naamEditTekst = (EditText) findViewById( R.id.naamedittext );
		naamEditTekst.setText(this.naam );

        //Adres
		EditText adresEditTekst = (EditText) findViewById( R.id.adresedittext );
		adresEditTekst.setText(this.adres );

        //Postcode
		EditText postcodeEditTekst = (EditText) findViewById( R.id.postcodedittext );
		postcodeEditTekst.setText(this.postcode );

        //Woonplaats
		EditText plaatsEditTekst = (EditText) findViewById( R.id.plaatsedittext );
		plaatsEditTekst.setText(this.woonplaats);

        //Telefoonnummer
		EditText telefoonEditTekst = (EditText) findViewById( R.id.telefoonedittext );
		telefoonEditTekst.setText(this.telnr);

        //E-mail
		EditText emailEditTekst = (EditText) findViewById( R.id.emailedittext );
		emailEditTekst.setText(this.email );
	}

	//De ingevoerde karakters worden veranderd naar een string
    //Dit wordt gebruikt voor het versturen naar de server
	public void setData()
	{
		EditText naamEditTekst = (EditText) this.findViewById(R.id.naamedittext);
		this.naam = naamEditTekst.getText().toString();

		EditText adresEditTekst = (EditText) this.findViewById(R.id.adresedittext);
		this.adres = adresEditTekst.getText().toString();

		EditText postcodeEditTekst = (EditText) this.findViewById( R.id.postcodedittext );
		this.postcode = postcodeEditTekst.getText().toString();

		EditText plaatsEditTekst = (EditText) this.findViewById( R.id.plaatsedittext );
		this.woonplaats = plaatsEditTekst.getText().toString();

		EditText telefoonEditTekst = (EditText) this.findViewById(R.id.telefoonedittext);
		this.telnr = telefoonEditTekst.getText().toString();

		EditText emailEditTekst = (EditText) this.findViewById(R.id.emailedittext);
		this.email = emailEditTekst.getText().toString();

        //De strings worden opgeslagen in de preferences
		Editor editor = preferences.edit();
		editor.putString("Naam", naam);
		editor.putString("Adres", adres);
		editor.putString("Postcode", postcode);
		editor.putString("Plaats", woonplaats);
		editor.putString("Telefoonnummer", telnr);
		editor.putString("Email", email);
		editor.commit();
	}

    //Deze methode geeft de 2 buttons functionaliteit mee
    //BoekingButton verstuurt alle verkregen informatie naar de server
	public void buttonListener()
	{
		final Context context = this;

		BoekingButton = (Button) findViewById(R.id.boekingbutton);
		BoekingButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
                //Methode setData() bevat de strings die toegekend worden
                //aan een JSONobject
				setData();
				JSONObject boeking = new JSONObject();
				JSONObject boekingsgegevens = new JSONObject();
				JSONObject klantgegevens = new JSONObject();
				JSONArray boekInfo = new JSONArray();
				 try
				 {
                     boekingsgegevens.put("ticketaantal", aantalTickets);
                     boekingsgegevens.put("hotelnaam", hotelnaam);

					 klantgegevens.put("kopernaam", naam);
					 klantgegevens.put("koperadres", adres + " " + postcode + " " + woonplaats);
					 klantgegevens.put("kopertelnr", telnr);
					 klantgegevens.put("koperemail", email);

                     //De objecten worden in een array gestopt
                     boekInfo.put(boekingsgegevens);
                     boekInfo.put(klantgegevens);

                     //boeking is het complete geheel alle boekingsinfo
                     boeking.put("Boeken", boekInfo);

				 }
				 catch (JSONException e)
				 {
				   // TODO Auto-generated catch block
				   e.printStackTrace();

				 }
				 //Alle informatie van boeking wordt naar de server verzonden
				 new ServerCommunicator(view.getContext(), "192.168.2.8", 4444, boeking.toString()).execute();

                //Het aanmaken van een dialog (voor een bevestiging)
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                //De titel van de dialog
                alertDialogBuilder.setTitle("Boeking is voltooid");

                //Het instellen van het bericht in de dialog
                alertDialogBuilder
                        .setMessage("Klik 'Ok' om terug te gaan naar het hoofdmenu")
                        .setCancelable(false)
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Na het klikken op 'Ok' zal de MainActivity weer worden geopend
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
		});
        //Het hotelscherm wordt weer geopend
		AnnuleerButton = (Button) findViewById(R.id.annuleerbutton);
		AnnuleerButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(context, Hotelscherm.class);
                startActivity(intent);
			}
		});
	}
}
