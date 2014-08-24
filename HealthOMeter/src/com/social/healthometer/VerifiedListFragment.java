package com.social.healthometer;


import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.social.healthometer.adapter.CustomArrayAdapter;

import com.social.healthometer.model.TodoItem;
import com.social.utilities.ServiceHandler;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class VerifiedListFragment extends Fragment implements OnItemClickListener  {
	 
	 String[] member_names;
	 TypedArray profile_pics;
	 String[] mob_num;
	 String[] code;
	 String url_add_beneficiary;
	// List<CheckBoxItem> rowCheckBoxItems ;
	 //List<ListItems> listItems;
	 ListView mylistview , checklistview;
	 private ProgressDialog pd;
	 private CustomArrayAdapter customArrayAdapter;
		Boolean ready;
		
	 
	// List<CheckBoxItem> rowCheckBoxItems ;
		private ArrayList<TodoItem> listItems;

	 @Override
	    public View onCreateView(LayoutInflater inflater, 
	              ViewGroup container, Bundle savedInstanceState) {
	        
	  View view = inflater.inflate(R.layout.fragment_verified,  container, false);
	   mylistview = (ListView) view.findViewById(R.id.listverified);

	   url_add_beneficiary = getString(R.string.url_add_beneficiary);
		
	   mylistview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	  
	  listItems = new ArrayList<TodoItem>();  
	//  rowCheckBoxItems = new ArrayList<CheckBoxItem>();  
	  customArrayAdapter = new CustomArrayAdapter(getActivity().getApplicationContext(),listItems);
	  mylistview.setAdapter(customArrayAdapter);

	  
	  mylistview.setOnItemClickListener(this);
	 
	 
	
		
		refreshItemsFromTable();
	  
	  
	  return view;

	 }

	
	 
	 
	 @Override
	 public void onItemClick(AdapterView<?> parent, View view, int position,
	   long id) {

		  String member_name = listItems.get(position).getText();
	  Toast.makeText(this.getActivity().getApplicationContext(), "df" + member_name,
	    Toast.LENGTH_SHORT).show();
	    
	  Fragment fragment2 = new FragmentUpdate();
	  Bundle args = new Bundle();
     args.putString(FragmentUpdate.NAME, member_name);
     fragment2.setArguments(args);
	  FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
	  fragmentTransaction.replace(R.id.frame_container, fragment2, member_name);
	  fragmentTransaction.addToBackStack(member_name);
	  fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	  fragmentTransaction.commit();
	
	  
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
	    private class PopulateVerifiedDetails extends AsyncTask<Void, Void, Void> 
	    {
	    	 ArrayList<TodoItem> arrayItem = new ArrayList<TodoItem>();
	            String hw_number;
	    	// ProgressDialog pDialog= new ProgressDialog(context);
	    	@Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	             
	           createProgress();
	    
	    	}
		
	        @Override
	        protected Void doInBackground(Void... arg0) 
	        {
	            // Creating service handler class instance
	            ServiceHandler sh = new ServiceHandler();
	            hw_number= "false";
	            
	            hw_number = ViewDetailFragment.getDefaults("mobileNo" , getActivity());
	            
	            // Making a request to url and getting response
	            Log.d("url_add_beneficiary",url_add_beneficiary);
	            url_add_beneficiary = url_add_beneficiary+"?is_verified=1";
	            String  jsonStr = sh.makeServiceCall(url_add_beneficiary, ServiceHandler.GET) ;
	            
	            Log.d("Response: ", "> " + jsonStr);
	           
	           
	            		
	            if (jsonStr != null) {
	                try {
	                	JSONArray jArray = new JSONArray(jsonStr);
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
	                  //    customArrayAdapter.add(item);
	                	}
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }  
	            } else {
	           }
	            
	            
	            return null;    
	        }
		     
		    @Override
		    protected void onPostExecute(Void result) {
		    
		    	super.onPostExecute(result);
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
	    }
	    public static String getDefaults(String key, Context context) {
		    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		    return preferences.getString(key, "false");
		}
	 
	 private void refreshItemsFromTable() {

		 PopulateVerifiedDetails populateVerifiedDetails= new PopulateVerifiedDetails();
		 populateVerifiedDetails.execute();
		
		}

	 
	 public void ShowMessage(String title, String message)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

			builder.setMessage(message);
			builder.setTitle(title);
			builder.create().show();
		}
	 
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


