package com.anderson.rejectcallmsg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import com.anderson.include.Constants;
import com.anderson.include.Utilities;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class InsertBlackListNumberViaMissedCallList extends Activity {
	private int ret_val = 0;
	private Button mContactSubmitter;
	private Button mHome;
	private Button mAddButton;
	private Button mResetButton;
	private EditText mConfirmNumber;
	private EditText mPhoneNumber;
	private EditText mMonth;
	private EditText mDate;
	private EditText mYear;
	private LinearLayout mListView;
	private ScrollView mScrollMsgView;
	private Button mSmsOptionNo;
	private Button mSmsOptionYes;
	private Button mSmsIndicatorNo;
	private Button mSmsIndicatorYes;
	private EditText  mYourMsg;
	private LinearLayout mSmsSelection = null;
	private String SmsIndicator = Constants.SMS_NO_Indicator;
	private String SmsOption = Constants.SMS_NO_OPTION;
	Context mContext;
	private final int PHONE_CONTACT = 12;
	private Button m_phoneContactList;
	public static final String CONTACT_NUMBER = "contact_number";
	private boolean SELECT_FROM_CONTACT_LIST = false;
	private final int until_year = 2025;
	private final int until_month = 12;
	private final int until_date = 30;
	
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
				String compareStr = mPhoneNumber.getText().toString().trim();
			
				int mPhoneNumberLen = compareStr.length();
				mResetButton.setVisibility(View.VISIBLE);
			
				
					m_phoneContactList.setVisibility(View.GONE);
					mAddButton.setVisibility(View.GONE);
					mResetButton.setVisibility(View.VISIBLE);
					
				
				if ( mPhoneNumber.getText().toString().trim().length() == 10){
					compareStr = mPhoneNumber.getText().toString().trim();
					mConfirmNumber.requestFocus();
				}
				
				 if (mConfirmNumber.getText().toString().trim().length() == mPhoneNumberLen){
					   String compareStr2 = mConfirmNumber.getText().toString().trim();
				    	  if (compareStr.contentEquals(compareStr2)){
				    		  mMonth.setEnabled(true);
						      mMonth.requestFocus();
				    	  }
				    	  else {
				    		    Utilities.putToast("Can not confirm the phone number.", 0, 400, getApplicationContext());
							    return; 
				    	  }
				      }	
				
				if ( mMonth.getText().toString().trim().length() == 2){
					/* if (mDate.getText().toString().trim().length() < 2){
						   mDate.requestFocus();
				    	}
					   else
						 {
						    mYear.requestFocus();
						 }
						 */
					    mDate.setEnabled(true);
					    mDate.requestFocus();
				}	
				
				if (mDate.getText().toString().trim().length() == 2) {
				  /* if (mMonth.getText().toString().trim().length() < 2){
					   mMonth.requestFocus();
			    	}
				   else
					 {
					    mYear.requestFocus();
					 }
					 */
					mYear.setEnabled(true);
					mYear.requestFocus();
				}	
				
			  if (SELECT_FROM_CONTACT_LIST){
				  if ( ( mPhoneNumber.getText().toString().trim().length()>= 3) &&
						(mConfirmNumber.getText().toString().trim().length() ==
							        mPhoneNumber.getText().toString().trim().length()) &&
							        (mMonth.getText().toString().trim().length() == 2) &&
							        (mDate.getText().toString().trim().length() == 2) &&
							        ( mYear.getText().toString().trim().length() == 4 )){
						
						mContactSubmitter.setVisibility(View.VISIBLE);
			      }
				  else {
						mContactSubmitter.setVisibility(View.GONE);
						
				  }
			  }
			  else{
				  if ( ( mPhoneNumber.getText().toString().trim().length()>= 10) &&
							(mConfirmNumber.getText().toString().trim().length() ==
								        mPhoneNumber.getText().toString().trim().length()) &&
								        (mMonth.getText().toString().trim().length() == 2) &&
								        (mDate.getText().toString().trim().length() == 2) &&
								        ( mYear.getText().toString().trim().length() == 4 )){
							mContactSubmitter.setVisibility(View.VISIBLE);
				      }
					  else {
							mContactSubmitter.setVisibility(View.GONE);
							
					  }
			  }		    		
		}
	};
	    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertblacklistnumberviamissedcalllist);
        Intent getPassedIn =  getIntent();
        String passedInNumb = getPassedIn.getStringExtra(Constants.MISSED_CALL_NUMB);
    	mContext = this;
		mPhoneNumber = (EditText)findViewById(R.id.GmailAddressEntry);
		mPhoneNumber.setText(passedInNumb);
		mConfirmNumber = (EditText)findViewById(R.id.ConfirmAddressEntry);
		mConfirmNumber.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mPhoneNumber, InputMethodManager.SHOW_IMPLICIT);
		mYourMsg = (EditText)findViewById(R.id.YourMessage);
		mYourMsg.setOnTouchListener(new View.OnTouchListener(){
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
				}
		});
		
		 m_phoneContactList = (Button)findViewById(R.id.PhoneContactList);
         m_phoneContactList.setOnClickListener(new Button.OnClickListener(){
         	   public void onClick(View v) {
         		startSelectContactActivity();
         		
            }
        });
                  
		
		mSmsOptionNo = (Button) findViewById(R.id.SmsOptionNo);
		mSmsOptionNo.setOnClickListener(new Button.OnClickListener(){
    	 	 public void onClick(View v) {
    	 		SmsOption = Constants.SMS_YES_OPTION;
    	 		mSmsOptionNo.setVisibility(View.GONE);
    	 		mSmsOptionYes.setVisibility(View.VISIBLE);
    	 	 }
      });
		
		mSmsOptionYes = (Button) findViewById(R.id.SmsOptionYes);
		mSmsOptionYes.setOnClickListener(new Button.OnClickListener(){
    	 	 public void onClick(View v) {
    	 		SmsOption = Constants.SMS_NO_OPTION;
    	 		mSmsOptionNo.setVisibility(View.VISIBLE);
    	 		mSmsOptionYes.setVisibility(View.GONE);
    	 		checkForKITKAT();
    	 	 }
      });
		
		
		mSmsIndicatorNo = (Button) findViewById(R.id.SmsIndicatorNo);
		mSmsIndicatorYes = (Button) findViewById(R.id.SmsIndicatorYes);
		//m_bottomPanel = (TextView) findViewById(R.id.bottomPanel);
		
		mSmsIndicatorNo.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
     	 		SmsIndicator = Constants.SMS_YES_Indicator;
     	 		mSmsIndicatorNo.setVisibility(View.GONE);
     	 		mSmsIndicatorYes.setVisibility(View.VISIBLE);
     	 		mScrollMsgView.setVisibility(View.VISIBLE);
     	 		//mYourMsg.setVisibility(View.VISIBLE);
     	 		mYourMsg.requestFocus();
     	 		//mSmsSelection.setVisibility(View.GONE);
     	 		
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
	       
	        mDate = (EditText)findViewById(R.id.Date);
	        mMonth = (EditText)findViewById(R.id.Month);
	        mYear = (EditText)findViewById(R.id.Year);
				
			mPhoneNumber.setOnTouchListener(new View.OnTouchListener(){
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						return false;
					}
					
			});
				
				
        mConfirmNumber.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mConfirmNumber.requestFocus();
				return false;
			}
		});
        
        mMonth.setOnTouchListener(new View.OnTouchListener(){
    			@Override
    			public boolean onTouch(View v, MotionEvent event) {
	    			mMonth.requestFocus();
    			    return false;
    			}
    	});
        
        mDate.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
					mDate.requestFocus();
			   return false;
			}
	  });
        
    mYear.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mYear.requestFocus();
				mHome.setVisibility(View.GONE);
				return false;
			}
	  });
      
       // mGmailInbox = (ListView) findViewById(R.id.gmailContactInbox);
        mContactSubmitter = (Button)findViewById(R.id.submitButton);
        mContactSubmitter.setVisibility(View.GONE);
        mHome =  (Button)findViewById(R.id.homeButton);
       
        
       // mWrappedView = (ScrollView)findViewById(R.id.WrapView);
        mScrollMsgView = (ScrollView)findViewById(R.id.ScrollMsgView);
        mListView = (LinearLayout)findViewById(R.id.WrappedViewTwo);
       
        mAddButton = (Button) findViewById(R.id.button_add);
        mResetButton = (Button) findViewById(R.id.button_reset);
        
        mPhoneNumber.addTextChangedListener(mTextWatcher);
		mConfirmNumber.addTextChangedListener(mTextWatcher);
		mMonth.addTextChangedListener(mTextWatcher);
		mDate.addTextChangedListener(mTextWatcher);
		mYear.addTextChangedListener(mTextWatcher);
		mSmsSelection = (LinearLayout)findViewById(R.id.SmsSelection);
  
	    mAddButton.setOnClickListener(new Button.OnClickListener(){
      	 	 public void onClick(View v) {
      	 		// mWrappedView.setVisibility(View.VISIBLE);
      	 		 mListView.setVisibility(View.GONE);
      	 		 mAddButton.setVisibility(View.GONE);
      	 		 mResetButton.setVisibility(View.GONE);
      	 		 m_phoneContactList.setVisibility(View.VISIBLE);
      	 	 }
        });

		
  
        mResetButton.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
     	 		if (SmsIndicator.contentEquals(Constants.SMS_YES_Indicator)){
     	 		   SmsIndicator = Constants.SMS_NO_Indicator;
     	 		   mSmsIndicatorYes.setVisibility(View.GONE);
     	 		   mSmsIndicatorNo.setVisibility(View.VISIBLE);
     	 	    }
     	 		
     	 		if (SmsOption.contentEquals(Constants.SMS_YES_OPTION)){
      	 		   SmsOption = Constants.SMS_NO_OPTION;
      	 		   mSmsOptionYes.setVisibility(View.GONE);
      	 		   mSmsOptionNo.setVisibility(View.VISIBLE);
      	 	    }
     	 		
     	 		mConfirmNumber.setText("");
     	 		mPhoneNumber.setText("");
     	 		mMonth.setText("");
     	 		mDate.setText("");
     	 		mYear.setText("");
     	 		mYourMsg.setText("");
     	 		
     	 		mHome.setVisibility(View.VISIBLE);
     	 		mContactSubmitter.setVisibility(View.GONE);
     	 		mResetButton.setVisibility(View.GONE);
     	 		mAddButton.setVisibility(View.GONE);
     	 		m_phoneContactList.setVisibility(View.VISIBLE);
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
	   	 		
	   	 		 if (Utilities.checkDuplicatedNameNumber(t_PhoneStr, Global.mRejectedList, mContext,true) == 1){ //Anderson
	   	 			Utilities.putToast("Phone number is already in Blacklist.",0, 400, getApplicationContext());
	   	 			
	   	 	 	    if (SmsIndicator.contentEquals(Constants.SMS_YES_Indicator)){
	     	 		   SmsIndicator = Constants.SMS_NO_Indicator;
	     	 		   mSmsIndicatorYes.setVisibility(View.GONE);
	     	 		   mSmsIndicatorNo.setVisibility(View.VISIBLE);
	     	 	    }
	     	 		
	     	 		if (SmsOption.contentEquals(Constants.SMS_YES_OPTION)){
	      	 		   SmsOption = Constants.SMS_NO_OPTION;
	      	 		   mSmsOptionYes.setVisibility(View.GONE);
	      	 		   mSmsOptionNo.setVisibility(View.VISIBLE);
	      	 	    }
	     	 		
	     	 		mConfirmNumber.setText("");
	     	 		mPhoneNumber.setText("");
	     	 		mMonth.setText("");
	     	 		mDate.setText("");
	     	 		mYear.setText("");
	     	 		mYourMsg.setText("");
	     	 		
	     	 		mHome.setVisibility(View.VISIBLE);
	     	 		mContactSubmitter.setVisibility(View.GONE);
	     	 		mResetButton.setVisibility(View.GONE);
	     	 		mAddButton.setVisibility(View.GONE);
	     	 		m_phoneContactList.setVisibility(View.VISIBLE);
	     	 		mPhoneNumber.requestFocus();
	                return;
	   	 		 }
	   	 		 
	   	 		 if (t_PhoneStr.contains(Constants.END_MARK)){
	   	 			Utilities.putToast("Phone number is invalid: " + Constants.END_MARK + " is not accepted.",0, 400, getApplicationContext());
	   	 			return;
	   	 		 }
	   	 	     String t_ConfirmStr  = mConfirmNumber.getText().toString().trim(); 
	   	 		 Pattern phonePattern = Patterns.PHONE; // API level 8+
	   		     Log.d("Setup Phone STR", t_PhoneStr);
	   		   
	   		     if ((t_PhoneStr.length() == 0) && (t_ConfirmStr.length() == 0)){
	   			    Utilities.putToast("Missing both phone number and confirm number.",0, 400, getApplicationContext());
			        mPhoneNumber.requestFocus();
				    return;
			     }
	   		   else
	   		   if (t_PhoneStr.length() == 0){
	   			    Utilities.putToast("Missing phone number.",0, 400, getApplicationContext());
		            mPhoneNumber.requestFocus();
				    return;
	   		     }
	   		   else 
	   		     if (t_ConfirmStr.length() == 0){
	   		    	Utilities.putToast("Missing confirm number.",0, 400, getApplicationContext());
	   		    	mConfirmNumber.requestFocus();
				    return;
	   		     }
	   		   
	   		   if (SELECT_FROM_CONTACT_LIST){
		   		   if (!phonePattern.matcher(t_PhoneStr).matches()){
		   			   Utilities.putToast("Phone number is invalid.",0, 400, getApplicationContext());
		   			   mPhoneNumber.requestFocus();
		   			   return;
		   		   }
	   		   }
	   		   
	   		   if (!t_PhoneStr.contentEquals(t_ConfirmStr)){
	   			    Utilities.putToast("Confirmation fails.",0, 400, getApplicationContext());
				    return;
			   }
	   		    String mYearStr = mYear.getText().toString().trim();
			    String mMonthStr = mMonth.getText().toString().trim();		 
			    String mDateStr = mDate.getText().toString().trim();
			    
				if ((Integer.parseInt(mYearStr) == 0) &&
				   (Integer.parseInt(mMonthStr) == 0) && 
				   (Integer.parseInt(mDateStr) == 0)){
					    mYearStr = String.valueOf(until_year);
					    mMonthStr = String.valueOf(until_month);
					    mDateStr = String.valueOf(until_date);
				}
	   		    if(!Utilities.CheckValidDate(mMonthStr , mDateStr, mYearStr, mContext)){
	   		    	 return;
	   		    	 
	   		     }
	   		     String msgStr = "";
	   		     if (SmsIndicator.contentEquals(Constants.SMS_YES_Indicator)){
	   		         msgStr   = mYourMsg.getText().toString().trim(); 
	   		         if (Utilities.checkForSpecialCharacter(msgStr, mContext)){
	   		        	 return;
	   		         }
	   		     }
	   		     String FullString = t_PhoneStr + "\n" + FormMonthDateYearStr(mMonthStr, mDateStr, mYearStr) + "\n" 
	   		                         + SmsIndicator + "\n" + SmsOption;
	   		     
	   			 Log.d("--- SMS INDICATOR ---------    ",SmsIndicator );
	   			 Log.d("--- CONSTANT SMS INDICATOR ----     ",Constants.SMS_YES_Indicator);
	   			 Log.d("--- Message Message  22----: ", msgStr);
	   		     if (SmsIndicator.contentEquals(Constants.SMS_YES_Indicator)){
	   		    	Log.d("--- Message Message  22----: ", msgStr);
	   		        Global.mMap.put(t_PhoneStr, msgStr );
	   		     }
	   		       // Global.mNode.add(Utilities.GetNode(FullString, ""));
	   		        mScrollMsgView.setVisibility(View.GONE);
	   		      //  mYourMsg.setVisibility(View.GONE)
	   		        SmsIndicator = "";
	   		       // m_bottomPanel.setVisibility(View.GONE);
	   		        mYourMsg.setClickable(false);
	   		        mSmsSelection.setVisibility(View.VISIBLE); 
	   		        mSmsIndicatorYes.setVisibility(View.GONE);
	   		        mSmsIndicatorNo.setVisibility(View.VISIBLE);
	   		        
	   		     //   Global.mNode.
	   		     
	   	 		 Global.mRejectedList.add(FullString);
	   	 		// adapter.notifyDataSetChanged();
	   	 		 
	   	 		 mConfirmNumber.setText("");
	   	 		 mPhoneNumber.setText("");
	   	 		 mMonth.setText("");
	   	 		 mDate.setText("");
	   	 		 mYear.setText("");
	   	 		
	   	 		// mListView.setVisibility(View.VISIBLE);
	   	 		 Utilities.BackUpMsgMap(mContext);
	   	 		 BackUpGmailContacts(Global.mRejectedList);
	   	 		 Utilities.putToast(t_PhoneStr + " is added to black list.",0, 400, getApplicationContext());
		         finish();
	   	 	 }
   	 	 
        });
        
        mHome.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
     	 		 
     	 	    BackUpGmailContacts(Global.mRejectedList);
     	 		 
     	 		if (SmsIndicator.contentEquals(Constants.SMS_YES_Indicator)){
      	 		   SmsIndicator = Constants.SMS_NO_Indicator;
      	 		   mSmsIndicatorYes.setVisibility(View.GONE);
      	 		   mSmsIndicatorNo.setVisibility(View.VISIBLE);
      	 	    }
      	 		
      	 		if (SmsOption.contentEquals(Constants.SMS_YES_OPTION)){
       	 		   SmsOption = Constants.SMS_NO_OPTION;
       	 		   mSmsOptionYes.setVisibility(View.GONE);
       	 		   mSmsOptionNo.setVisibility(View.VISIBLE);
       	 	    }
      	 		
      	 		mConfirmNumber.setText("");
      	 		mPhoneNumber.setText("");
      	 		mMonth.setText("");
      	 		mDate.setText("");
      	 		mYear.setText("");
      	 		
      	 		mContactSubmitter.setVisibility(View.GONE);
      	 		mResetButton.setVisibility(View.GONE);
      	 		mAddButton.setVisibility(View.GONE);
      	 		m_phoneContactList.setVisibility(View.VISIBLE);
      	 		
	   	 	    finish();
     	 	 }
          });  
	}

	 // Write the messages in Global.alertedmsgInfo to file 
    public void BackUpGmailContacts( ArrayList<String> tGmailList){
    	    String temp = "";
    	    int length = tGmailList.size();
    	    if (length == 0){
    	    	deleteFile (Constants.REJECTED_LIST_FILE);
    	    	return;
    	    	
    	    }
    	    for (int i = 0; i < length; i++){
    	    	String temp_str = tGmailList.get(i);
    	        String  Ticker_str = temp_str + Constants.END_MARK;
    	    		   temp += Ticker_str;
    	    		   Ticker_str = null;	   
    	    }
   
    	    Log.d("BackupStocks", "Stocks are = " + temp);
    	            
    	    FileOutputStream f_writer = null;
			try {
				f_writer = openFileOutput(Constants.REJECTED_LIST_FILE, Context.MODE_PRIVATE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
    	    if (f_writer != null){
    	       	try {
					 f_writer.write(temp.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
    	    
    	    try {
				f_writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
    }
	
   
	 @Override
		protected void onResume() {
			super.onResume();
			
			 SmsIndicator = Constants.SMS_NO_Indicator;
			 try {
					Utilities.get_MessageList(mContext, Constants.MESSAGE_MAP_FILE);
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 }
			 
			
			  try {
				    Global.mRejectedList.clear();
					ret_val = Utilities.getListFromFile(mContext, Global.mRejectedList, Constants.REJECTED_LIST_FILE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  
            if (ret_val != 1){
       	    Log.d("content buff   12:  ", "CONTACT INBOX is either empty or not existed");
       	 	
           }  
	 }
 
	 protected void onPause() {
	        super.onPause();
	        BackUpGmailContacts(Global.mRejectedList);
	        Utilities.BackUpMsgMap(mContext);
	    }
	
		public String FormMonthDateYearStr(String monthStr, String dateStr, String yearStr){
			
			String formedString = "Block until : " + monthStr + "/" + dateStr + "/" + yearStr;
			
		    return formedString;
			
		}
		
		public void SetTrueFalse(boolean trueOrfalse){
			mConfirmNumber.setClickable(trueOrfalse);
			mMonth.setClickable(trueOrfalse);
			mDate.setClickable(trueOrfalse);
			mYear.setClickable(trueOrfalse);
			mPhoneNumber.setClickable(trueOrfalse);
		}
		
		
	
		  void startSelectContactActivity() {
			   
				String [] phoneContactList = null;
			    phoneContactList =   getPhoneContactList();
			    Intent PhoneListIntent = new Intent(this, SelectPhoneContact.class);
			    PhoneListIntent.putExtra(Constants.PHONE_LIST, phoneContactList);
			    startActivityForResult(PhoneListIntent, Constants.PHONE_CONTACT); 
		   }
		  
		 @Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				super.onActivityResult(requestCode, resultCode, data);
				
				String lContactNumber = " ";
				Log.d("REQUESTED CODE",  String.valueOf(requestCode));
			    if (requestCode == Constants.PHONE_CONTACT){
				    if (data != null){
				    	Log.d("REQUESTED CODE",  String.valueOf(requestCode));
				    	String phoneStr = data.getStringExtra(Constants.PHONE_NUMBER);
				    	lContactNumber = Utilities.getDigitsOnly(phoneStr.substring(phoneStr.indexOf(",")));
				    	mPhoneNumber.setText(Utilities.getDigitsOnly(lContactNumber));
						mConfirmNumber.setText(Utilities.getDigitsOnly(lContactNumber));
				    }	
				    else {
				    		 Log.d("REQUESTED CODE",  "DATA = NULL");
				    }
				  }
		 }
		 
		 private String[] getPhoneContactList(){
			    ContentResolver cr = getContentResolver();
			    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			    ArrayList<String> contact_names = new ArrayList<String>();
			    
			    if (cur.getCount() > 0) {
			        while (cur.moveToNext()) {
			            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
			            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			            if (cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER.trim()))
			            		.equalsIgnoreCase("1")){
			                if (name!=null){
			                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{id}, null);
			                    while (pCur.moveToNext()) {
			                        String PhoneNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			                        contact_names.add(name + ", " + PhoneNumber);
			                    }
			                    pCur.close();
			                    pCur.deactivate();
			                }
			            }
			        }
			        cur.close();
			        cur.deactivate();
			    }

			    String[] contactList = new String[contact_names.size()]; 
			    for(int j = 0; j < contact_names.size(); j++){
			        contactList[j] = contact_names.get(j);
			    }
			    return contactList;
			}
		 
		 
		 public void checkForKITKAT(){
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
	}
		