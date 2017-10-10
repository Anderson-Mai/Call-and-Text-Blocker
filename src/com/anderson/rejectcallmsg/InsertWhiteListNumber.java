package com.anderson.rejectcallmsg;

import java.io.IOException;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class InsertWhiteListNumber extends Activity {
	private int ret_val = 0;
	private Button mContactSubmitter;
	private Button mBack;
	//private Button mAddButton;
	private Button mResetButton;
	private EditText mConfirmNumber;
	private EditText mPhoneNumber;
	private EditText mNameEntry = null;
	private LinearLayout mListView;
	Context mContext;
	private Button m_phoneContactList;
	public static final String CONTACT_NUMBER = "contact_number";
	
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

			String no_space_confirm =  Utilities.skipSpacesInString(mConfirmNumber.getText().toString().trim());
			String no_space_number =  Utilities.skipSpacesInString(mPhoneNumber.getText().toString().trim());

			int numbStrLength_1 = no_space_number.length();
			int numbStrLength_2 = no_space_confirm.length();


			if ((numbStrLength_1 >= 3) && (numbStrLength_1 == numbStrLength_2)) {

				if (no_space_number.equalsIgnoreCase(no_space_confirm)) {
					mContactSubmitter.setVisibility(View.VISIBLE);
					View view = getCurrentFocus();
					Utilities.closeSoftKeyboard(mContext, view);
					m_phoneContactList.setVisibility(View.GONE);
					mResetButton.setVisibility(View.VISIBLE);
					mContactSubmitter.setVisibility(View.VISIBLE);

				} else {
					Utilities.putToast("Can not confirm this number.", 0, 300, mContext);
					mContactSubmitter.setVisibility(View.GONE);
					mResetButton.setVisibility(View.GONE);
					m_phoneContactList.setVisibility(View.VISIBLE);

				}
			}
			else {
				        mContactSubmitter.setVisibility(View.GONE);
						mResetButton.setVisibility(View.GONE);
						m_phoneContactList.setVisibility(View.VISIBLE);

			}
		}
    };
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertwhitelistnumber);

		mContext = this;
		mPhoneNumber = (EditText)findViewById(R.id.GmailAddressEntry);
		mPhoneNumber.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mPhoneNumber.setText("");
				return true;
			}
		});
		mPhoneNumber.addTextChangedListener(mTextWatcher);
		//mPhoneNumber.requestFocus();
		
		mNameEntry = (EditText) findViewById(R.id.NameEntry);
		mNameEntry.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mNameEntry.setText("");
				return true;
			}
		});

		mNameEntry.requestFocus();
	
		//InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		//imm.showSoftInput(mPhoneNumber, InputMethodManager.SHOW_IMPLICIT);
		
		m_phoneContactList = (Button)findViewById(R.id.PhoneContactList);
        m_phoneContactList.setOnClickListener(new Button.OnClickListener(){
         	   public void onClick(View v) {
				Utilities.closeSoftKeyboard(mContext, m_phoneContactList );
         		startSelectContactActivity();
            }
        });
                  
		mConfirmNumber = (EditText)findViewById(R.id.ConfirmAddressEntry);	
		mConfirmNumber.addTextChangedListener(mTextWatcher);
        mConfirmNumber.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mConfirmNumber.requestFocus();
				return false;
			}
		});
        
        mContactSubmitter = (Button)findViewById(R.id.submitButton);
        mBack =  (Button)findViewById(R.id.backButton);
        mListView = (LinearLayout)findViewById(R.id.WrappedViewTwo);
       
        //mAddButton = (Button) findViewById(R.id.button_add);
        mResetButton = (Button) findViewById(R.id.button_reset);
		
	  /*  mAddButton.setOnClickListener(new Button.OnClickListener(){
      	 	 public void onClick(View v) {
      	 		// mWrappedView.setVisibility(View.VISIBLE);
      	 		 mListView.setVisibility(View.GONE);
      	 		 mAddButton.setVisibility(View.GONE);
      	 		 mResetButton.setVisibility(View.GONE);
      	 		 m_phoneContactList.setVisibility(View.VISIBLE);
      	 	 }
        });
    */
        mResetButton.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
				mNameEntry.setText("");
     	 		mConfirmNumber.setText("");
     	 		mPhoneNumber.setText("");
     	 		mBack.setVisibility(View.VISIBLE);
     	 		mContactSubmitter.setVisibility(View.GONE);
     	 		mResetButton.setVisibility(View.GONE);
     	 		//mAddButton.setVisibility(View.GONE);
     	 		m_phoneContactList.setVisibility(View.VISIBLE);
				mNameEntry.requestFocus();
				Utilities.openSoftKeyboard(mContext);
     	 	 }
       });
        
        
        mContactSubmitter.setOnClickListener(new Button.OnClickListener(){
   	 	 public void onClick(View v) {
   	 	
	   	 	   // InputMethodManager imm = (InputMethodManager)
	   	 		// getSystemService(Context.INPUT_METHOD_SERVICE);
	   	 		// imm.hideSoftInputFromInputMethod(null, DEFAULT_KEYS_DISABLE);
	   	 		// imm.hideSoftInputFromWindow(mYourMsg.getWindowToken(), 0);
   	 		 
   	 	        String t_NameStr = Utilities.reduceSpacesInString(mNameEntry.getText().toString().trim());
	   	 		String t_PhoneStr  = Utilities.skipSpacesInString(mPhoneNumber.getText().toString().trim());
	   	 		String t_ConfirmPhoneStr = Utilities.skipSpacesInString(mConfirmNumber.getText().toString().trim());
	   	 	    if ((t_PhoneStr.length() == 0) && (t_ConfirmPhoneStr.length() == 0)){
	   			    Utilities.putToast("Missing both phone number and confirm number.",0, 400, getApplicationContext());
			        mPhoneNumber.requestFocus();
				    return;
			     }
	   		   else
	   		   if ((t_PhoneStr.length() == 0) && (t_ConfirmPhoneStr.length() != 0)) {
	   			    Utilities.putToast("Missing phone number.",0, 400, getApplicationContext());
		            mPhoneNumber.requestFocus();

				    return;
	   		     }
	   		   else 
	   		     if ((t_PhoneStr.length() != 0) && (t_ConfirmPhoneStr.length() == 0)){
	   		    	Utilities.putToast("Missing confirm number.",0, 400, getApplicationContext());
	   		    	mConfirmNumber.requestFocus();
				    return;
	   		     }
	   	     else
	   	 	 if (t_PhoneStr.contentEquals(t_ConfirmPhoneStr)){
				 if (t_PhoneStr.length() < 3){
					 mNameEntry.setText("");
					 mNameEntry.requestFocus();
					 mPhoneNumber.setText("");
					 mNameEntry.setText("");
					 mConfirmNumber.setText("");
					 return;
				 }
				 int checked_value = 0;
				 boolean flag_to_set = true;

				 if (t_PhoneStr.contains("*") || t_PhoneStr.contains("#") ){
					 flag_to_set = false;
				 }

				 Log.d("INPUT: ---------:  ", t_PhoneStr);
	   	 		 if ((checked_value = Utilities.checkDuplicatedNameNumber( t_PhoneStr, Global.mWhiteList, mContext, flag_to_set )) != 0){
						mNameEntry.setText("");
						mNameEntry.requestFocus();
						mPhoneNumber.setText("");
						mConfirmNumber.setText("");

						mBack.setVisibility(View.VISIBLE);
						mResetButton.setVisibility(View.GONE);
						m_phoneContactList.setVisibility(View.VISIBLE);
						Utilities.openSoftKeyboard(mContext);
					    Utilities.putToast(t_PhoneStr + " is already into Whitelist", 0, 400, getApplicationContext());
						return;
	   	 		 }
	   	 		 else {
	   	 			if (t_PhoneStr.contains(Constants.END_MARK)){
		   	 			Utilities.putToast("Phone number is invalid: " + Constants.END_MARK + " is not accepted.",0, 400, getApplicationContext());
						mPhoneNumber.setText("");
						mConfirmNumber.setText("");
						mPhoneNumber.requestFocus();
		   	 			return;
	   	 			}
					 Utilities.insertInOrder(Global.mWhiteList, Constants.NAMESTR +  t_NameStr + "\n" + Constants.NUMBER_TITLE + t_PhoneStr );
	   	 			 Utilities.putToast(t_PhoneStr + " is added into white list", 0, 400, getApplicationContext());
					 Utilities.BackUpAList(Global.mWhiteList, Constants.WHITE_LIST_FILE, mContext);
	   	 			 mNameEntry.setText("");
	   	 		     mPhoneNumber.setText("");
	   	 		     mConfirmNumber.setText("");
	   	 		     mNameEntry.requestFocus();
	   	 			 finish();
	   	 		 }
	   	 	}
   	 	 else {
   	 		   Utilities.putToast("Can not confirm the phone number.", 0, 400, getApplicationContext());
   	 	 }
        }
        });
        
        mBack.setOnClickListener(new Button.OnClickListener(){
     	 	 public void onClick(View v) {
     	 		 
      	 		mConfirmNumber.setText("");
      	 		mPhoneNumber.setText("");
      	 		mContactSubmitter.setVisibility(View.GONE);
      	 		mResetButton.setVisibility(View.GONE);
      	 		//mAddButton.setVisibility(View.GONE);
      	 		m_phoneContactList.setVisibility(View.VISIBLE);
      	 		
	   	 	    finish();
     	 	 }
          });  
	}

	 @Override
		protected void onResume() {
			super.onResume();

		    Utilities.openSoftKeyboard(mContext);
			try {
				    Global.mWhiteList.clear();
					ret_val = Utilities.getListFromFile(mContext, Global.mWhiteList, Constants.WHITE_LIST_FILE);
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
	        Utilities.BackUpAList(Global.mWhiteList, Constants.WHITE_LIST_FILE, mContext);
		    final View view = (View)((View) this.findViewById(android.R.id.content));
		    Utilities.closeSoftKeyboard(mContext, view);
	    }

	/*public String FormMonthDateYearStr(String monthStr, String dateStr, String yearStr){
			
			String formedString = "Block until : " + monthStr + "/" + dateStr + "/" + yearStr;
		    return formedString;
	}
	*/
	public void SetTrueFalse(boolean trueOrfalse){
			mConfirmNumber.setClickable(trueOrfalse);
			mPhoneNumber.setClickable(trueOrfalse);
	}

	void startSelectContactActivity() {

			    Intent PhoneListIntent = new Intent(this, SelectPhoneContact.class);
			    //PhoneListIntent.putExtra(Constants.PHONE_LIST, phoneContactList);
			    startActivityForResult(PhoneListIntent, Constants.PHONE_CONTACT); 
	}
		  
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				super.onActivityResult(requestCode, resultCode, data);
				
				String lContactNumber = " ";
				Log.d("REQUESTED CODE", String.valueOf(requestCode));
			    if ((requestCode == Constants.PHONE_CONTACT) && (resultCode != -1)){
				    if (data != null) {
						Log.d("REQUESTED CODE", String.valueOf(requestCode));
						//String phoneStr = data.getStringExtra(Constants.PHONE_NUMBER);
						String namephoneStr = data.getStringExtra(Constants.PHONE_NUMBER);
						if (resultCode == 2){
							if (namephoneStr.contentEquals(Constants.PORTING_ALL)) {
								Utilities.putToast("Contact list has been ported to WhiteList.", 0, 300, mContext);
								finish();
							}
						}
						else
						if (resultCode == 1){
							 finish();
						}
					}
				   // else if ((requestCode == Constants.PHONE_CONTACT) && (resultCode == -1)){{

				   // }
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
		