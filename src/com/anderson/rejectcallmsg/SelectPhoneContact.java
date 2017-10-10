package com.anderson.rejectcallmsg;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SelectPhoneContact extends Activity {
	private final int RESULT_OK_SELECT = 1;
	private final int RESULT_OK_PORT = 2;
	private final int RESULT_FAILS = -1;

	private ListView mContactList;
	private Button mBackButton;
	private Button mPortAllButton;
	protected ArrayAdapter<String> contactListAdapter;
	protected Context mContext;
	private final String TAG = "SelectPhoneContact";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_contact_list);

		mContext = this;
		final String [] phoneContactList = Utilities.getPhoneContactList(this);

    	mContactList = (ListView)findViewById(R.id.contactList);
    	contactListAdapter = new ArrayAdapter<String>(this,
		        	R.layout.simple_list_item_5, phoneContactList );
	
    	mContactList.setAdapter(contactListAdapter);
		if (phoneContactList == null){
			Intent returnIntent = new Intent();
			returnIntent.putExtra(Constants.PHONE_NUMBER, Constants.NONE);
			setResult(RESULT_FAILS, returnIntent);
			Utilities.putToast("Contact list is empty",0, 300, mContext);
			finish();
		}

        mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?>parent, View view, int position, long id){
				String t_PhoneStr = phoneContactList[position];
				String [] fields  = t_PhoneStr.split(",");
				String cleanedPhoneStr  = "";
				if (t_PhoneStr.contains("*") || t_PhoneStr.contains("#") ){
					cleanedPhoneStr = Utilities.skipSpacesInString(fields[1]);
				}
				else {
					cleanedPhoneStr = Utilities.skipSpacesInString(Utilities.cleanPhoneString(fields[1]));
				}

				if (Utilities.checkDuplicatedNameNumber(cleanedPhoneStr, Global.mWhiteList , mContext, false ) == 0) {
					String addedStr = Constants.NAMESTR + Utilities.reduceSpacesInString(fields[0]) + "\n"
															+ Constants.NUMBER_TITLE + cleanedPhoneStr;
					Utilities.insertInOrder(Global.mWhiteList, addedStr);
					Utilities.putToast(t_PhoneStr + " is added into white list", 0, 400, getApplicationContext());
					Utilities.BackUpAList(Global.mWhiteList, Constants.WHITE_LIST_FILE, mContext);
					Intent returnIntent = new Intent();
					returnIntent.putExtra(Constants.PHONE_NUMBER, phoneContactList[position]);
					setResult(RESULT_OK_SELECT, returnIntent);
					finish();
				}
				else {
				     Utilities.putToast(t_PhoneStr  + " is already in Whitelist", 0, 400, getApplicationContext());
				}
		   }	
	   });

		mBackButton = (Button)findViewById(R.id.backButton);
		mBackButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		//Select porting contact list to WhiteList
		mPortAllButton = (Button)findViewById(R.id.portButton);
		mPortAllButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				portContactListToWhiteList(phoneContactList);
			}
		});
	}
    // Port contact list to WhiteList
	private void portContactListToWhiteList(String [] contactList){

		for (int i = 0; i <contactList.length; i++ ){
			String StrContent = contactList[i];
			String [] fields = StrContent.split(",");
			String cleanedPhoneStr  = "";
			if (StrContent.contains("*") ||(StrContent .contains("#")) ){
				cleanedPhoneStr = Utilities.skipSpacesInString(fields[1]);
			}
			else {
				cleanedPhoneStr = Utilities.skipSpacesInString(Utilities.cleanPhoneString(fields[1]));
			}

				if (Utilities.checkForValidInternationalNumber(Utilities.skipSpacesInString(cleanedPhoneStr), mContext)) {
					if (Utilities.checkDuplicatedNameNumber(cleanedPhoneStr, Global.mWhiteList , mContext, false ) == 0){
						String FullString = Constants.NAMESTR + fields[0] + "\n" + Constants.NUMBER_TITLE + cleanedPhoneStr;
						Utilities.insertInOrder(Global.mWhiteList, FullString);
				}
			}
		}

		Utilities.BackUpAList(Global.mWhiteList, Constants.WHITE_LIST_FILE, mContext );
		Intent returnIntent = new Intent();
		returnIntent.putExtra(Constants.PHONE_NUMBER, Constants.PORTING_ALL);
		setResult(RESULT_OK_PORT, returnIntent);
		finish();
	}
	// Back up WhiteList to file
	public void BackUpWhiteList( ArrayList<String> tGmailList){
		String temp = "";
		int length = tGmailList.size();
		if (length == 0){
			deleteFile (Constants.WHITE_LIST_FILE);
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
			f_writer = openFileOutput(Constants.WHITE_LIST_FILE, Context.MODE_PRIVATE);
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
		}

	@Override
	 	protected void onStop() {
	        super.onStop();
	    }
	}
		