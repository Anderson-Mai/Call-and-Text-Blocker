package com.anderson.rejectcallmsg;
import com.anderson.include.Constants;
import com.anderson.include.Utilities;
import com.android.internal.telephony.ITelephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyOldReceiver extends BroadcastReceiver {

	private String formatedNumberStr = "";
	private String formatedCallStr = "";

	final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	String msg_from;
	Context m_context;

	@Override
	public void onReceive(Context context, Intent intent) {
		m_context = context;
		boolean found_in_AC = false;
		boolean found_in_USA = false;

		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
			Log.d("OLD RECEIVER", "OLD RECEIVER");
			Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
			SmsMessage msgs = null;
			if (bundle != null) {
				//---retrieve the SMS message received---
				try {
					Object[] pdus = (Object[]) bundle.get("pdus");
					// msgs = new SmsMessage[pdus.length];

					msgs = SmsMessage.createFromPdu((byte[]) pdus[0]);

					msg_from = msgs.getOriginatingAddress();
					Log.d("msg_from _ OLD IN SMS: ", msg_from);

				} catch (Exception e) {
//                          Log.d("Exception caught",e.getMessage());
				}

				formatedCallStr = Utilities.getDigitLetterOnly(msg_from);
				if (Utilities.checkFromWhiteList(formatedCallStr, Global.mWhiteList)) {
					return;
				}
				if (!Global.mAreaCodeRejectedList.isEmpty()) {
					for (int i = 0; i < Global.mAreaCodeRejectedList.size(); i++) {
						OneNode checkNode = Utilities.GetNode(Global.mAreaCodeRejectedList.get(i), "");
						formatedNumberStr = checkNode.numberStr.substring(Constants.AREA_CODE_TITLE.length()).trim();
						String area_code_str = formatedCallStr.substring(1, 4);
						Log.d("-------Incoming AC -----: ", area_code_str);
						Log.d("------ In list  ------: ", formatedNumberStr);
						if (Utilities.getDigitsOnly(formatedNumberStr).contains(area_code_str)) {
							Log.d("- AC- ", "MATCHED");
							found_in_AC = true;
							SimpleDateFormat sdf =
									new SimpleDateFormat(Constants.DATE_FORMAT);
							Calendar c1 = Calendar.getInstance(); // today
							String dateStr = sdf.format(c1.getTime());

							if (!Utilities.checkDateExpiracy(checkNode.month, checkNode.day, checkNode.year, dateStr, m_context)) {
								Log.d("-------DATE EXPIRED -----: ", "-----");
								break;
							}
							abortBroadcast();
							String blocked_Time = "Blocked text from: " + formatedCallStr + "  " + dateStr;
							Global.mBlockedList.add(blocked_Time);
							Utilities.BackUpAList(Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE, m_context);

							if (checkNode.smsOption.contentEquals(Constants.SMS_NO_OPTION)) {
								Log.d("-------SMS_NO_OPTION-----: ", "-----");
								break;
							}

							//	  Log.d("Number of deletion:  ", String.valueOf(deleteSMS(context, msg_from, msg_time)));
							if (checkNode.smsResponse.contentEquals(Constants.SMS_YES_Indicator)) {
								SmsManager smsManager = SmsManager.getDefault();
								checkNode.yourMessage = Global.mMap.get(formatedNumberStr);
								smsManager.sendTextMessage(msg_from, null, checkNode.yourMessage, null, null);
								Log.d("SMS", " ----------------  SENT SMS -----------------");
								Toast.makeText(context, "Terminated sms*** " + msg_from, 1000).show();
								Log.d("SMS RESPONSE : ", "YES");
								Log.d("-----YOUR MSG:-------- ", checkNode.yourMessage);
							}
							break;
						}
					} //for loop
				}
				else
					if ((!Global.mRejectedList.isEmpty()) && (!found_in_AC)) {
						for (int i = 0; i < Global.mRejectedList.size(); i++) {
							OneNode checkNode = Utilities.GetNode(Global.mRejectedList.get(i), "");
							formatedNumberStr = checkNode.numberStr.substring(Constants.NUMBER_TITLE.length());
							if (formatedCallStr.contains(Utilities.getDigitsOnly(formatedNumberStr)) ||
									Utilities.getDigitsOnly(formatedNumberStr).contains(formatedCallStr)) {
								found_in_USA = true;

								SimpleDateFormat sdf =
										new SimpleDateFormat(Constants.DATE_FORMAT);
								Calendar c1 = Calendar.getInstance(); // today
								String dateStr = sdf.format(c1.getTime());
								if (!Utilities.checkDateExpiracy(checkNode.month, checkNode.day, checkNode.year, dateStr, m_context)) {
									break;
								}

								abortBroadcast();
								String blocked_Time = "Blocked text from: " + formatedCallStr + "  " + dateStr;
								Global.mBlockedList.add(blocked_Time);
								Utilities.BackUpAList(Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE, m_context);

								if (checkNode.smsOption.contentEquals(Constants.SMS_NO_OPTION)) {
									Log.d("SMS", "Constants.SMS_NO_OPTION");
									break;
								}

								//  Log.d("Number of deletion:  ", String.valueOf(deleteSMS(context, msg_from, msg_time)));
								if (checkNode.smsResponse.contentEquals(Constants.SMS_YES_Indicator)) {
									SmsManager smsManager = SmsManager.getDefault();
									checkNode.yourMessage = Global.mMap.get(formatedNumberStr);
									smsManager.sendTextMessage(msg_from, null, checkNode.yourMessage, null, null);
									Log.d("SMS", " ----------------  SENT SMS -----------------");
									Toast.makeText(context, "Terminated sms*** " + msg_from, Toast.LENGTH_SHORT).show();
									Log.d("SMS RESPONSE : ", "YES");
									Log.d("-----YOUR MSG:-------- ", checkNode.yourMessage);
								}
								break;
							}
						}
				}
				else
					if ((!found_in_USA) && (!found_in_AC) && (!Global.mInternationalRejectedList.isEmpty())) {
							Log.d("COME IN INTERNATIONAL", "COME IN INTERNATIONAL");

							for (int i = 0; i < Global.mInternationalRejectedList.size(); i++) {
								OneNode checkNode = Utilities.GetNode(Global.mInternationalRejectedList.get(i), "");
								formatedNumberStr = checkNode.numberStr.substring(Constants.NUMBER_TITLE.length());
								if (formatedCallStr.contains(Utilities.getDigitsOnly(formatedNumberStr)) ||
										Utilities.getDigitsOnly(formatedNumberStr).contains(formatedCallStr)) {
									found_in_USA = true;

									SimpleDateFormat sdf =
											new SimpleDateFormat(Constants.DATE_FORMAT);
									Calendar c1 = Calendar.getInstance(); // today
									String dateStr = sdf.format(c1.getTime());
									if (!Utilities.checkDateExpiracy(checkNode.month, checkNode.day, checkNode.year, dateStr, m_context)) {
										break;
									}

									abortBroadcast();
									String blocked_Time = "Blocked text from: " + formatedCallStr + "  " + dateStr;
									Global.mBlockedList.add(blocked_Time);
									Utilities.BackUpAList(Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE, m_context);

									if (checkNode.smsOption.contentEquals(Constants.SMS_NO_OPTION)) {
										Log.d("SMS", "Constants.SMS_NO_OPTION");
										break;
									}

									//  Log.d("Number of deletion:  ", String.valueOf(deleteSMS(context, msg_from, msg_time)));
									if (checkNode.smsResponse.contentEquals(Constants.SMS_YES_Indicator)) {
										SmsManager smsManager = SmsManager.getDefault();
										checkNode.yourMessage = Global.mMap.get(formatedNumberStr);
										smsManager.sendTextMessage(msg_from, null, checkNode.yourMessage, null, null);
										Log.d("SMS", " ----------------  SENT SMS -----------------");
										Toast.makeText(context, "Terminated sms*** " + msg_from, Toast.LENGTH_SHORT).show();
										Log.d("SMS RESPONSE : ", "YES");
										Log.d("-----YOUR MSG:-------- ", checkNode.yourMessage);
									}
									break;
								}
							}


				}
			} // bundle

		}

	}
}