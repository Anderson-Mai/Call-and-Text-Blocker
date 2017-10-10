package com.anderson.rejectcallmsg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class InsertBlackListNumber extends Activity {
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
	private EditText mNameEntry = null;
	private String SmsIndicator = Constants.SMS_NO_Indicator;
	private String SmsOption = Constants.SMS_NO_OPTION;
	private Context mContext;
	private Button mListMenu;
	public static final String CONTACT_NUMBER = "contact_number";

	private String mPhoneStr  = "";
	private String mMonthStr = "";
	private String mYearStr = "";
	private String mDateStr = "";

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
					String temp_ConfirmStr = mConfirmNumber.getText().toString().trim();
					boolean confirm_flag = false;
					int phoneLen = compareStr.length();
					int confirm_Len = temp_ConfirmStr.length();

					if (phoneLen > 0){
						mListMenu.setVisibility(View.GONE);
						mAddButton.setVisibility(View.GONE);
						mResetButton.setVisibility(View.VISIBLE);
					}
					else{
						mResetButton.setVisibility(View.GONE);
						mListMenu.setVisibility(View.VISIBLE);
					}

					//if (mPhoneNumber.hasFocus())
					if (phoneLen > 0){

						if (compareStr.substring(0, 1).contentEquals("1")) {
							if (phoneLen == 11) {
									if (Utilities.readAndCheck(R.raw.areacode, compareStr.substring(1, 4), mContext) != null) {
										mConfirmNumber.requestFocus();
									} else {
										Utilities.putToast("Contains invalid Area Code", 0, 300, mContext);
										mPhoneNumber.setText("");
										mPhoneNumber.requestFocus();
										return;
									}
							}
						}
						else {
							if (phoneLen == 10) {
								if (Utilities.readAndCheck(R.raw.areacode, compareStr.substring(0, 3), mContext) != null) {
									mConfirmNumber.requestFocus();
								}
								else {
									Utilities.putToast("Contains invalid Area Code", 0, 300, mContext);
									mPhoneNumber.setText("");
									mPhoneNumber.requestFocus();
									return;
								}
							}

						}
					}

					//if (mConfirmNumber.hasFocus())
					{

								if (confirm_Len > 0) {
									String temp_Char = temp_ConfirmStr.substring(0, 1);
									if (temp_Char.contentEquals("1")) {
										if (confirm_Len == 11) {
											if (phoneLen == confirm_Len) {
												if (compareStr.contentEquals(temp_ConfirmStr)) {
													mMonth.setEnabled(true);
													mMonth.requestFocus();
													confirm_flag = true;

												} else {
													Utilities.putToast("Confirmation fails.", 0, 400, getApplicationContext());
													mContactSubmitter.setVisibility(View.GONE);
													mConfirmNumber.setText("");
													mConfirmNumber.requestFocus();
													return;
												}
											}
											else
											if (phoneLen == (confirm_Len - 1)){
												if (compareStr.contentEquals(temp_ConfirmStr.substring(1))) {
													mMonth.setEnabled(true);
													mMonth.requestFocus();
													confirm_flag = true;

												} else {
													Utilities.putToast("Confirmation fails.", 0, 400, getApplicationContext());
													mContactSubmitter.setVisibility(View.GONE);
													mConfirmNumber.setText("");
													mConfirmNumber.requestFocus();
													return;
												}

											}

										}
									}
								    else{
										if (confirm_Len == 10) {
											if (phoneLen == confirm_Len) {
												if (compareStr.contentEquals(temp_ConfirmStr)) {
													mMonth.setEnabled(true);
													mMonth.requestFocus();
													confirm_flag = true;
												} else {
													Utilities.putToast("Confirmation fails.", 0, 400, getApplicationContext());
													mContactSubmitter.setVisibility(View.GONE);
													mConfirmNumber.setText("");
													mConfirmNumber.requestFocus();
													confirm_flag = false;
													return;
												}
											}
											else
											if (phoneLen == (confirm_Len + 1)){
												if (temp_ConfirmStr.contentEquals(compareStr.substring(1))) {
													mMonth.setEnabled(true);
													mMonth.requestFocus();
													confirm_flag = true;
												} else {
													Utilities.putToast("Confirmation fails.", 0, 400, getApplicationContext());
													mContactSubmitter.setVisibility(View.GONE);
													mConfirmNumber.setText("");
													mConfirmNumber.requestFocus();
													confirm_flag = false;
													return;
												}

											}

										}
									}
								}
							}


                       if ((!mConfirmNumber.hasFocus()) && (!mPhoneNumber.hasFocus())){
						if (mMonth.getText().toString().trim().length() == 2) {
							mDate.setEnabled(true);
							mDate.requestFocus();
							if (mDate.getText().toString().trim().length() == 2) {
								mYear.setEnabled(true);
								mYear.requestFocus();
							}
						}
					}


					if ( confirm_flag  && (mMonth.getText().toString().trim().length() == 2) &&
									        (mDate.getText().toString().trim().length() == 2) &&
									        ( mYear.getText().toString().trim().length() == 4 )){
						       int ret_val = Utilities.checkDuplicatedNameNumber(compareStr, Global.mRejectedList, mContext, true);

						if (ret_val == 1){
								   mNameEntry.setText("");
								   mConfirmNumber.setText("");
								   mPhoneNumber.setText("");
								   mMonth.setText("");
								   mDate.setText("");
								   mYear.setText("");
								   mNameEntry.requestFocus();
								   return;
								}
								mContactSubmitter.setVisibility(View.VISIBLE);
						        Utilities.closeSoftKeyboard(mContext, mYear);
					}
					else {
						 mContactSubmitter.setVisibility(View.GONE);
						 // m_rejectedNumberList.setVisibility(View.VISIBLE);
					}
				  }		    		
			
		};
	    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertblacklistnumber);

    	mContext = this;
    	mPhoneNumber = (EditText)findViewById(R.id.GmailAddressEntry);
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mPhoneNumber, InputMethodManager.SHOW_IMPLICIT);
		
		mNameEntry = (EditText) findViewById(R.id.NameEntry);
		mNameEntry.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mNameEntry.setText("");
				return true;
			}
		});
		mNameEntry.requestFocus();
		
		mYourMsg = (EditText)findViewById(R.id.YourMessage);
		mYourMsg.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mYourMsg.setText("");
				mYourMsg.requestFocus();
				return true;
			}
		});

		mYourMsg.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

		mListMenu  = (Button)findViewById(R.id.list_menu);
		mListMenu.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Utilities.closeSoftKeyboard(mContext, mListMenu);
				openOptionsMenu();

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
				 mListMenu.setVisibility(View.VISIBLE);
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
     	 		mNameEntry.setText("");
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
				mListMenu.setVisibility(View.VISIBLE);
     	 		mNameEntry.requestFocus();
				Utilities.openSoftKeyboard(mContext);
     	 		//InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    			//View view = getCurrentFocus();
    			//imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
     	 	 }
       });

        mContactSubmitter.setOnClickListener(new Button.OnClickListener(){
   	 	 public void onClick(View v) {
   	 	
	   	 	    /* InputMethodManager imm = (InputMethodManager)
	   	 		 getSystemService(Context.INPUT_METHOD_SERVICE);
	   	 		 imm.hideSoftInputFromInputMethod(null, DEFAULT_KEYS_DISABLE);
	   	 		 imm.hideSoftInputFromWindow(mYourMsg.getWindowToken(), 0);
	   	 		 */
			     Utilities.closeSoftKeyboard(mContext, mYear);
   	 		     String t_NameStr = mNameEntry.getText().toString().trim();
			     if (t_NameStr.isEmpty()){
					 t_NameStr = Constants.anonymous;
				 }
				 String	 t_PhoneStr = mPhoneNumber.getText().toString().trim();
			     t_PhoneStr = Utilities.uniformNumberFormat(Utilities.getDigitsOnly(t_PhoneStr));

			     /*if (Utilities.readAndCheck(R.raw.areacode,t_PhoneStr.substring(1,4), mContext) == null){
					 Utilities.putToast(t_PhoneStr + " is not existed", 0, 300, mContext);

					// InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// View view = getCurrentFocus();
					// imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

					 Utilities.openSoftKeyboard(mContext);

					 mNameEntry.setText("");
					 mConfirmNumber.setText("");
					 mPhoneNumber.setText("");
					 mMonth.setText("");
					 mDate.setText("");
					 mYear.setText("");
					 mYourMsg.setText("");
					 mNameEntry.requestFocus();
					 return;
			     }
	   	 		*/
	   	 		 /* if (Utilities.checkDuplicatedNameNumber(t_PhoneStr, t_NameStr, Global.mRejectedList, mContext, true, true) != 0){
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
	     	 		mListMenu.setVisibility(View.VISIBLE);
					mNameEntry.setText("");
					mNameEntry.requestFocus();
	                return;
	   	 		 }
	   	 		*/
	   	 		// if (t_PhoneStr.contains(Constants.END_MARK)){
	   	 		//	Utilities.putToast("Phone number is invalid: " + Constants.END_MARK + " is not accepted.",0, 400, getApplicationContext());
	   	 		//	return;
	   	 		// }
	   	 	    // String t_ConfirmStr  = mConfirmNumber.getText().toString().trim();
	   	 		// Pattern phonePattern = Patterns.PHONE; // API level 8+
	   		   //  Log.d("Setup Phone STR", t_PhoneStr);
	   		   
	   		   // if ((t_PhoneStr.length() == 0) && (t_ConfirmStr.length() == 0)){
	   			//    Utilities.putToast("Missing both phone number and confirm number.",0, 400, getApplicationContext());
			   //     mPhoneNumber.requestFocus();
			   //    return;
			  //   }
	   		 //  else
	   		 //  if (t_PhoneStr.length() == 0){
	   		//	    Utilities.putToast("Missing phone number.",0, 400, getApplicationContext());
		    //        mPhoneNumber.requestFocus();
			//	    return;
	   		 //    }
	   		 //  else 
	   		 //    if (t_ConfirmStr.length() == 0){
	   		//    	Utilities.putToast("Missing confirm number.",0, 400, getApplicationContext());
	   		//    	mConfirmNumber.requestFocus();
			//	    return;
	   		//     }
	   		   
	   		 //  if (SELECT_FROM_CONTACT_LIST){
		   	//	   if (!phonePattern.matcher(t_PhoneStr).matches()){
		   	//		   Utilities.putToast("Invalid USA Phone number.",0, 400, getApplicationContext());
		   	//		   mPhoneNumber.requestFocus();
		   	//		   return;
		   	////	   }
	   		//   }
	   		   
	   		//   if (!t_PhoneStr.contentEquals(t_ConfirmStr)){
	   		//	    Utilities.putToast("Confirmation fails.",0, 400, getApplicationContext());
		//		    return;
		//	   }
	   	       String mYearStr = mYear.getText().toString().trim();
		       String mMonthStr = mMonth.getText().toString().trim();		 
			   String mDateStr = mDate.getText().toString().trim();
			    
				if ((Integer.parseInt(mYearStr) == 0) &&
				   (Integer.parseInt(mMonthStr) == 0) && 
				   (Integer.parseInt(mDateStr) == 0)||
			         (mYearStr.isEmpty() && mMonthStr.isEmpty() && mDateStr.isEmpty())){
					    mYearStr = String.valueOf("9999");
					    mMonthStr = String.valueOf("12");
					    mDateStr = String.valueOf("30");
				}
	   		    if(!Utilities.CheckValidDate(mMonthStr , mDateStr, mYearStr, mContext)){
	   		    	 return; 
				}

	   		     String msgStr = "";
	   		     if (SmsIndicator.contentEquals(Constants.SMS_YES_Indicator)){
	   		         msgStr = mYourMsg.getText().toString().trim();
	   		        // if (Utilities.checkForSpecialCharacter(msgStr, mContext)){
	   		        //	 return;
	   		        // }
	   		     }
	   		     String FullString = Constants.NAMESTR + t_NameStr + "\n" + Constants.NUMBER_TITLE + t_PhoneStr + "\n" + 
	   		                         FormMonthDateYearStr(mMonthStr, mDateStr, mYearStr) + "\n" + SmsIndicator + "\n" + SmsOption;
	   		     
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
	   		     Utilities.insertInOrder(Global.mRejectedList, FullString);
	   	 		 //Global.mRejectedList.add(FullString);
	   	 		// adapter.notifyDataSetChanged();
	   	 		 
	   	 		 mConfirmNumber.setText("");
	   	 		 mPhoneNumber.setText("");
	   	 		 mMonth.setText("");
	   	 		 mDate.setText("");
	   	 		 mYear.setText("");
	   	 		
	   	 		// mListView.setVisibility(View.VISIBLE);
	   	 		 Utilities.BackUpMsgMap(mContext);
	   	 		 Utilities.BackUpAList(Global.mRejectedList, Constants.REJECTED_LIST_FILE, mContext);
	   	 		 Utilities.putToast(t_PhoneStr + " is added to BlackList.",0, 400, getApplicationContext());
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
      	 		mListMenu.setVisibility(View.VISIBLE);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.source_menu, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.rejected_call_list:
				startSelectRejectedCallActivity();
				return true;
			case R.id.contact_list:
				startSelectContactActivity();
				return true;
			default:
				return super.onOptionsItemSelected(item);
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
			  Intent PhoneListIntent = new Intent(this, SelectPhoneContact_BlackList.class);
			  startActivityForResult(PhoneListIntent, Constants.PHONE_CONTACT);
		   }
		  
		  void startSelectRejectedCallActivity() {
				//String [] phoneRejectList = null;
				//phoneRejectList =   Utilities.getPhoneRejectList(mContext);
				//if (phoneRejectList == null){
				//	Utilities.putToast("No missing calls", 0, 300, mContext);
				//	return;
				//}
			    Intent callRejectIntent = new Intent(this, SelectPhoneRejectedList.class);
			    callRejectIntent.putExtra(Constants.PHONE_REJECTED_LIST, Constants.BLACKLIST_TYPE);
			    startActivityForResult(callRejectIntent, Constants.PHONE_REJECT); 
		   }
		  
		 @Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				super.onActivityResult(requestCode, resultCode, data);
			    int contactLength = 0;
				String lContactNumber = " ";
				Log.d("REQUESTED CODE", String.valueOf(requestCode));


			 if ((requestCode == Constants.PHONE_CONTACT) && (resultCode != -1)){
				 if (data != null) {
					 Log.d("REQUESTED CODE", String.valueOf(requestCode));
					 //String phoneStr = data.getStringExtra(Constants.PHONE_NUMBER);
					 String namephoneStr = data.getStringExtra(Constants.PHONE_NUMBER);
					 if (resultCode == 2){
						 if (namephoneStr.contentEquals(Constants.PORTING_ALL)) {
							 Utilities.putToast("Contact list has been ported to USA BackList.", 0, 300, mContext);
							 finish();
						 }
					 }
					 else
					 if (resultCode == 1){
						 finish();
					 }
				 }
			 }
			 else
			 if (requestCode == Constants.PHONE_REJECT){
					    if (data != null){
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
		 protected boolean readAndCheckAC(int rawFileId, String mAreaCodeStr){
		     int  mAreaCodeInt =  Integer.parseInt(mAreaCodeStr);
			 InputStream inputStream = mContext.getResources().openRawResource(rawFileId);

             InputStreamReader inputreader = new InputStreamReader(inputStream);
             BufferedReader buffreader = new BufferedReader(inputreader);
              
             String line = "";
              
             try {
                while (( line = buffreader.readLine()) != null) {
                	 String temp_AreaCodeStr =   line.substring(0, 3);
                	
                     if (temp_AreaCodeStr.contains(mAreaCodeStr)){
                    	 return true;
                     }
                     else if (Integer.parseInt(temp_AreaCodeStr) > mAreaCodeInt){
                    	 return false;	 
                     }
                     line = "";
                }
            } catch (IOException e) {
            	return false;
            }	
             return false;
		}
	}
		