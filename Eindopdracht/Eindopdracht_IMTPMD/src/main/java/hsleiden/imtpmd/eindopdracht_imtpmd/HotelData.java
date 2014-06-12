package hsleiden.imtpmd.eindopdracht_imtpmd;

import java.util.HashMap;
import java.util.List;

public class HotelData
{
	//singleton
	private static HotelData _instance;
	public static HotelData getInstance()
	{
		if( _instance == null )
			_instance = new HotelData();

		return _instance;
	}

    static List<String> bestemminglijst;
    private static MainActivity activity;

	private String[] bestemmingLijst = {"Parijs", "Reykjavik", "Berlijn", "Amsterdam"};
	private HashMap<String, String[]> hotels;
	private HashMap<String, String> hotelInfo;

	private HotelData()
	{
		hotels = new HashMap<String,String[]>();
		hotelInfo = new HashMap<String,String>();

		String[] parijsHotels = { "Hotel Jules", "Hotel Jardin Des Plantes" };
		hotels.put("Parijs", parijsHotels );

		hotelInfo.put( "Hotel Jules", "4-sterren,150 euro,vlak naast het Louvre" );
		hotelInfo.put( "Hotel Jardin Des Plantes", "2-sterren,70 euro,tegenover de botanische tuinen" );


		String[] reykjavikHotels = { "Hotel Odinsve", "Hotel Bjork" };
		hotels.put("Reykjavik", reykjavikHotels );

		hotelInfo.put("Hotel Odinsve", "4-sterren,190 euro,gelegen aan het Odinstorg Square" );
		hotelInfo.put("Hotel Bjork", "3-sterren,120 euro,5 minuten lopen van het centrum" );


		String[] berlijnHotels = { "Derag Livinghotel", "Heart Of Gold Hostel" };
		hotels.put("Berlijn", berlijnHotels );

		hotelInfo.put("Derag Livinghotel", "4-sterren,200 euro,klassieke stijl met veel geschiedenis" );
		hotelInfo.put("Heart Of Gold Hostel", "1-ster,20 euro,Moderne eenvoud. Zeer goedkoop" );


		String[] amsterdamHotels = { "Western Apollo Museumhotel", "Hotel Krasnapolsky" };
		hotels.put("Amsterdam", amsterdamHotels );

		hotelInfo.put("Western Apollo Museumhotel", "3-sterren,140 euro,Midden in het centrum naast de Nationale Gallerij" );
		hotelInfo.put("Hotel Krasnapolsky", "5-sterren,260 euro,Aan de Dam tegenover het Koninklijk Palijs" );
	}

	public String[] getBestemmingLijst()
	{
		return bestemmingLijst;
	}

	public String[] getHotels( String bestemming )
	{
		return hotels.get( bestemming );
	}

	public String getHotelInfo( String hotelNaam )
	{
		return hotelInfo.get( hotelNaam );
	}
}
