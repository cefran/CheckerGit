package com.example.moodwalk.activities;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.moodwalk.R;
import com.example.moodwalk.object.Place;
import com.example.moodwalk.object.User;
import com.example.moodwalk.adapters.NavDrawerListAdapter;
import com.example.moodwalk.fragments.CommunityFragment;
import com.example.moodwalk.fragments.GoogleResultFragment;
import com.example.moodwalk.fragments.Map_Fragment;
import com.example.moodwalk.fragments.PagesFragment;
import com.example.moodwalk.fragments.ProfilFragment;
import com.example.moodwalk.fragments.WhatsHotFragment;
import com.example.moodwalk.object.NavDrawerItem;
import com.example.moodwalk.services.LocationListenerPerso;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;

public class MainActivity extends ActionBarActivity implements GoogleResultFragment.DataSaving, ProfilFragment.UserSaving{

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private Boolean flag=false;
	private ActionBarDrawerToggle mDrawerToggle;
	private GoogleApiClient mGoogleApiClient;
	
	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private double longitude;
	private double latitude;
	User user = new User();
	
	//Fragment object
	Fragment map_fragment = new Map_Fragment();
	Fragment list_place = new GoogleResultFragment();
	Fragment profil_fragment = new ProfilFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//To desactivate the print window
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		//local variables
		
		Bitmap profile_picture;
		
		setContentView(R.layout.activity_main);
		
		Intent intent= getIntent();
		Bundle bdl = intent.getBundleExtra("user1");
		if(bdl!=null){
			Log.v("BUNDLE", "NON NULL");
		}else{
			Log.v("BUNDLE", "NULL");
		}
		
		user = bdl.getParcelable("user");
		
		if(user==null)
		{
			Log.v("TEST USER", "NULL");
		}else{
			Log.v("TEST USER", user.getFirst_name());
		}
		
		//Pass User object to ProfilFragment
		Bundle args = new Bundle();
        args.putParcelable("user", user);
	    profil_fragment.setArguments(args);
		

		
		//profile picture, Facebook NavDrawer
		
		profile_picture = decodeBase64(user.getProfil_picture());
		profile_picture = profile_picture.createScaledBitmap(profile_picture, 78, 78, false);
		
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		
		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		NavDrawerItem profil = new NavDrawerItem(navMenuTitles[0], profile_picture);
		
		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Profil
		navDrawerItems.add(profil);
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Map
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			
			//When Drawer is closed
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				flag=false;
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			//When Drawer is Open
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				flag=true;
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(1);
		}
		
	}

	
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		String tag = "";
		
		
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = profil_fragment;
			tag="profil";
			break;
		case 1:
			fragment = list_place;
			Bundle args = new Bundle();
			if((GoogleResultFragment)getFragmentManager().findFragmentByTag("listPlace")==null){
				fragment.setArguments(args);
			}
			tag="listPlace";
			break;
		case 2:
			fragment = map_fragment;
			tag="map";
			break;
		case 3:
			fragment = new CommunityFragment();
			break;
		case 4:
			fragment = new PagesFragment();
			break;
		case 5:
			fragment = new WhatsHotFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			Fragment frg = getFragmentManager().findFragmentByTag(tag);
			
			//Add to BackStack if first instance 
			if(frg==null){
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment, tag)
					.addToBackStack(tag)
					.commit();
			//Get the backstack instance 
			}else{
				getFragmentManager().beginTransaction()
				.replace(R.id.frame_container, frg)
				.commit();
				
			}

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	

	//Method used to convert JSON to User Object
	public User getUser() throws JSONException{
		
		User user = new User();
		String user_JSON = getIntent().getStringExtra("user");
		
		
		if(user_JSON == null){
			Log.v("test", "no String");
		}
		else
		{
			Log.v("test", user_JSON);
		}	
		user = user.toUser(user_JSON);
		return user;
	
	}

	// Method used to decode a string to a Bitmap
	public static Bitmap decodeBase64(String input) 
	{
	    byte[] decodedByte = Base64.decode(input, 0);
	    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
	}

	
	//Method used to pass place data to the Map fragment
	@Override
	public void PlacesTargeted(ArrayList<Place> place) {
		// TODO Auto-generated method stub
		  
		  Map_Fragment mapFragment = (Map_Fragment)getFragmentManager().findFragmentByTag("map");
		 
		  //Bundle
		  Bundle args = new Bundle();
          args.putSerializable("place", place);
          
		  if(mapFragment==null)
		  {
	          map_fragment.setArguments(args);
	          
		  }else{
			 Bundle bdl = mapFragment.getArguments();
			 bdl.putSerializable("place", place);
		  }       
              
	}
	
	
	//Method used to pass geolocalisation data from Activity to GoogleResult Fragment
	@Override
	public void geoLocalisation() {
	
		
		Location location = getLocation();
		
		longitude = location.getLongitude();
		latitude = location.getLatitude();
		GoogleResultFragment GooglePlaceFragment = (GoogleResultFragment)getFragmentManager().findFragmentByTag("listPlace");
		Bundle bdl = new Bundle();
		
		Log.v("TEST", String.valueOf(longitude));
			  bdl = GooglePlaceFragment.getArguments();
			  bdl.putString("longitude", String.valueOf(longitude));
			  bdl.putString("latitude", String.valueOf(latitude));  
	}
	
	
	//Method to define the actions when onBack Button is pressed 
	@Override
	public void onBackPressed() {
	    
		if(flag==true){
		mDrawerLayout.closeDrawer(mDrawerList);

		}else{
		
		GoogleResultFragment myFragment = (GoogleResultFragment)getFragmentManager().findFragmentByTag("listPlace");
		if (!myFragment.isVisible()) {
			
			getFragmentManager().beginTransaction()
			.replace(R.id.frame_container, myFragment)
			.commit();
			
		}
		
		}
	}

    //Method to get the last known Location
    public Location getLocation(){
    	
    	LocationManager mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	List<String> providers = mLocationManager.getProviders(true);
		Location bestLocation = null;
		for (String provider : providers) {
			Location l = mLocationManager.getLastKnownLocation(provider);

			if (l == null) {
				continue;
			}
			if (bestLocation == null
					|| l.getAccuracy() < bestLocation.getAccuracy()) {
				bestLocation = l;
			}
		}
		if (bestLocation == null) {
			return null;
		}
		return bestLocation;
    
    }

	@Override
	public void saveAlbum() {
		ProfilFragment profilFragment = (ProfilFragment)getFragmentManager().findFragmentByTag("profil");
		 
		  //Bundle
		  Bundle args = new Bundle();
          args.putParcelable("user", user);
        	  Log.v("TEST USER1", user.getProfilAlbum().get(0));
	          profil_fragment.setArguments(args);
		
		  } 
	}
	
