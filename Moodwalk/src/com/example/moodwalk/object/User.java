package com.example.moodwalk.object;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class User implements Parcelable{
	
	private String user_id;
	private ArrayList<String> profilAlbum;
	private String first_name;
	private String last_name;
	private String profil_picture;
	
	public User(){};
	
	public ArrayList<String> getProfilAlbum() {
		return profilAlbum;
	}

	public void setProfilAlbum(ArrayList<String> profilAlbum) {
		this.profilAlbum = profilAlbum;
	}
	
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getProfil_picture() {
		return profil_picture;
	}

	public void setProfil_picture(String profil_picture) {
		this.profil_picture = profil_picture;
	}

	public String toJSON(){

		Gson gson = new Gson();
	    JSONObject jsonObject= new JSONObject();
	    try {
	        jsonObject.put("user_id", getUser_id());
	        jsonObject.put("last_name", getLast_name());
	        jsonObject.put("first_name", getFirst_name());
	        jsonObject.put("profil_picture", getProfil_picture());
	        jsonObject.put("profil_album", getProfilAlbum());
	        
	        return jsonObject.toString(); 
	    } catch (JSONException e) {
	        // TODO A
	    	// TODO Auto-generated catch block
	        e.printStackTrace();
	        return "";
}
	}
	
	public User toUser(String JSON) throws JSONException{
		
		
		GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        
		User user = new User();
		JSONObject jsonObject= new JSONObject(JSON);
		

		user = gson.fromJson(jsonObject.toString(),  User.class);
//	    user.setFirstName(jsonObject.getString("first_name"));
//	    user.setId(jsonObject.getString("id"));
//	    user.setLastName(jsonObject.getString("last_name"));
//		Log.v("Testouille3, msg));
		return user;
	        
}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(user_id);
		dest.writeString(first_name);
		dest.writeString(last_name);
		dest.writeString(profil_picture);
		dest.writeList(profilAlbum);
		
	}
	
	private User(Parcel in){
        this.user_id = in.readString();
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.profil_picture = in.readString();
        this.profilAlbum = in.readArrayList(String.class.getClassLoader());
    }
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		 
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }
 
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
