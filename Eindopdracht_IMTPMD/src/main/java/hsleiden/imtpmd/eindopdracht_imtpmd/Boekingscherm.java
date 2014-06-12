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
	}
}
