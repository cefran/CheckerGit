package com.example.moodwalk.fragments;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.moodwalk.R;
import com.example.moodwalk.adapters.UserPicAdapters;
import com.example.moodwalk.fragments.GoogleResultFragment.DataSaving;
import com.example.moodwalk.object.Place;
import com.example.moodwalk.object.User;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfilFragment extends Fragment  {
	
	UserSaving mCallback;
	private ArrayList<Bitmap> userPicBitmap;
	
	public ProfilFragment(){}
	View rootView;
	ViewPager viewPager;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        User user = (User) getArguments().getParcelable("user");
        ArrayList<String> album = user.getProfilAlbum();
        new getAlbumPic(album).execute();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		String albumUserJson="";
        rootView = inflater.inflate(R.layout.fragment_profil, container, false);
        
//        mCallback.saveAlbum();
        User user = (User) getArguments().getParcelable("user");
        ArrayList<String> album = user.getProfilAlbum();
        Log.v("Test", user.getProfilAlbum().get(1));
        
//          new getAlbumPic(album).execute();
        Log.v("ALBUM1", user.getProfilAlbum().get(1));
        
        if (userPicBitmap==null){
        	Log.v("ALBUM2", "null");
        }else
        {
        	Log.v("ALBUM2", "non null");
        }
        
        return rootView;
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (UserSaving) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement OnHeadlineSelectedListener");
        }
    }
	
	public interface UserSaving {
        public void saveAlbum();
    }
	
	// Method used to decode a string to a Bitmap
		public static Bitmap decodeBase64(String input) 
		{
		    byte[] decodedByte = Base64.decode(input, 0);
		    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
		}
		
		
		private class getAlbumPic extends AsyncTask<Object, Object, String> {

			ArrayList<String> urls;
			
			public getAlbumPic(ArrayList<String> urls){
				this.urls=urls;
			}
			@Override
			protected String doInBackground(Object... params) {
				// TODO Auto-generated method stub
				
				 // Pass results to ViewPagerAdapter Class
				try {
					userPicBitmap=addUserAlbum(urls);
					Log.v("ALBUM3", userPicBitmap.get(1).toString());
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
			protected void onPostExecute(String result) {
				
				UserPicAdapters adapter = new UserPicAdapters(getActivity(), userPicBitmap);
		        viewPager=(ViewPager) rootView.findViewById(R.id.pager);
		        viewPager.setAdapter(adapter);
		       
			}
		
		}
		
		public ArrayList<Bitmap> addUserAlbum(ArrayList<String> urls) throws IOException, JSONException{
			
			Bitmap bitmap = null;
			ArrayList<Bitmap> userPicBitmap=new ArrayList<Bitmap>();


			for(int i=0; i<urls.size();i++){
			 ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    Log.d("TAG2", "Loading Picture");
			    URL img_value = new URL(urls.get(i));
			        bitmap = BitmapFactory.decodeStream((InputStream) img_value.openConnection().getInputStream());
			        if(bitmap!=null){
			        	Log.v("ALBUM_PICTURE", "NON NUL");
			        }else{
			        	Log.v("ALBUM_PICTURE", "NUL");
			        }
			        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			        userPicBitmap.add(bitmap);
			       
			}
			return userPicBitmap;
		}
}
			
