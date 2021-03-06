package com.social.healthometer;
import java.util.Locale;

import com.social.utilities.Localization;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class PasskeyActivity extends Activity  {
	

    private Locale activityLocale;
    private OnClickListener onClickListener;
    private  EditText passkey;
    private  EditText mobileNo;
    private Button btnlogIn;
    SharedPreferences sharedP;
    String lang;
    String isLangSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        new SimpleEula(this).show();
setContentView(R.layout.activity_passkey);


passkey = (EditText) findViewById(R.id.td_passkeyId);
mobileNo =(EditText)findViewById(R.id.tv_mobileNoID);
btnlogIn = (Button)findViewById(R.id.btn_loginId);


btnlogIn.setOnClickListener( new View.OnClickListener() {

@Override
public void onClick(View v) {
// TODO Auto-generated method stub
	 String  passkeyStr = passkey.getText().toString();
	 String  mobileNoStr = mobileNo.getText().toString();

if(passkeyStr.contentEquals("0212"))
{
PasskeyActivity.setDefaults("passKey", passkeyStr , v.getContext());
PasskeyActivity.setDefaults("mobileNo", mobileNoStr , v.getContext());    


isLangSet= "false";
lang = "EN";
lang = PasskeyActivity.getDefaults("lang" , getApplicationContext());
isLangSet = PasskeyActivity.getDefaults("isLangSet" , getApplicationContext()); 		

if(isLangSet.contentEquals("false"))
{
	Intent i = new Intent(PasskeyActivity.this,ChooseLanguageActivity.class);
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
			
		Intent i = new Intent(PasskeyActivity.this,MainMenuActivity.class);
		startActivity(i);
	}

}
else
{
ShowMessage( getString(R.string.InformationHindi) , getString(R.string.wrongPasskeyHindi));
}
}
});

/** Create an array adapter to populate dropdownlist */





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
    
	public void ShowMessage(String title, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		
		builder.setIcon(R.drawable.info);
		
		builder.setTitle(title);
		builder.create().show();
	}
}
