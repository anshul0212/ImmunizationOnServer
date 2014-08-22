package com.social.healthometer;

import java.util.Locale;

import com.social.actionlisteners.ButtonClickListener;
import com.social.utilities.Localization;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class SplashScreen extends Activity {
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    SharedPreferences sharedP;
    String passKey;
    String mobileNo;
    
   
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	
                  passKey= "false";
                  
                  passKey = SplashScreen.getDefaults("passKey" , getApplicationContext());
                  
          		Log.d("passKey= ",passKey);
          		
          		
          		if(passKey.contentEquals("0212")){
          			Intent i = new Intent(SplashScreen.this,MainMenuActivity.class);
          			startActivity(i);
          		}
          		else
          		{
          			
          				Intent i = new Intent(SplashScreen.this,PasskeyActivity.class);
          				startActivity(i);
          			}
          		
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
        
      
    }
	public static String getDefaults(String key, Context context) {
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    return preferences.getString(key, "false");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

}
