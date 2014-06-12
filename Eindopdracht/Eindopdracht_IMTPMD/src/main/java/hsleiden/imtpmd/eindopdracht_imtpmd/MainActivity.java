package hsleiden.imtpmd.eindopdracht_imtpmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity
{
    static ArrayList<String> bestemminglijst;

    static ArrayList<HashMap<String, String>> hotelData;

    //Test
    public static MainActivity activity;
    private static String url = "145.101.82.118";
    public static String gekozenBestemming, value, prijs, sterren;

    //default naam voor tekstveld zolang er niet geklikt is
    public static String gekozenStad = "Parijs";

    public ListView list_categorie;
    int position;

    public SharedPreferences sharedPreferences;
    public int stadCategorie;
    public static String prijswaarde, info;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bestemminglijst = getBestemmingLijst();

        ArrayList<HashMap<String, String>> lijst = getHotelData(0);

        sharedPreferences = this.getSharedPreferences("", position);
        position = sharedPreferences.getInt("", position);

        list_categorie = getListView();

        ListAdapter adapter = new SimpleAdapter(this, lijst, R.layout.listview_item,
                new String[] {"hotelnaam"},
                new int[] {R.id.beknopteInfo});

        list_categorie.setAdapter(adapter);
        setListener();
    }

    //methode die het Hotelscherm opent en de juiste strings opslaat
    //voor verder gebruik
    private void setListener(){
        list_categorie.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = null;
                intent = new Intent(getApplicationContext(), Hotelscherm.class);
                startActivity(intent);
                value = list_categorie.getItemAtPosition(position).toString();
                String[] items = value.split("=");

                for(int i=0; i < items.length; i++){
                    System.out.println(items[i]);
                }

                gekozenBestemming = items[1];
                gekozenBestemming = gekozenBestemming.substring(0, gekozenBestemming.length() - 1);

                prijs = getPrijs().toString();
                prijs = prijs.substring(1, prijs.length() - 1);

                prijswaarde = String.valueOf(prijs.replaceAll("\\D", ""));

                sterren = getSterren().toString();
                sterren = sterren.substring(1, sterren.length() - 1);

                info = getInfo().toString();
                info = info.substring(1, info.length() - 1);
            }
        });
    }

    public void hotelCategorie(int soortBestemming){
        Editor editor = sharedPreferences.edit();
        stadCategorie = soortBestemming;

        editor.putInt("lijst", stadCategorie);
        editor.commit();

        ArrayList<HashMap<String, String>> lijst = getHotelData(stadCategorie);

        ListAdapter adapter = new SimpleAdapter(this, lijst, R.layout.listview_item,
                new String[] {"hotelnaam"},
                new int[] {R.id.beknopteInfo});

        list_categorie.setAdapter(adapter);
    }

    //Na het klikken op Parijs wordt de categorie op 0 ingesteld
    public void parijs(View view){
        gekozenStad = "Parijs";
        stadCategorie = 0;
        hotelCategorie(0);
    }

    //Na het klikken op Reykjavik wordt de categorie op 1 ingesteld
    public void reykjavik(View view){
        gekozenStad = "Reykjavik";
        stadCategorie = 1;
        hotelCategorie(1);
    }

    //Na het klikken op Berlijn wordt de categorie op 2 ingesteld
    public void berlijn(View view){
        gekozenStad = "Berlijn";
        stadCategorie = 2;
        hotelCategorie(2);
    }

    //Na het klikken op Amsterdam wordt de categorie op 3 ingesteld
    public void amsterdam(View view){
        gekozenStad = "Amsterdam";
        stadCategorie = 3;
        hotelCategorie(3);
    }

    //Methode die een arraylist van alle hotels bij de betreffende bestemming opvraagt
    public static ArrayList<HashMap<String, String>> getHotelData(int gekozenBestemming)
    {
        //nieuwe hashmap op de producten in op te slaan
        hotelData = new ArrayList<HashMap<String, String>>();

        System.out.println(gekozenBestemming);
        JSONObject bestemmingdataJObject = new JSONObject();
        try
        {
            bestemmingdataJObject.put("hotellijst", bestemminglijst.get(gekozenBestemming));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String response = null;
        try
        {
            try
            {
                //servercommunicator om verbinding te maken met de server
                response = new ServerCommunicator( activity, url, 4444, bestemmingdataJObject.toString()).execute().get();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        };

        String jsonFix = response.replace("null", "");

        JSONArray hotelJArray = null;
        try
        {
            hotelJArray = new JSONArray(jsonFix);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONObject bestemmingobject = null;
        ArrayList<HashMap<String, String>> hotellijst = new ArrayList<HashMap<String, String>>();

        for (int i = 0 ; i < hotelJArray.length(); i++)
        {
            try
            {
                bestemmingobject = hotelJArray.getJSONObject(i);

                //stop de producten in de productenlijst
                HashMap<String, String> bestemming = new HashMap<String, String>();
                bestemming = new HashMap<String,String>();
                bestemming.put("hotelnaam", bestemmingobject.getString("hotelnaam"));
//                bestemming.put("hotelprijs", bestemmingobject.getString("hotelprijs"));
//                bestemming.put("hotelsterren", bestemmingobject.getString("hotelsterren"));
                hotelData.add(bestemming);
                hotellijst.add(bestemming);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return hotellijst;
    }

    //De steden worden opgehaald om aan de buttons op de Main pagina toe te voegen
    static ArrayList<String> getBestemmingLijst()
    {
        bestemminglijst = new ArrayList<String>();

        //aanmaken van een nieuwe jsonobject
        JSONObject categorieJObject = new JSONObject();
        try
        {
            //verzenden van het jsonobject wat de server verwacht
            categorieJObject.put("bestemminglijst", "" );
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String response = null;
        try
        {
            try
            {
                //servercommunicator om verbinding te maken met de server
                response = new ServerCommunicator( activity, url, 4444, categorieJObject.toString()).execute().get();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }
        //om errors te voorkomen
        String jsonFix = response.replace("null", "");

        JSONArray categorieJArray = null;
        try
        {
            categorieJArray = new JSONArray(jsonFix);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONObject jObject = null;
        String value = null;
        bestemminglijst = new ArrayList<String>();

        for (int i = 0 ; i < categorieJArray.length(); i++)
        {
            try
            {
                jObject = (JSONObject) categorieJArray.getJSONObject(i);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                value = jObject.getString("bestemminglijst");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            bestemminglijst.add(value);
        }
        return bestemminglijst;
    }

    //Methode om informatie van de hotels op te vragen
    static ArrayList<String> getInfoLijst()
    {
        ArrayList bestemmingInfoLijst = new ArrayList<String>();

        //aanmaken van een nieuw jsonobject
        JSONObject categorieJObject = new JSONObject();
        try
        {
            //verzenden van het jsonobject
            categorieJObject.put("hotelinfo", gekozenBestemming );
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String response = null;
        try
        {
            try
            {
                //servercommunicator proberen te verbinden met de server
                response = new ServerCommunicator( activity, url, 4444, categorieJObject.toString()).execute().get();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }
        //om errors te voorkomen
        String jsonFix = response.replace("null", "");

        JSONArray soortenBestemmingLijstJA = null;
        try
        {
            soortenBestemmingLijstJA = new JSONArray(jsonFix);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONObject jObject = null;
        String info = null;
        String hotelprijs = null;
        String sterren = null;


        for (int i = 0 ; i < soortenBestemmingLijstJA.length(); i++)
        {
            try
            {
                jObject = (JSONObject) soortenBestemmingLijstJA.getJSONObject(i);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {

                hotelprijs = jObject.getString("hotelprijs");
                info = jObject.getString("hotelinfo");
                sterren = jObject.getString("hotelsterren");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            bestemmingInfoLijst.add(hotelprijs);

            bestemmingInfoLijst.add(info);

            bestemmingInfoLijst.add(sterren);

        }
        //geef de lijst met bestemmingen terug
        return bestemmingInfoLijst;
    }

    //Methode om de prijzen van de hotels op te vragen
    static ArrayList<String> getPrijs()
    {
        ArrayList bestemmingInfoLijst = new ArrayList<String>();

        //aanmaken van een nieuw jsonobject
        JSONObject categorieJObject = new JSONObject();
        try
        {
            //verzenden van het jsonobject
            categorieJObject.put("hotelinfo", gekozenBestemming );
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String response = null;
        try
        {
            try
            {
                //servercommunicator proberen te verbinden met de server
                response = new ServerCommunicator( activity, url, 4444, categorieJObject.toString()).execute().get();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }
        //om errors te voorkomen
        String jsonFix = response.replace("null", "");

        JSONArray soortenBestemmingLijstJA = null;
        try
        {
            soortenBestemmingLijstJA = new JSONArray(jsonFix);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONObject jObject = null;
        String hotelprijs = null;

        for (int i = 0 ; i < soortenBestemmingLijstJA.length(); i++)
        {
            try
            {
                jObject = (JSONObject) soortenBestemmingLijstJA.getJSONObject(i);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                hotelprijs = jObject.getString("hotelprijs");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            bestemmingInfoLijst.add(hotelprijs);
        }
        System.out.println(bestemmingInfoLijst);
        //geef de lijst met bestemmingen terug
        return bestemmingInfoLijst;
    }

    //Methode om het aantal sterren per hotel op te vragen
    static ArrayList<String> getSterren()
    {
        ArrayList bestemmingInfoLijst = new ArrayList<String>();

        //aanmaken van een nieuw jsonobject
        JSONObject categorieJObject = new JSONObject();
        try
        {
            //verzenden van het jsonobject
            categorieJObject.put("hotelinfo", gekozenBestemming );
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String response = null;
        try
        {
            try
            {
                //servercommunicator proberen te verbinden met de server
                response = new ServerCommunicator( activity, url, 4444, categorieJObject.toString()).execute().get();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }
        //om errors te voorkomen
        String jsonFix = response.replace("null", "");

        JSONArray soortenBestemmingLijstJA = null;
        try
        {
            soortenBestemmingLijstJA = new JSONArray(jsonFix);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONObject jObject = null;
        String hotelsterren = null;

        for (int i = 0 ; i < soortenBestemmingLijstJA.length(); i++)
        {
            try
            {
                jObject = (JSONObject) soortenBestemmingLijstJA.getJSONObject(i);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                hotelsterren = jObject.getString("hotelsterren");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            bestemmingInfoLijst.add(hotelsterren);
        }
        //geef de lijst met bestemmingen terug
        return bestemmingInfoLijst;
    }

    //Methode om de beschrijving per hotel op te vragen
    static ArrayList<String> getInfo()
    {
        ArrayList bestemmingInfoLijst = new ArrayList<String>();

        //aanmaken van een nieuw jsonobject
        JSONObject categorieJObject = new JSONObject();
        try
        {
            //verzenden van het jsonobject
            categorieJObject.put("hotelinfo", gekozenBestemming );
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        String response = null;
        try
        {
            try
            {
                //servercommunicator proberen te verbinden met de server
                response = new ServerCommunicator( activity, url, 4444, categorieJObject.toString()).execute().get();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }
        //om errors te voorkomen
        String jsonFix = response.replace("null", "");

        JSONArray soortenBestemmingLijstJA = null;
        try
        {
            soortenBestemmingLijstJA = new JSONArray(jsonFix);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONObject jObject = null;
        String hotelinfo = null;

        for (int i = 0 ; i < soortenBestemmingLijstJA.length(); i++)
        {
            try
            {
                jObject = (JSONObject) soortenBestemmingLijstJA.getJSONObject(i);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                hotelinfo = jObject.getString("hotelinfo");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            bestemmingInfoLijst.add(hotelinfo);
        }
        //geef de lijst met bestemmingen terug
        return bestemmingInfoLijst;
    }
}