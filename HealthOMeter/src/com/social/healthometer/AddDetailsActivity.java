package com.social.healthometer;


import android.app.Activity;

import android.app.FragmentManager;
import android.os.Bundle;

public class AddDetailsActivity extends Activity 
{

	private FragmentAddDetails addDetailsFragment;
	
	public AddDetailsActivity()
	{
		
	}
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_add_details);
		addDetailsFragment=new FragmentAddDetails();
		if (ViewDetailFragment.ITEM_TO_EDIT!=null) {
			addDetailsFragment.setItem(ViewDetailFragment.ITEM_TO_EDIT);
			ViewDetailFragment.ITEM_TO_EDIT=null;
		}
		

	FragmentManager fragmentManager=getFragmentManager();
	android.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
	fragmentTransaction.add(R.id.frame,addDetailsFragment);
	fragmentTransaction.commit();
	
	}

}
