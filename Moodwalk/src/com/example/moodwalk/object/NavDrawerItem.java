package com.example.moodwalk.object;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class NavDrawerItem {
    
    private String title;
    private int icon;
    private Bitmap iconBit;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;
     
    public NavDrawerItem(){}
 
    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }
    public NavDrawerItem(String title, Bitmap icon){
        this.title = title;
        this.iconBit = icon;
    }
     
    public Bitmap getIconBit() {
		return iconBit;
	}

	public void setIconBit(Bitmap iconBit) {
		this.iconBit = iconBit;
	}

	public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }
     
    public String getTitle(){
        return this.title;
    }
     
    public int getIcon(){
        return this.icon;
    }
     
    public String getCount(){
        return this.count;
    }
     
    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }
     
    public void setTitle(String title){
        this.title = title;
    }
     
    public void setIcon(int icon){
        this.icon = icon;
    }
     
    public void setCount(String count){
        this.count = count;
    }
     
    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}
