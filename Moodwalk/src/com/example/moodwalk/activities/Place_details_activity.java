package com.example.moodwalk.activities;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.moodwalk.R;
import com.example.moodwalk.fragments.DetailsFragment;
import com.example.moodwalk.fragments.DetailsFragment.PlaceData;
import com.example.moodwalk.fragments.CommunityFragment;
import com.example.moodwalk.fragments.GoogleResultFragment;
import com.example.moodwalk.fragments.MainFragment;
import com.example.moodwalk.fragments.MapDetails_fragment;
import com.example.moodwalk.fragments.PagesFragment;
import com.example.moodwalk.fragments.ProfilFragment;
import com.example.moodwalk.fragments.WhatsHotFragment;
import com.example.moodwalk.object.Place;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Place_details_activity extends FragmentActivity implements PlaceData{
	Place place ;
	Bitmap cover;
	Bundle args;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Intent intent= getIntent();
		Bundle bdl = intent.getBundleExtra("place");
		Place place = bdl.getParcelable("place");
		
		args = new Bundle();
        args.putParcelable("place", place);
        
	    setContentView(R.layout.place_details_activity);
	    
	    //let'sgo button
	    Button letsgo = (Button) findViewById(R.id.go_button);
	    letsgo.setBackgroundResource(R.drawable.button_selector);
	    
	    letsgo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Date_activity.class);
				i.putExtra("place", args);
				startActivity(i);		
				
			}
	    	
	    });
	    //ImageView
	    ImageView picture = (ImageView) findViewById(R.id.place_cover);
	    
	    if(place.getPicture()!=null){	
	    	picture.setImageBitmap(decodeBase64(place.getPicture()));
	    }
	    
	    
	    //display view on buttons pressed
	    Button detail = (Button) findViewById(R.id.detail_button);
	    Button map = (Button) findViewById(R.id.map_button);
	    
	    detail.setBackgroundResource(R.drawable.button_selector);
	    detail.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				displayView(1);	
			}	
	    });
	    
	    map.setBackgroundResource(R.drawable.button_selector);
	    map.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				displayView(2);
			}
	    });
	    
	    if (savedInstanceState == null) {
	    	displayView(1);
		}
	}

	@Override
	public Place placeDetailed() {
		// TODO Auto-generated method stub
		Intent intent= getIntent();
		Bundle bdl = intent.getBundleExtra("place");
		Place place = bdl.getParcelable("place");
		
		return place;
	}
	
	public static Bitmap decodeBase64(String input) 
	{
	    byte[] decodedByte = Base64.decode(input, 0);
	    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
	}
	
	private void displayView(int position) {
		
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 1:
			fragment = new DetailsFragment();
			break;
		case 2:
			fragment = new MapDetails_fragment();
			fragment.setArguments(args);
			break;
		default:
			break;
		}
    	FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_detail_container, fragment)
				.addToBackStack(null)
				.commit();
	}
}
