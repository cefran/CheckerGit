package com.example.moodwalk.fragments;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.moodwalk.R;
import com.example.moodwalk.activities.Place_details_activity;
import com.example.moodwalk.adapters.PlaceAdapters;
import com.example.moodwalk.object.Localisation;
import com.example.moodwalk.object.Place;




import com.google.android.gms.common.api.GoogleApiClient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;

public class GoogleResultFragment extends Fragment {
	
	public GoogleResultFragment(){}

	
	ArrayList<Place> venuesList;
	ArrayList<Localisation> resultLoc;
	Localisation localisationFinal;
	Localisation localisation2;
	DataSaving mCallback;

	//place_detail
	Place place_detail;
	
	//ListView
	ListView listPlace;
	
	//View
	View rootView;
	
	// the google key
	final String GOOGLE_KEY = "AIzaSyCJB6Mxx64wr3FdQ3S_12Hh77aZWJ1x9UA";
	
	//URL Google API
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String TYPE_DETAIL = "/details";
	private static final String OUT_JSON = "/json";
	private static final String PHOTO = "/photo?photoreference=";
	private static final String LOG_TAG = "ExampleApp";
	private static final String types = "night_club|bar|restaurant";
	
	//Longitude and lititude
	String latitude = "40.7463956";
	String longtitude = "-73.9852992";
	ArrayAdapter myAdapter;
	
	//Localisation object
	Localisation localisation = new Localisation();

//ChIJ65xzsc8EdkgR1CPqm1x5rsU
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Log.i("Right", "onCreate()");
	        //Current location Data from activity
	        mCallback.geoLocalisation();
		    longtitude= getArguments().getString("longitude");
		    latitude= getArguments().getString("latitude");
		    System.out.println(longtitude);
	    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_accueil, container, false);
		listPlace = (ListView) rootView.findViewById(R.id.list);
		
		AutoCompleteTextView autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompletePlace);
		
	    
	    googleplaces googlesplace = new googleplaces();
	    googlesplace.execute();
	    
	    autoCompView.setOnItemClickListener(new OnItemClickListener() {
	    	   @Override
	    	   public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
	    		   String str = (String) adapterView.getItemAtPosition(position);
	    		   localisationFinal = findLoc(resultLoc, str);
	    		   new latlong().execute();
	    		   new googleplaces().execute();
	    	   }
	    	   } );
	    
	    PlacesAutoCompleteAdapter autoAdap = new PlacesAutoCompleteAdapter(getActivity(), R.layout.auto_complete_place);
	    autoCompView.setAdapter(autoAdap);

	    listPlace.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				
				//get the place selected
				place_detail = venuesList.get(position);
				
				//add detail to the place object selected
				new Details(place_detail).execute();
