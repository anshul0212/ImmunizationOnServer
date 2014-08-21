package com.social.healthometer;
import java.util.Locale;

import com.social.utilities.Localization;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;


public class SettingsActivity extends Activity implements OnItemSelectedListener , OnClickListener{
	private Context context;
	private Spinner languagePicker;
	private boolean itemSelected=false;
	private Locale activityLocale;
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_settings);    
	        languagePicker=(Spinner)this.findViewById(R.id.spinner1);
	        languagePicker.setOnItemSelectedListener(this);
	        activityLocale=this.getResources().getConfiguration().locale;
	        TelephonyManager telephonyManager;

	        telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
			
		     String imei = telephonyManager.getDeviceId();
		    EditText imeiEditText = (EditText)findViewById(R.id.et_phoneSettingId); 
		    imeiEditText.setText(imei.toString());
		    
	        Button submit = (Button)findViewById(R.id.btn_hwSubmitId);
	        submit.setOnClickListener(this);
	    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int selectedItem,
			long arg3) {
		
		context=this;
	
		
		if (selectedItem==Localization.HINDI_SELECTOR&&!(activityLocale.equals("hi"))) {
		
		    Localization.setLanguage(getApplicationContext(),"hi",activityLocale);
		    setContentView(R.layout.activity_settings);
		    languagePicker=(Spinner)this.findViewById(R.id.spinner1);
	        languagePicker.setOnItemSelectedListener(this);
	        
		}
		
		if (selectedItem==Localization.ENGLISH_SELECTOR&&!(activityLocale.equals("en"))) {
				
			 Localization.setLanguage(getApplicationContext(),"en",activityLocale);
			 setContentView(R.layout.activity_settings);
			 languagePicker=(Spinner)this.findViewById(R.id.spinner1);
		     languagePicker.setOnItemSelectedListener(this);
			 
		}
        
		
		
		
	}

	   
    

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		  if (v.getId()==R.id.btn_hwSubmitId) {
			  
		Toast.makeText(getApplicationContext(), "Phone Num updated", Toast.LENGTH_LONG).show();
				
		  }
		  else
			  if (v.getId()==R.id.menusetting_button_id) {
				  
				  Intent i = new Intent(this, MainMenuActivity.class);
					this.startActivity(i);
	
					  }
		  
		    	
			    
		
	}

    
	
	
	
	

}
