package com.example.moodwalk.fragments;

import java.util.ArrayList;

import com.example.moodwalk.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moodwalk.object.Place;


public class Map_Fragment extends Fragment {
	
	public Map_Fragment(){}
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
	    
	    ArrayList<Place> places = (ArrayList<Place>) getArguments().getSerializable("place");
	    onMapReady(map, places);
	    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(places.get(0).getLatitude(), places.get(0).getLongitude()), 15.0f));
	    
	    
	    return view;
    }
	
	public void onMapReady(GoogleMap map, ArrayList<Place> places) {

		for(int i=0; i<places.size();i++){
			map.addMarker(new MarkerOptions()
	        .position(new LatLng(places.get(i).getLatitude(), places.get(i).getLongitude()))
	        .title(places.get(i).getName()));
		}
	}
	
}
