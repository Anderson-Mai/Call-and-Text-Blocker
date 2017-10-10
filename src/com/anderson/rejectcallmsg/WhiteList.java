package com.anderson.rejectcallmsg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WhiteList extends Activity implements CustomList_One.customButtonListener {
	private int ret_val = 0;
	private final int RESULT_OK = 2;
	private final int CODE_ONE = 1;
	private ListView listView;
	private LinearLayout mListView;
	private Button mHome;
	private Button mButton_DeteleAll;
	private Button mButton_add;
	private LinearLayout eEditViewer = null;
	private TextView mModifyHeader = null;
	private RelativeLayout mTopHeaderLayout = null;
	protected CustomList_One m_adapter;
	Context mContext;
	//protected LinearLayout mOptionsBar = null;
	boolean reset_flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whitelist);

		mContext = this;
		//mOptionsBar = (LinearLayout) findViewById(R.id.OptionsBar);
		mHome = (Button) findViewById(R.id.homeButton);

		mHome.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// BackUpUSA_NumberRecords(Constants.mRejectedList);
				finish();
			}
		});

		mTopHeaderLayout = (RelativeLayout) findViewById(R.id.topHeaderLayout);
		mButton_add = (Button) findViewById(R.id.button_add);

		mButton_add.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent button_next_intent = new Intent(getApplicationContext(), InsertWhiteListNumber.class);
				startActivity(button_next_intent);
			}
		});

		mButton_DeteleAll = (Button) findViewById(R.id.empty_list_button);
		mButton_DeteleAll.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Global.mWhiteList.clear();
				m_adapter.notifyDataSetChanged();
				Utilities.BackUpAList(Global.mWhiteList, Constants.WHITE_LIST_FILE, mContext);
				mButton_DeteleAll.setVisibility(View.GONE);
			}
		});

		listView = (ListView) findViewById(R.id.gmailContactInbox);
	}

	// Write the messages in Global.alertedmsgInfo to file 
	public void BackUpWhiteList(ArrayList<String> tGmailList) {
		String temp = "";
		int length = tGmailList.size();
		if (length == 0) {
			deleteFile(Constants.WHITE_LIST_FILE);
			return;
		}

		for (int i = 0; i < length; i++) {
			String temp_str = tGmailList.get(i);
			String Ticker_str = temp_str + Constants.END_MARK;
			temp += Ticker_str;
			Ticker_str = null;
		}

		FileOutputStream f_writer = null;
		try {
			f_writer = openFileOutput(Constants.WHITE_LIST_FILE, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		if (f_writer != null) {
			try {
				f_writer.write(temp.getBytes());
			} catch (IOException e) {
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
		try {
			Global.mWhiteList.clear();
			ret_val = Utilities.getListFromFile(mContext, Global.mWhiteList, Constants.WHITE_LIST_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m_adapter = new CustomList_One(this, Global.mWhiteList);
		m_adapter.setCustomButtonListner(this);

		if (!Global.mWhiteList.isEmpty()) {
			mButton_DeteleAll.setVisibility(View.VISIBLE);
		} else {
			mButton_DeteleAll.setVisibility(View.GONE);
		}

		listView.setAdapter(m_adapter);
	}


	protected void onStop() {
		super.onStop();
		BackUpWhiteList(Global.mWhiteList);
	}

		/*@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (data != null) {
				if (requestCode == CODE_ONE) {
			  	  if (resultCode == RESULT_OK) {
			  		m_adapter.notifyDataSetChanged();
			  		mModifyHeader.setVisibility(View.GONE);
			        mTopHeaderLayout.setVisibility(View.VISIBLE);
			        eEditViewer.setVisibility(View.GONE);
			        mListView.setVisibility(View.VISIBLE);
					}
				 }
			}		
		}
        */

	@Override
	public void onButtonClickListner(int position, String value) {
		String strContent = Global.mWhiteList.get(position);
		ArrayList<String> mList = Utilities.getPhoneAndNameStrings(strContent);

		final String phoneNumber = Utilities.cleanPhoneString(mList.get(1));
		String MCC_Str = getMCC(mContext);
		Log.i("MCC STR    = ", MCC_Str);

		String SIM_country_code = getUserCountry(mContext);
		Log.i("SIM_CODE   = ", SIM_country_code);

		 switch(SIM_country_code){
			 case "us": 	// This call is made in USA
				 if ((phoneNumber.length() == 11) && (Utilities.readAndCheck(R.raw.areacode,
						 phoneNumber.substring(1, 4), mContext) != null)) {
					 makeACall(phoneNumber);
				 } else if ((phoneNumber.length() == 10) && (Utilities.readAndCheck(R.raw.areacode,
						 phoneNumber.substring(0, 3), mContext) != null)) {
					 // Local call
					 makeACall(phoneNumber);
				 } else if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
					 // International call
					 final String usa_code = "011";
					 if (phoneNumber.substring(0, 3).contentEquals(usa_code)) {
						 makeACall(phoneNumber);
					 } else {
						 makeACall(usa_code + phoneNumber);
					 }
				 } else {
					 Utilities.putToast("Error: Invalid phone number", 0, 300, mContext);
				 }
				 break;
			 case "vn":
				// break;
			 case "fr" :
				// break;
			 case "at":
				// break;
			 case "gb":
				// break;
			 case "cn":
				// break;
			 case "hk":
				// break;
			 case "mo":
				// break;
			 case "kh":
				// break;
			 case "la":
				// break;
			 case "kp":
				// break;
			 case "tw":
				// break;
			 case "au":
				// break;
			 case "id":
				// break;
			 case "mx":
				// break;
			 case "in":
				// break;
			 case "ph":
				// break;
			 case "th":
				// break;
			 case "sg":
				// break;
			 case "gu":
				// break;
			 case "jp":
				// break;
			 case "br":
				 Utilities.putToast("Will support ...",0, 300, mContext);
				 return;
			 default:
				 Utilities.putToast("This version supports for calling from USA only.",0, 300, mContext);
				 return;

		 }

	}

	@Override
	public void onButtonOneClickListner(int position, String value) {
		String strContent = Global.mWhiteList.get(position);
		ArrayList<String> mList = Utilities.getPhoneAndNameStrings(strContent);
		final String phoneNumber = Utilities.cleanPhoneString(mList.get(1));
		Utilities.putToast("-- BUTTON VIEW - MAIN  ONE",0, 300, mContext);
		Intent viewIntent = new Intent(mContext, SmsSingleAccount.class );
		viewIntent.putExtra("ADDRESS", phoneNumber);
		viewIntent.putExtra("NAME", mList.get(0));
		startActivity(viewIntent);

	}

	@Override
	public void onButtonTwoClickListner(int position, String value) {




		String strContent = Global.mWhiteList.get(position);
		ArrayList<String> mList = Utilities.getPhoneAndNameStrings(strContent);
		final String phoneNumber = Utilities.cleanPhoneString(mList.get(1));
		Utilities.putToast("-- BUTTON VIEW - MAIN  TWO",0, 300, mContext);
		Intent viewIntent = new Intent(mContext, SmsSingleAccount.class );
		viewIntent.putExtra("ADDRESS", phoneNumber);
		viewIntent.putExtra("NAME", mList.get(0));
		startActivity(viewIntent);
	}

	@Override
	public void onButtonThreeClickListner(int position, String value) {
		String strContent = Global.mWhiteList.get(position);
		ArrayList<String> mList = Utilities.getPhoneAndNameStrings(strContent);
		final String phoneNumber = Utilities.cleanPhoneString(mList.get(1));
		Utilities.putToast("-- BUTTON VIEW - MAIN  THREE",0, 300, mContext);
		Intent viewIntent = new Intent(mContext, SmsSingleAccount.class );
		viewIntent.putExtra("ADDRESS", phoneNumber);
		viewIntent.putExtra("NAME", mList.get(0));
		startActivity(viewIntent);
	}

	@Override
	public void onButtonFourClickListner(int position, String value) {


	}


	private void makeACall(String phoneNumber) {
		String uri = "tel:" + phoneNumber;
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(uri));
		startActivity(intent);
	}


	/**
	 * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
	 *
	 * @param context Context reference to get the TelephonyManager instance from
	 * @return country code or null
	 */
	private String getUserCountry(Context context) {
		try {
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			final String simCountry = tm.getSimCountryIso();
			if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
				return simCountry.toLowerCase(Locale.US);
			}
			else
			if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_GSM) { // device is not 3G (would be unreliable)
				String networkCountry = tm.getNetworkCountryIso();
				if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
					return networkCountry.toLowerCase(Locale.US);
				}
			}

		} catch (Exception e) {

		}
		return null;
	}


	private String getMCC(Context context) {

		 TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		 String networkOperator = tm.getNetworkOperator();
		 int mcc = 0, mnc = 0;

		/* if (networkOperator != null) {
			mcc = Integer.parseInt(networkOperator.substring(0, 3));
			mnc = Integer.parseInt(networkOperator.substring(3));
			return String.valueOf(mcc);
		 }

		 else */

		{
			 final String simCountry = tm.getSimCountryIso();
			 if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
				 return simCountry.toLowerCase(Locale.US);
			 }
			 else
			 if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_GSM) { // device is not 3G (would be unreliable)
				 String networkCountry = tm.getNetworkCountryIso();
				 if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
					 return networkCountry.toLowerCase(Locale.US);
				 }
			 }

		 }
		 return null;

    }
	 private String getCountryExitCode( String MCC_Str   ){

		 return  null;

	 }


}
		