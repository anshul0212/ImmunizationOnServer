package com.social.healthometer;
import java.util.Locale;

import com.social.utilities.Localization;

import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;


public class SettingsActivity extends Activity implements OnItemSelectedListener , OnClickListener{
	private Context context;
	private Spinner languagePicker;
	private boolean itemSelected=false;
	private Locale activityLocale;
	String mobileNo;
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_settings);    
	        languagePicker=(Spinner)this.findViewById(R.id.spinner1);
	        languagePicker.setOnItemSelectedListener(this);
	        activityLocale=this.getResources().getConfiguration().locale;
	        
	        mobileNo = SettingsActivity.getDefaults("mobileNo" , getApplicationContext());
            if(mobileNo.contentEquals("false"))
            {
            	
            }
            else
            {
            	 EditText mobEditText = (EditText)findViewById(R.id.et_phoneSettingId); 
            	 mobEditText.setText(mobileNo);
            }
            	
	        
            Button menu = (Button)findViewById(R.id.menusetting_button_id);
	        menu.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(v.getContext(), MainMenuActivity.class);
					v.getContext().startActivity(i);
					
				}
			});
	        
	        Button submit = (Button)findViewById(R.id.btn_hwSubmitId);
	        submit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					  
					  EditText mobEditText = (EditText)findViewById(R.id.et_phoneSettingId); 
			         	SettingsActivity.setDefaults("mobileNo", mobEditText.getText().toString() , v.getContext());    
			         	ShowMessage( getString(R.string.Information) , getString(R.string.mobileNoUpdated));  
					
				}
			});
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

	public static String getDefaults(String key, Context context) {
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    return preferences.getString(key, "false");
	}

	 
    public static void setDefaults(String key, String value, Context context) {
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(key, value);
	    editor.commit();
	}
    

    

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	public void ShowMessage(String title, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		
		builder.setIcon(R.drawable.checkmark);
		builder.setTitle(title);
		builder.create().show();
	}




	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}


    
	
	
	
	

}
