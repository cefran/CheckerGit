package com.example.moodwalk.fragments;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.moodwalk.R;
import com.example.moodwalk.activities.MainActivity;
import com.example.moodwalk.object.User;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;


public class MainFragment extends Fragment{

//	UserID = 10153089123294201
	
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	private String user_JSON="" ;
	private static final int REQUEST_CODE = 1;
	final User user_object = new User();
	
	private Session.StatusCallback callback = new Session.StatusCallback(){
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	    
	}
	    
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
		
	    View view = inflater.inflate(R.layout.connection_facebook, container, false);
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.login_button);
	    authButton.setFragment(this);
	    authButton.setReadPermissions(Arrays.asList("user_location", "user_status", "user_photos"));
	    authButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
	    	   } );
	    
	    
	    return view;
	}
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
		
	    
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		        new UserGetSet(Session.getActiveSession()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	        else
		        new UserGetSet(Session.getActiveSession()).execute();
	        
	    }else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	    
	    //local variables
	    final Session session = Session.getActiveSession();
	  
	    if (session != null && session.getState().isOpened()){
	         Log.i("sessionToken", session.getAccessToken());
		      
	    }
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	private class UserGetSet extends AsyncTask<User, Object, String> {

		Session session;
		
		
		public UserGetSet(Session session){
			this.session = session;
		}
		
		@Override
		protected String doInBackground(User... user) {
			
			Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user, Response response) {
					// TODO Auto-generated method stub
					// If the response is successful
					Log.v("TEST", "DoInBackground");
	                if (session == Session.getActiveSession()) {
	                    if (user != null) {
	                    	
	                       try {
						   user_object.setProfil_picture(getUserPic(user.getId()));
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                       user_object.setUser_id(user.getId());
	                       user_object.setFirst_name(user.getFirstName());
	                       user_object.setLast_name(user.getLastName());
	                      
//	                       https://www.facebook.com/album.php?fbid=443546909200&id=10153089123294201&aid=200252
	                       
	                       //Get the entire profile album picture
	                       String temp = makeCall("https://graph.facebook.com/"+user.getId()+"/albums?access_token="+session.getAccessToken());
	                       User user_bis = new User();
	                       
	                       try {
							extractUserProfilAlbum(temp, session, user_object);
         					} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                       
	                       user_JSON = user_bis.toJSON();
	                       
	                       //Save User JSON FILE
	                       try {
							saveFile(user_JSON);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                       
	                       //Start new activity
	                       Bundle args = new Bundle();
	                       args.putParcelable("user", user_object);
	                       Intent i = new Intent(getActivity(), MainActivity.class);
	                       i.putExtra("user1", args);
	       	               startActivity(i);
	                    }
	                    }
	                        
     
	                    }   
	                }
				   
	        ); 
	        Request.executeAndWait(request);
			return "";
		} 
		
		@Override
		protected void onPreExecute() {
			// we can start a progress bar here
			
			Log.v("TEST", "OnPreExecute");
		}

		protected void onPostExecute(String result) {
			Log.v("TEST", "OnPostExecute");
		}
		
		
	}
	
	public static String makeCall(String url) {

		// string buffers the url
		StringBuffer buffer_string = new StringBuffer(url);
		String replyString = "";

		// instanciate an HttpClient
		HttpClient httpclient = new DefaultHttpClient();
		// instanciate an HttpGet
		HttpGet httpget = new HttpGet(buffer_string.toString());

		try {
			// get the responce of the httpclient execution of the url
			HttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();

			// buffer input stream the result
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			// the result as a string is ready for parsing
			replyString = new String(baf.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// trim the whitespaces
		return replyString.trim();
	}
	
	public void extractUserProfilAlbum(String response, Session session, User user) throws JSONException, MalformedURLException{
		
		JSONObject jsonObject = new JSONObject(response);
		JSONObject jsonAlbum = new JSONObject();
		String id=null;
		String answer=null;
		String url=null;
		ArrayList<String> links = new ArrayList<String>();
		Log.v("USERID", response);
		if (jsonObject.has("data")) {
			
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			if (jsonArray.getJSONObject(0).getString("type").equals("profile")) {
				id = jsonArray.getJSONObject(0).getString("id");
				answer = makeCall("https://graph.facebook.com/"+id+"/photos?access_token="+session.getAccessToken());
				Log.v("ANSWER", answer);
				jsonAlbum = new JSONObject(answer);
				JSONArray jsonAlbumArray = jsonAlbum.getJSONArray("data");
				for(int i=0; i<4;i++){
					if (jsonAlbumArray.getJSONObject(i).has("source")) {
						url=jsonAlbumArray.getJSONObject(i).getString("source");
						links.add(url);
					}
				}
				}
		}
		user.setProfilAlbum(links);	
	}
	
	
	public String getProfilAlbumPic(String url) throws MalformedURLException{
		Bitmap bitmap = null;
	    String encodedImage = null;
		
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    Log.d("TAG2", "Loading Picture");
	    URL img_value = new URL(url);
	    try {
	        bitmap = BitmapFactory.decodeStream((InputStream) img_value.openConnection().getInputStream());
	        if(bitmap!=null){
	        	Log.v("ALBUM_PICTURE", "NON NUL");
	        }else{
	        	Log.v("ALBUM_PICTURE", "NUL");
	        }
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	        
	        byte[] imageBytes = baos.toByteArray();
	        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
	        
	    } catch (Exception e) {
	        Log.d("TAG2", "Loading Picture FAILED");
	        e.printStackTrace();
	    }
	    
	    return encodedImage;
	}

	public String getUserPic(String userID) throws MalformedURLException {
	    Bitmap bitmap = null;
	    String encodedImage = null;
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    Log.d("TAG1", "Loading Picture");
	    URL img_value = new URL("https://graph.facebook.com/"+userID+"/picture?type=large");
	    try {
	        bitmap = BitmapFactory.decodeStream((InputStream) img_value.openConnection().getInputStream());
	        if(bitmap!=null){
	        	Log.v("PROFIL_PICTURE", "NON NUL");
	        }else{
	        	Log.v("PROFIL_PICTURE", "NUL");
	        }
	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
	        byte[] imageBytes = baos.toByteArray();
	        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
	        
	    } catch (Exception e) {
	        Log.d("TAG1", "Loading Picture FAILED");
	        e.printStackTrace();
	    }
	    
	    return encodedImage;
	}
	
	public void saveFile(String Json) throws IOException{
		
		FileOutputStream fOut = getActivity().openFileOutput("JsonUser.txt", Context.MODE_WORLD_READABLE);
		
		OutputStreamWriter osw = new OutputStreamWriter(fOut); 
		
		osw.write(Json);
		
		osw.flush();
	    osw.close();
	}
	}
