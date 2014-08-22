package com.social.healthometer.model;

import java.sql.Date;


/**
 * Represents an item in a ToDo list
 */
public class TodoItem {

	private String mText;
	private String mSex;
	private String mDOB;
	private String hw_number;
	private String gaurdian_name;
	private String lang;
	private String reg_code;
	private String ModifiedOn;
	private String is_verified;
	private String mNotifyNumber;
	private String mId;
	private boolean mComplete;

	/**
	 * ToDoItem constructor
	 */
	public TodoItem() {

	}
	

	/**
	 * Initializes a new ToDoItem
	 * 
	 * @param text
	 *            The item text
	 * @param id
	 *            The item id
	 */
	public TodoItem(String text, String id) {
		this.setText(text);
		this.setId(id);
	}
	
	public String getHw_number() {
		return hw_number;
	}



	public void setHw_number(String hw_number) {
		this.hw_number = hw_number;
	}



	public String getGaurdian_name() {
		return gaurdian_name;
	}



	public void setGaurdian_name(String gaurdian_name) {
		this.gaurdian_name = gaurdian_name;
	}



	public String getLang() {
		return lang;
	}



	public void setLang(String lang) {
		this.lang = lang;
	}



	public String getReg_code() {
		return reg_code;
	}



	public void setReg_code(String reg_code) {
		this.reg_code = reg_code;
	}



	public String getModifiedOn() {
		return ModifiedOn;
	}



	public void setModifiedOn(String modifiedOn) {
		ModifiedOn = modifiedOn;
	}



	public String getIs_verified() {
		return is_verified;
	}



	public void setIs_verified(String is_verified) {
		this.is_verified = is_verified;
	}


	 public boolean isChecked() {
         return checkbox;
     }

     public void setChecked(boolean checkbox) {
         this.checkbox = checkbox;
     }

	private boolean checkbox = false;
	public void toggleChecked() {
		checkbox = !checkbox;
    }
	


	/**
	 * Returns the item text
	 */
	public String getText() {
		return mText;
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setText(String text) {
		mText = text;
	}
	
	public String getSex() {
		return mSex;
	}
	
	public final void setSex(String sex) {
		mSex = sex;
	}
	
	public final void setNotifyNumber(String number) {
		mNotifyNumber = number;
	}
	
	public String getNotifyNumber() {
		return mNotifyNumber;
	}
	
	
	
	public final void setMobileNumber(String number) {
		mNotifyNumber = number;
	}
	
	public String getMobileNumber() {
		return mNotifyNumber;
	}
	
	public final void setDateOfBirth(String dob) {
		mDOB = dob;
	}
	
	public String getDateOfBirth() {		
		return mDOB;
	}

	/**
	 * Returns the item id
	 */
	public String getId() {
		return mId;
	}

	/**
	 * Sets the item id
	 * 
	 * @param id
	 *            id to set
	 */
	public final void setId(String id) {
		mId = id;
	}

	/**
	 * Indicates if the item is marked as completed
	 */
	public boolean isComplete() {
		return mComplete;
	}

	/**
	 * Marks the item as completed or incompleted
	 */
	public void setComplete(boolean complete) {
		mComplete = complete;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof TodoItem && ((TodoItem) o).mId == mId;
	}
}
