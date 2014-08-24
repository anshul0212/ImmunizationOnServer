package com.social.healthometer;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.social.actionlisteners.ButtonClickListener;
import com.social.healthometer.model.TodoItem;
import com.social.utilities.ServiceHandler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;


public class FragmentAddDetails extends Fragment implements OnDateSetListener,OnTouchListener,OnClickListener  {
	
	String deviceNum , imei;
	RadioButton maleRadioButton;
	RadioButton femaleRadioButton;
	Button submitButton;
	Button resetButton;
	EditText nameEditText;
	EditText cellNoEditText;
	DatePicker DOBDatePicker;

	//private DatePicker datePicker;
	private EditText dateOfbirth;
	
	private TodoItem item;
    private int mYear, mMonth, mDay; 
    
    String  url_add_beneficiary;
    private Context context;
	private ProgressDialog pd;
	//private Button b;
	Button menuButton;
    ButtonClickListener onClickListener ;
    
    Button deleteItem;
	public TodoItem getItem() {
		return item;
	}

	public void setItem(TodoItem item) {
		this.item = item;
	}

	public View onCreateView(  LayoutInflater inflater, ViewGroup container , Bundle bundle)
	{
		context = this.getActivity();
		View rootView = inflater.inflate(R.layout.fragment_add_edit_details, container, false);
		
		
		return rootView;
	}
	

