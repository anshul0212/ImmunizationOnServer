package com.social.healthometer;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.TextView;

public class VerificationActivity extends Activity  {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		Fragment fragment = null;	
		fragment = new PendingListFragment();
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();		
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			}
		else {
			}
					
			TextView textObj = (TextView)findViewById(R.id.pending_text_id);
			textObj.setOnClickListener( new OnClickListener() {
	            @Override
	            public void onClick(View viewIn) {
	            	 Fragment fragmentPending = null;
	     			
	            	 fragmentPending = new PendingListFragment();
	     			if (fragmentPending != null) {
	     				FragmentManager fragmentManager = getFragmentManager();
	     				fragmentManager.beginTransaction().replace(R.id.frame_container, fragmentPending).commit();

	     			} else {
	     			}
	     		
	            }
	        });
			
			TextView textObjVerified = (TextView)findViewById(R.id.verified_text_id);
			textObjVerified.setOnClickListener( new OnClickListener() {
	            @Override
	            public void onClick(View viewIn) {
	            	 Fragment fragmentVerified = null;
	     			
	            	 fragmentVerified = new VerifiedListFragment();
	            	
	     			if (fragmentVerified != null) {
	     				FragmentManager fragmentManager = getFragmentManager();
	     				fragmentManager.beginTransaction().replace(R.id.frame_container, fragmentVerified).commit();

	     			} else {
	     				// error in creating fragment
	     			}
	     		
	            }
	        });
		
			
	            	 
			
			
	}
	
 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.verification, menu);
		return true;
	}


	
	public void ShowMessage(String title, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
}
