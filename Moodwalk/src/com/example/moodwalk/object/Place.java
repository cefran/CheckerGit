package com.example.moodwalk.object;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
	private String name;
	private String category;
	private String rating;
	private String open;
	private String vicinity;
	private String address;
	private String phone;
	private String web;
	private String id;
	private Double latitude;
	private Double longitude;
	private String reference;
	private String picture;
	private String weekday;
	private int day;
	private int month;
	private int year;
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public Place() {
		this.name = "";
		this.rating = "";
		this.open = "";
		this.setCategory("");
		this.vicinity = "";
		this.id = "";
		this.reference="";
		this.latitude= (double) 0;
		this.longitude= (double) 0;
		this.picture=null;
		this.weekday=null;
		this.day=0;
		this.month=0;
		this.year=0;
	}

	 private Place(Parcel in){
	        this.name = in.readString();
	        this.rating = in.readString();
	        this.open = in.readString();
	        this.category = in.readString();
	        this.vicinity = in.readString();
	        this.id = in.readString();
	        this.reference = in.readString();
	        this.latitude = in.readDouble();
	        this.longitude = in.readDouble();
	        this.picture = in.readString();
	        this.address = in.readString();
	        this.phone = in.readString();
	        this.web = in.readString();
	        this.weekday = in.readString();
	        this.day= in.readInt();
	        this.month= in.readInt();
	        this.year= in.readInt();
	    }
	 
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(rating);
		dest.writeString(category);
		dest.writeString(open);
		dest.writeString(vicinity);
		dest.writeString(id);
		dest.writeString(reference);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeString(picture);
		dest.writeString(address);
		dest.writeString(phone);
		dest.writeString(web);
		dest.writeString(weekday);
		dest.writeInt(day);
		dest.writeInt(month);
		dest.writeInt(year);
	}
	
	 public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
		 
	        @Override
	        public Place createFromParcel(Parcel source) {
	            return new Place(source);
	        }
	 
	        @Override
	        public Place[] newArray(int size) {
	            return new Place[size];
	        }
	    };
	    
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getRating() {
		return rating;
	}

	public void setOpenNow(String open) {
		this.open = open;
	}

	public String getOpenNow() {
		return open;
	}
	
	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
	
	public String getVicinity() {
		return vicinity;
	}

}
