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
    String lang;
    String isLangSet;
    
    private Locale activityLocale;
    
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
            	  sharedP = getSharedPreferences("Language", Context.MODE_PRIVATE);

                  isLangSet= "false";
                  lang = "EN";
          		lang = SplashScreen.getDefaults("lang" , getApplicationContext());
          		isLangSet = SplashScreen.getDefaults("isLangSet" , getApplicationContext()); 		
          		
          		Log.d("isLangSet= ",isLangSet);
          		
          		
          		if(isLangSet.contentEquals("false")){
          			Intent i = new Intent(SplashScreen.this,ChooseLanguageActivity.class);
          			startActivity(i);
          		}
          		else
          			if(isLangSet.contentEquals("true"))
          			{
          			
          				activityLocale=getApplicationContext().getResources().getConfiguration().locale;
          				
          				if (lang.contentEquals("EN")) {
          			    	
          			    	
          			    	  Localization.setLanguage(getApplicationContext(), "en",activityLocale); 
          			    	 
          				}		
          			 		
          				if (lang.contentEquals("HIN")) {
          			    	
          			    	 Localization.setLanguage(getApplicationContext(), "hi",activityLocale); 
          			   	  
          				}
          				Toast.makeText(getApplicationContext(), "lang Already Exists", 1000).show();
          				
          				Intent i = new Intent(SplashScreen.this,MainMenuActivity.class);
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
