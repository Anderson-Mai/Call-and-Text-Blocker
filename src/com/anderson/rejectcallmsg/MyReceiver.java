package com.anderson.rejectcallmsg;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;
import com.android.internal.telephony.ITelephony;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
	 
  //private String formatedNumberStr = "";
  //private String formatedCallStr = "";
  final String CALL_RECEIVED_ACTION ="android.intent.action.PHONE_STATE";
  Uri SMS_INBOX = Uri.parse("content://sms/inbox");
  String msg_from;
  Context m_context;
  
  @Override
  public void onReceive(Context context, Intent intent) {
	  m_context = context;
	  Boolean found_in_AC = false;
	  Boolean found_in_USA = false;
	  Boolean found_in_INTNAL = false;

	  if (intent.getAction().equals(CALL_RECEIVED_ACTION)) {
		  Log.d("CALLER : ", "COME IN  CALL_RECEIVED_ACTION ");
		  String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

		  if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			  String formatedNumberStr = "";
			  String formatedCallStr = "";
			  String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

			  formatedCallStr = Utilities.getDigitLetterOnly(phoneNumber);
			  Log.d("INCOMING NUMBER : ",formatedCallStr );
			  if (Utilities.checkFromWhiteList(formatedCallStr, Global.mWhiteList)) {
				  return;
			  }

			  // Global.AreaCodeChecker
			  if (!Global.mAreaCodeRejectedList.isEmpty()) {
				  Log.d("COME IN AREA CODE", "COME IN AREA CODE");
				  String area_code_str = formatedCallStr.substring(1, 4);
				  Log.d("area code call string: ", area_code_str);
				  for (int i = 0; i < Global.mAreaCodeRejectedList.size(); i++) {
					  OneNode checkNode = Utilities.GetNode(Global.mAreaCodeRejectedList.get(i), "");
					  formatedNumberStr = checkNode.numberStr.substring(Constants.AREA_CODE_TITLE.length()).trim();
					  Log.d("area code in list: ", formatedNumberStr);
					  if (formatedNumberStr.contentEquals(area_code_str)) {
						  found_in_AC = true;

						  SimpleDateFormat sdf =
								  new SimpleDateFormat(Constants.DATE_FORMAT);
						  Calendar c1 = Calendar.getInstance(); // today
						  String dateStr = sdf.format(c1.getTime());

						  if (!Utilities.checkDateExpiracy(checkNode.month, checkNode.day, checkNode.year, dateStr, context)){
							  return;
						  }
						  TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
						  Class clazz = null;
						  try {
							  clazz = Class.forName(telephonyManager.getClass().getName());
						  } catch (ClassNotFoundException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  Method method = null;
						  try {
							  method = clazz.getDeclaredMethod("getITelephony");
						  } catch (NoSuchMethodException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  method.setAccessible(true);
						  ITelephony telephonyService = null;
						  try {
							  telephonyService = (ITelephony) method.invoke(telephonyManager);
						  } catch (IllegalArgumentException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  } catch (IllegalAccessException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  } catch (InvocationTargetException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }

						  try {
							  //telephonyService.silenceRinger();
							  telephonyService.endCall();
							  String blocked_Time = "Blocked call from: " + phoneNumber + "  " + dateStr;
							  Global.mBlockedList.add(blocked_Time);
							  Utilities.BackUpAList(Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE, m_context);
							 //  Utilities.writeBlockedNumber(blocked_Time, Constants.BLOCKED_CALL_SMS_RECORD_FILE, m_context);
						  } catch (RemoteException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }

						  Log.d("Check Node Response ", checkNode.smsResponse);

						  if (checkNode.smsResponse.contentEquals(Constants.SMS_YES_Indicator)) {
							  SmsManager smsManager = SmsManager.getDefault();
							  checkNode.yourMessage = Global.mMap.get(formatedNumberStr);
							  smsManager.sendTextMessage(phoneNumber, null, checkNode.yourMessage, null, null);
							  Log.d("SMS", " ----------------  SENT SMS -----------------");
							  Utilities.putToast("Terminated Call Number*** " + phoneNumber, 0, 400, context);
							  Log.d("SMS RESPONSE : ", "YES");
							  Log.d("-----YOUR MSG:-------- ", checkNode.yourMessage);
						  }
						  break;
					  }
				  }

			  }
			  else
			  if ( (!Global.mRejectedList.isEmpty()) && (!found_in_AC)) {
				  for (int i = 0; i < Global.mRejectedList.size(); i++) {
					  OneNode checkNode = Utilities.GetNode(Global.mRejectedList.get(i), "");
					  formatedNumberStr = checkNode.numberStr.substring(Constants.NUMBER_TITLE.length());
					  Log.i("Call in number:  ",formatedCallStr );
					  Log.i("number from the list: ",formatedNumberStr );

					  if (Utilities.getDigitsOnly(formatedNumberStr).contains(formatedCallStr)){
						  found_in_USA = true;
						  SimpleDateFormat sdf =
								  new SimpleDateFormat(Constants.DATE_FORMAT);
						  Calendar c1 = Calendar.getInstance(); // today
						  String dateStr = sdf.format(c1.getTime());

						  if (!Utilities.checkDateExpiracy(checkNode.month, checkNode.day, checkNode.year, dateStr,  m_context)){
						 	  return;
						  }
						  TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
						  Class clazz = null;
						  try {
							  clazz = Class.forName(telephonyManager.getClass().getName());
						  } catch (ClassNotFoundException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  Method method = null;
						  try {
							  method = clazz.getDeclaredMethod("getITelephony");
						  } catch (NoSuchMethodException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  method.setAccessible(true);
						  ITelephony telephonyService = null;
						  try {
							  telephonyService = (ITelephony) method.invoke(telephonyManager);
						  } catch (IllegalArgumentException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  } catch (IllegalAccessException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  } catch (InvocationTargetException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  if (telephonyService == null){
							  return;
						  }
						  try {
							 // telephonyService.silenceRinger();
							  telephonyService.endCall();
							  String blocked_Time = "Blocked call from: " + phoneNumber + "  " + dateStr;

							  Utilities.putToast("Add Str : " + blocked_Time , 0 , 300, context);
							  Global.mBlockedList.add(blocked_Time);
							  Log.d("ADD :", blocked_Time );

							  Utilities.BackUpAList(Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE, m_context);
						  } catch (RemoteException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }

						  Log.d("Check Node Response ", checkNode.smsResponse);

						  if (checkNode.smsResponse.contentEquals(Constants.SMS_YES_Indicator)) {
							  SmsManager smsManager = SmsManager.getDefault();
							  checkNode.yourMessage = Global.mMap.get(formatedNumberStr);
							  smsManager.sendTextMessage(phoneNumber, null, checkNode.yourMessage, null, null);
							  Log.d("SMS", " ----------------  SENT SMS -----------------");
							  Utilities.putToast("Terminated Call Number:" + checkNode.yourMessage, 0, 400, context);
						  }
						  break;
					  }    //if
				  } // for loop
			  }
              else
			  if((!found_in_USA) && (!found_in_AC) && (!Global.mInternationalRejectedList.isEmpty())){
				  Log.d("COME IN INTERNATIONAL", "COME IN INTERNATIONAL");

				  // find it in International number list

				  for (int i = 0; i < Global.mInternationalRejectedList.size(); i++) {
					  OneNode checkNode = Utilities.GetNode(Global.mInternationalRejectedList.get(i), "");
					  formatedNumberStr = checkNode.numberStr.substring(Constants.NUMBER_TITLE.length());
					  Log.i("Call in number:  ",formatedCallStr );
					  Log.i("number from the list: ",formatedNumberStr );

					  if (Utilities.getDigitsOnly(formatedNumberStr).contains(formatedCallStr)){
						  found_in_USA = true;
						  SimpleDateFormat sdf =
								  new SimpleDateFormat(Constants.DATE_FORMAT);
						  Calendar c1 = Calendar.getInstance(); // today
						  String dateStr = sdf.format(c1.getTime());

						  if (!Utilities.checkDateExpiracy(checkNode.month, checkNode.day, checkNode.year, dateStr,  m_context)){
							  return;
						  }
						  TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
						  Class clazz = null;
						  try {
							  clazz = Class.forName(telephonyManager.getClass().getName());
						  } catch (ClassNotFoundException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  Method method = null;
						  try {
							  method = clazz.getDeclaredMethod("getITelephony");
						  } catch (NoSuchMethodException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  method.setAccessible(true);
						  ITelephony telephonyService = null;
						  try {
							  telephonyService = (ITelephony) method.invoke(telephonyManager);
						  } catch (IllegalArgumentException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  } catch (IllegalAccessException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  } catch (InvocationTargetException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  if (telephonyService == null){
							  return;
						  }
						  try {
							  // telephonyService.silenceRinger();
							  telephonyService.endCall();
							  String blocked_Time = "Blocked call from: " + phoneNumber + "  " + dateStr;

							  Utilities.putToast("Add Str : " + blocked_Time , 0 , 300, context);
							  Global.mBlockedList.add(blocked_Time);
							  Log.d("ADD :", blocked_Time );

							  Utilities.BackUpAList(Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE, m_context);
						  } catch (RemoteException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }

						  Log.d("Check Node Response ", checkNode.smsResponse);

						  if (checkNode.smsResponse.contentEquals(Constants.SMS_YES_Indicator)) {
							  SmsManager smsManager = SmsManager.getDefault();
							  checkNode.yourMessage = Global.mMap.get(formatedNumberStr);
							  smsManager.sendTextMessage(phoneNumber, null, checkNode.yourMessage, null, null);
							  Log.d("SMS", " ----------------  SENT SMS -----------------");
							  Utilities.putToast("Terminated Call Number:" + checkNode.yourMessage, 0, 400, context);
						  }
						  break;
					  }    //if
				  } // for loop



			  }
		  }
	  }
  }

}

