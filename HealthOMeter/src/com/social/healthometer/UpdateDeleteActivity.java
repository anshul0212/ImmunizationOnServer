package com.social.healthometer;

import java.net.MalformedURLException;
import java.sql.Date;
import java.util.Calendar;

import com.social.healthometer.model.TodoItem;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

public class UpdateDeleteActivity extends Activity {

	RadioButton maleRadioButton;
	RadioButton femaleRadioButton;
	Button saveButton;
	Button deleteButton;
	EditText nameEditText;
	EditText cellNoEditText;
	DatePicker DOBDatePicker;
	private Boolean ready;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_update_delete);
		// Show the Up button in the action bar.
		setupActionBar();
		
		maleRadioButton = (RadioButton)findViewById(R.id.radioButton1);
		femaleRadioButton = (RadioButton)findViewById(R.id.radioButton2);
		nameEditText = (EditText)findViewById(R.id.editText1);
		cellNoEditText = (EditText)findViewById(R.id.editText2);
		saveButton = (Button)findViewById(R.id.buttonSave);
		deleteButton = (Button)findViewById(R.id.buttonDelete);
		DOBDatePicker = (DatePicker)findViewById(R.id.datePicker1);
		
		if(ViewDetailFragment.ITEM_TO_EDIT != null)
		{
			nameEditText.setText(ViewDetailFragment.ITEM_TO_EDIT.getText());
			
			if(ViewDetailFragment.ITEM_TO_EDIT.getSex().compareToIgnoreCase("male") == 0)
			{
				maleRadioButton.setChecked(true);
			}
			else
			{
				femaleRadioButton.setChecked(true);
			}
			
			cellNoEditText.setText(ViewDetailFragment.ITEM_TO_EDIT.getNotifyNumber());
			String dob = ViewDetailFragment.ITEM_TO_EDIT.getDateOfBirth();
			Calendar cal = Calendar.getInstance();
			//Date date = new Date(2007,01,01);
			String dobaArray[] = new String[3];

	    	if(dob!=null)
	    	{
	    	
	    	
	    	dobaArray = dob.split("/");
	    	
	    
			
			cal.set(Integer.parseInt(dobaArray[2]), Integer.parseInt(dobaArray[1]) , Integer.parseInt(dobaArray[0]));
	    	}
	    	else
	    		cal.set(2007, 2,2);
			DOBDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
					
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_delete, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
		
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void MaleSelected(View view)
	{
		maleRadioButton.setChecked(true);
		femaleRadioButton.setChecked(false);
	}
	
	public void FemaleSelected(View view)
	{
		femaleRadioButton.setChecked(true);
		maleRadioButton.setChecked(false);
	}
	
	public void SaveClicked(View view)
	{
		if((nameEditText.getText().length() == 0) ||
				(cellNoEditText.getText().length() == 0) ||
				((!maleRadioButton.isChecked()) && (!femaleRadioButton.isChecked())))
		{
			ShowMessage("Warning", "Please provide full detail then retry");
			return;
		}
		
		ViewDetailFragment.ITEM_TO_EDIT.setText(nameEditText.getText().toString());
		ViewDetailFragment.ITEM_TO_EDIT.setNotifyNumber(cellNoEditText.getText().toString());
		
		if(this.maleRadioButton.isChecked())
		{
			ViewDetailFragment.ITEM_TO_EDIT.setSex("male");
		}
		else
		{
			ViewDetailFragment.ITEM_TO_EDIT.setSex("female");
		}
		
		Date dob = new Date(1000);
		
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(DOBDatePicker.getYear());
		strBuilder.append("-");
		strBuilder.append(DOBDatePicker.getMonth());
		strBuilder.append("-");
		strBuilder.append(DOBDatePicker.getDayOfMonth());

		dob = Date.valueOf(strBuilder.toString());
		ViewDetailFragment.ITEM_TO_EDIT.setDateOfBirth(strBuilder.toString());
	
	}
	
	public void DeleteClicked(View view)
	{
		if(ViewDetailFragment.ITEM_TO_EDIT == null)
		{
			return;
		}
	}
	
	public void ShowMessage(String title, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
	
}
