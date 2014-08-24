package com.social.healthometer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.social.healthometer.adapter.CustomArrayAdapter;
import com.social.healthometer.model.TodoItem;
import com.social.utilities.ServiceHandler;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;

public class PendingListFragment extends Fragment implements OnItemClickListener  
{
	 
	 String[] member_names;
	 TypedArray profile_pics;
	 String[] mob_num;
	 String[] code;
	 SparseBooleanArray mCheckStates ;
	 private CustomArrayAdapter customArrayAdapter;
		private ProgressDialog pd;
		Boolean ready;
		ListView mylistview ;
	 String url_add_beneficiary;
	// List<CheckBoxItem> rowCheckBoxItems ;
		private ArrayList<TodoItem> listItems;
		//private ListView mylistview ;

	 @Override
	    public View onCreateView(LayoutInflater inflater, 
	              ViewGroup container, Bundle savedInstanceState) {
	        
	  View view = inflater.inflate(R.layout.fragment_pending,  container, false);
	   mylistview = (ListView) view.findViewById(R.id.list);
	//  l.setBackgroundColor(color.dark_red_color);
		url_add_beneficiary = getString(R.string.url_add_beneficiary);
		
		 
	  
	   mylistview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	  
	  listItems = new ArrayList<TodoItem>();  
	//  rowCheckBoxItems = new ArrayList<CheckBoxItem>();  
	  customArrayAdapter = new CustomArrayAdapter(getActivity().getApplicationContext(),listItems);
	
	 // mylistview.setItemsCanFocus(true);
	  mylistview.setAdapter(customArrayAdapter);
	 // ShowMessage("Success", "MobileServiceClient created ii successfull");
	  


	//  profile_pics.recycle();
	mylistview.setFocusable(false);
	 mylistview.setOnItemClickListener( this);
	 
	// Toast.makeText(getActivity().getApplicationContext(), "hjhghg",	    Toast.LENGTH_SHORT).show();

	  
	
	  
	
		Button btnObjVerified = (Button)view.findViewById(R.id.verify_button_id);
		btnObjVerified.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View view) {
            	
            	int y1 = mylistview.getCount() ;
            	
            	
                
            	if(y1>0)
            	{
            		createDialog();
            	}
            	
            }});
		
		   
		
		
		
		refreshItemsFromTable();
	  
	  return view;
	  

	 }


	public void createProgress()
	{
		String Processing = getResources().getString(R.string.Processing);
		String pleaseWait = getResources().getString(R.string.PleaseWait);
		
	pd = new ProgressDialog(this.getActivity());
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
	private void createDialog() {
		 
	    AlertDialog.Builder alertDlg = new AlertDialog.Builder(this.getActivity());
	 
	     
	
		  alertDlg.setMessage(getString(R.string.ConfirmationVerifiy));
		
	 
	    alertDlg.setCancelable(false); // We avoid that the dialong can be cancelled, forcing the user to choose one of the options
	 
	    alertDlg.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
			 
		       
			 
		      public void onClick(DialogInterface dialog, int id) {
		 
		    	  VerifyRecords verifyRecords= new VerifyRecords();
	            	verifyRecords.execute();
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
	private class VerifyRecords extends AsyncTask<Void , Void , Void>
	{
        String url_add_beneficiary ;
        String networkFlag ;
        int count;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createProgress();
            networkFlag = new String("0");
            count=0;
    	}
        
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			 // Creating service handler class instance
			
			TodoItem item = new TodoItem();
			 url_add_beneficiary = getString(R.string.url_add_beneficiary);
			 int y= mylistview.getCount() ;
         	
         	
        	 for (int i = 0; i < y; i++)
        	  {
        		  networkFlag ="1";
        		  item = customArrayAdapter.getItem(i);
        		
                  if ( item.isChecked()	)
                  {
                	  String id = item.getId();
                    
                    // Making a request to url and getting response
                    
                    url_add_beneficiary = url_add_beneficiary+id+"/";
                    
                    // Building post parameters, key and value pair
                       List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);
                       //nameValuePair.add(new BasicNameValuePair("Id", "1"));
                       
                       nameValuePair.add(new BasicNameValuePair("is_verified", "1"));
                     
                       Log.d("url_add_beneficiary2",url_add_beneficiary);
                       ServiceHandler sh = new ServiceHandler();
                       
                    String  jsonStr = sh.makeServiceCall(url_add_beneficiary, ServiceHandler.POST , nameValuePair) ;
            
                   
                    if(jsonStr == null)
                    {
                    	networkFlag ="2";
                    	break;
                    }
                    count++;
                    Log.d("Response: ", "> " + jsonStr);
                   
                    
                    
                	  listItems.remove(i);
                	 y-- ;
                	 i-- ;

                  } 
        	  }
        	
        	
			return null;
		}
		
		  @Override
		    protected void onPostExecute(Void result) {
				super.onPostExecute(result);
			if(networkFlag.contentEquals("1"))
			{
			  dismissProgress();
			  customArrayAdapter.notifyDataSetChanged(); 
              mylistview.clearChoices();
              if( count >0)
              ShowMessage(getString(R.string.Success),count+" "+getString(R.string.Records)+" "+getString(R.string.VerifiedSuccess) , getResources().getDrawable(R.drawable.checkmark) );
              else
              {
            	  
              }
			}
			else
				if(networkFlag.contentEquals("2"))
				{
				 dismissProgress();
				 ShowMessage(getString(R.string.Information), getString(R.string.NoNetwork), getResources().getDrawable(R.drawable.info) );
		    		
			}
				
             
		  }
		    
			  
		
	}
	
		
		
		
	 @Override
	 public void onItemClick(AdapterView<?> parent, View view, int position,
	   long id) {
		
	 }
	 

	    
	    private class PopulatePendingDetails extends AsyncTask<Void, Void, Void> 
	    {
	    	 String  jsonStr;
	    	 ArrayList<TodoItem> arrayItem ;
	            String hw_number;
	    	// ProgressDialog pDialog= new ProgressDialog(context);
	    	@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            arrayItem = new ArrayList<TodoItem>();
	            jsonStr = new String();
	            
	            createProgress();
	    
	    	}
		
	        @Override
	        protected Void doInBackground(Void... arg0) 
	        {
	            // Creating service handler class instance
	            ServiceHandler sh = new ServiceHandler();
	            
                url_add_beneficiary = getString(R.string.url_add_beneficiary);
       		
	            // Making a request to url and getting response
	          
	            hw_number= "false";
	            
	            hw_number = ViewDetailFragment.getDefaults("mobileNo" , getActivity());
	            
	        
	   		 
	            url_add_beneficiary = url_add_beneficiary+"?is_verified=0"+"&hw_num="+hw_number;
	            Log.d("url_add_beneficiary",url_add_beneficiary);
	        	
	              jsonStr = sh.makeServiceCall(url_add_beneficiary, ServiceHandler.GET) ;
	            
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
	                		 
	                		 item.setDateOfBirth(dob.toString());
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
		    	
		    	Log.d("jsonStr: ", "> " + jsonStr);
		    	 Log.d("arrayItem: ", "> " + arrayItem);
		    	 if(jsonStr != null)
		    	 {
		    		 if(jsonStr.length() > 2)
		    		 {
		    			 Iterator<TodoItem> itr =  arrayItem.iterator();
				    	 
		 		    	int er =0;
		 		    	while(itr.hasNext())
		 		    	{
		 		    	// Log.d("arrayItem: ", "> " +);
		 		    	// er++;
		 		    		customArrayAdapter.add(itr.next());
		 		    	}
		 		    	dismissProgress();
		 		    	
		    		 }
		    		 else
		    		 {
		    			 dismissProgress();
		    			 ShowMessage(getString(R.string.Information), getString(R.string.NoSearchResult ), getResources().getDrawable(R.drawable.info) );
		 		    	
		    		 }
		    	 }
		    	 else
		    	 {
		    		 
		    		 dismissProgress();
		    			ShowMessage(getString(R.string.Information), getString(R.string.NoNetwork), getResources().getDrawable(R.drawable.info) );
			    		
		    	 }
		    		 
		    	
		    	
		    	
		    }    
	    }
	    
	    public static String getDefaults(String key, Context context) {
		    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		    return preferences.getString(key, "false");
		}

	 private void refreshItemsFromTable() {

			// Get the items that weren't marked as completed and add them in the
			// adapter
	
		 PopulatePendingDetails populatePendingDetails = new PopulatePendingDetails();
		 
		 populatePendingDetails.execute();
	
		}

		public void ShowMessage(String title, String message , Drawable drawable)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
			builder.setInverseBackgroundForced(true);
			
			builder.setMessage(message);
			
			builder.setIcon(drawable);
			builder.setTitle(title);
			builder.create().show();
		}
		ImageView seeDetailImg;
		
		
		 @Override
		 public void onActivityCreated(Bundle savedInstanceState) {
		     // TODO Auto-generated method stub
		     super.onActivityCreated(savedInstanceState);
	
		 	final Button btnMenu = (Button) getView().findViewById(R.id.menu_button_id);
			
			//final View v = view;
				btnMenu.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						
						//Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_LONG).show();
						Intent i = new Intent(v.getContext(), MainMenuActivity.class);
						startActivity(i);
					}
				});	
		 }
	
	} 


