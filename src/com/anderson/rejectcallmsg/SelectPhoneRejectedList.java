package com.anderson.rejectcallmsg;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SelectPhoneRejectedList extends Activity {
	private final int RESULT_OK_SELECT = 2;
	private ListView mRejectList;
	protected ArrayAdapter<String> rejectListAdapter;
	Context mContext;
	private  final String TAG = "SelectPhoneContact";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_reject_list);


		mContext = this;
		Intent passedType = getIntent();
		final String type = passedType.getStringExtra(Constants.PHONE_REJECTED_LIST);
		final String [] phoneRejectList = Utilities.getPhoneRejectList(mContext);

		if (phoneRejectList == null) {
			Utilities.putToast("No missing calls", 0, 300, mContext);
			return;
		}

		mRejectList = (ListView) findViewById(R.id.rejectList);
		rejectListAdapter = new ArrayAdapter<String>(this,
				R.layout.simple_list_item_5, phoneRejectList);

		mRejectList.setAdapter(rejectListAdapter);
		mRejectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String t_PhoneStr = phoneRejectList[position];
				String[] fields = t_PhoneStr.split(",");
                if (fields[0].contains("null")){
					fields[0] = "Anonymous";
				}
				else
				if (fields[0].length() > 19) {
					String first_Eighteen = fields[0].substring(0, 16);
					fields[0] = first_Eighteen + "..";
				}

				String lContactNumber = Utilities.getDigitsOnly(fields[1]);
				lContactNumber = Utilities.getDigitsOnly(fields[1]);
				if (lContactNumber.length() < 10) {
					Utilities.putToast("Error: Missing digits: .", 0, 300, mContext);
					Utilities.openSoftKeyboard(mContext);
					return;
				} else if (lContactNumber.length() > 11) {
					Utilities.putToast("Error: Extra digits.", 0, 300, mContext);
					Utilities.openSoftKeyboard(mContext);
					return;
				} else {
					String mAC = null;
					if (lContactNumber.length() == 10) {
						mAC = lContactNumber.substring(0, 3);
						if (!readAndCheckAC(R.raw.areacode, mAC)) {
							Utilities.putToast("Error: Area Code is not existed.", 0, 300, mContext);
							Utilities.openSoftKeyboard(mContext);
							return;
						} else {
							lContactNumber = "1" + lContactNumber;
						}
					} else if (lContactNumber.length() == 11) {
						if (!lContactNumber.substring(0, 1).contentEquals("1")) {
							Utilities.putToast("Error 11 : Invalid USA phone number.", 0, 300, mContext);
							Utilities.openSoftKeyboard(mContext);
							return;
						} else {
							mAC = lContactNumber.substring(1, 4);
							if (!readAndCheckAC(R.raw.areacode, mAC)) {
								Utilities.putToast("Error: Area Code is not existed.", 0, 300, mContext);
								Utilities.openSoftKeyboard(mContext);
								return;
							}
						}
					}
				}
				if (!Utilities.checkForValidInternationalNumber(lContactNumber, mContext)) {
					return;
				}
				if (type.contentEquals(Constants.BLACKLIST_TYPE)) {
					if (Utilities.checkDuplicatedNameNumber(fields[1], Global.mRejectedList, mContext, true) == 0) {
						String FullString = Constants.NAMESTR + fields[0] + "\n" + Constants.NUMBER_TITLE + Utilities.uniformNumberFormat(lContactNumber) + "\n" +
								FormMonthDateYearStr("01", "01", "2019") + "\n" + Constants.SMS_NO_Indicator + "\n" + Constants.SMS_NO_OPTION;
						Utilities.insertInOrder(Global.mRejectedList, FullString);
						Utilities.BackUpAList(Global.mRejectedList, Constants.REJECTED_LIST_FILE, mContext);
						Utilities.putToast(t_PhoneStr + " is added into USA BlackList", 0, 400, getApplicationContext());
						Intent returnIntent = new Intent();
						returnIntent.putExtra(Constants.PHONE_NUMBER, "SUCCESS");
						setResult(RESULT_OK_SELECT, returnIntent);
						finish();
					} else {
						Utilities.putToast(t_PhoneStr + " is already in USA BlackList", 0, 400, getApplicationContext());
					}
				} else if (type.contentEquals(Constants.WORLD_TYPE)) {
					if (Utilities.checkDuplicatedNameNumber(fields[1], Global.mInternationalRejectedList, mContext, true) == 0) {
						String FullString = Constants.NAMESTR + fields[0] + "\n" + Constants.NUMBER_TITLE + Utilities.uniformNumberFormat(lContactNumber) + "\n" +
								FormMonthDateYearStr("01", "01", "2019") + "\n" + Constants.SMS_NO_Indicator + "\n" + Constants.SMS_NO_OPTION;
						Utilities.insertInOrder(Global.mInternationalRejectedList, FullString);
						Utilities.BackUpAList(Global.mInternationalRejectedList, Constants.INTERNATIONAL_REJECTED_LIST_FILE, mContext);
						Utilities.putToast(t_PhoneStr + " is added into World BlackList", 0, 400, getApplicationContext());
						Intent returnIntent = new Intent();
						returnIntent.putExtra(Constants.PHONE_NUMBER, "SUCCESS");
						setResult(RESULT_OK_SELECT, returnIntent);
						finish();
					} else {
						Utilities.putToast(t_PhoneStr + " is already in USA BlackList", 0, 400, getApplicationContext());
					}
				}
			}
		});

	}
		@Override
		protected void onResume(){
		  super.onResume();
		}
	 
	 protected void onStop()
	{
	        super.onStop();
	}

	public String FormMonthDateYearStr(String monthStr, String dateStr, String yearStr){

		String formedString = "Block until : " + monthStr + "/" + dateStr + "/" + yearStr;

		return formedString;
	}
	protected boolean readAndCheckAC(int rawFileId, String mAreaCodeStr) {
		int mAreaCodeInt = Integer.parseInt(mAreaCodeStr);
		InputStream inputStream = mContext.getResources().openRawResource(rawFileId);

		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);

		String line = "";

		try {
			while ((line = buffreader.readLine()) != null) {
				String temp_AreaCodeStr = line.substring(0, 3);

				if (temp_AreaCodeStr.contains(mAreaCodeStr)) {
					return true;
				} else if (Integer.parseInt(temp_AreaCodeStr) > mAreaCodeInt) {
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