package com.anderson.rejectcallmsg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class InsertBlackListAreaCode extends Activity {
	private int ret_val = 0;
	private Button mContactSubmitter;
	private Button mHome;
	private Button mAddButton;
	private Button mResetButton;
	private EditText mConfirmNumber;
	private EditText mPhoneNumber;
	//private EditText mNameEntry = null;
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
	private String ExceptionOption = Constants.SMS_NO_Indicator;
	private String t_PhoneStr = null;
	Context mContext;
	private Button m_USA_AreaCodeList;
	public static final String CONTACT_NUMBER = "contact_number";
	private boolean SELECT_FROM_CONTACT_LIST = true;
	private final int until_year = 2025;
	private final int until_month = 12;
	private final int until_date = 30;
	private String phonesStr = "";
	
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
				String compareStr = "";
			
				if ( mPhoneNumber.getText().toString().trim().length() > 0){
					//m_phoneContactList.setVisibility(View.GONE);
					mAddButton.setVisibility(View.GONE);
					mResetButton.setVisibility(View.VISIBLE);
				}
				else{
					mResetButton.setVisibility(View.GONE);
					//m_phoneContactList.setVisibility(View.GONE);
					//m_phoneContactList.setVisibility(View.VISIBLE);
				}
				
				if ( mPhoneNumber.getText().toString().trim().length() == 3){
					compareStr = mPhoneNumber.getText().toString().trim();
					mConfirmNumber.requestFocus();
				}
				
				 if (mConfirmNumber.getText().toString().trim().length() == 3){
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
					    mDate.setEnabled(true);
					    mDate.requestFocus();
				}	
				
				if (mDate.getText().toString().trim().length() == 2) {
					mYear.setEnabled(true);
					mYear.requestFocus();
				}	
				

				  if ( ( mPhoneNumber.getText().toString().trim().length()>= 3) &&
						(mConfirmNumber.getText().toString().trim().length() ==
							        mPhoneNumber.getText().toString().trim().length()) &&
							        (mMonth.getText().toString().trim().length() == 2) &&
							        (mDate.getText().toString().trim().length() == 2) &&
							        ( mYear.getText().toString().trim().length() == 4 )){
						
						mContactSubmitter.setVisibility(View.VISIBLE);
					    Utilities.closeSoftKeyboard(mContext, mConfirmNumber);
			      }
				  else {
						mContactSubmitter.setVisibility(View.GONE);
						
				  }


		}
	};
	    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertblacklistareacode);
       
        SELECT_FROM_CONTACT_LIST = true;
    	mContext = this;
    	//mNameEntry = (EditText) findViewById(R.id.NameEntry);
		//mNameEntry.requestFocus();
		mPhoneNumber = (EditText)findViewById(R.id.GmailAddressEntry);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mPhoneNumber, InputMethodManager.SHOW_IMPLICIT);
		
		
		mYourMsg = (EditText)findViewById(R.id.YourMessage);
		mYourMsg.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mYourMsg.setText("");
				return true;
			}
		});

		mYourMsg.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		
		 m_USA_AreaCodeList = (Button)findViewById(R.id.USA_AreaCodeList);
		 m_USA_AreaCodeList.setOnClickListener(new Button.OnClickListener(){
         	   public void onClick(View v) {
         	   Intent AC_Intent = new Intent( getApplicationContext(), SelectFromAreaCodeList.class);
               startActivityForResult(AC_Intent, Constants.AC_LIST);
         	   }
        });

		mSmsOptionNo = (Button) findViewById(R.id.SmsOptionNo);
		mSmsOptionNo.setOnClickListener(new Button.OnClickListener(){
    	 	 public void onClick(View v) {
    	 		SmsOption = Constants.SMS_YES_OPTION;
    	 		mSmsOptionNo.setVisibility(View.GONE);
    	 		mSmsOptionYes.setVisibility(View.VISIBLE);
    	 		checkForKITKAT();
    	 	 }
        });
		
		mSmsOptionYes = (Button) findViewById(R.id.SmsOptionYes);
		mSmsOptionYes.setOnClickListener(new Button.OnClickListener(){
    	 	 public void onClick(View v) {
    	 		SmsOption = Constants.SMS_NO_OPTION;
    	 		mSmsOptionNo.setVisibility(View.VISIBLE);
    	 		mSmsOptionYes.setVisibility(View.GONE);
    	 	 }
       });
		
		mSmsIndicatorNo = (Button) findViewById(R.id.SmsIndicatorNo);
		mSmsIndicatorYes = (Button) findViewById(R.id.SmsIndicatorYes);
		
		mSmsIndicatorNo.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
     	 		SmsIndicator = Constants.SMS_YES_Indicator;
     	 		mSmsIndicatorNo.setVisibility(View.GONE);
     	 		mSmsIndicatorYes.setVisibility(View.VISIBLE);
     	 		mScrollMsgView.setVisibility(View.VISIBLE);
     	 		mYourMsg.requestFocus();
     	 		
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

		mConfirmNumber = (EditText)findViewById(R.id.ConfirmAddressEntry);
		mConfirmNumber.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mConfirmNumber.setText("");
				return true;
			}
		});

	    mDate = (EditText)findViewById(R.id.Date);
		mDate.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mDate.setText("");
				return true;
			}
		});

	    mMonth = (EditText)findViewById(R.id.Month);
		mMonth.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mMonth.setText("");
				return true;
			}
		});

	    mYear = (EditText)findViewById(R.id.Year);
		mYear.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mYear.setText("");
				return true;
			}
		});

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
        mContactSubmitter = (Button)findViewById(R.id.submitButton);
        mContactSubmitter.setVisibility(View.GONE);
        mHome =  (Button)findViewById(R.id.homeButton);
       
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
      	 		 mListView.setVisibility(View.GONE);
      	 		 mAddButton.setVisibility(View.GONE);
      	 		 mResetButton.setVisibility(View.GONE);
      	 		// m_phoneContactList.setVisibility(View.VISIBLE);
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
				m_USA_AreaCodeList.setVisibility(View.VISIBLE);
     	 		mPhoneNumber.requestFocus();
     	 	 }
        });
        
        
         mContactSubmitter.setOnClickListener(new Button.OnClickListener(){
   	 	   public void onClick(View v) {
   	 	  
	   	 	     InputMethodManager imm = (InputMethodManager)
	   	 		 getSystemService(Context.INPUT_METHOD_SERVICE);
	   	 		 imm.hideSoftInputFromInputMethod(null, DEFAULT_KEYS_DISABLE);
	   	 		 imm.hideSoftInputFromWindow(mYourMsg.getWindowToken(), 0);
			     t_PhoneStr  = mPhoneNumber.getText().toString().trim();

                 String city_Str = "";
			     if ((city_Str = Utilities.readAndCheck(R.raw.areacode, t_PhoneStr, mContext)) == null){
					 Utilities.putToast("Area Code " + t_PhoneStr + " is not existed", 0, 300, mContext);
					 mConfirmNumber.setText("");
					 mPhoneNumber.setText("");
					 mMonth.setText("");
					 mDate.setText("");
					 mYear.setText("");
					 mYourMsg.setText("");
					 return;
				 }

	   	 		 if (Utilities.checkDuplicatedAreaCode(t_PhoneStr, Global.mAreaCodeRejectedList, mContext)){
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
	     	 		//m_phoneContactList.setVisibility(View.VISIBLE);
	     	 		mPhoneNumber.requestFocus();
	                return;
	   	 		 }
	   	 		 
	   	 		 if (t_PhoneStr.contains(Constants.END_MARK)){
	   	 			Utilities.putToast("This area code is invalid: " + Constants.END_MARK + " is not accepted.",0, 400, getApplicationContext());
	   	 			return;
	   	 		 }
	   	 	     String t_ConfirmStr  = mConfirmNumber.getText().toString().trim(); 
	   		     Log.d("Setup Phone STR", t_PhoneStr);
	   		   
	   		     if ((t_PhoneStr.length() == 0) && (t_ConfirmStr.length() == 0)){
	   			    Utilities.putToast("Missing both area code and confirm number.",0, 400, getApplicationContext());
			        mPhoneNumber.requestFocus();
				    return;
			     }
	   		   else
	   		   if (t_PhoneStr.length() == 0){
	   			    Utilities.putToast("Missing area code.",0, 400, getApplicationContext());
		            mPhoneNumber.requestFocus();
				    return;
	   		     }
	   		   else 
	   		     if (t_ConfirmStr.length() == 0){
	   		    	Utilities.putToast("Missing confirm number.",0, 400, getApplicationContext());
	   		    	mConfirmNumber.requestFocus();
				    return;
	   		     }
	   		   
	   		 /*  if (SELECT_FROM_CONTACT_LIST){
		   		   if (!phonePattern.matcher(t_PhoneStr).matches()){
		   			   Utilities.putToast("Phone number is invalid.",0, 400, getApplicationContext());
		   			   mPhoneNumber.requestFocus();
		   			   return;
		   		   }
	   		   }
	   		  */ 
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

			     Utilities.putToast("City :" + city_Str, 0 , 100, mContext);
	   		     String FullString = Constants.CITY + city_Str + "\n"+ Constants.AREA_CODE_TITLE +
						             t_PhoneStr + "\n" + Utilities.FormMonthDateYearStr(mMonthStr, mDateStr, mYearStr) + "\n"
						             + SmsIndicator + "\n" + SmsOption;
	   		     

	   		     if (SmsIndicator.contentEquals(Constants.SMS_YES_Indicator)){
	   		    	Log.d("--- MESSAGE: ", msgStr);
	   		        Global.mMap.put(t_PhoneStr, msgStr );
	   		     }
	   		     //if (ExceptionOption.contentEquals(Constants.EXCPT_YES_OPTION)){
	   		    //	Log.d("--- Excepcted numbers----: ", msgStr);
	   		     //   Global.mMap_ExceptedNumber.put(t_PhoneStr, phonesStr );
	   		     //}
	   		     
	   		        mScrollMsgView.setVisibility(View.GONE);
	   		        SmsIndicator = "";
	   		        mYourMsg.setClickable(false);
	   		        mSmsSelection.setVisibility(View.VISIBLE); 
	   		        mSmsIndicatorYes.setVisibility(View.GONE);
	   		        mSmsIndicatorNo.setVisibility(View.VISIBLE);
			        Utilities.insertInOrder(Global.mAreaCodeRejectedList, FullString);
	   	 		   // Global.mAreaCodeRejectedList.add(FullString);
	   	 		    Global.AreaCodeChecker = true;
	   	 		    mConfirmNumber.setText("");
	   	 		    mPhoneNumber.setText("");
			   mMonth.setText("");
			   mDate.setText("");
			   mYear.setText("");
	   	 		
	   	 		// mListView.setVisibility(View.VISIBLE);
	   	 		 Utilities.BackUpMsgMap(mContext);
			   Utilities.BackUpAList(Global.mAreaCodeRejectedList, Constants.AREA_CODE_REJECTED_LIST_FILE, mContext);
	   	 		 Utilities.putToast(t_PhoneStr + " is added to black list.",0, 400, getApplicationContext());
		         finish();
	   	 	 }
   	 	 
        });
        
        mHome.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
     	 		 
     	 	    BackUpGmailContacts(Global.mAreaCodeRejectedList);
     	 		 
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
      	 		//m_phoneContactList.setVisibility(View.VISIBLE);
      	 		
	   	 	    finish();
     	 	 }
          });  
	}

	 // Write the messages in Global.alertedmsgInfo to file 
    public void BackUpGmailContacts( ArrayList<String> tGmailList){
    	    String temp = "";
    	    int length = tGmailList.size();
    	    if (length == 0){
    	    	deleteFile (Constants.AREA_CODE_REJECTED_LIST_FILE);
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
				f_writer = openFileOutput(Constants.AREA_CODE_REJECTED_LIST_FILE, Context.MODE_PRIVATE);
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
				    Global.mAreaCodeRejectedList.clear();
					ret_val = Utilities.getListFromFile(mContext, Global.mAreaCodeRejectedList, Constants.AREA_CODE_REJECTED_LIST_FILE);
			 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 }
			
             if (ret_val != 1){
       	     Log.d("content buff   12:  ", "No rejected number found");
       	 	
           }  
	 }
 
	 protected void onPause() {
	        super.onPause();
	        BackUpGmailContacts(Global.mAreaCodeRejectedList);
	        Utilities.BackUpMsgMap(mContext);
	    }
		
	public void SetTrueFalse(boolean trueOrfalse){
			mConfirmNumber.setClickable(trueOrfalse);
			mMonth.setClickable(trueOrfalse);
			mDate.setClickable(trueOrfalse);
			mYear.setClickable(trueOrfalse);
			mPhoneNumber.setClickable(trueOrfalse);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				super.onActivityResult(requestCode, resultCode, data);

				Log.d("REQUESTED CODE",  String.valueOf(requestCode));
			    if (requestCode == Constants.AC_LIST){
			    	  if (data != null) {
						  finish();
					  }
			    }
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
		