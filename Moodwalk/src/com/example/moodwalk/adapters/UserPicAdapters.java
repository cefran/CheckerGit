package com.example.moodwalk.adapters;

import java.util.ArrayList;

import com.example.moodwalk.R;
import com.example.moodwalk.fragments.ProfilFragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.v4.view.ViewPager;

public class UserPicAdapters extends PagerAdapter {

	Context context;
	ArrayList<Bitmap> album;
	LayoutInflater inflater;
	
	public UserPicAdapters(Context context, ArrayList<Bitmap> album) {
		this.context = context;
		this.album = album;
	}
	
	@Override
	public int getCount() {
		return album.size();
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
 
		// Declare Variables
		ImageView img;
 
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.viewpager_item, container,
				false);
 
		// Locate the TextViews in viewpager_item.xml
		img = (ImageView) itemView.findViewById(R.id.viewpager_image);
		img.setScaleType(ImageView.ScaleType.FIT_XY);
 
		// Capture position and set to the TextViews
		img.setImageBitmap(album.get(position));
 
		// Add viewpager_item.xml to ViewPager
		((ViewPager) container).addView(itemView);
 
		return itemView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((LinearLayout) object);
 
	}

}
