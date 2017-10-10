package com.anderson.rejectcallmsg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class ModifyAreaCodeBlackList extends Activity {
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
	
	private LinearLayout mSmsSelection = null;
	private String SmsIndicator = Constants.SMS_NO_Indicator;
	String     mName;
	String     mPhoneNumberStr;
	String     mMonthStr;
	String     mDateStr;
	String     mYearStr;
    String     yesORnoStr;
	String     sms_msg;
	String     smsBlock0ption;
	int        mPosInt;
	String     mOldNumber;
	String     exceptedNumbs;
	
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

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {

			int counter = 0;
			boolean phone_flag = false;
			String compareStr = mPhoneNumber.getText().toString().trim();

			if (compareStr.length()== 3){
			        counter++;
					if (Utilities.readAndCheck(R.raw.areacode, compareStr , mContext) != null) {
						mMonth.requestFocus();
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
									mContactSubmitter.setVisibility(View.VISIBLE);
									Utilities.closeSoftKeyboard(mContext, mYear);
								}
							}
						}
					}
				    else {
						  Utilities.putToast(compareStr + " : Invalid Area Code.",0, 300, mContext);
					}

			}

			/*if( phone_flag && (mMonth.getText().toString().trim().length() == 2)){
				counter++;
				mDate.setEnabled(true);
				mDate.requestFocus();
				if (mDate.getText().toString().trim().length() == 2) {
					counter++;
					mYear.setEnabled(true);
					mYear.requestFocus();
					if ( mYear.getText().toString().trim().length() == 4 ){
						counter++;
						mContactSubmitter.setVisibility(View.VISIBLE);
						Utilities.closeSoftKeyboard(mContext, mYear);
					}
				}
			}
			*/
			if (counter != 4) {
				mContactSubmitter.setVisibility(View.GONE);
			}

		}
    };
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_areacode_blacklist);
        Intent gIntent = getIntent();
    	
	    mPosInt  =  gIntent.getIntExtra("POSITION", -1);   
	    String fullStr = Global.mAreaCodeRejectedList.get(mPosInt);
	    Log.d("FULL STRING: ",  fullStr);
	    
	    Log.d("----  POS-----",  String.valueOf(mPosInt));
	    OneNode modifiedNode = Utilities.GetNode(fullStr, "");
		mName = modifiedNode.nameStr;
	    mPhoneNumberStr = modifiedNode.numberStr.substring(12, 15);
	    mMonthStr =    modifiedNode.month;
	    mDateStr =     modifiedNode.day;
	    mYearStr =     modifiedNode.year;
		yesORnoStr =   modifiedNode.smsResponse;
		smsBlock0ption =  modifiedNode.smsOption;
		exceptedNumbs = modifiedNode.exceptedNumbOption;
		
        if (yesORnoStr.contains(Constants.SMS_YES_Indicator)){
			sms_msg = Global.mMap.get(mPhoneNumberStr);
    	}
        
    	mContext = this;
		mYourMsg = (EditText)findViewById(R.id.YourMessage);
		mOldNumber =  mPhoneNumberStr.trim();
	    
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
        
        mPhoneNumber.setText(mOldNumber.trim());
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
    			
    	 	 }
		 });
		
		 mSmsOptionYes.setOnClickListener(new Button.OnClickListener(){
	   	 	 public void onClick(View v) {
	   	 		smsBlock0ption = Constants.SMS_NO_OPTION;
	   			mSmsOptionYes.setVisibility(View.GONE);
	   			mSmsOptionNo.setVisibility(View.VISIBLE);
	   	 	 }
		 });
		
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
		
		mSmsIndicatorNo.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
     	 		SmsIndicator = Constants.SMS_YES_Indicator;
     	 		mSmsIndicatorNo.setVisibility(View.GONE);
     	 		mSmsIndicatorYes.setVisibility(View.VISIBLE);
     	 		mScrollMsgView.setVisibility(View.VISIBLE);
				mYourMsg.requestFocus();
     	 		//mYourMsg.setHint("Max message length is 160 characters");
     	 	 }
       });
		
		
		mButtonBack.setOnClickListener(new Button.OnClickListener(){
    	 	 public void onClick(View v) {
    	 		finish();
    	 		
    	 	 }
      });
		
		mSmsIndicatorYes.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
     	 		 
     	 		SmsIndicator = Constants.SMS_NO_Indicator;
     	 		mSmsIndicatorYes.setVisibility(View.GONE);
     	 		mSmsIndicatorNo.setVisibility(View.VISIBLE);
     	 		mScrollMsgView.setVisibility(View.GONE);
     	 	 }
       });
		
		
		  mResetButton.setOnClickListener(new Button.OnClickListener(){
	     	 public void onClick(View v) {

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
   	 
   	 	     InputMethodManager imm = (InputMethodManager)
   	 		 getSystemService(Context.INPUT_METHOD_SERVICE);
   	 		 imm.hideSoftInputFromInputMethod(null, DEFAULT_KEYS_DISABLE);
   	 		 imm.hideSoftInputFromWindow(mYourMsg.getWindowToken(), 0);
   	 	    
   	 		 String t_PhoneStr  = mPhoneNumber.getText().toString().trim();
			 Log.d("t_PhoneStr ", t_PhoneStr);
   		     mYearStr = mYear.getText().toString().trim();
   		     mMonthStr = mMonth.getText().toString().trim();		 
   		     mDateStr = mDate.getText().toString().trim();
   		     String msgStr   = mYourMsg.getText().toString().trim();
   		     if (Utilities.checkForSpecialCharacter(msgStr, mContext)){
	        	 return;
   		     }
   		     Log.d("Get month:   ", mMonthStr);
   		     String untilString = "";
			 if ((Integer.parseInt(mMonthStr) == 0) && (Integer.parseInt(mMonthStr)== 0)
					                              &&(Integer.parseInt(mMonthStr) == 0)){
				 mMonthStr = "12"; mDateStr = "12"; mYearStr = "9999";
				 untilString = "Blocked until: " + mMonthStr + "/" + mDateStr + "/" + mYearStr;
			 }
			 else
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

			 String city_Str = "";
			 if (( city_Str = Utilities.readAndCheck(R.raw.areacode, t_PhoneStr, mContext))== null) {
				 Utilities.putToast("Area Code " + t_PhoneStr + " is not existed", 0, 300, mContext);
				 return;
			 }

			 if (Utilities.checkDuplicatedNumber(t_PhoneStr, Global.mAreaCodeRejectedList, mOldNumber)){
					   Utilities.putToast("Phone number is already in  area-code blacklist.",0, 300, getApplicationContext());
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

   		   /* String FullString =  t_PhoneStr + "\n" + untilString + "\n" + 
   		                                    SmsIndicator + "\n" + smsBlock0ption + "\n" + exceptedNumbs;
   		   */
   		  String FullString =  Constants.CITY + city_Str + "\n" + Constants.AREA_CODE_TITLE + t_PhoneStr + "\n" + untilString + "\n" +
                SmsIndicator + "\n" + smsBlock0ption;

   		     if (!FullString.contentEquals( Global.mAreaCodeRejectedList.get(mPosInt))){
    	 		Global.mAreaCodeRejectedList.set(mPosInt, FullString);
	   		    adapter.notifyDataSetChanged();
    	 		Utilities.BackUpAList(Global.mAreaCodeRejectedList, Constants.AREA_CODE_REJECTED_LIST_FILE, mContext);
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
			
			 try {
					Utilities.get_MessageList(mContext, Constants.MESSAGE_MAP_FILE);
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 }
			 
			adapter = new ArrayAdapter<String>(this,
		        	R.layout.simple_list_item_6, Global.mAreaCodeRejectedList);
			  try {
				    Global.mAreaCodeRejectedList.clear();
					ret_val = Utilities.getListFromFile(mContext, Global.mAreaCodeRejectedList, Constants.AREA_CODE_REJECTED_LIST_FILE);
					
			  } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			  }
			 if (Global.mAreaCodeRejectedList.size() > 0){
				 Global.AreaCodeChecker = true;
			 }

      //   mGmailInbox.setAdapter(adapter);
         if (ret_val != 1){
       	    Log.d("content buff   12:  ", "CONTACT INBOX is either empty or not existed");
         }
    	
	 }
 
	 protected void onStop() {
	        super.onStop();
	     //   Utilities.BackUpAList(Global.mAreaCodeRejectedList, Constants.AREA_CODE_REJECTED_LIST_FILE, mContext);
	     //   Utilities.BackUpMsgMap(mContext);
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
		