package com.example.moodwalk.activities;

import com.example.moodwalk.R;
import com.example.moodwalk.object.Place;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class Time_activity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//sets the main layout of the activity
		setContentView(R.layout.time_activity);
		
		//validate Button
		Button validate = (Button) findViewById(R.id.validate_button_time);
		validate.setBackgroundResource(R.drawable.button_selector);
		
		//get date
		Intent intent= getIntent();
		Bundle bdl = intent.getBundleExtra("place");
		Place place = bdl.getParcelable("place");
		Log.v("DATE", place.getAddress());
		
		//pass place arguments to profil
		
		
	}

}