//				Log.v("FROMATED", place.getAddress());
				
				//pass data and open new activity
			}
	    	
	    });
	    return rootView ;
	}
	
	public interface DataSaving {
        public void PlacesTargeted(ArrayList<Place> place);
        
        public void geoLocalisation();
    }
	 
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (DataSaving) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement OnHeadlineSelectedListener");
        }
    }
	
	private class googleplaces extends AsyncTask<Object, Object, String> {

		String temp;
		
		@Override
		protected void onPreExecute() {
			// we can start a progress bar here
		}

		protected void onPostExecute(String result) {
			
			Drawable d = getResources().getDrawable(R.drawable.default_logo);
			if (temp == null) {
				// we have an error to the call
				// we can also stop the progress bar
			} else {
				// all things went right
				
				// parse Google places search result
				Context context = getActivity().getApplicationContext();
				mCallback.PlacesTargeted(venuesList);
			
				//CustomAdapters for PlaceItems
				PlaceAdapters placeAdapters = new PlaceAdapters(context, venuesList, d);
				listPlace.setAdapter(placeAdapters);
				listPlace.setDivider(null);
			}
		}

		@Override
		protected String doInBackground(Object... params) {
			// make Call to the url
					    
			 
					    //call the Place API
						try {
							temp = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longtitude + "&radius=100&types="+URLEncoder.encode(types,"UTF-8")+"&sensor=true&key=" + GOOGLE_KEY);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						venuesList = (ArrayList<Place>) parseGoogleParse(temp);
						return "";
		}
		
		public ArrayList<Place> getListPlace(){
			return venuesList;
		}

	}
	
	private class latlong extends AsyncTask<Object, Object, String> {

		@Override
		protected String doInBackground(Object... params) {
			localisation2 = geoCode(localisationFinal);
			latitude = localisation2.getLat();
			longtitude = localisation2.getLng();
			
			return "";
		}
		
	}
	
	
	private class Details extends AsyncTask<Object, Object, String> {
		
		

		public Details(Place place){
		}
		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			
			addDetails(place_detail);
			Log.v("ASYNC", place_detail.getAddress());
			Log.v("TEST", place_detail.getId());
			Bundle bdl = new Bundle();
			Intent i = new Intent(getActivity(),Place_details_activity.class);
			bdl.putParcelable("place", place_detail);
			i.putExtra("place", bdl);
			startActivity(i);
			return null;
		}
		
	}
	private void addDetails(Place place){
		 HttpURLConnection conn = null;
		 String address ="";
		 String phone = "";
		 String web = "";
		 
		    StringBuilder jsonResults = new StringBuilder();
		    try {
		        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAIL + OUT_JSON);
		        sb.append("?placeid=" + place.getId());
		        sb.append("&key=" + GOOGLE_KEY);


		        URL url = new URL(sb.toString());
		        conn = (HttpURLConnection) url.openConnection();
		        InputStreamReader in = new InputStreamReader(conn.getInputStream());

		        // Load the results into a StringBuilder
		        int read;
		        char[] buff = new char[1024];
		        while ((read = in.read(buff)) != -1) {
		            jsonResults.append(buff, 0, read);
		        }
		    } catch (MalformedURLException e) {
		        Log.e(LOG_TAG, "Error processing Places API URL", e);
		        
		    } catch (IOException e) {
		        Log.e(LOG_TAG, "Error connecting to Places API", e);
		        
		    } finally {
		        if (conn != null) {
		            conn.disconnect();
		        }
		    }
		    
		    try {
		        // Create a JSON object hierarchy from the results
		        JSONObject jsonObj = new JSONObject(jsonResults.toString());
		        JSONObject result = jsonObj.getJSONObject("result");
		        
		        if(result.has("formatted_address")){
		        address = result.optString("formatted_address");
		        }
		        if(result.has("international_phone_number")){
		        phone = result.optString("international_phone_number");
		        }
		        if(result.has("website")){
		        web = result.optString("website");
		        }
		        if(result.has("opening_hours")){
		        	if(result.getJSONObject("opening_hours").has("weekday_text"))
		        	{
		        		
		        		JSONArray openingArray = result.getJSONObject("opening_hours").getJSONArray("weekday_text");
						for(int j = 0; j < openingArray.length(); j++) {
							Log.v("WEEKDAY", openingArray.getString(j));
							if(place.getWeekday()==null){
								place.setWeekday(openingArray.getString(j));
							}else{
								place.setWeekday(place.getWeekday() + "\n" + openingArray.getString(j));
							}
						}
						
		        	}
		        }
		        
		        place.setAddress(address);
		        place.setPhone(phone);
		        place.setWeb(web);

		        Log.v("ADDRESS", place.getAddress());
		        // Extract the Place descriptions from the results
		        
		    } catch (JSONException e) {
		        Log.e(LOG_TAG, "Cannot process JSON results", e);
		    }
		
	}
	
	private Localisation geoCode(Localisation loc)
	{
		
		
		 HttpURLConnection conn = null;
		    StringBuilder jsonResults = new StringBuilder();
		    try {
		        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAIL + OUT_JSON);
		        sb.append("?placeid=" + loc.getId());
		        sb.append("&key=" + GOOGLE_KEY);
		        

		        URL url = new URL(sb.toString());
		        conn = (HttpURLConnection) url.openConnection();
		        InputStreamReader in = new InputStreamReader(conn.getInputStream());

		        // Load the results into a StringBuilder
		        int read;
		        char[] buff = new char[1024];
		        while ((read = in.read(buff)) != -1) {
		            jsonResults.append(buff, 0, read);
		        }
		    } catch (MalformedURLException e) {
		        Log.e(LOG_TAG, "Error processing Places API URL", e);
		        return loc;
		    } catch (IOException e) {
		        Log.e(LOG_TAG, "Error connecting to Places API", e);
		        return loc;
		    } finally {
		        if (conn != null) {
		            conn.disconnect();
		        }
		    }
		    
		    try {
		        // Create a JSON object hierarchy from the results
		        JSONObject jsonObj = new JSONObject(jsonResults.toString());
		        JSONObject result = jsonObj.getJSONObject("result");
		        JSONObject geometry = result.getJSONObject("geometry");
		        JSONObject location = geometry.getJSONObject("location");
		        String lat = location.getString("lat");
		        String lon = location.getString("lng");
		        loc.setLat(lat);
		        loc.setLng(lon);
		        
		        Log.d("Description", loc.getDescription());
		        Log.d("id", loc.getId());
		        Log.d("lat", loc.getLat());
		        

		        // Extract the Place descriptions from the results
		        
		    } catch (JSONException e) {
		        Log.e(LOG_TAG, "Cannot process JSON results", e);
		    }

		    return loc;
	}
	
	private Localisation findLoc(ArrayList<Localisation> localisation, String name){
		Localisation loc = new Localisation();
		for(int g=0;g<localisation.size();g++){
			if(localisation.get(g).getDescription().equals(name)){
				loc = localisation.get(g);
			}
		}
		return loc;
	}
	private ArrayList<Localisation> autocomplete(String input) {
	    ArrayList<Localisation> resultLoc = null;
	    

	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	        sb.append("?input=" + URLEncoder.encode(input, "utf8"));
	        sb.append("&types=geocode");
	        sb.append("&language=en&sensor=true");
	        sb.append("&key=" + GOOGLE_KEY);
	        
	        

	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());

	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e(LOG_TAG, "Error processing Places API URL", e);
	        return resultLoc;
	    } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return resultLoc;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

	        // Extract the Place descriptions from the results
	        resultLoc = new ArrayList<Localisation>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	        	Localisation localisation = new Localisation();
	        	localisation.setDescription(predsJsonArray.getJSONObject(i).getString("description"));
	        	localisation.setId(predsJsonArray.getJSONObject(i).getString("place_id"));
	        
	            resultLoc.add(localisation);
	        }
	    } catch (JSONException e) {
	        Log.e(LOG_TAG, "Cannot process JSON results", e);
	    }

	    return resultLoc;
	}
	
	public static String makeCall(String url) {

		// string buffers the url
		StringBuffer buffer_string = new StringBuffer(url);
		String replyString = "";

		// instanciate an HttpClient
		HttpClient httpclient = new DefaultHttpClient();
		// instanciate an HttpGet
		HttpGet httpget = new HttpGet(buffer_string.toString());

		try {
			// get the responce of the httpclient execution of the url
			HttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();

			// buffer input stream the result
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			// the result as a string is ready for parsing
			replyString = new String(baf.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// trim the whitespaces
		return replyString.trim();
	}

	private static ArrayList<Place> parseGoogleParse(final String response) {

		ArrayList<Place> temp = new ArrayList();
		try {

			// make an jsonObject in order to parse the response
			JSONObject jsonObject = new JSONObject(response);

			// make an jsonObject in order to parse the response
			if (jsonObject.has("results")) {

				JSONArray jsonArray = jsonObject.getJSONArray("results");

				for (int i = 0; i < jsonArray.length(); i++) {
					Place poi = new Place();
					if (jsonArray.getJSONObject(i).has("name")) {
						poi.setName(jsonArray.getJSONObject(i).optString("name"));
						poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));

						if (jsonArray.getJSONObject(i).has("opening_hours")) {
							if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
								if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
									poi.setOpenNow("YES");
								} else {
									poi.setOpenNow("NO");
								}
							}
						} else {
							poi.setOpenNow("Not Known");
						}
						if (jsonArray.getJSONObject(i).has("types")) {
							JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

							for (int j = 0; j < typesArray.length(); j++) {
								poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
							}
						}
						if(jsonArray.getJSONObject(i).has("vicinity")) {
							poi.setVicinity(jsonArray.getJSONObject(i).optString("vicinity"));
						}
						if(jsonArray.getJSONObject(i).has("place_id")){
							poi.setId(jsonArray.getJSONObject(i).optString("place_id"));
						}
						if(jsonArray.getJSONObject(i).has("reference")){
							poi.setReference(jsonArray.getJSONObject(i).optString("reference"));
						}
						if(jsonArray.getJSONObject(i).has("photos")){
							JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("photos");

							for (int j = 0; j < typesArray.length(); j++) {
								if(typesArray.getJSONObject(j).has("photo_reference")){
									poi.setPicture(getPlacePic(typesArray.getJSONObject(j).optString("photo_reference")));
								}
							}
						}
						
						if(jsonArray.getJSONObject(i).has("geometry")){
							if(jsonArray.getJSONObject(i).getJSONObject("geometry").has("location")){
								
								poi.setLatitude((jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optDouble("lat")));
								poi.setLongitude((jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optDouble("lng")));
								
							}
						}
					}
					temp.add(poi);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		}
		return temp;

	}
	
	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	    

	    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
	        super(context, textViewResourceId);
	    }

	    @Override
	    public int getCount() {
	        return resultLoc.size();
	    }

	    @Override
	    public String getItem(int index) {
	        return resultLoc.get(index).getDescription();
	    }
	    
	    @Override
	    public Filter getFilter() {
	        Filter filter = new Filter() {
	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	            	
	            	
	                FilterResults filterResults = new FilterResults();
	                if (constraint != null) {
	                    // Retrieve the autocomplete results.
	                    resultLoc = autocomplete(constraint.toString());
	                   
	                    // Assign the data to the FilterResults
	                    filterResults.values = resultLoc;
	                    filterResults.count = resultLoc.size();
	                }
	                return filterResults;
	            }

	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	                if (results != null && results.count > 0) {

	                    notifyDataSetChanged();
	                }
	                else {
	                    notifyDataSetInvalidated();
	                }
	            }};
	            
	        return filter;
	    }
	}
	
	public static String getPlacePic(String userID) throws MalformedURLException {
	    Bitmap bitmap = null;
	    String encodedImage = null;
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    Log.d("TAG", "Loading Picture");
	    URL img_value = new URL("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+ userID +"&key=AIzaSyCJB6Mxx64wr3FdQ3S_12Hh77aZWJ1x9UA");
	    try {
	        bitmap = BitmapFactory.decodeStream((InputStream) img_value.openConnection().getInputStream());
	        if(bitmap!=null){
	        	Log.v("PLACE PICTURE", "NON NUL");
	        }else{
	        	Log.v("PLACE PICTURE", "NUL");
	        }
	    
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	        byte[] imageBytes = baos.toByteArray();
	        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
	     
	    } catch (Exception e) {
	        Log.d("TAG", "Loading Picture FAILED");
	        e.printStackTrace();
	    }
	    
	    return encodedImage;
	}
	
	}
