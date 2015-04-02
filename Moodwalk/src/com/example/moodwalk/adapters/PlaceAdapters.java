package com.example.moodwalk.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import com.example.moodwalk.R;
import com.example.moodwalk.activities.Place_details_activity;
import com.example.moodwalk.object.Place;

public class PlaceAdapters extends ArrayAdapter<Place>{
	
	Drawable default_logo;
	
	    public PlaceAdapters(Context context, ArrayList<Place> places, Drawable myDrawable) {
	       super(context, 0, places);
	       default_logo= myDrawable;
	       
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	          
	    	Place place = getItem(position);
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.place_row, parent, false);
	       }
	       // Lookup view for data population
	       TextView placeName = (TextView) convertView.findViewById(R.id.placeName);
	       TextView placeAdress = (TextView) convertView.findViewById(R.id.placeAdress);
	       TextView placeRating = (TextView) convertView.findViewById(R.id.placeRating);
	       TextView placeType = (TextView) convertView.findViewById(R.id.PlaceType);
	       TextView placeOpen = (TextView) convertView.findViewById(R.id.PlaceOpen);
	       
	       // Image Item
           Bitmap image;
           String encodedPic;
	       ImageView place_image = (ImageView) convertView.findViewById(R.id.imagePlace);
	       encodedPic=place.getPicture();
	       
	       if(encodedPic!=null){
	    	   Log.v("ENCODED PLACE PICTURE", "NON NULL");
	    	   image = decodeBase64(place.getPicture());
	    	   place_image.setImageBitmap(image);
	       }else{
	    	   place_image.setImageDrawable(default_logo);
	       }
	      
	       // Populate the data into the template view using the data object
	       placeName.setText(place.getName());
	       placeAdress.setText("Location : " + place.getVicinity());
	       placeRating.setText("Rating : " + place.getRating());
	       placeType.setText("Type : " + place.getCategory() );
	       placeOpen.setText("Open : " + place.getOpenNow());
	       
	       // Return the completed view to render on screen
	       return convertView;
	   }
	 // Method used to decode a string to a Bitmap
		public static Bitmap decodeBase64(String input) 
		{
		    byte[] decodedByte = Base64.decode(input, 0);
		    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
		}
	}