	public void SubmitClicked(View view)
	{
		Button submitButton1 = (Button)getActivity().findViewById(R.id.button1);
		
		Button deleteButton = (Button)getActivity().findViewById(R.id.btn_deleteId);
		
		if(submitButton1.getText().toString().contentEquals("Edit"))
		{
			deleteButton.setVisibility(View.VISIBLE);
			nameEditText.setEnabled(true);
			maleRadioButton.setEnabled(true);
			femaleRadioButton.setEnabled(true);
			dateOfbirth.setEnabled(true);
			cellNoEditText.setEnabled(true);
			submitButton1.setText("Submit");
		}
		else
		{
	
		EditText	 etName1 = (EditText)getActivity().findViewById(R.id.enter_name_text);
	    	String name1 = etName1.getText().toString();
	    	
	    	EditText	 etNotifyNum1 = (EditText)getActivity().findViewById(R.id.phone_num_text);
	    	String	notify_num1 = etNotifyNum1.getText().toString();
	    	
	    	EditText	 etDate1 = (EditText)getActivity().findViewById(R.id.date_of_birth_text);
	    	String dob1 = etDate1.getText().toString();
	    
	    	if(name1.equals("")||notify_num1.equals("")||dob1.equals(""))
	    	{
	    		
	    
			 ShowMessage(getString(R.string.Information), getString(R.string.FillMandatory), getResources().getDrawable(R.drawable.info) );
	    		
		 }
		 else
		 {
			 addUpdateDialog();

			 if(item !=null)
				{
					nameEditText.setEnabled(false);
					maleRadioButton.setEnabled(false);
					femaleRadioButton.setEnabled(false);
					dateOfbirth.setEnabled(false);
					cellNoEditText.setEnabled(false);
					submitButton1.setText("Edit");
					deleteButton.setVisibility(View.GONE);
				}
		 }
		
		}
	
		 
	}
	 private void addUpdateDialog() {
		 
		    AlertDialog.Builder alertDlg = new AlertDialog.Builder(this.getActivity());
		 
		     
		 if(item!=null)
		    alertDlg.setMessage(getString(R.string.ConfirmationUpdate));
		 else
			  alertDlg.setMessage(getString(R.string.ConfirmationAdd));
			
		 
		    alertDlg.setCancelable(false); // We avoid that the dialong can be cancelled, forcing the user to choose one of the options
		 
		    alertDlg.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
				 
			       
				 
			      public void onClick(DialogInterface dialog, int id) {
			 
			    	  GetProfileDetails addBenificiary = new GetProfileDetails();
						 addBenificiary.execute();
			           }
			 
			    }
			 
			     );
			 
			     alertDlg.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
			 
			    @Override
			 
			    public void onClick(DialogInterface dialog, int which) {
			 
			      // We do nothing
			 
			    }
			 
			    });
		 
		  
		 
		     alertDlg.create().show();
		  }

    private class GetProfileDetails extends AsyncTask<Void, Void, Void> 
    {
    	HashMap<String, String> user;
    	 String  jsonStr, flagNetwork , hw_number;
    	
    	 String namePrev , dobPrev , cellNoPrev, sexPrev ;
    	
    	  EditText etName, etDate,etNotifyNum;
       
    	  @Override
        protected void onPreExecute() {
            super.onPreExecute();
           Log.d("in pre", "preee");
            hw_number = new String("");
            
            jsonStr = new String();
            
            namePrev = new String();
            dobPrev = new String();
            cellNoPrev = new String();
            sexPrev= new String();
            url_add_beneficiary = getString(R.string.url_add_beneficiary);
    		
            flagNetwork = "0";
            
            createProgress();
 
    	}
   	 
    	
        @Override
        protected Void doInBackground(Void... arg0) 
        {
        	
    			
    				if(item!=null)
    				{
    					 namePrev = item.getText();
    					 dobPrev = item.getDateOfBirth();
    					 cellNoPrev =item.getNotifyNumber();
    					 sexPrev = item.getSex();
    					 
    				      String  dateOfbirth=((EditText)getActivity().findViewById(R.id.date_of_birth_text)).getText().toString();
    				       maleRadioButton = (RadioButton)getActivity().findViewById(R.id.male_radio);
    				       femaleRadioButton = (RadioButton)getActivity().findViewById(R.id.female_radio);
    				       String sex = new String();
    				       
    				       if(maleRadioButton.isChecked())
    				    	   sex =   "M";
    				       else
    				    	   if(femaleRadioButton.isChecked())
    				    		  sex =   "F";
    				       
    				      String  nameEditText = ((EditText)getActivity().findViewById(R.id.enter_name_text)).getText().toString();
    				     String	cellNoEditText = ((EditText)getActivity().findViewById(R.id.phone_num_text)).getText().toString();
    				    	
    				    	
    					if(dateOfbirth.contentEquals(dobPrev)&&nameEditText.contentEquals(namePrev)&&cellNoEditText.contentEquals(cellNoPrev)&&sex.contentEquals(sexPrev))
    					{
    						flagNetwork= "1";
    						return null;
    					}
    					else	
    					{
    						url_add_beneficiary = url_add_beneficiary+item.getId()+"/";
    					}
    				
    				}
    				else
    				{
    					
    				}
    				
    		String name, dob , notify_num, sex ;
    		 name = new String();
             notify_num = new String();
             
             dob = new String();
             sex = new String();
             
           
        	 etName = (EditText)getActivity().findViewById(R.id.enter_name_text);
	    	name = etName.getText().toString();
	    	
	    	 etNotifyNum = (EditText)getActivity().findViewById(R.id.phone_num_text);
	    	notify_num = etNotifyNum.getText().toString();
	    	
	    	 etDate = (EditText)getActivity().findViewById(R.id.date_of_birth_text);
	    	dob = etDate.getText().toString();
	   
	    	RadioButton etSex = (RadioButton)getActivity().findViewById(R.id.male_radio);
	    	 if(etSex.isChecked())
	    		 sex = "M";
	    	 else
	    		 sex = "F";
	    	 
	    	
	    	if(dob!=null&&dob.contains("-"))
	    	{
	    	String dobaArray[] = new String[3];
	    	
	    	dobaArray = dob.split("-");
	    	
	    	dob = "";
	    	dob = dob + dobaArray[2] +"/"+ dobaArray[1] +"/"+ dobaArray[0] ;
	    	}
	    	else
	    	{
	    		 Log.d("dob","dob is null");
	    	}	
        	
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            Log.d("dob",dob);
            
            // Making a request to url and getting response
            Log.d("url_add_beneficiary",url_add_beneficiary);
            
            hw_number= "";
            
            hw_number = FragmentAddDetails.getDefaults("mobileNo" , context);
            
            if(hw_number.contentEquals(""))
            {
            	jsonStr=null;
            	
            }
            else
            {
		         // Building post parameters, key and value pair
		            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);
		            //nameValuePair.add(new BasicNameValuePair("Id", "1"));
		            nameValuePair.add(new BasicNameValuePair("name", name));
		           nameValuePair.add(new BasicNameValuePair("notif_num", notify_num));
		            nameValuePair.add(new BasicNameValuePair("dob", dob));
		            nameValuePair.add(new BasicNameValuePair("sex", sex));
		      		
		            nameValuePair.add(new BasicNameValuePair("hw_num", hw_number));
		            
		            Log.d("name", ""+name);
					Log.d("notify_num", ""+notify_num);
						Log.d("dob", ""+dob);
						Log.d("hw_number", hw_number);
		            
		              jsonStr = sh.makeServiceCall(url_add_beneficiary, ServiceHandler.POST, nameValuePair) ;
		            
		            Log.d("Response: ", "> " + jsonStr);
		 
		            if (jsonStr != null) 
		            {
		                try {
		                	JSONObject c = new JSONObject(jsonStr);
		                     
		                   
		                        String nameResponse = c.getString("name");
		                        String dobResponse = c.getString("dob");             
		                        user = new HashMap<String, String>();
		                        // adding each child node to HashMap key => value
		                        user.put("name", nameResponse);
		                        user.put("dob", dobResponse);
		                     
		                       Log.d("user: ", "> " + user);
		                       
		                  
		                } catch (JSONException e) {
		                    e.printStackTrace();
		                }  
		            } else {
		          
		            }
            
            }
            return null;    
        }
	     
	    @Override
	    protected void onPostExecute(Void result) {
	    
	    	super.onPostExecute(result);
	    	
	    	if(jsonStr== null||jsonStr.length()==2)
	    	{
	    		if(flagNetwork.contentEquals("1"))
	    		{
	    			
	    		}
	    		else
	    			if(hw_number.contentEquals(""))
		    			ShowMessage(getString(R.string.Information), "No Hardware Key", getResources().getDrawable(R.drawable.info) );
	    			else	
	    				ShowMessage(getString(R.string.Information), getString(R.string.NoNetwork), getResources().getDrawable(R.drawable.info) );
	    		
	    	
	    	}
	    	else
	    	{
	    		if(item!=null)
	    		{
	    	
	    			if(jsonStr.contentEquals("Vaccination Beneficiary ID is not correct"))
	    			{
	    				ShowMessage(getString(R.string.Information), getString(R.string.BenificiaryDonotExist) , getResources().getDrawable(R.drawable.info) );
			    		
	    			}
	    			else
		    		ShowMessage(getString(R.string.Success), getString(R.string.UpdatedSuccess), getResources().getDrawable(R.drawable.checkmark) );
		    		
	    		}	
	    		else
		    	{
		    		ShowMessage(getString(R.string.Success), getString(R.string.AddedSuccess ), getResources().getDrawable(R.drawable.checkmark) );
		    	
		    		etName.setText("");
		    		etDate.setText("");
		    		etNotifyNum.setText("");
		    		}
	    		
	    		
	    	
	    	}
        
	    	dismissProgress();
	    	
	    }    
    }
    
    ////////////
    private void deleteDialog() {
		 
	    AlertDialog.Builder alertDlg = new AlertDialog.Builder(this.getActivity());
	
	    alertDlg.setMessage(getString(R.string.ConfirmationDelete));
		
	 
	    alertDlg.setCancelable(false); // We avoid that the dialong can be cancelled, forcing the user to choose one of the options
	 
	    alertDlg.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
			 
		       
			 
		      public void onClick(DialogInterface dialog, int id) {
		 
		    	  DeleteRecord deleteRecord = new DeleteRecord();
		        		deleteRecord.execute();
		        		if(item !=null)
						{

		        			Button submitButton1 = (Button)getActivity().findViewById(R.id.button1);
		        			
		        			Button deleteButton = (Button)getActivity().findViewById(R.id.btn_deleteId);
		        			
		        			
							nameEditText.setEnabled(false);
							maleRadioButton.setEnabled(false);
							femaleRadioButton.setEnabled(false);
							dateOfbirth.setEnabled(false);
							cellNoEditText.setEnabled(false);
							submitButton1.setText("Edit");
							deleteButton.setVisibility(View.GONE);
						}
		        		 }
		 
		    }
		 
		     );
		 
		     alertDlg.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
		 
		    @Override
		 
		    public void onClick(DialogInterface dialog, int which) {
		 
		      // We do nothing
		 
		    }
		 
		    });
	 
	  
	 
	     alertDlg.create().show();
	  }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         onClickListener = new ButtonClickListener(this.getActivity());
         menuButton = (Button)getActivity().findViewById(R.id.menuadd_button_id);
         menuButton.setOnClickListener(onClickListener);
         deleteItem = (Button)getActivity().findViewById(R.id.btn_deleteId);
         
         deleteItem.setOnClickListener(
   		        new Button.OnClickListener() {
   		        	public void onClick(View v) {
   		        		
   		        		EditText	 etName1 = (EditText)getActivity().findViewById(R.id.enter_name_text);
   		 	    	String name1 = etName1.getText().toString();
   		 	    	
   		 	    	EditText	 etNotifyNum1 = (EditText)getActivity().findViewById(R.id.phone_num_text);
   		 	    	String	notify_num1 = etNotifyNum1.getText().toString();
   		 	    	
   		 	    	EditText	 etDate1 = (EditText)getActivity().findViewById(R.id.date_of_birth_text);
   		 	    	String dob1 = etDate1.getText().toString();
   		 	    
   		 	    	if(name1.equals("")||notify_num1.equals("")||dob1.equals(""))
   		 	    	{
   		 	    		
   		 	    
   		 			 ShowMessage(getString(R.string.Information), getString(R.string.FillMandatory), getResources().getDrawable(R.drawable.info) );
   		 	    		
   		 		 }
   		 		 else
   		 		 {
   		        		deleteDialog();
   		 		 }	
   		        		}
   		        	}
   		        );

         final Calendar c = Calendar.getInstance();
         mYear = c.get(Calendar.YEAR);
         mMonth = c.get(Calendar.MONTH);
         mDay = c.get(Calendar.DAY_OF_MONTH);
   	  
        dateOfbirth=(EditText)getActivity().findViewById(R.id.date_of_birth_text);
        maleRadioButton = (RadioButton)getActivity().findViewById(R.id.male_radio);
        femaleRadioButton = (RadioButton)getActivity().findViewById(R.id.female_radio);
        nameEditText = (EditText)getActivity().findViewById(R.id.enter_name_text);
    	cellNoEditText = (EditText)getActivity().findViewById(R.id.phone_num_text);
    	
    	submitButton = (Button)getActivity().findViewById(R.id.button1);
    	
    	InputMethodManager imm=(InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromInputMethod(dateOfbirth.getWindowToken(),0);
        dateOfbirth.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
    	 dateOfbirth.setOnClickListener(this);
    	  
    	
    	  
    	  maleRadioButton.setOnClickListener(
    		        new RadioButton.OnClickListener() {
    		        	public void onClick(View v) {
    		                	  // Code to be performed when 
    					  // the button is clicked
    		        		MaleSelected(v);
    		        		}
    		        	}
    		        );
    	  
    		
    		femaleRadioButton.setOnClickListener(
    			        new RadioButton.OnClickListener() {
    			        	public void onClick(View v) {
    			                	  // Code to be performed when 
    						  // the button is clicked
    			        		FemaleSelected(v);
    			        		}
    			        	}
    			        );
   
    		submitButton.setOnClickListener(
    		        new Button.OnClickListener() {
    		        	public void onClick(View v) {
    		                	  // Code to be performed when 
    					  // the button is clicked
    		        		SubmitClicked(v);
    		        		}
    		        	}
    		        );
    	
    	
    		
		if (item!=null) {
			
		Log.d("item not null" , item.toString());
		
			if(item.getText() != null)
			{
				nameEditText.setText(item.getText());
			}
			else
			{
				nameEditText.setText("");
				nameEditText.setHint("");
			}
		
			
			if (item.getSex().contentEquals("M")) {
				maleRadioButton.setChecked(true);
				femaleRadioButton.setChecked(false);
			}
			else
			{
				femaleRadioButton.setChecked(true);
				maleRadioButton.setChecked(false);
				
			}
			
			if(item.getDateOfBirth() != null)
			{
				dateOfbirth.setText(item.getDateOfBirth());
			}
			else
			{
				dateOfbirth.setText("");
				dateOfbirth.setHint("");
			
			}
			
			if(item.getMobileNumber() != null)
			{
				cellNoEditText.setText(item.getMobileNumber());
			}
			else{
				cellNoEditText.setText("");
				cellNoEditText.setHint("");
			}
			
			
			
			nameEditText.setEnabled(false);
			maleRadioButton.setEnabled(false);
			femaleRadioButton.setEnabled(false);
			dateOfbirth.setEnabled(false);
			cellNoEditText.setEnabled(false);
			submitButton.setText("Edit");
		}
		else
		{
					}	
	  
	}
	
	 
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		
    monthOfYear=monthOfYear+1;		
	dateOfbirth.setText(dayOfMonth+"-"+monthOfYear+"-"+year);
		
	
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		DatePickerDialog datePicker=  new DatePickerDialog(this.getActivity(), this, mYear, mMonth, mDay);
		   datePicker.show();
		return true;
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

	public void ResetClicked(View view)
	{
		nameEditText.setText("");
		cellNoEditText.setText("");
		femaleRadioButton.setChecked(false);
		maleRadioButton.setChecked(false);	
		
	}

	public static String getDefaults(String key, Context context) {
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    return preferences.getString(key, "");
	}

	public void ShowMessage(String title, String message, Drawable drawable )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

		builder.setMessage(message);
	
		builder.setIcon(drawable);
		builder.setTitle(title);
		builder.create().show();
	}
	public void createProgress()
	{
		String Processing = getResources().getString(R.string.Processing);
		String pleaseWait = getResources().getString(R.string.PleaseWait);
		
	pd = new ProgressDialog(context);
	pd.setTitle(Processing);
	pd.setMessage(pleaseWait);
	pd.setCancelable(false);
	pd.setIndeterminate(true);
	pd.show();
	}
	
	public void dismissProgress()
	{
		
		if (pd!=null) {
			pd.dismiss();
			submitButton.setEnabled(true);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		 DatePickerDialog datePicker=  new DatePickerDialog(this.getActivity(), this, mYear, mMonth, mDay);
		   datePicker.show();
	
	}
	

    private class DeleteRecord extends AsyncTask<Void, Void, Void> 
    {
    	
    	String name, dob , notify_num, sex , hw_number, jsonStr;
    	EditText etName, etDate,etNotifyNum;
    	// ProgressDialog pDialog= new ProgressDialog(context);
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            etName = (EditText)getActivity().findViewById(R.id.enter_name_text) ;
            etDate = (EditText)getActivity().findViewById(R.id.date_of_birth_text) ;
            etNotifyNum = (EditText)getActivity().findViewById(R.id.phone_num_text) ;
            
            url_add_beneficiary = getString(R.string.url_add_beneficiary);
            jsonStr = new String();
            createProgress();
    	}
   	 
    	
        @Override
        protected Void doInBackground(Void... arg0) 
        {
    		if(item!=null)
    		{
    			url_add_beneficiary = url_add_beneficiary+item.getId()+"/";
    				
    			// Creating service handler class instance
    			ServiceHandler sh = new ServiceHandler();
           
    			// Making a request to url and getting response
    			Log.d("url_add_beneficiary",url_add_beneficiary);
               jsonStr = sh.makeServiceCall(url_add_beneficiary, ServiceHandler.DELETE) ;
            
               Log.d("Response: ", "> " + jsonStr);
 
    		}
            return null;    
        }
	     
	    @Override
	    protected void onPostExecute(Void result) {
	    
	    	super.onPostExecute(result);
	    	if(jsonStr != null)
	    	{
	    		
	    		etName.setText("");
	    		etDate.setText("");
	    		etNotifyNum.setText("");
	    		dismissProgress();
	    		
	    		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	    		builder.setMessage(getString(R.string.DeletedSuccess));
	    	
	    		builder.setIcon(getResources().getDrawable(R.drawable.checkmark));
	    		builder.setTitle(getString(R.string.Success));
	    		

	    		builder.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
	    			 
	    		       
	    			 
	    		      public void onClick(DialogInterface dialog, int id) {
	    		 
	    		    	  Intent i = new Intent(getActivity(), ViewDetailFragment.class);
	    					startActivity(i);
	    		        		 }
	    		 
	    		    }
	    		 
	    		     );
	    		
	    		builder.create().show();
	    		
	    		//ShowMessage(), getString(R.string.DeletedSuccess), getResources(). );
	    		
	    		
	    		//Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_LONG).show();
				
			
	    		//Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_LONG).show();
				
			
	    	}
	    	else
	    	{
	    		dismissProgress();
	    		ShowMessage(getString(R.string.Information), getString(R.string.NoNetwork), getResources().getDrawable(R.drawable.info) );
	    	}
	    	
	    	
	    }    
    }
	
	
}
