package com.social.healthometer;
import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceQuery;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.social.healthometer.adapter.CustomArrayAdapter;
import com.social.healthometer.adapter.ExpandableListAdapter;
import com.social.healthometer.model.TodoItem;
import com.social.healthometer.model.SearchFilterItem;
import com.social.utilities.ServiceHandler;

import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;



public class ViewDetailFragment extends Activity {
	public static TodoItem ITEM_TO_EDIT = null;

	private Button searchButton;
	private EditText searchEditText;
	private ListView listView;
  
	private CustomArrayAdapter customArrayAdapter;
	private ArrayList<TodoItem> listItems;
	private MobileServiceClient mClient;
	private MobileServiceTable<TodoItem> mToDoTable;
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
			 imgExpandContract.setBackgroundResource(R.drawable.ic_maximized);
			   		
			 imgExpandContract.setOnClickListener(new OnClickListener() {
		   		  @Override
				  public void onClick(View cv)
		     	  {
		   			
		   			  LinearLayout contactLayout = (LinearLayout)findViewById(R.id.frameFilters);
		   			  if(contactLayout.getVisibility() == View.VISIBLE)
		   			  {
		   				  contactLayout.setVisibility(View.GONE);
		   				cv.setBackgroundResource(R.drawable.ic_minimized);
		   		   		
		   			
		   			  }
		   			  else
		   			  {
		   				contactLayout.setVisibility(View.VISIBLE);
		   				cv.setBackgroundResource(R.drawable.ic_maximized);
		   			  }
		   				
				  }
			  });    
	   

	       
	    
	        
	      
	        //expandableListView.setFadingEdgeLength(3);
	        
   
	        ////////////////

	        
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
			try{
			
			
			listItems = new ArrayList<TodoItem>();
			customArrayAdapter = new CustomArrayAdapter(getApplicationContext(),listItems);
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
				ShowMessage("ListView", "Exception:"+ee);
			}
			
			
			refreshItemsFromTable();
			

	}
	
	
	
	
	public void SearchClicked()
	{
		PopulateSearchedDetails populateSearchedDetails = new PopulateSearchedDetails();
		populateSearchedDetails.execute();

		
		
	}
	
	  private class PopulateSearchedDetails extends AsyncTask<Void, Void, Void> 
	    {
	    	 ArrayList<TodoItem> arrayItem = new ArrayList<TodoItem>();
	            String url_add_beneficiary, name, notifyNum , hw_number;
	    	// ProgressDialog pDialog= new ProgressDialog(context);
	    	@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            
	          //  createProgress();
	    
	    	}
		
	        @Override
	        protected Void doInBackground(Void... arg0) 
	        {
	            // Creating service handler class instance
	            ServiceHandler sh = new ServiceHandler();
	          
	            // Making a request to url and getting response
	        
	            
	            url_add_beneficiary = getString(R.string.url_add_beneficiary);
	        	
		    	EditText etName = (EditText)findViewById(R.id.txtSearchName_id);
		    	
		    	
	            name = (etName).getText().toString();
	            notifyNum = ((EditText)findViewById(R.id.txtSearchPhone_id)).getText().toString();
	      
	            hw_number= "false";
	            
	            hw_number = ViewDetailFragment.getDefaults("mobileNo" , getApplicationContext());
	            
	        
	            
	           
	            
	            if(name.length() <= 0 && notifyNum.length() >= 0 )
	    		{

		            url_add_beneficiary = url_add_beneficiary+"?notif_num="+notifyNum;
	    		}
	    		else
	    		if(notifyNum.length() <= 0 && name.length() >= 0)
	    		{

		            url_add_beneficiary = url_add_beneficiary+"?name="+name;
	    			
	    		}
	    		else
	    			if(notifyNum.length() >= 0 && name.length() >= 0)
	    			{

	    	            url_add_beneficiary = url_add_beneficiary+"?name="+name+"&notif_num="+notifyNum;
	    			}
	    			else
	    			{
	    				
	    			}
	            url_add_beneficiary = url_add_beneficiary +"&hw_num="+hw_number;
	            
	            Log.d("url_add_beneficiary: ", "> " + url_add_beneficiary);
		           
	            String  jsonStr = sh.makeServiceCall(url_add_beneficiary, ServiceHandler.GET) ;
	            
	            Log.d("Response: ", "> " + jsonStr);
	           
	           
	            		
	            if (jsonStr != null) {
	                try {
	                	JSONArray jArray = new JSONArray(jsonStr);
	                     
	                	  Log.d("jArray: ", "> " + jArray.toString());
	                      
	                	  Log.d("jArray.length(): ", "> " + jArray.length());
	                	for(int i=0; i<jArray.length() ; i++)
	                	{
	                		JSONObject c = jArray.getJSONObject(i);
	                		 TodoItem item = new TodoItem();
	                   
	                		 /*
	                		  * name
								notif_num
								dob
								sex
								gaurdian_name
								language
								hw_num
								
								{
  "notify_number": "9390681183",
  "health_worker_phone": "9390681183",
  "sex": "M",
  "gaurdian_name": "mahesh",
  "lang": "HIN",
  "reg_code": 7799,
  "ModifiedOn": "2014-08-19T17:04:21Z",
  "name": "suresh",
  "dob": "2014-08-15",
  "is_verified": false,
  "Id": "p8otBedSSMOdKAPoaO1JiQ"
}
	                		  */
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
	                    //    customArrayAdapter.add(item);
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
		    	 Log.d("arrayItem: ", "> " + arrayItem);
		    	 Iterator<TodoItem> itr =  arrayItem.iterator();
		    	 
		    	int er =0;
		    	customArrayAdapter.clear();
		    	while(itr.hasNext())
		    	{
		    	// Log.d("arrayItem: ", "> " +);
		    	// er++;
		    		customArrayAdapter.add(itr.next());
		    	}
		    	
		    	customArrayAdapter.notifyDataSetChanged();	  
		    	
		    	 
		    	//
		    	//customArrayAdapter.notifyDataSetChanged();	  
		    	/*
		    	EditText etName = (EditText)getActivity().findViewById(R.id.enter_name_text);
		    	etName.setText(user.get("name"));
		    	
		    	EditText etDate = (EditText)getActivity().findViewById(R.id.date_of_birth);
		    	etDate.setText(user.get("dob"));
		    	//dismissProgress();
		    	*/
		    }    
	    }


	  public static String getDefaults(String key, Context context) {
		    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		    return preferences.getString(key, "false");
		}
	  
	private void refreshItemsFromTable() {

		// Get the items that weren't marked as completed and add them in the
		// adapter
		/*
		 *
		 * mToDoTable.where().field("complete").eq(val(false)).execute(new TableQueryCallback<TodoItem>() {
		 

			public void onCompleted(List<TodoItem> result, int count, Exception exception, ServiceFilterResponse response) {
				if (exception == null) {
					customArrayAdapter.clear();

					for (TodoItem item : result) {
						customArrayAdapter.add(item);
					}

				} else {
					ShowMessage("exception", exception.toString());
				}
			}
		});
		*/
	}

	
	
	public void ShowMessage(String title,
			String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
	
	
	
	  
	
}
