package com.anderson.rejectcallmsg;

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

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SelectPhoneContact_BlackList extends Activity {
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

                if (fields[0].length() > 19 ){
					String first_Eighteen = fields[0].substring(0, 16);
					fields[0] = first_Eighteen + "..";
				}

			    String lContactNumber = Utilities.getDigitsOnly(fields[1]);
				lContactNumber = Utilities.getDigitsOnly(fields[1]);
				if (lContactNumber.length() < 10) {
					Utilities.putToast("Error: Missing digits: .", 0, 300, mContext);
					Utilities.openSoftKeyboard(mContext);
					return;
				}
				else
				if (lContactNumber.length() > 11){
					Utilities.putToast("Error: Extra digits.", 0, 300, mContext);
					Utilities.openSoftKeyboard(mContext);
					return;
				}
				else {
					String mAC = null;
					if (lContactNumber.length()== 10){
						mAC = lContactNumber.substring(0,3);
						if (!readAndCheckAC(R.raw.areacode, mAC)){
							Utilities.putToast("Error: Area Code is not existed.", 0, 300, mContext);
							Utilities.openSoftKeyboard(mContext);
							return;
						}
						else {
							lContactNumber = "1" + lContactNumber;
						}
					}
					else
					if (lContactNumber.length() == 11) {
						if (!lContactNumber.substring(0, 1).contentEquals("1")) {
							Utilities.putToast("Error 11 : Invalid USA phone number.", 0, 300, mContext);
							Utilities.openSoftKeyboard(mContext);
							return;
						}
						else{
							mAC = lContactNumber.substring(1, 4);
							if (!readAndCheckAC(R.raw.areacode, mAC)) {
								Utilities.putToast("Error: Area Code is not existed.", 0, 300, mContext);
								Utilities.openSoftKeyboard(mContext);
								return;
							}
						}   }
				}
				if (!Utilities.checkForValidInternationalNumber(lContactNumber, mContext)) {
					return;
				}

				if (Utilities.checkDuplicatedNameNumber(fields[1], Global.mRejectedList , mContext, true) == 0) {
					String FullString = Constants.NAMESTR + fields[0] + "\n" + Constants.NUMBER_TITLE + Utilities.uniformNumberFormat(lContactNumber) + "\n" +
							FormMonthDateYearStr("01", "01", "2019") + "\n" + Constants.SMS_NO_Indicator + "\n" + Constants.SMS_NO_OPTION;
					Utilities.insertInOrder(Global.mRejectedList, FullString);
					Utilities.BackUpAList(Global.mRejectedList, Constants.REJECTED_LIST_FILE, mContext);
					Utilities.putToast(t_PhoneStr + " is added into USA BlackList", 0, 400, getApplicationContext());
					Intent returnIntent = new Intent();
					returnIntent.putExtra(Constants.PHONE_NUMBER, "SUCCESS");
					setResult(RESULT_OK_SELECT, returnIntent);
					finish();
				}
				else {
					Utilities.putToast(t_PhoneStr  + " is already in USA BlackList", 0, 400, getApplicationContext());
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
				portContactListToBlackList(phoneContactList);
			}
		});
	}
    // Port contact list to WhiteList
	private void portContactListToBlackList(String [] contactList){
		for (int i = 0; i <contactList.length; i++ ){
			String StrContent = contactList[i];
			String [] fields = StrContent.split(",");
			String cleanedPhoneStr = Utilities.getDigitsOnly(fields[1]);
			if (Utilities.checkDuplicatedNameNumber(fields[1],Global.mRejectedList , mContext, true ) == 0){
					String FullString = Constants.NAMESTR + fields[0] + "\n" + Constants.NUMBER_TITLE + Utilities.uniformNumberFormat(cleanedPhoneStr) + "\n" +
							FormMonthDateYearStr("01", "01", "2019") + "\n" + Constants.SMS_NO_Indicator + "\n" + Constants.SMS_NO_OPTION;
				    Utilities.insertInOrder(Global.mRejectedList, FullString);
			}
		}

		BackUpBlackList(Global.mRejectedList );
		Intent returnIntent = new Intent();
		returnIntent.putExtra(Constants.PHONE_NUMBER, Constants.PORTING_ALL);
		setResult(RESULT_OK_PORT, returnIntent);
		finish();
	}
	// Back up WhiteList to file
	public void BackUpBlackList( ArrayList<String> tGmailList){
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
		}

	@Override
	 	protected void onStop() {
	        super.onStop();
	    }

	public String FormMonthDateYearStr(String monthStr, String dateStr, String yearStr){

		String formedString = "Block until : " + monthStr + "/" + dateStr + "/" + yearStr;

		return formedString;

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
		