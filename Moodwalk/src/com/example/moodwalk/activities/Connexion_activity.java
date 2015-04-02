package com.example.moodwalk.activities;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.moodwalk.fragments.MainFragment;


public class Connexion_activity extends FragmentActivity {
	
	private MainFragment mainFragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        mainFragment = new MainFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	        
	    } else {
	    	
	        // Or set the fragment from restored state info
	        mainFragment = (MainFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	        
	    }
	}
	
	public void Save(String User) throws FileNotFoundException{
		
	}
 
    }

