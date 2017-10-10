package com.anderson.rejectcallmsg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import java.io.IOException;
import java.util.ArrayList;

public class Modify_World_BlackList extends Activity {
	private int ret_val = 0;
	private final int RESULT_OK = 2;
	//private final String GMAILADDRESS = "GmailAddress";
	private Button mContactSubmitter;
	private Button mButtonBack;
	private Button mResetButton;
	private EditText mPhoneNumber;
	private EditText mMonth;
	private EditText mDate;
	private EditText mYear;
	private ScrollView mScrollMsgView;
	private Button mSmsIndicatorNo;
	private Button mSmsIndicatorYes;
	private Button mSmsOptionNo;
	private Button mSmsOptionYes;
	private EditText  mYourMsg;
	private TextView mHeader;
	
	private LinearLayout mSmsSelection = null;
	private String SmsIndicator = Constants.SMS_NO_Indicator;
	String     mNameStr;
	String     mPhoneNumberStr;
	String     mMonthStr;
	String     mDateStr;
	String     mYearStr;
    String     yesORnoStr;
	String     sms_msg;
	String     smsBlock0ption;
	int        mPosInt;
	String     mOldNumber;
	
	protected ArrayAdapter<String> adapter;
	Context mContext;
	ArrayList<String> getData;
	
