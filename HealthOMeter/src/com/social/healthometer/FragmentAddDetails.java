package com.social.healthometer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.social.actionlisteners.ButtonClickListener;
import com.social.healthometer.model.TodoItem;
import com.social.utilities.ServiceHandler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

public class FragmentAddDetails extends Fragment implements OnDateSetListener,OnTouchListener,OnClickListener  {
	 private ProgressBar spinner;
	 
	 TelephonyManager telephonyManager;
		
	 String namePrev , dobPrev , cellNoPrev ;
		String deviceNum , imei;
	private MobileServiceClient mClient;
	private Boolean ready;
	RadioButton maleRadioButton;
	RadioButton femaleRadioButton;
	Button submitButton;
	Button resetButton;
	EditText nameEditText;
	EditText cellNoEditText;
	DatePicker DOBDatePicker;
	private Button searchButton;
	private EditText searchEditText;
	private ListView listView;
	//private DatePicker datePicker;
	private EditText dateOfbirth;
	private MobileServiceTable<TodoItem> mToDoTable;
	private TodoItem item;
    private int mYear, mMonth, mDay,mHour,mMinute; 
    
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
		
		url_add_beneficiary = getString(R.string.url_add_beneficiary);
		
	      
			//Log.d("deviceNum", deviceNum);
		
