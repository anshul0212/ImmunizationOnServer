package com.social.healthometer.adapter;

import java.util.ArrayList;

import com.social.healthometer.R;
import com.social.healthometer.model.TodoItem;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Color;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomArrayAdapter extends ArrayAdapter<TodoItem> {
	
	private final ArrayList<TodoItem> items;
	private final Context context;
	
	public CustomArrayAdapter(Context context, ArrayList<TodoItem> values) {
	    super(context, R.layout.list_pending_item, values);
	    this.context = context;
	    this.items = values;
	  }
	 private String nameVal[] ;

	public View getView( int position, View convertView, ViewGroup parent) {
	   
	LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.list_pending_item, parent, false);

	    TextView nameTextView = (TextView) rowView.findViewById(R.id.name_id);
	 
	    TextView mob_num = (TextView) rowView.findViewById(R.id.mob_id);
	    
	    TextView genderText = (TextView) rowView.findViewById(R.id.gender_id);
	    
	    EditText code = (EditText) rowView.findViewById(R.id.verificiation_code_id);
	    final int a = position;
	    code.setOnKeyListener( new OnKeyListener() {
 	
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if( keyCode != KeyEvent.KEYCODE_BACK )
			      { 
				   items.get(a).setReg_code( ((EditText)arg0.findViewById(R.id.verificiation_code_id)).getText().toString());
				  
				return false;
			    }
				else
				{
					return false;
				}
				
				
				
			}
			
			 
	    	
	    });
	    
	    
	    CheckBox ck = (CheckBox)rowView.findViewById(R.id.checkBox_id);
	    
	   
	   if(parent.getId() == R.id.listverified||parent.getId() == R.id.listView1) 
	   {
		  
		   ck.setVisibility(View.GONE);
	   }
	
	   EditText otp= (EditText)rowView.findViewById(R.id.verificiation_code_id);
	  
	   if(parent.getId() == R.id.listView1) 
	   {
		  // otpReadOnly.setText(otp.getText().toString());
		   otp.setVisibility(View.GONE);
		  // otpReadOnly.setVisibility(View.VISIBLE);
			  
		 //  otp.setEnabled(false);
		  // otp.setBackgroundColor(color.darker_gray);
	   } 
	   //ck.setFocusable(true);
	   ck.setClickable(true);
	   ck.setChecked(items.get(position).isChecked());
	    //ck.setChecked(items.get(position).isChecked());
	  if( items.get(position).isChecked() )
	  ck.setBackgroundResource(R.drawable.dark_green_gradient);
	  
	    ck.setOnClickListener(new OnClickListener() {
	        @SuppressLint("ResourceAsColor")
			@Override
	        public void onClick(View arg0) {
	        	if(!items.get(a).isChecked())
	        	{
	        		items.get(a).setChecked(true);
	        		arg0.setBackgroundResource(R.drawable.dark_green_gradient);
	        	}
	        	else
	        	{
	        		items.get(a).setChecked(false);
	        		arg0.setBackgroundResource(R.drawable.checkbox_gradient);
	        	}
	        		
	            // Do something here.
	        }
	    });
	
	 
	    nameTextView.setText(items.get(position).getText());
	    genderText.setText(items.get(position).getSex());
	    mob_num.setText(items.get(position).getNotifyNumber());
	    code.setText(items.get(position).getReg_code());
	  
	
	    nameTextView.setTextColor(Color.BLACK);
	    genderText.setTextColor(Color.BLACK);
	     mob_num.setTextColor(Color.BLACK);
	     code.setTextColor(Color.BLACK);
	  
	   
	     genderText.setTextColor(Color.BLACK);
	      return rowView;
	}
	
}
