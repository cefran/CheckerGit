package com.example.moodwalk.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moodwalk.R;
import com.example.moodwalk.object.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDetails_fragment extends Fragment {

	public MapDetails_fragment(){}
	private static View view;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	        view = inflater.inflate(R.layout.fragment_map, container, false);
	    } catch (InflateException e) {
	        /* map is already there, just return view as it is */
	    }
	    MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
	    GoogleMap map = f.getMap();
	    
	    Place place = (Place) getArguments().getParcelable("place");
	    
	    Log.v("LATITUDE", place.getLatitude().toString());
	    onMapReady(map, place);
	    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatitude(), place.getLongitude()), 15.0f));
	    
	    
	    return view;
    }
	
	public void onMapReady(GoogleMap map, Place place) {

		
			map.addMarker(new MarkerOptions()
	        .position(new LatLng(place.getLatitude(), place.getLongitude()))
	        .title(place.getName()));
		
	}
	
}
