package com.anderson.rejectcallmsg;
import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyDefaultReceiver extends BroadcastReceiver {
	 
  private String formatedNumberStr = "";
  private String formatedCallStr = "";
 
  final String SMS_DEFAULT_RECEIVED_ACTION ="android.provider.Telephony.SMS_DELIVER";

  String msg_from = null;
  String body = null;
  Context m_context = null;
  Uri smsUri = Uri.parse(Constants.SMS_URI);
  
  @Override
  public void onReceive(Context context, Intent intent) {
	  m_context = context;
	  
	  if(intent.getAction().equals(SMS_DEFAULT_RECEIVED_ACTION)){
		  Log.d("DEFAULT RECEIVER", "DEFAULT RECEIVER");
          Bundle bundle = intent.getExtras(); 
          SmsMessage msgs = null;
          String msg_time = null;
          boolean found_in_AC = false;
		  boolean found_in_USA = false;
		  boolean found_in_INTNAL = false;

          if (bundle != null){
             try {
                  Object[] pdus = (Object[]) bundle.get("pdus");
                  msgs = SmsMessage.createFromPdu((byte[])pdus[0]);

                  msg_from = msgs.getOriginatingAddress();
                  body = msgs.getMessageBody();
                  msg_time = String.valueOf(msgs.getTimestampMillis());
                  Global.recent_sender = msg_from;
                  Global.recent_sms_time  = msg_time;
                  Global.recent_sms_body = body;
                  Log.d("msg_from _ IN SMS: ", msg_from);
                
              }catch(Exception e){
                    Log.d("Exception caught",e.getMessage());
              }
              
              formatedCallStr = Utilities.getDigitLetterOnly(msg_from);
              if (Utilities.checkFromWhiteList(formatedCallStr, Global.mWhiteList)){
				  ContentResolver contentResolver = context.getContentResolver();
				  ContentValues values = new ContentValues();
				  values.put(Constants.COLUMN_ADDRESS, msg_from);
				  values.put(Constants.COLUMN_BODY, body);
				  contentResolver.insert(smsUri, values);
				  start_blue_bubble_dark(context);

				  return;
			  }
              
              if (!Global.mAreaCodeRejectedList.isEmpty()) {
            	  for (int i = 0; i < Global.mAreaCodeRejectedList.size(); i++){
					OneNode checkNode = Utilities.GetNode(Global.mAreaCodeRejectedList.get(i), "");
					formatedNumberStr = checkNode.numberStr.substring(Constants.AREA_CODE_TITLE.length()).trim();
					String area_code_str = formatedCallStr.substring(1, 4);

  				    Log.d("area code call string: ", area_code_str);
  				    Log.d("formatedNumberStr : ", formatedNumberStr );
  				    if (formatedNumberStr.contains(Utilities.getDigitsOnly(area_code_str))) {
						found_in_AC = true;
						SimpleDateFormat sdf =
								new SimpleDateFormat(Constants.DATE_FORMAT);
						Calendar c1 = Calendar.getInstance(); // today
						String dateStr = sdf.format(c1.getTime());

						if (!Utilities.checkDateExpiracy(checkNode.month, checkNode.day, checkNode.year, dateStr,  m_context)){
							return;
						}
						Log.d("Number of deletion:  ", String.valueOf(Utilities.deleteSMS(context, msg_from, msg_time)));
						String blocked_Time = "Blocked text from: " + formatedCallStr + "  " + dateStr;
						Global.mBlockedList.add(blocked_Time);
						Utilities.BackUpAList(Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE, m_context);

						if (checkNode.smsOption.contentEquals(Constants.SMS_NO_OPTION)){
							break;
						}

						if (checkNode.smsResponse.contentEquals(Constants.SMS_YES_Indicator)){
								  SmsManager smsManager = SmsManager.getDefault();
								  checkNode.yourMessage = Global.mMap.get(formatedNumberStr);
							Log.d("YOUR MSG:  ", checkNode.yourMessage );
								  smsManager.sendTextMessage(msg_from, null, checkNode.yourMessage  , null, null);
								  Log.d("SMS", " ----------------  SENT SMS -----------------");
								  Toast.makeText(context, "Terminated sms*** " + msg_from, 1000).show();
								  Log.d("SMS RESPONSE : ", "YES");
								  Log.d("-----YOUR MSG:-------- ", checkNode.yourMessage);
						}
						break;
					}
            	  } //for loop
            	  
  				 	/* if(!AC_Match)  {
  				         ContentResolver contentResolver = context.getContentResolver();
  				        ContentValues values = new ContentValues();
  			            values.put(Constants.COLUMN_ADDRESS, msg_from);
  			            values.put(Constants.COLUMN_BODY, body);
  				    	contentResolver.insert(smsUri, values);
  				    	 start_blue_bubble_dark(context);
  				    */

  				    }
                
              }  // Global.AreaCodeChecker

		      if ( (!Global.mRejectedList.isEmpty()) && (!found_in_AC)){
                   for (int i = 0; i < Global.mRejectedList.size(); i++){
					    OneNode checkNode = Utilities.GetNode(Global.mRejectedList.get(i), "");
					    formatedNumberStr = checkNode.numberStr.substring(Constants.NUMBER_TITLE.length());
					    if (formatedCallStr.contains(Utilities.getDigitsOnly(formatedNumberStr)) ||
								             Utilities.getDigitsOnly(formatedNumberStr).contains(formatedCallStr)) {
							found_in_USA = true;
							SimpleDateFormat sdf =
									new SimpleDateFormat(Constants.DATE_FORMAT);
							Calendar c1 = Calendar.getInstance(); // today
							String dateStr = sdf.format(c1.getTime());

							if (!Utilities.checkDateExpiracy(checkNode.month, checkNode.day, checkNode.year, dateStr,  m_context)){
								return;
							}
							Log.d("Number of deletion:  ", String.valueOf(Utilities.deleteSMS(context, msg_from, msg_time)));
							String blocked_Time = "Blocked text from: " + formatedCallStr + "  " + dateStr;
							Global.mBlockedList.add(blocked_Time);
							Utilities.BackUpAList(Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE, m_context);

							if (checkNode.smsOption.contentEquals(Constants.SMS_NO_OPTION)) {
								Log.d("SMS", "Constants.SMS_NO_OPTION");
								break;
							}

							if (checkNode.smsResponse.contentEquals(Constants.SMS_YES_Indicator)){
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
                   
                   /* if (!AC_Match){
					    	   ContentResolver contentResolver = context.getContentResolver();
		  				        ContentValues values = new ContentValues();
		  			            values.put(Constants.COLUMN_ADDRESS, msg_from);
		  			            values.put(Constants.COLUMN_BODY, body);
		  				    	contentResolver.insert(smsUri, values);
		  				    	start_blue_bubble_dark(context);
					}
					*/
		      }

		      if((!found_in_USA) && (!found_in_AC) && (!Global.mInternationalRejectedList.isEmpty())){
				  for (int i = 0; i < Global.mInternationalRejectedList.size(); i++) {
					  OneNode checkNode = Utilities.GetNode(Global.mInternationalRejectedList.get(i), "");
					  formatedNumberStr = checkNode.numberStr.substring(Constants.NUMBER_TITLE.length());
					  if (formatedCallStr.contains(Utilities.getDigitsOnly(formatedNumberStr)) ||
							        Utilities.getDigitsOnly(formatedNumberStr).contains(formatedCallStr)) {
						  found_in_INTNAL = true;

						  SimpleDateFormat sdf =
								  new SimpleDateFormat(Constants.DATE_FORMAT);
						  Calendar c1 = Calendar.getInstance(); // today
						  String dateStr = sdf.format(c1.getTime());

						  if (!Utilities.checkDateExpiracy(checkNode.month, checkNode.day, checkNode.year, dateStr, m_context)) {
							  return;
						  }
						  Log.d("Number of deletion:  ", String.valueOf(Utilities.deleteSMS(context, msg_from, msg_time)));
						  String blocked_Time = "Blocked text from: " + msg_from + "  " + dateStr;
						  Global.mBlockedList.add(blocked_Time);
						  Utilities.BackUpAList(Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE, m_context);
						  if (checkNode.smsOption.contentEquals(Constants.SMS_NO_OPTION)) {
							  Log.d("SMS", "Constants.SMS_NO_OPTION");
							  break;
						  }

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

			  if ((!found_in_AC) && (!found_in_USA) && (!found_in_INTNAL)) {
				  ContentResolver contentResolver = context.getContentResolver();
				  ContentValues values = new ContentValues();
				  values.put(Constants.COLUMN_ADDRESS, msg_from);
				  values.put(Constants.COLUMN_BODY, body);
				  contentResolver.insert(smsUri, values);
				  start_blue_bubble_dark(context);
			  }
		  }
	  }

       public String getDigitsOnly(String phoneNumber){
    	   
    	   if ((phoneNumber == null ) || phoneNumber.isEmpty()){
    		   return null;
    	   }
    	   
    	   String phoneNumberStr = "";
    	   for (int i = 0; i < phoneNumber.length(); i++){
    		   char c = phoneNumber.charAt(i);
    		   if (Character.isDigit(c)){
	    		   String intStr = Character.toString(c);
	    		   phoneNumberStr += intStr; 
    		   }
    	   }
    	   return phoneNumberStr;
       }
       
       private Notification.Builder notificationBuilder(int icon , Context mContext, int time) {
		    Notification.Builder builder = 
		      new Notification.Builder(mContext);
		    builder.setSmallIcon(icon)
		           .setTicker("Text");
		    return builder;
	}
       
  	 protected void start_blue_bubble_dark(Context context){
			Global.icon = R.drawable.sms_icon;
			String ns = Context.NOTIFICATION_SERVICE;
		    Global.NotifyManager = (NotificationManager) context.getSystemService(ns);
			Notification.Builder mBuilder = notificationBuilder(Global.icon,  context, 0);

			CharSequence contentTitle = "New message";
			CharSequence contentText = "";
			Intent notificationIntent = new Intent(context, UnreadSmsActivity.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent contentIntent = 
				PendingIntent.getActivity(context, 0, notificationIntent,0);
		    mBuilder.setContentIntent(contentIntent);
		    Notification notification = mBuilder.build();
			Global.NotifyManager.notify(Global.Notify_ID, notification);
	  }
} 