	/**
	 * Text watcher for detecting price fields changed
	 */
	private TextWatcher mTextWatcher = new TextWatcher() {
        
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String compareStr = "";
			int counter = 0;

			String temp_PhoneStr = mPhoneNumber.getText().toString().trim();

			if (temp_PhoneStr.length() >= 8){
				if (mMonth.getText().toString().trim().length() == 2) {
					if (mDate.getText().toString().trim().length() == 2) {
						if (mYear.getText().toString().trim().length() == 4) {
							if (!mPhoneNumber.hasFocus()) {
								Utilities.closeSoftKeyboard(mContext, mYear);
							}
							mContactSubmitter.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			else {
				mContactSubmitter.setVisibility(View.GONE);
			}

			if (!mPhoneNumber.hasFocus()){
			  if (temp_PhoneStr.length() >= 8){
				   if (mMonth.getText().toString().trim().length() == 2){
					  counter++;
					  mDate.setEnabled(true);
					  mDate.requestFocus();
					  if (mDate.getText().toString().trim().length() == 2) {
						  counter++;
						  mYear.setEnabled(true);
						  mYear.requestFocus();
						  if (mYear.getText().toString().trim().length() == 4) {
							  counter++;
							//  mContactSubmitter.setVisibility(View.VISIBLE);
							  Utilities.closeSoftKeyboard(mContext, mYear);
						  }
						  else {
							  mContactSubmitter.setVisibility(View.GONE);
						  }
					  }
					  else {
						  mContactSubmitter.setVisibility(View.GONE);
					  }
				   }
				   else {
					  mContactSubmitter.setVisibility(View.GONE);
				   }
			    }
				else{
				  mContactSubmitter.setVisibility(View.GONE);
			    }
			}

		}
    };
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_world_blacklist);

	    Intent gIntent = getIntent();
	
	    mPosInt  =  gIntent.getIntExtra("POSITION", -1);   
	    String fullStr = Global.mInternationalRejectedList.get(mPosInt);
	    Log.d("FULL STRING: ",  fullStr);
	    
	    Log.d("----  POS-----",  String.valueOf(mPosInt));
	    OneNode modifiedNode = Utilities.GetNode(fullStr, "");
	    mNameStr = modifiedNode.nameStr.substring(Constants.NAMESTR.length());
	    mPhoneNumberStr = modifiedNode.numberStr.substring(Constants.NUMBER_TITLE.length());
	    mMonthStr =    modifiedNode.month;
	    mDateStr =     modifiedNode.day;
	    mYearStr =     modifiedNode.year;
		yesORnoStr =   modifiedNode.smsResponse;
		smsBlock0ption =  modifiedNode.smsOption;
		
        if (yesORnoStr.contains(Constants.SMS_YES_Indicator)){
			sms_msg = Global.mMap.get(mPhoneNumberStr);
    	}
		
    	mContext = this;
		mYourMsg = (EditText)findViewById(R.id.YourMessage);
		mOldNumber =  mPhoneNumberStr.trim();
		
		Log.d("OLD PHONE NUMBER: ",  mPhoneNumberStr);
	    Log.d(" --- YES OR NO ---", yesORnoStr);

	    mHeader = (TextView)findViewById(R.id.header);
        mContactSubmitter = (Button)findViewById(R.id.submitButton);
        mButtonBack =  (Button)findViewById(R.id.backButton);
        mResetButton = (Button) findViewById(R.id.button_reset);
        mScrollMsgView = (ScrollView)findViewById(R.id.ScrollMsgView);

        mPhoneNumber = (EditText) findViewById(R.id.GmailAddressEntry);
		mPhoneNumber.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mPhoneNumber.setText("");
				return true;
			}
		});

		mMonth = (EditText) findViewById(R.id.Month);
		mMonth.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mMonth.setText("");
				return true;
			}
		});

		mDate = (EditText) findViewById(R.id.Date);
		mDate.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mDate.setText("");
				return true;
			}
		});

		mYear = (EditText) findViewById(R.id.Year);
		mYear.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mYear.setText("");
				return true;
			}
		});

        mPhoneNumber.setText(mOldNumber);
        mMonth.setText(mMonthStr.trim());
        mDate.setText(mDateStr.trim());
        mYear.setText(mYearStr.trim());
     
		mSmsSelection = (LinearLayout)findViewById(R.id.SmsSelection);
		mSmsIndicatorNo = (Button) findViewById(R.id.SmsIndicatorNo);
		mSmsIndicatorYes = (Button) findViewById(R.id.SmsIndicatorYes);
		mSmsOptionNo = (Button) findViewById(R.id.SmsOptionNo);
		mSmsOptionYes = (Button) findViewById(R.id.SmsOptionYes);
		
		mPhoneNumber.addTextChangedListener(mTextWatcher);
	    mMonth.addTextChangedListener(mTextWatcher);
		mDate.addTextChangedListener(mTextWatcher);
		mYear.addTextChangedListener(mTextWatcher);
		
		if (smsBlock0ption.contentEquals(Constants.SMS_YES_OPTION)){
				mSmsOptionYes.setVisibility(View.VISIBLE);
				mSmsOptionNo.setVisibility(View.GONE);
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){    
  		    	  final String myPackageName = getPackageName();
  		          if (!Telephony.Sms.getDefaultSmsPackage(mContext).equals(myPackageName)) {
  		                      Intent intent =
  		                              new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
  		                      intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, 
  		                              myPackageName);
  		                      startActivity(intent);
  		                  }
  		     	 }
		}
		else {
				mSmsOptionNo.setVisibility(View.VISIBLE);
				mSmsOptionYes.setVisibility(View.GONE);
		}
		
		mSmsOptionNo.setOnClickListener(new Button.OnClickListener(){
    	 	 public void onClick(View v) {
    	 		smsBlock0ption = Constants.SMS_YES_OPTION;
    	 		mSmsOptionNo.setVisibility(View.GONE);
    	 		mSmsOptionYes.setVisibility(View.VISIBLE);
    	 		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){    
    		    	  final String myPackageName = getPackageName();
    		          if (!Telephony.Sms.getDefaultSmsPackage(mContext).equals(myPackageName)) {
    		                      Intent intent =
    		                              new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
    		                      intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, 
    		                              myPackageName);
    		                      startActivity(intent);
    		                  }
    		     	 }
    			
    	 	 }
		 });
		
		 mSmsOptionYes.setOnClickListener(new Button.OnClickListener(){
	   	 	 public void onClick(View v) {
	   	 		smsBlock0ption = Constants.SMS_NO_OPTION;
	   			mSmsOptionYes.setVisibility(View.GONE);
	   			mSmsOptionNo.setVisibility(View.VISIBLE);
	   	 	 }
		 });
		
		/* if (yesORnoStr.contentEquals(Constants.SMS_YES_Indicator)){
			 mSmsIndicatorNo.setVisibility(View.GONE);
			 mSmsIndicatorYes.setVisibility(View.VISIBLE);
			 mScrollMsgView.setVisibility(View.VISIBLE);
			 SmsIndicator = Constants.SMS_YES_Indicator;
			 mYourMsg.setVisibility(View.VISIBLE);
			 mYourMsg.setText(sms_msg);
		}
		else{
			mSmsIndicatorNo.setVisibility(View.VISIBLE);
			mSmsIndicatorYes.setVisibility(View.GONE);
			SmsIndicator = Constants.SMS_NO_Indicator;
		}
		*/

	//	m_bottomPanel = (TextView) findViewById(R.id.bottomPanel);
		
		mSmsIndicatorNo.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
				 yesORnoStr =  Constants.SMS_YES_Indicator;
     	 		SmsIndicator = Constants.SMS_YES_Indicator;
     	 		mSmsIndicatorNo.setVisibility(View.GONE);
     	 		mSmsIndicatorYes.setVisibility(View.VISIBLE);
     	 		mScrollMsgView.setVisibility(View.VISIBLE);
				mYourMsg.requestFocus();
     	 		// .setHint("Max message length is 160 characters");
     	 	//	mSmsSelection.setVisibility(View.GONE);
     	 		
     	 	 }
       });
		
		
		mButtonBack.setOnClickListener(new Button.OnClickListener(){
    	 	 public void onClick(View v) {
    	 		finish();
    	 		
    	 	 }
      });
		
		mSmsIndicatorYes.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
				 yesORnoStr = Constants.SMS_YES_Indicator;
     	 		SmsIndicator = Constants.SMS_NO_Indicator;
     	 		mSmsIndicatorYes.setVisibility(View.GONE);
     	 		mSmsIndicatorNo.setVisibility(View.VISIBLE);
     	 		mScrollMsgView.setVisibility(View.GONE);
     	 		// mYourMsg.setVisibility(View.GONE);
     	 		// mYourMsg.setText="";
     	 	 }
       });
		
		
		  mResetButton.setOnClickListener(new Button.OnClickListener(){
	     	 public void onClick(View v) {
	     	 	// mPhoneNumber.setText(mPhoneNumberStr);
	     		 mPhoneNumber.setText("");
	     	     mMonth.setText("");
	     	     mDate.setText("");
	     	     mYear.setText("");
	     		
	     		if (yesORnoStr.contentEquals(Constants.SMS_YES_Indicator)){
	     			 SmsIndicator = Constants.SMS_NO_Indicator;
	     			 mSmsIndicatorNo.setVisibility(View.VISIBLE);
	     			 mSmsIndicatorYes.setVisibility(View.GONE);
	     		  	 mYourMsg.setText("");
	     			 mScrollMsgView.setVisibility(View.GONE);
	     		}
	     		mPhoneNumber.requestFocus();
	     		
	     	 }
	    });
		  
        mContactSubmitter.setOnClickListener(new Button.OnClickListener(){
        	
   	 	 public void onClick(View v) {
			 Utilities.closeSoftKeyboard(mContext, mPhoneNumber);
   	 
   	 	   /*  InputMethodManager imm = (InputMethodManager)
   	 		 getSystemService(Context.INPUT_METHOD_SERVICE);
   	 		 imm.hideSoftInputFromInputMethod(null, DEFAULT_KEYS_DISABLE);
   	 		 imm.hideSoftInputFromWindow(mYourMsg.getWindowToken(), 0);
   	 		*/
   	 		 String t_PhoneStr  = mPhoneNumber.getText().toString().trim();
   		     Log.d("Setup", "001");
   
   		     mYearStr = mYear.getText().toString().trim();
   		     mMonthStr = mMonth.getText().toString().trim();		 
   		     mDateStr = mDate.getText().toString().trim();
   		     String msgStr   = mYourMsg.getText().toString().trim();
   		     if (Utilities.checkForSpecialCharacter(msgStr, mContext)){
	        	 return;
   		     }
   		     Log.d("Get month:   ", mMonthStr);
   		     String untilString = "";
   		     if (Utilities.CheckValidDate(mMonthStr, mDateStr, mYearStr, mContext)){
   		    	untilString = "Blocked until: " + mMonthStr + "/" + mDateStr + "/" + mYearStr;
   		     }
   		     else{
   		    	  return;
   		     }
   		     
   		     if (SmsIndicator.contentEquals(Constants.SMS_YES_Indicator)){
	   		     if (msgStr.length() == 0){
	 		    	SmsIndicator = Constants.SMS_NO_Indicator;
	   		     }
   	 	     }
   		     else {
   		    	SmsIndicator = Constants.SMS_NO_Indicator;
   		     }
   		     
   		     if (Utilities.checkDuplicatedNumber(t_PhoneStr, Global.mRejectedList, mOldNumber)){
 	 			   Utilities.putToast("Phone number is already in blacklist.",0, 400, getApplicationContext());
                   return;
 	 		 }
   		    
   		     if (SmsIndicator.contentEquals(Constants.SMS_YES_Indicator)){
   		    	Global.mMap.remove(mOldNumber);
   		        if (!t_PhoneStr.contentEquals(mOldNumber)){
	   		        Global.mMap.put(t_PhoneStr, msgStr );
   		        }
   		        else {
   		        	Global.mMap.put(mOldNumber, msgStr );
   		        	
   		        }

		   		//mScrollMsgView.setVisibility(View.GONE);
		   		     
		   		mSmsSelection.setVisibility(View.VISIBLE); 
		   		mSmsIndicatorYes.setVisibility(View.VISIBLE);
		   		mSmsIndicatorNo.setVisibility(View.GONE);
		   		Utilities.BackUpMsgMap(mContext); 
   		    }
   		    else {
   		     
   		    	Global.mMap.remove(mOldNumber);
   		        Utilities.BackUpMsgMap(mContext);
   		    }
   		     
   		    String FullString =  Constants.NAMESTR + mNameStr + "\n" + Constants.NUMBER_TITLE + t_PhoneStr + "\n"
   				   				+ untilString + "\n" + SmsIndicator + "\n" + smsBlock0ption;
   		
   		     if (!FullString.contentEquals( Global.mInternationalRejectedList.get(mPosInt))){
    	 		 Global.mInternationalRejectedList.set(mPosInt, FullString);
				// Collections.sort(Global.mRejectedList);
				 Utilities.BackUpAList(Global.mInternationalRejectedList, Constants.INTERNATIONAL_REJECTED_LIST_FILE, mContext);
				 Utilities.putToast(mOldNumber + " is modified.", 0, 400, getApplicationContext());
   		     }
			 else {
				 Utilities.putToast(mOldNumber + " is unmodified.", 0, 400, getApplicationContext());
			 }
   		     
	         Intent returnIntent = new Intent();
	         setResult(RESULT_OK, returnIntent);
	         finish();
   	 	 }
        });  
	}

	
	 @Override
		protected void onResume() {
			super.onResume();
			mHeader.setText( mNameStr.trim() +"\nModify Record");
			
			 try {
					Utilities.get_MessageList(mContext, Constants.MESSAGE_MAP_FILE);
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 }

			 if (yesORnoStr.contentEquals(Constants.SMS_YES_Indicator)){
				 mSmsIndicatorNo.setVisibility(View.GONE);
				 mSmsIndicatorYes.setVisibility(View.VISIBLE);
				 mScrollMsgView.setVisibility(View.VISIBLE);
				 SmsIndicator = Constants.SMS_YES_Indicator;
				 mYourMsg.setVisibility(View.VISIBLE);
				 mYourMsg.setText(sms_msg);
			 }
			 else{
				 mSmsIndicatorNo.setVisibility(View.VISIBLE);
				 mSmsIndicatorYes.setVisibility(View.GONE);
				 SmsIndicator = Constants.SMS_NO_Indicator;
			 }
	 }
 
	 protected void onStop() {
	        super.onStop();
	        Utilities.BackUpAList(Global.mInternationalRejectedList, Constants.INTERNATIONAL_REJECTED_LIST_FILE, mContext);
	        Utilities.BackUpMsgMap(mContext);
	 }
	 	
	  public String FormMonthDateYearStr(String monthStr, String dateStr, String yearStr){
			int Current_year = 9999;
			int Current_month = 12;
			int Current_date = 30;
			
			if (monthStr.length() == 0){
				monthStr = String.valueOf(Current_month);
			}
			
			if (dateStr.length() == 0){
				dateStr = String.valueOf(Current_date);
			}
			
			if (yearStr.length() == 0){
				yearStr = String.valueOf(Current_year);
			}
			
			if  (Integer.parseInt(yearStr) <= Current_year){
				yearStr =  String.valueOf(Current_year);
				
				if (Integer.parseInt(monthStr) < Current_month){
						monthStr = String.valueOf(Current_month);
						if (Integer.parseInt(dateStr) < Current_date){
							monthStr =  String.valueOf(Current_date);
						}
			   }
			}
			else {
				 if (Integer.parseInt(monthStr) > 12){
					monthStr =  String.valueOf(12);
				 }
				 if (Integer.parseInt(dateStr) > 31){
				    monthStr =  String.valueOf(31);	
			     }
			
			}
			
			String formedString = "Blocked until : " + monthStr + "/" + dateStr + "/" + yearStr;
		    return formedString;
		}
		
		public void hideSoftKeyboard(View view) {
		    InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
		    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		
			}
		
		public void SetTrueFalse(boolean trueOrfalse){
			mMonth.setClickable(trueOrfalse);
			mDate.setClickable(trueOrfalse);
			mYear.setClickable(trueOrfalse);
			mPhoneNumber.setClickable(trueOrfalse);
		}
		
	}
		