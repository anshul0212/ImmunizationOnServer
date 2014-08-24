package com.social.healthometer;

import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.social.healthometer.adapter.CustomArrayAdapter;

import com.social.healthometer.model.TodoItem;

import com.social.utilities.ServiceHandler;




public class ViewDetailFragment extends Activity {
	public static TodoItem ITEM_TO_EDIT = null;

	private Button searchButton;
	private EditText searchEditText;
	private ListView listView;
	
	private ProgressDialog pd;
	private CustomArrayAdapter customArrayAdapter;
	private ArrayList<TodoItem> listItems;
	
	Boolean ready;
	 /** An array of strings to populate dropdown list */
    String[] actions = new String[] {
        "Menu",
        "Add Detail",
        "Search",
        "Verify",
        "Setting"
    };

		
		
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fragment_edit_patient);
	        ///////////////////////
	        /** Create an array adapter to populate dropdownlist */
	    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actions);

	    	/** Enabling dropdown list navigation for the action bar */
	    	getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);



	    	/** Defining Navigation listener */
	    	ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {

	    	    @Override
	    	    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
	    	     

	    				switch (itemPosition) {
	    				case 0:
	    							//Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_LONG).show();
	    							//Intent i = new Intent(getBaseContext(), AddDetailsActivity.class);
	    							//startActivity(i);
	    					
	    				    break;
	    				case 1:
	    					Intent i = new Intent(getBaseContext(), AddDetailsActivity.class);
	    					startActivity(i);
	    				    break;
	    				case 2:
	    					Intent j = new Intent(getBaseContext(), ViewDetailFragment.class);
	    					startActivity(j);
	    				    break;
	    				case 3:
	    					Intent k = new Intent(getBaseContext(), VerificationActivity.class);
	    					startActivity(k);
	    				    break;
	    				case 4:
	    					Intent l = new Intent(getBaseContext(), SettingsActivity.class);
	    					startActivity(l);
	    				    break;
	    				default:
	    				    break;
	    				}
	    				return true;
	    			    }
	    	};

	    	/** Setting dropdown items and item navigation listener for the actionbar */
	    	getActionBar().setListNavigationCallbacks(adapter, navigationListener);  
	    	final Button btnMenu = (Button) findViewById(R.id.menusearch_button_id);
			btnMenu.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_LONG).show();
					Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
					startActivity(i);
				}
			});	
	        
	    
			 ImageView imgExpandContract = (ImageView)findViewById(R.id.toggleImage);
			 imgExpandContract.setFocusable(true);
			imgExpandContract.setBackgroundResource(R.drawable.arrowhead_up);
			   		
			 imgExpandContract.setOnClickListener(new OnClickListener() {
		   		  @Override
				  public void onClick(View cv)
		     	  {
		   			  LinearLayout contactLayout = (LinearLayout)findViewById(R.id.frameFilters);
		   			  if(contactLayout.getVisibility() == View.VISIBLE)
		   			  {
		   				  contactLayout.setVisibility(View.GONE);
		   				cv.setBackgroundResource(R.drawable.arrowhead_down);
		   		   	 }
		   			  else
		   				 if(contactLayout.getVisibility() == View.GONE)
		   			  {
		   				contactLayout.setVisibility(View.VISIBLE);
		   				cv.setBackgroundResource(R.drawable.arrowhead_up);
		   			  }
		   				
				  }
			  });    
	  
	        searchButton = (Button)findViewById(R.id.search_btn_id);
	        searchButton.setOnClickListener(
			        new Button.OnClickListener() {
			        	public void onClick(View v) {
			        	
			        		SearchClicked();
			        		}
			        	}
			        );
			   
			listView = (ListView)findViewById(R.id.listView1);
			searchEditText = (EditText)findViewById(R.id.editText1);
			try
			{
				listItems = new ArrayList<TodoItem>();
				customArrayAdapter = new CustomArrayAdapter(this,listItems);
				listView.setAdapter(customArrayAdapter);
				listView.setOnItemClickListener(new ListView.OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView l, View v, int position, long id) {
						TodoItem item = (TodoItem) customArrayAdapter.getItem(position);
					    Intent intent = new Intent(getBaseContext(), AddDetailsActivity.class);
					    ITEM_TO_EDIT = item;
					    startActivity(intent);
					  }
				} );
			}catch( Exception ee){
				ShowMessage("ListView", "Exception:"+ee , getResources().getDrawable(R.drawable.cancel));
			}
			refreshItemsFromTable();
	}
	
	public void SearchClicked()
	{
		PopulateSearchedDetails populateSearchedDetails = new PopulateSearchedDetails();
		populateSearchedDetails.execute();	
	}
	
	public void createProgress()
	{
		String Processing = getResources().getString(R.string.Processing);
		String pleaseWait = getResources().getString(R.string.PleaseWait);
		pd = new ProgressDialog(this);
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
			
		}
	}
	
	
	
	  private class PopulateSearchedDetails extends AsyncTask<Void, Void, Void> 
	    {
	    	 ArrayList<TodoItem> arrayItem = new ArrayList<TodoItem>();
	         String url_get_beneficiary, name, notifyNum , hw_number, jsonStr;
	    	// ProgressDialog pDialog= new ProgressDialog(context);
	    	@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            createProgress();
	            name = new String();
	            notifyNum = new String();
	            hw_number = new String();
	            jsonStr = new String("");
	    
	    	}
		
	        @Override
	        protected Void doInBackground(Void... arg0) 
	        {
	            // Creating service handler class instance
	            ServiceHandler sh = new ServiceHandler();
	          
	            url_get_beneficiary = getString(R.string.url_get_beneficiary);
	        	
		    	EditText etName = (EditText)findViewById(R.id.txtSearchName_id);
		    	
		    	
	            name = (etName).getText().toString();
	            
	            EditText   etnotifyNum = (EditText)findViewById(R.id.txtSearchPhone_id);
	      
	            notifyNum = (etnotifyNum).getText().toString();
	            hw_number= "false";
	            
	            hw_number = ViewDetailFragment.getDefaults("mobileNo" , getApplicationContext());
	            
	        
	            if(name.length() <= 0 && notifyNum.length() > 0 )
	    		{

	            	Log.d("notifffff: len", "> " + notifyNum.length());
	    		      
		            url_get_beneficiary = url_get_beneficiary+"?notif_num="+notifyNum+"&hw_num="+hw_number;
	    		}
	    		else
	    		if(notifyNum.length() <= 0 && name.length() > 0)
	    		{

	    			url_get_beneficiary = url_get_beneficiary+"?name="+name+"&hw_num="+hw_number;
	    			
	    		}
	    		else
	    			if(notifyNum.length() > 0 && name.length() > 0)
	    			{

	    				  Log.d("no null: ", "> " + url_get_beneficiary);
	    			      
	    				  url_get_beneficiary = url_get_beneficiary+"?name="+name+"&notif_num="+notifyNum+"&hw_num="+hw_number;
	    			}
	    			else
	    			{
	    				url_get_beneficiary = url_get_beneficiary +"?hw_num="+hw_number;
	    				  Log.d("null: ", "> " + url_get_beneficiary);
	    			      
	    			}
	            
	            
	            Log.d("url_get_beneficiary: ", "> " + url_get_beneficiary);
	            Log.d("jsonStr: b4 ", "> " + jsonStr);
		           
	              jsonStr = sh.makeServiceCall(url_get_beneficiary, ServiceHandler.GET, null) ;
	            
	            Log.d("Response: ", "> " + jsonStr);
	           	
	            if (jsonStr != null) 
	            {
	                try 
	                {
	                	JSONArray jArray = new JSONArray(jsonStr);
	                     
	                	  Log.d("jArray: ", "> " + jArray.toString());
	                      
	                	  Log.d("jArray.length(): ", "> " + jArray.length());
	                	for(int i=0; i<jArray.length() ; i++)
	                	{
	                		JSONObject c = jArray.getJSONObject(i);
	                		 TodoItem item = new TodoItem();
	                  
	                		 String name = c.getString("name");
	                		 String dob = c.getString("dob");    
	                		 String notify_number = c.getString("notify_number");
	                		 String health_worker_phone = c.getString("health_worker_phone");    
	                		 String sex = c.getString("sex");
	                		 String gaurdian_name = c.getString("gaurdian_name");    
	                		 String lang = c.getString("lang");
	                		 String reg_code = c.getString("reg_code");    
	                		 String ModifiedOn = c.getString("ModifiedOn");
	                		 String is_verified = c.getString("is_verified");    
	                		 String Id = c.getString("Id");
	                		 
	                		 item.setDateOfBirth(dob);
	                		 item.setText(name);
	                		 item.setNotifyNumber(notify_number);
	                		 item.setHw_number(health_worker_phone);
	                		 item.setSex(sex);
	                		 item.setGaurdian_name(gaurdian_name);
	                		 item.setLang(lang);
	                		 item.setReg_code(reg_code);
	                		 item.setModifiedOn(ModifiedOn);
	                		 item.setIs_verified(is_verified);
	                		 item.setId(Id);
	                		 
	                		
	                        arrayItem.add(item);
	                        Log.d("itemgetText: ", "> " + item.getText());
	                        Log.d("arrayItem: ", "> " + arrayItem);
	                  
	                	}
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
		    	 Log.d("post execute jsonStr: ", "> " + jsonStr);
                 
                
		    	customArrayAdapter.clear();
		    	
		    		if(jsonStr == null)
		    		{
		    			dismissProgress();
			    		ShowMessage( getString(R.string.Information), getString(R.string.NoNetwork), getResources().getDrawable(R.drawable.info) );
			    		
		    		}
		    		else
		    			if(jsonStr.length() == 2)
				    	{
				    		dismissProgress();
				    		ShowMessage( getString(R.string.Information), getString(R.string.NoSearchResult), getResources().getDrawable(R.drawable.info) );
				    		
				    	}
		    			else
		    			{
		    				Iterator<TodoItem> itr =  arrayItem.iterator();
		    	 
					    	
					    	while(itr.hasNext())
					    	{
					    		customArrayAdapter.add(itr.next());
					    	}
					    	
					    	customArrayAdapter.notifyDataSetChanged();	  
					    	dismissProgress();
		    	
		    			}
		    }    
	    }


	  public static String getDefaults(String key, Context context) {
		    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		    return preferences.getString(key, "false");
		}
	  
	private void refreshItemsFromTable() {


	}

	
	
	public void ShowMessage(String title,
			String message, Drawable drawable)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(drawable);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
	
	
	
	  
	
}
