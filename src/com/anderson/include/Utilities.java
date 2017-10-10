package com.anderson.include;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.anderson.rejectcallmsg.Global;
import com.anderson.rejectcallmsg.OneNode;
import com.anderson.rejectcallmsg.Sms;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map.Entry;


public class Utilities {

	public static void writeBlockedNumber(String blocked_Time, String mFilePath, Context mContext) {
		String tempStr = blocked_Time + Constants.END_MARK;
		FileOutputStream f_writer = null;
		try {
			f_writer = mContext.openFileOutput(mFilePath, Context.MODE_APPEND | Context.MODE_PRIVATE);

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (f_writer != null) {
			try {
				f_writer.write(tempStr.getBytes());
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

	public static boolean checkForSpecialCharacter(String msgStr, Context my_context) {
		if (msgStr.contains("#") || msgStr.contains("@") || msgStr.contains("$") || msgStr.contains("&")) {
			Utilities.putToast("Message will not accept special characters # @ $ &", 0, 300, my_context);
			return true;
		}
		return false;
	}

	public static String getDigitLetterOnly(String phoneNumber) {

		if ((phoneNumber == null) || phoneNumber.isEmpty()) {
			return null;
		}

		String phoneNumberStr = "";
		for (int i = 0; i < phoneNumber.length(); i++) {
			char c = phoneNumber.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				String intStr = Character.toString(c);
				phoneNumberStr += intStr;
			}
		}
		return phoneNumberStr;
	}

	public static String uniformNumberFormat(String t_PhoneStr) {
		String uniformStr = "";
		if (t_PhoneStr.length() == 10) {
			uniformStr = "1 " + t_PhoneStr.substring(0, 3) + "-" + t_PhoneStr.substring(3);
		} else if (t_PhoneStr.length() == 11) {
			uniformStr = t_PhoneStr.substring(0, 1) + " " + t_PhoneStr.substring(1, 4) + "-" + t_PhoneStr.substring(4);
		}
		return uniformStr;
	}


	public static String getDigitsOnly(String phoneNumber) {

		if ((phoneNumber == null) || phoneNumber.isEmpty()) {
			return null;
		}

		String phoneNumberStr = "";
		for (int i = 0; i < phoneNumber.length(); i++) {
			char c = phoneNumber.charAt(i);
			if (Character.isDigit(c)) {
				String intStr = Character.toString(c);
				phoneNumberStr += intStr;
			}
		}
		return phoneNumberStr;
	}

	public static String getPhoneNumberOnly(String phoneNumber) {

		if ((phoneNumber == null) || phoneNumber.isEmpty()) {
			return null;
		}

		String phoneNumberStr = "";
		for (int i = 0; i < phoneNumber.length(); i++) {
			char c = phoneNumber.charAt(i);
			if (Character.isDigit(c)) {
				String intStr = Character.toString(c);
				phoneNumberStr += intStr;
			}
		}
		return phoneNumberStr;
	}

	public static int getListFromFile(Context m_Context,
									  ArrayList<String> tGmailList, String filePath) throws IOException {

		String str = null;
		String tempStr = null;

		File file = m_Context.getFileStreamPath(filePath);

		if (file.exists()) {
			FileInputStream fs = m_Context.openFileInput(filePath);

			if (fs == null) {
				return -1;
			} else if (fs.available() == 0) {
				return -2;

			} else if ((fs != null) && (fs.available() != 0)) {
				byte[] buffer = new byte[fs.available()];
				fs.read(buffer, 0, fs.available());
				str = new String(buffer);
				// Log.d("Get data frpm backup Old message", "str = " + str);

				fs.close();
				int start = 0, end = 0;
				while ((end = str.indexOf(Constants.END_MARK, start)) != -1) {
					tempStr = str.substring(start, end);
					//t_nodeList.add(Utilities.GetNode(tempStr, ""));
					tGmailList.add(tempStr);
					start = end + 1;
					str = str.substring(start);
					start = 0;
				}
				return 1;
			}

		}
		return -3;
	}


	public static boolean needAdjustmentForKidKat(ArrayList<String> NumberRejectList, ArrayList<String> ACRejectList) {
		for (int i = 0; i < NumberRejectList.size(); i++) {
			String[] strNode = NumberRejectList.get(i).split("\n");
			if (strNode[3].contains(Constants.SMS_YES_OPTION)) {
				return true;
			}

			strNode = null;
			strNode = ACRejectList.get(i).split("\n");
			if (strNode[3].contains(Constants.SMS_YES_OPTION)) {
				return true;
			}
		}

		return false;
	}

	public static int get_MessageList(Context m_Context, String filePath) throws IOException {

		String str = null;
		String tempStr = null;


		File file = m_Context.getFileStreamPath(filePath);

		if (file.exists()) {
			FileInputStream fs = m_Context.openFileInput(filePath);

			if (fs == null) {
				return -1;
			} else if (fs.available() == 0) {
				return -2;
			} else if ((fs != null) && (fs.available() != 0)) {
				byte[] buffer = new byte[fs.available()];
				fs.read(buffer, 0, fs.available());
				str = new String(buffer);
				fs.close();
				int start = 0, end = 0;
				while ((end = str.indexOf(Constants.END_MARK, start)) != -1) {
					tempStr = str.substring(start, end);
					int keyPos = tempStr.indexOf("@");
					String key = tempStr.substring(0, keyPos);
					String message = tempStr.substring(keyPos + 1);
					Global.mMap.put(key, message);
					start = end + 1;
					str = str.substring(start);
					start = 0;
				}
				return 1;
			}

		}
		return -3;
	}

	public static int get_ExceptedNumberList(Context m_Context, String filePath) throws IOException {

		String str = null;
		String tempStr = null;
		File file = m_Context.getFileStreamPath(filePath);
		if (file.exists()) {
			FileInputStream fs = m_Context.openFileInput(filePath);
			if (fs == null) {
				return -1;
			} else if (fs.available() == 0) {
				return -2;

			} else if ((fs != null) && (fs.available() != 0)) {

				byte[] buffer = new byte[fs.available()];

				fs.read(buffer, 0, fs.available());
				str = new String(buffer);
				//Log.d("Get data frpm backup Old message", "str = " + str);

				fs.close();
				int start = 0, end = 0;
				while ((end = str.indexOf(Constants.END_MARK, start)) != -1) {

					tempStr = str.substring(start, end);
					int keyPos = tempStr.indexOf("@");
					String key = tempStr.substring(0, keyPos);
					String exceptedNumber = tempStr.substring(keyPos + 1);
					Global.mMap_ExceptedNumber.put(key, exceptedNumber);
					start = end + 1;
					str = str.substring(start);
					start = 0;
				}
				return 1;
			}

		}
		return -3;
	}


	// Write the messages in Global.alertedmsgInfo to file
	public static void BackUpMsgMap(Context tContext) {
		String temp = "";
		int length = Global.mMap.size();
		if (length == 0) {
			tContext.deleteFile(Constants.MESSAGE_MAP_FILE);
			return;
		}
		for (Entry<String, String> entry : Global.mMap.entrySet()) {
			String temp_str = entry.getKey() + "@" + entry.getValue();
			String Ticker_str = temp_str + Constants.END_MARK;
			temp += Ticker_str;
			Ticker_str = null;
		}

		Log.d("BackupStocks", "Stocks are = " + temp);

		FileOutputStream f_writer = null;
		try {
			f_writer = tContext.openFileOutput(Constants.MESSAGE_MAP_FILE, Context.MODE_PRIVATE);

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (f_writer != null) {
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

	public static void putToast(String message, int x, int y, Context my_context) {
		Toast toast = new Toast(my_context);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, x, y);
		toast = Toast.makeText(my_context, message, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static boolean checkDuplicatedNumber(String newNumber, ArrayList<String> StrList, String oldNumber) {
		if (newNumber.contentEquals(oldNumber)) {
			return false;
		}
		for (int i = 0; i < StrList.size(); i++) {
			String StrContent = StrList.get(i);
			Log.i("String Content ", StrContent);
			String[] fields = StrContent.split("\n");
			String phoneNumber = fields[1].substring(Constants.NUMBER_TITLE.length());
			Log.i("---Phone Number", phoneNumber);
			Log.i("---NewNumber", newNumber);
			if (newNumber.contentEquals(phoneNumber)) {
				Log.i("Duplicated :   ", newNumber);
				return true;
			}
		}
		return false;
	}

	public static ArrayList<String> getPhoneAndNameStrings(String StrContent) {
		String[] fields = StrContent.split("\n");
		String phoneName = fields[0].substring(Constants.NAMESTR.length());
		String phoneNumber = fields[1].substring(Constants.NUMBER_TITLE.length());
		ArrayList<String> mList = new ArrayList<String>();
		mList.add(phoneName);
		mList.add(phoneNumber);
		Log.i("Phone Name", phoneName);
		Log.i("Phone Number", phoneNumber);
		return mList;
	}

	public static int checkDuplicatedNameNumber(String newNumber, ArrayList<String> StrList, Context my_context, boolean digit_only) {

		int checked_value = 0;
		String nameStr = "";
		String newDigitStr = "";
		String fieldDigitStr = "";

		if (digit_only && (newNumber.length() > 7)) {
			newDigitStr = Utilities.getDigitLetterOnly(newNumber);
		} else {
			newDigitStr = Utilities.skipSpacesInString(newNumber);
		}
		Log.i("NEW NUMBER:   ", newDigitStr);

		for (int i = 0; i < StrList.size(); i++) {
			String StrContent = StrList.get(i);
			String[] fields = StrContent.split("\n");
			if (digit_only && (newNumber.length() > 7)) {
				fieldDigitStr = Utilities.getDigitLetterOnly(fields[1].substring(Constants.NUMBER_TITLE.length()));
			} else {
				fieldDigitStr = Utilities.skipSpacesInString(fields[1].substring(Constants.NUMBER_TITLE.length()));

			}

			Log.i("FIELD_DIGIT:   ", fieldDigitStr);

			int fieldLength = fieldDigitStr.length();
			int newLength = newDigitStr.length();
			if (fieldLength == newLength) {
				if (fieldDigitStr.equalsIgnoreCase(newDigitStr)) {
					checked_value = 1;

					Log.i("CHECK VALUE :   1111:   ", "111111111111111");
				}
			} else if ((fieldLength == (newLength + 1)) && (fieldDigitStr.charAt(0) == '1')) {
				if (fieldDigitStr.substring(1).equalsIgnoreCase(newDigitStr)) {
					Log.i("newDigitStr: ", newDigitStr);
					Log.i("fieldDigitStr: ", fieldDigitStr.substring(1));
					checked_value = 1;
					Log.i("CHECK VALUE :   22222:   ", "222222");
				}
			} else if ((fieldLength == (newLength - 1)) && ((newDigitStr.charAt(0) == '1'))) {
				if (fieldDigitStr.equalsIgnoreCase(newDigitStr.substring(1))) {
					checked_value = 1;
					Log.i("CHECK VALUE :   3333:   ", "3333");
				}
			} else {
				checked_value = 0;
				Log.i("CHECK VALUE :   4444:   ", "4444");

			}
		}
		return checked_value;
	}


	public static boolean checkDuplicatedAreaCode(String newNumber, ArrayList<String> StrList, Context my_context) {

		for (int i = 0; i < StrList.size(); i++) {
			String StrContent = StrList.get(i);
			String[] fields = StrContent.split("\n");

			if ((fields[1] != null) && (fields[1].substring(Constants.AREA_CODE_TITLE.length()).contentEquals(newNumber))) {
				Utilities.putToast("This Area Code is already in the list.", 0, 300, my_context);
				return true;
			}
		}
		return false;
	}

	public static OneNode GetNode(String fullStr, String oldPhoneNumber) {
		OneNode parseNode = new OneNode();
		String[] strNode = fullStr.split("\n");
		parseNode.nameStr = strNode[0];
		parseNode.numberStr = strNode[1];
		Log.d("NUM STR :   ", parseNode.numberStr);
		Log.d("STRING LENGTH :   ", String.valueOf(strNode.length));
		String[] expiryParts = strNode[2].split(":");
		String[] expireStrs = expiryParts[1].split("/");
		parseNode.month = expireStrs[0];
		parseNode.day = expireStrs[1];
		parseNode.year = expireStrs[2];
		parseNode.smsResponse = strNode[3];
		Log.d(" ----- I  AM HERE ----: ", "   ");
		parseNode.smsOption = strNode[4];
		if (strNode.length > 5) {
			parseNode.exceptedNumbOption = strNode[5];
		}
		Log.d(" ----- smsRESPONSE ----: ", parseNode.smsResponse);

		if (parseNode.smsResponse.contains(Constants.SMS_YES_Indicator)) {
			Log.d(" ---- Old Phone Number----:  ", oldPhoneNumber);
			if (!oldPhoneNumber.isEmpty()) {
				parseNode.yourMessage = Global.mMap.get(oldPhoneNumber);
			} else {
				parseNode.yourMessage = Global.mMap.get(parseNode.numberStr);

			}
		} else {
			parseNode.yourMessage = "";
			Log.d(" ---- Your message----: ", "Empty");
		}
		return parseNode;
	}

	public static boolean isLeapYear(String yearStr) {
		int mYear = Integer.parseInt(yearStr);

		if (mYear % 4 == 0) {
			if (mYear % 100 == 0) {
				if (mYear % 400 == 0) {
					return true;
				} else {
					return false;
				}
			}

		}
		return false;
	}

	public static boolean CheckValidDate(String mMonth, String mDate, String mYear, Context mContext) {
		if (mMonth.isEmpty() || mDate.isEmpty() || mYear.isEmpty()) {
			return false;
		}
		boolean day_bool = true;
		boolean month_bool = true;
		boolean year_bool = true;
		String day_fault_str = "";
		String month_fault_str = "";
		String year_fault_str = "";
			  
			 /* int day = Integer.parseInt(mDate);
			   if (( day > 31) ||( day < 1)){
				   day_fault_str = "Date must be in range of 1 - 31\n";
				  // toastingMessage("Date must be in range of 1 - 31. Try again.");
	    		  // return false; 
				   day_bool = false;
			   }   
			   */

		int month = Integer.parseInt(mMonth);
		if ((month > 12) || (month < 1)) {
			// toastingMessage("Month must be in range of 1 - 12. Try again.");
			// return false;
			month_bool = false;
			month_fault_str = "Month must be in range of 1 - 12\n";
		}

		int day = Integer.parseInt(mDate);
		int days_in_month = 0;

		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				if ((day > 31) || (day < 1)) {
					day_fault_str = "Date must be in range of 1 - 31\n";
					// toastingMessage("Date must be in range of 1 - 31. Try again.");
					// return false;
					day_bool = false;
				}
				break;
			case 2:
				if (Utilities.isLeapYear(mYear)) {
					days_in_month = 29;
				} else {
					days_in_month = 28;
				}

				if ((day > days_in_month) || (day < 1)) {
					day_fault_str = "Date must be in range of 1 - " + String.valueOf(days_in_month) + "\n";
					// toastingMessage("Date must be in range of 1 - 31. Try again.");
					// return false;
					day_bool = false;
				}
				break;

			case 4:
			case 6:
			case 9:
			case 11:
				if ((day > 30) || (day < 1)) {
					day_fault_str = "Date must be in range of 1 - 30\n";
					// toastingMessage("Date must be in range of 1 - 31. Try again.");
					// return false;
					day_bool = false;
				}
				break;


		}
		// int day = Integer.parseInt(mDate);
		//  if (( day > 31) ||( day < 1)){
		//	   day_fault_str = "Date must be in range of 1 - 31\n";
		// toastingMessage("Date must be in range of 1 - 31. Try again.");
		// return false;
		//	   day_bool = false;
		//  }

		int year = Integer.parseInt(mYear);

		Log.d("CURR YEAR 1 : ", String.valueOf(year));
		Log.d("CURR MONTH 1: ", String.valueOf(month));
		Log.d("CURR DAY : 1 ", String.valueOf(day));

		String DATE_FORMAT = "yyyyMMdd";
		SimpleDateFormat sdf =
				new SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); // today
		String dateStr = sdf.format(c1.getTime());

		int currentYear = Integer.parseInt(dateStr.substring(0, 4));
		int currentMonth = Integer.parseInt(dateStr.substring(4, 6));
		int currentDay = Integer.parseInt(dateStr.substring(6, 8));

		Log.d("CURR YEAR : ", String.valueOf(currentYear));
		Log.d("CURR MONTH : ", String.valueOf(currentMonth));
		Log.d("CURR DAY : ", String.valueOf(currentDay));

		if (year > currentYear) {
			year_bool = true;
		} else if (year == currentYear) {
			if (month > currentMonth) {
				month_bool = true;
			} else if (month == currentMonth) {
				if (day >= currentDay) {
					day_bool = true;
				} else {
					day_bool = false;
					day_fault_str = "Date must be " + currentDay + " or later.\n";
				}
			} else {
				month_bool = false;
				month_fault_str = "Month must be " + currentMonth + " or later \n";
			}
		} else {
			year_bool = false;
			year_fault_str = "Year must be " + currentYear + " or later.\n";
		}
		if (year_bool && month_bool && day_bool) {
			return true;
		} else {
			String error_msg = day_fault_str + month_fault_str + year_fault_str;
			Utilities.putToast(error_msg, 0, 100, mContext);
			return false;
		}
	}


	public static boolean checkFromWhiteList(String phoneNumber, ArrayList<String> tWhiteList) {
		return checkDuplicatedNumber(phoneNumber, tWhiteList, "XXXXXX");

	}

	public static boolean checkForValidInternationalNumber(String phoneNumberStr, Context my_context) {
		if ((phoneNumberStr.length() < 3) || (!PhoneNumberUtils.isGlobalPhoneNumber(phoneNumberStr))) {
			Utilities.putToast(phoneNumberStr + " : not a valid phone number.", 0, 300, my_context);
			return false;
		}
		return true;
	}

	public static void closeSoftKeyboard(Context m_context, View view) {
		//View view = getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) m_context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public static void openSoftKeyboard(Context m_context) {
		//View view = getCurrentFocus();
		InputMethodManager imm = (InputMethodManager) m_context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static String skipSpacesInString(String str) {
		return str.trim().replaceAll(" +", "");
	}

	public static String reduceSpacesInString(String str) {
		return str.trim().replaceAll(" +", " ");
	}

	public static String[] getPhoneContactList(Context m_context) {
		ContentResolver cr = m_context.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		ArrayList<String> contact_names = new ArrayList<String>();

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER.trim()))
						.equalsIgnoreCase("1")) {
					if (name != null) {
						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
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
		for (int j = 0; j < contact_names.size(); j++) {
			contactList[j] = contact_names.get(j);
		}
		return contactList;
	}

	public static String cleanPhoneString(String oPhoneStr) {
		String adjustedPhoneStr = oPhoneStr.replace("(", "");
		adjustedPhoneStr = adjustedPhoneStr.replace(")", "");
		adjustedPhoneStr = adjustedPhoneStr.replace("+", "");
		adjustedPhoneStr = adjustedPhoneStr.replace("-", "");
		return adjustedPhoneStr;
	}

	public static boolean checkDateExpiracy(String mMonth, String mDay, String mYear, String dateStr, Context mContext) {
		boolean day_bool = false;
		boolean month_bool = false;
		boolean year_bool = false;
		String day_fault_str = "";
		String month_fault_str = "";
		String year_fault_str = "";
		int year = Integer.parseInt(mYear.trim());
		int month = Integer.parseInt(mMonth.trim());
		int day = Integer.parseInt(mDay.trim());

		Log.d("CURR YEAR 1 : ", String.valueOf(year));
		Log.d("CURR MONTH 1: ", String.valueOf(month));
		Log.d("CURR DAY : 1 ", String.valueOf(day));

		// String DATE_FORMAT = "yyyyMMdd";
		// SimpleDateFormat sdf =
		//		 new SimpleDateFormat(DATE_FORMAT);
		// Calendar c1 = Calendar.getInstance(); // today
		// String dateStr = sdf.format(c1.getTime());

		int currentYear = Integer.parseInt(dateStr.substring(0, 4));
		int currentMonth = Integer.parseInt(dateStr.substring(5, 7));
		int currentDay = Integer.parseInt(dateStr.substring(8, 9));

		Log.d("CURR YEAR : ", String.valueOf(currentYear));
		Log.d("CURR MONTH : ", String.valueOf(currentMonth));
		Log.d("CURR DAY : ", String.valueOf(currentDay));

		if (year > currentYear) {
			return true;
		} else if (year == currentYear) {
			if (month > currentMonth) {
				return true;
			} else if (month == currentMonth) {
				if (day >= currentDay) {
					return true;
				} else {
					Utilities.putToast("Day expired.", 0, 100, mContext);
					return false;
				}
			} else {
				Utilities.putToast("Month expired.", 0, 100, mContext);
				return false;
			}
		} else {
			Utilities.putToast("Year expired.", 0, 100, mContext);
			return false;
		}
	}

	public static String readAndCheck(int rawFileId, String mAreaCodeStr, Context mContext) {
		int mAreaCodeInt = Integer.parseInt(mAreaCodeStr);
		InputStream inputStream = mContext.getResources().openRawResource(rawFileId);

		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);

		String line = "";
		String cityStr = null;

		try {
			while ((line = buffreader.readLine()) != null) {
				String temp_AreaCodeStr = line.substring(0, 3);

				if (temp_AreaCodeStr.contentEquals(mAreaCodeStr)) {
					cityStr = line.substring(line.indexOf("-") + 2);
					return cityStr;
				} else if (Integer.parseInt(temp_AreaCodeStr) > mAreaCodeInt) {
					return cityStr;
				}
				line = "";
			}
		} catch (IOException e) {
			return cityStr;
		}
		return cityStr;
	}

	// Back up a BlackList
	public static void BackUpAList(ArrayList<String> tGmailList, String mFilePath, Context mContext) {
		String temp = "";
		int length = tGmailList.size();
		if (length == 0) {
			mContext.deleteFile(mFilePath);
			return;
		}
		for (int i = 0; i < length; i++) {
			String temp_str = tGmailList.get(i);
			String Ticker_str = temp_str + Constants.END_MARK;
			temp += Ticker_str;
			Ticker_str = null;
		}

		Log.d("BackupStocks", "Stocks are = " + temp);

		FileOutputStream f_writer = null;
		try {
			f_writer = mContext.openFileOutput(mFilePath, Context.MODE_PRIVATE);

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (f_writer != null) {
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

	public static String[] getPhoneRejectList(Context mContext) {
		ArrayList<String> reject_calls = new ArrayList<String>();
		final String[] projection = null;
		final String selection = null;
		final String[] selectionArgs = null;
		final String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					Uri.parse("content://call_log/calls"),
					projection,
					selection,
					selectionArgs,
					sortOrder);
			while (cursor.moveToNext()) {
				String callName = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
				// String callLogID = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls._ID));
				String callNumber = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
				//String callDate = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.DATE));
				String callType = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
				String isCallNew = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NEW));
				// if(Integer.parseInt(callType) == android.provider.CallLog.Calls.MISSED_TYPE && Integer.parseInt(isCallNew) > 0){
				if (Integer.parseInt(callType) == android.provider.CallLog.Calls.MISSED_TYPE) {
					reject_calls.add(callName + ", " + callNumber);
					//Log.v("Missed Call Found: ", callNumber);
				}
			}
		} catch (Exception ex) {
			Log.e("ERROR: ", ex.toString());
		} finally {
			cursor.close();
		}
		String[] rejectedCallsList = null;
		if (!reject_calls.isEmpty()) {
			rejectedCallsList = new String[reject_calls.size()];
			for (int j = 0; j < reject_calls.size(); j++) {
				rejectedCallsList[j] = reject_calls.get(j);
			}
		}
		return rejectedCallsList;
	}

	// Write the messages in Global.alertedmsgInfo to file
	public static void BackUpUSA_NumberRecords(ArrayList<String> tGmailList, Context mContext) {
		String temp = "";
		int length = tGmailList.size();
		if (length == 0) {
			//android.content.ContextWrapper.
			mContext.deleteFile(Constants.REJECTED_LIST_FILE);
			return;

		}

		// if (length > 0)
		{

			for (int i = 0; i < length; i++) {
				String temp_str = tGmailList.get(i);
				String Ticker_str = temp_str + Constants.END_MARK;
				temp += Ticker_str;
				Ticker_str = null;
			}

			Log.d("BackupStocks", "Stocks are = " + temp);

			FileOutputStream f_writer = null;
			try {

				f_writer = mContext.openFileOutput(Constants.REJECTED_LIST_FILE, Context.MODE_PRIVATE);

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (f_writer != null) {
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
	}

	public static void insertInOrder(ArrayList<String> mList, String mData) {

		String str = mData.substring(0, mData.indexOf(":"));
		int length = 0;

		if (str.contentEquals(Constants.NAMESTR)) {
			length = Constants.NAMESTR.length();
		} else {
			length = Constants.CITY.length();
		}

		if (mList.isEmpty()) {
			mList.add(mData);
			Log.i("Empty list ---  ", "Add -----------");
		} else {
			int lastPos = (mData.length() >= length + 2) ? (length + 2) : (length + 1);
			String searchKey = mData.substring(length, length + 2);
			Log.i("searchKey:  ", searchKey);
			String keyChar = mList.get(mList.size() - 1).substring(length, lastPos);

			Log.i("keyChar:  ", keyChar);

			if (searchKey.compareToIgnoreCase(keyChar) >= 0) {
				mList.add(mData);
				Log.i("End of list list ---  ", "Add -----------");
			} else {
				for (int i = 0; i < mList.size(); i++) {
					keyChar = mList.get(i).substring(length, lastPos);
					if (keyChar.compareToIgnoreCase(searchKey) > 0) {
						Log.i("Add data at : ", String.valueOf(i));
						mList.add(i, mData);
						break;
					}
				}
			}
		}
	}

	public static String FormMonthDateYearStr(String monthStr, String dateStr, String yearStr) {
		String formedString = "Block until : " + monthStr + "/" + dateStr + "/" + yearStr;
		return formedString;
	}

	public static boolean CompareTwoObjects(Object objOne, Object objTwo) {
		String objectString1 = new Gson().toJson(objOne);
		String objectString2 = new Gson().toJson(objTwo);
		if (objectString1.contentEquals(objectString2)) {
			return true;
		}
		return false;

	}


	public static int deleteSMS(Context con, String originalAddress, String msgs_time)
	{
		Uri smsUri = Uri.parse(Constants.SMS_URI);
		int no_of_messages_deleted=0;
		no_of_messages_deleted=  con.getContentResolver().delete(smsUri, "address=? and date=?",new String[] {originalAddress, msgs_time});
		return no_of_messages_deleted;

	}

	public static int deleteSMSbyAddress(Context con, String originalAddress) {

			Uri message = Uri.parse("content://sms/all");
			ContentResolver cr = con.getContentResolver();
			Cursor c = cr.query(message, null, null, null, null);
			//getApplicationContext().startManagingCursor(c);
			if (c.moveToFirst()) {
					String infile_address = c.getString(c
							.getColumnIndexOrThrow("address"));
					if (originalAddress.contains(infile_address)) {
					    Log.d("Phone number get ---", "--------------");
						String Id = c.getString(c
								.getColumnIndexOrThrow("_id"));
						cr.delete(message, "_id=?", new String[]{Id});
					}
			}while (c.moveToNext());
			return 0;
	}


	//	Uri smsUri = Uri.parse(Constants.SMS_URI + "all/");
	//	int no_of_messages_deleted = 0;
	//	no_of_messages_deleted = con.getContentResolver().delete(smsUri, "address=?", new String[]{originalAddress});
	//	return no_of_messages_deleted;


	public static void setSMSRead(Context con, String SmsMessageId) {
		Uri smsUri = Uri.parse(Constants.SMS_URI);
		ContentValues values = new ContentValues();
		values.put("read", true);
		con.getContentResolver().update(smsUri, values, "_id=" + SmsMessageId, null);
	}

	public static String getColorForLetter(String firstLetter) {
		switch (firstLetter) {
			case "A":
			case "a":
				return ("#008000"); //Green
			case "B":
			case "b":
				return ("#00FF00");  //Lime
			case "C":
			case "c":
				return ("#808000");  //Olive
			case "D":
			case "d":
				return ("#FFFF00");  //Yellow
			case "E":
			case "e":
				return ("#000080");  //Navy
			case "F":
			case "f":
				return ("#0000FF");  //Blue
			case "G":
			case "g":
				return ("#008080");  //Teal
			case "Z":
			case "z":
				return ("#636363");  //Dark silver
			case "H":
			case "h":
				return ("#00FFFF");  //Aqua
			case "J":
			case "j":
				return ("#000000");  //Black
			case "K":
			case "k":
				return ("#C0C0C0");  //Silver
			case "L":
			case "l":
				return ("#808080");  //Gray
			case "M":
			case "m":
				return ("#800000");  //Maroon
			case "N":
			case "n":
				return ("#FF0000");  //Red
			case "O":
			case "o":
				return ("#800080");  //Purple
			case "P":
			case "p":
				return ("#FF00FF");  //Fuchsia
			case "Q":
			case "q":
				return ("#497FBF");  //Light purple
			case "R":
			case "r":
				return ("#648DCF");  //Light navy
			case "S":
			case "s":
				return ("#00AEED");  //Light blue
			case "T":
			case "t":
				return ("#00A54E");  //Light green
			case "U":
			case "u":
				return ("#8DCB41");  //Light lime
			case "V":
			case "v":
				return ("#FcF64C");  //Light yellow
			case "X":
			case "x":
				return ("#DC9000");  //Orange
			case "Y":
			case "y":
				return ("#7D7D7D");  //Orange
		}
		return Constants.anonymous;
	}

	public static String lookForName(String address, String [] phoneContactList, String [] rejectedPhoneList ){
		if ((phoneContactList != null) && (phoneContactList.length > 0)) {
			for (int i = 0; i < phoneContactList.length; i++) {
				String[] tokens = phoneContactList[i].split(",");
				String digitOnlyStr = getDigitsOnly(tokens[1]);
				if (digitOnlyStr.contains(address) || (address.contains(digitOnlyStr))) {
					if (!tokens[0].isEmpty()) {
						return (tokens[0]);
					}
				}
			}
		}
		if ((rejectedPhoneList != null) &&(rejectedPhoneList.length > 0)) {
			for (int i = 0; i < rejectedPhoneList.length; i++) {
				String[] m_tokens = rejectedPhoneList[i].split(",");
				String digitOnlyStr = getDigitsOnly(m_tokens[1]);
				if (digitOnlyStr.contains(address) || (address.contains(digitOnlyStr))) {
					if (!m_tokens[0].contains("null")) {
						return (m_tokens[0]);
					}
				}
			}
		}
		return Constants.anonymous;
	}

	public static ArrayList<Sms> revertOrder(ArrayList<Sms> smsList){
		if (smsList == null || smsList.isEmpty()){
		    return smsList;
	    }
		ArrayList<Sms> temp = new ArrayList<Sms>();

		for (int i = (smsList.size() - 1); i >= 0; i--){
			temp.add(smsList.get(i));
		}
		 return temp;
	}


}