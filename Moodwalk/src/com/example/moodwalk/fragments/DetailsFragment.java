package com.example.moodwalk.fragments;

import java.util.ArrayList;

import com.example.moodwalk.R;
import com.example.moodwalk.object.Place;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsFragment extends Fragment{

	//View
		View rootView;
		
		PlaceData mCallback;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

		Place place = mCallback.placeDetailed();
	    
		if(place.getWeekday()!=null){
			Log.v("TEST OPENING", "Non null");
			rootView = inflater.inflate(R.layout.details_opening_fragment, container, false);
			TextView opening = (TextView) rootView.findViewById(R.id.opening_days);
			opening.setText(place.getWeekday());
		}else{
			Log.v("TEST OPENING", "null");
			rootView = inflater.inflate(R.layout.details_fragment, container, false);
		}
	    
	    ImageView image = (ImageView) rootView.findViewById(R.id.place_cover);
	    TextView adress = (TextView) rootView.findViewById(R.id.address_information);
	    TextView cell = (TextView) rootView.findViewById(R.id.Cell_information);
	    TextView web = (TextView) rootView.findViewById(R.id.web_information);
	    TextView title = (TextView) rootView.findViewById(R.id.place_title);
	    
		
		adress.setText("Address : " + place.getAddress());
		cell.setText("Cell : " + place.getPhone());
		web.setText("Website : " + place.getWeb());
		title.setText(place.getName());
		
		
		return rootView;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (PlaceData) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement OnHeadlineSelectedListener");
        }
    }
	
	//passing data to the activity
	public interface PlaceData {
        public Place placeDetailed();
    }
}
