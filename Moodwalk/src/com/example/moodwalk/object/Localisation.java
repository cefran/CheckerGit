package com.example.moodwalk.object;

public class Localisation {
	
	private String description;
	private String lat;
	private String lng;
	private String ref;
	private String id;
	
	
	public Localisation(){
		lat="";
		lng="";
		ref="";
		description="";
		id="";
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description= description;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}

}
