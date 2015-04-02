package com.example.moodwalk.activities;

import java.util.Calendar;

import com.example.moodwalk.R;
import com.example.moodwalk.object.Place;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class Date_activity extends Activity {

	CalendarView calendar;
	String date;
	Place place;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//sets the main layout of the activity
		setContentView(R.layout.date_activity);
		
		//initializes the calendarview
		initializeCalendar();
		
		Button validate = (Button) findViewById(R.id.validate_button);
		validate.setBackgroundResource(R.drawable.button_selector);
		
		validate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("DATE", place.getAddress());
				Bundle arg = new Bundle();
				arg.putParcelable("place", place);
				Intent i = new Intent(getApplicationContext(), Time_activity.class);
				i.putExtra("place", arg);
				startActivity(i);
			}
			
		});
		
		
	}
	
	public void initializeCalendar() {
		calendar = (CalendarView) findViewById(R.id.calendar);

		// sets whether to show the week number.
		calendar.setShowWeekNumber(false);

		// sets the first day of week according to Calendar.
		// here we set Monday as the first day of the Calendar
		calendar.setFirstDayOfWeek(1);

		//The background color for the selected week.
		calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.blue));
		
		//sets the color for the dates of an unfocused month. 
		calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.darkgrey));
	
		//sets the color for the separator line between weeks.
		calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.white));
		
		//sets the color for the vertical bar shown at the beginning and at the end of the selected date.
		calendar.setSelectedDateVerticalBar(R.color.darkblue);
		
		//Do not show the previous month
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
		long dateMin = cal.getTime().getTime();
		calendar.setMinDate(dateMin);
	
		//sets the listener to be notified upon selected date change.
		calendar.setOnDateChangeListener(new OnDateChangeListener() {
                       //show the selected date as a toast
			public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
				Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
				
				Intent j = getIntent();
				Bundle bdl = j.getBundleExtra("place");
				place = bdl.getParcelable("place");
				
				
				place.setDay(day);
				place.setMonth(month);
				place.setYear(year);
	
			}
		});
	}
}
