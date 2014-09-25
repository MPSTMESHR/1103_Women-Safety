package com.androidhive.dashboard;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.androidhive.dashboard.example.R;


public class AndroidDashboardDesignActivity extends Activity {
	EditText etNumber;
	MediaPlayer mPlayer;
	GPSTracker gps;
	String address=null;
	String city =null;
	String country = null;
	/** Defining request code */
	private final int REQUEST1 = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        
        /**
         * Creating all buttons instances
         * */
        // Dashboard News feed button
        Button btn_newsfeed = (Button) findViewById(R.id.btn_news_feed);
        
        // Dashboard Friends button
        Button btn_friends = (Button) findViewById(R.id.btn_friends);
        
        // Dashboard Messages button
        Button btn_messages = (Button) findViewById(R.id.btn_messages);
        
        // Dashboard Places button
        Button btn_places = (Button) findViewById(R.id.btn_places);
        
        // Dashboard Events button
        Button btn_events = (Button) findViewById(R.id.btn_events);
        
        // Dashboard Photos button
        Button btn_photos = (Button) findViewById(R.id.btn_photos);
        
        /**
         * Handling all button click events
         * */
        
        // Listening to News Feed button click
        btn_newsfeed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching News Feed Screen
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+103));
				callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(callIntent);
				
			}
		});
        
       // Listening Friends button click
        btn_friends.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				String shareBody = "Help Me!!! I am in trouble!";
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Woman's Safety");
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(shareIntent, "Share via"));	
				
			}
		});
        
        // Listening Messages button click
        btn_messages.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				Intent i = new Intent("androidhive.dashboard.activityresult");		
				/** Starting the activity ActivityResultDemo */
				startActivityForResult(i, REQUEST1);
				 /*
	                 
	            
				*/
				
			}
		});
        
        
        
        // Listening to Places button click
        btn_places.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				 mPlayer = MediaPlayer.create(AndroidDashboardDesignActivity.this, R.raw.loud);
			     mPlayer.start();
			}
		});
        
        btn_events.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://en.m.wikipedia.org/wiki/Women's_rights"));
				startActivity(browserIntent);
				
			}
		});
    }
    /** A callback method, which is executed when the activity that is called from this activity is finished its execution */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	/** 
    	 * requestCode : an integer code passed to the called activity set by caller activity
    	 * resultCode : an integer code returned from the called activity
    	 * data : an intent containing data set by the called activity
    	 */
    	if(requestCode==REQUEST1 && resultCode==RESULT_OK){
    		String number=data.getStringExtra("data");
    		
    		
    		gps = new GPSTracker(AndroidDashboardDesignActivity.this);
			 
            // check if GPS enabled    
            if(gps.canGetLocation()){
                 
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

try{
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(AndroidDashboardDesignActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getAddressLine(1);
                country = addresses.get(0).getAddressLine(2);

}
catch(Exception e){

}
                Log.i("Send SMS", "");
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null,"Help Me..My Location is " +address + city  + country, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",   Toast.LENGTH_LONG).show();
                 } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                 }
                
                // \n is for new line
              //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();   
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
    		
    		//Toast.makeText(getBaseContext(), data.getStringExtra("data"), Toast.LENGTH_SHORT).show();
    	}
    	}
}