package com.example.moodwalk.services;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class LocationListenerPerso implements LocationListener {
	
	Context context;
	static double longitude;
	public static double getLongitude() {
		return longitude;
	}
	public static void setLongitude(double longitude) {
		LocationListenerPerso.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double latitude;

	public LocationListenerPerso(Context context){
	
	this.context = context;
	}
	@Override
	public void onLocationChanged(Location loc) {
		// TODO Auto-generated method stub
		
		
//		loc.getLatitude();
//		loc.getLongitude();
//		
		longitude = loc.getLongitude();
		latitude = loc.getLatitude();
		
//		
//		String Text = "My current location is: " +
//
//		"Latitud = " + loc.getLatitude() +
//		"Longitud = " + loc.getLongitude();
//
//		Toast.makeText( context,
//		Text,
//		Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText( context,
				"Gps Enabled",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText( context,
				"Gps Disabled",
				Toast.LENGTH_SHORT ).show();
	}
	
	

	
 
}
