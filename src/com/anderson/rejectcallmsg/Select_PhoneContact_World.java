package com.anderson.rejectcallmsg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

public class Select_PhoneContact_World extends Activity {
	private final int RESULT_OK_SELECT = 1;
	private final int RESULT_OK_PORT = 2;
	private final int RESULT_FAILS = -1;

	private ListView mContactList;
	private Button mBackButton;
	private Button mPortAllButton;
	protected ArrayAdapter<String> contactListAdapter;
	protected Context mContext;
	private final String TAG = "SelectPhoneContactWorld";
	
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
				if ((lContactNumber.length() < 8) || (lContactNumber.length() > 16)){
					Utilities.putToast("Error: Not a correct phone number.", 0, 300, mContext);
					Utilities.openSoftKeyboard(mContext);
					return;
				}

				if (!Utilities.checkForValidInternationalNumber(lContactNumber, mContext)) {
					return;
				}

				if (Utilities.checkDuplicatedNameNumber(fields[1], Global.mInternationalRejectedList, mContext, false ) == 0) {
					String FullString = Constants.NAMESTR + fields[0] + "\n" + Constants.NUMBER_TITLE + lContactNumber + "\n" +
							FormMonthDateYearStr("01", "01", "2019") + "\n" + Constants.SMS_NO_Indicator + "\n" + Constants.SMS_NO_OPTION;
					Utilities.insertInOrder(Global.mInternationalRejectedList, FullString);
					Utilities.BackUpAList(Global.mInternationalRejectedList, Constants.INTERNATIONAL_REJECTED_LIST_FILE, mContext);
					Utilities.putToast(t_PhoneStr + " is added into WhiteList", 0, 400, getApplicationContext());
					Intent returnIntent = new Intent();
					returnIntent.putExtra(Constants.PHONE_NUMBER, "SUCCESS");
					setResult(RESULT_OK_SELECT, returnIntent);
					finish();
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
			if (Utilities.checkDuplicatedNameNumber(fields[1], Global.mInternationalRejectedList , mContext, false ) == 0){
					String FullString = Constants.NAMESTR + fields[0] + "\n" + Constants.NUMBER_TITLE + Utilities.uniformNumberFormat(cleanedPhoneStr) + "\n" +
							FormMonthDateYearStr("01", "01", "2019") + "\n" + Constants.SMS_NO_Indicator + "\n" + Constants.SMS_NO_OPTION;
					Utilities.insertInOrder(Global.mInternationalRejectedList, FullString);
			}
		}

		Utilities.BackUpAList(Global.mInternationalRejectedList, Constants.INTERNATIONAL_REJECTED_LIST_FILE, mContext);
		Intent returnIntent = new Intent();
		returnIntent.putExtra(Constants.PHONE_NUMBER, Constants.PORTING_ALL);
		setResult(RESULT_OK_PORT, returnIntent);
		finish();
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


}
		