		return rootView;
	}
	
    
    /////////
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
		Log.d("update clickced", "msg:");
		
		GetProfileDetails addBenificiary = new GetProfileDetails();
		addBenificiary.execute();
		
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

    
    
    private class GetProfileDetails extends AsyncTask<Void, Void, Void> 
    {
    	HashMap<String, String> user;
    	 String  jsonStr, flagNetwork;
    	String name, dob , notify_num, sex , hw_number;
    	EditText etName, etDate,etNotifyNum;
    	
    	// ProgressDialog pDialog= new ProgressDialog(context);
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
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
    						
    				      String  dateOfbirth=((EditText)getActivity().findViewById(R.id.date_of_birth_text)).getText().toString();
    				       // maleRadioButton = (RadioButton)getActivity().findViewById(R.id.male_radio);
    				       // femaleRadioButton = (RadioButton)getActivity().findViewById(R.id.female_radio);
    				      String  nameEditText = ((EditText)getActivity().findViewById(R.id.enter_name_text)).getText().toString();
    				    String	cellNoEditText = ((EditText)getActivity().findViewById(R.id.phone_num_text)).getText().toString();
    				    	
    				    	
    					if(dateOfbirth.contentEquals(dobPrev)&&nameEditText.contentEquals(namePrev)&&cellNoEditText.contentEquals(cellNoPrev))
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
	    		 Log.d("dob","dob is null");
	    		
        	
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            Log.d("dob",dob);
            
            // Making a request to url and getting response
            Log.d("url_add_beneficiary",url_add_beneficiary);
            
            hw_number= "false";
            
            hw_number = FragmentAddDetails.getDefaults("mobileNo" , context);
            
        
         // Building post parameters, key and value pair
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);
            //nameValuePair.add(new BasicNameValuePair("Id", "1"));
            nameValuePair.add(new BasicNameValuePair("name", name));
           nameValuePair.add(new BasicNameValuePair("notif_num", notify_num));
            nameValuePair.add(new BasicNameValuePair("dob", dob));
            nameValuePair.add(new BasicNameValuePair("sex", sex));
      		
            nameValuePair.add(new BasicNameValuePair("hw_num", hw_number));
            
      		
            
              jsonStr = sh.makeServiceCall(url_add_beneficiary, ServiceHandler.POST, nameValuePair) ;
            
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                	JSONObject c = new JSONObject(jsonStr);
                     
                   
                        String name = c.getString("name");
                        String dob = c.getString("dob");             
                        user = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        user.put("name", name);
                        user.put("dob", dob);
                     
                       Log.d("user: ", "> " + user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }  
            } else {
            	
            
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            
            
            return null;    
        }
	     
	    @Override
	    protected void onPostExecute(Void result) {
	    
	    	super.onPostExecute(result);
	    	
	    	if(jsonStr != null)
	    	{
	    		if(item!=null)
	    		ShowMessage(getString(R.string.Success), getString(R.string.AddedSuccess), getResources().getDrawable(R.drawable.checkmark) );
	    	else
	    		ShowMessage(getString(R.string.Success), getString(R.string.UpdatedSuccess), getResources().getDrawable(R.drawable.checkmark) );
	    	
	    		etName.setText("");
	    		etDate.setText("");
	    		etNotifyNum.setText("");
	        	
	    	}
	    	else
	    	{
	    		if(flagNetwork.contentEquals("1"))
	    		{
	    			
	    		}
	    		else
	    		ShowMessage(getString(R.string.Information), getString(R.string.NoNetwork), getResources().getDrawable(R.drawable.info) );
	    		
	    	}
        	
        	
	    	/*
	    	if(user.isEmpty())
	    	{
	    		
	    	}
	    	else
	    	{
	    		EditText etName = (EditText)getActivity().findViewById(R.id.enter_name_text);
	    		if(user.containsKey("name"))
	    		etName.setText(user.get("name"));
	    	
	    		EditText etDate = (EditText)getActivity().findViewById(R.id.date_of_birth_text);
	    		if(user.containsKey("dob"))
	    		etDate.setText(user.get("dob"));
	    	}
	    	*/
	    	dismissProgress();
	    	
	    }    
    }
    
    ////////////
    

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         onClickListener=new ButtonClickListener(this.getActivity());
         menuButton = (Button)getActivity().findViewById(R.id.menuadd_button_id);
         menuButton.setOnClickListener(onClickListener);
         deleteItem = (Button)getActivity().findViewById(R.id.btn_deleteId);
         
         deleteItem.setOnClickListener(
   		        new Button.OnClickListener() {
   		        	public void onClick(View v) {
   		        		DeleteRecord deleteRecord = new DeleteRecord();
   		        		deleteRecord.execute();
   		        		
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
				nameEditText.setText("");

		
			
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
				Log.d("dob not null", item.getDateOfBirth() );
				dateOfbirth.setText(item.getDateOfBirth());
			}
			else
			{
				dateOfbirth.setHint("");
				Log.d("dob", "null");
			}
			
			if(item.getMobileNumber() != null)
			{
				Log.d("mob not null", item.getMobileNumber() );
				
				cellNoEditText.setText(item.getMobileNumber());
			}
			else{
				Log.d("mob", "null");
				cellNoEditText.setHint("");
			}
			
			
		 namePrev = nameEditText.getText().toString();
		 dobPrev = dateOfbirth.getText().toString();
		 cellNoPrev =cellNoEditText.getText().toString();;
			
			nameEditText.setEnabled(false);
			maleRadioButton.setEnabled(false);
			femaleRadioButton.setEnabled(false);
			dateOfbirth.setEnabled(false);
			cellNoEditText.setEnabled(false);
			submitButton.setText("Edit");
			
		}
		else
		{
			Log.d("item is null" , "");
		}	
		//cellNoEditText.setText(deviceNum);
        
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
	    return preferences.getString(key, "false");
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
	
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null &&
	    		activeNetworkInfo.isConnectedOrConnecting();
	   
	}
	
	public void updateInfo()
	{
		
		
	}

	
	@Override
	public void onClick(View v) {
		 DatePickerDialog datePicker=  new DatePickerDialog(this.getActivity(), this, mYear, mMonth, mDay);
		   datePicker.show();
	
	}
	

    private class DeleteRecord extends AsyncTask<Void, Void, Void> 
    {
    	HashMap<String, String> user;
    	String name, dob , notify_num, sex , hw_number, jsonStr;
    	EditText etName, etDate,etNotifyNum;
    	// ProgressDialog pDialog= new ProgressDialog(context);
    	@Override
        protected void onPreExecute() {
            super.onPreExecute();
            createProgress();
    	}
   	 
    	
        @Override
        protected Void doInBackground(Void... arg0) 
        {
    				if(item!=null)
    				{
    					url_add_beneficiary = url_add_beneficiary+item.getId()+"/";
    				}
    		 
      
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
	    		 Log.d("dob","dob is null");
	    		
        	
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            Log.d("dob",dob);
            
            // Making a request to url and getting response
            Log.d("url_add_beneficiary",url_add_beneficiary);
            
        
            
              jsonStr = sh.makeServiceCall(url_add_beneficiary, ServiceHandler.DELETE) ;
            
            Log.d("Response: ", "> " + jsonStr);
 
            
            return null;    
        }
	     
	    @Override
	    protected void onPostExecute(Void result) {
	    
	    	super.onPostExecute(result);
	    	if(jsonStr != null)
	    	{
	    		
	    		ShowMessage(getString(R.string.Success), getString(R.string.DeletedSuccess), getResources().getDrawable(R.drawable.checkmark) );
	    		etName.setText("");
	    		etDate.setText("");
	    		etNotifyNum.setText("");
	    	}
	    	else
	    		ShowMessage(getString(R.string.Information), getString(R.string.NoNetwork), getResources().getDrawable(R.drawable.info) );
        	
        	
	    	/*
	    	if(user.isEmpty())
	    	{
	    		
	    	}
	    	else
	    	{
	    		EditText etName = (EditText)getActivity().findViewById(R.id.enter_name_text);
	    		//if(user.containsKey("name"))
	    		etName.setText("");
	    	
	    		EditText etDate = (EditText)getActivity().findViewById(R.id.date_of_birth_text);
	    		//if(user.containsKey("dob"))
	    		etDate.setText("");
	    	}
	    	*/
	    	dismissProgress();
	    	
	    }    
    }
	
	
}
