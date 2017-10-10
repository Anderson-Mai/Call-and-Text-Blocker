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

public class MmsReceiver extends BroadcastReceiver {
	 
  private String formatedNumberStr = "";
  private String formatedCallStr = "";
 
  final String MMS_DEFAULT_RECEIVED_ACTION ="android.provider.Telephony.WAP_PUSH_DELIVER";
  
 
  String msg_from = null;
  String body = null;
  Context m_context = null;
  Uri smsUri = Uri.parse(Constants.SMS_URI);
  
  @Override
  public void onReceive(Context context, Intent intent) {
	  m_context = context;
	  
	  if(intent.getAction().equals(MMS_DEFAULT_RECEIVED_ACTION)){
		  
		  Log.d("DEFAULT RECEIVER"   ,   "DEFAULT RECEIVER");
          Bundle bundle = intent.getExtras(); 
          SmsMessage msgs = null;
          String msg_time = null;
          boolean AC_Match = false;
          
          if (bundle != null){
             try {
            	 
            	 
            	//  byte[] pushData = intent.getByteArrayExtra("data");
            	//    PduParser parser = new PduParser(pushData);
            	//    
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
              
              formatedCallStr = msg_from;
              if (Global.AreaCodeChecker){
            	  for (int i = 0; i < Global.mAreaCodeRejectedList.size(); i++){
  				    OneNode checkNode = Utilities.GetNode(Global.mAreaCodeRejectedList.get(i), "");
  				    formatedNumberStr = checkNode.numberStr;
  				    String area_code_str = getDigitsOnly(formatedCallStr).substring(1, 4);
  				    Log.d("area code call string: ", area_code_str);
  				    Log.d("formatedNumberStr string: ",formatedNumberStr );
  				    if (formatedNumberStr.contains(area_code_str)) {
  				    AC_Match = true;
  				    Log.d("Number of deletion:  ", String.valueOf(deleteSMS(context, msg_from, msg_time)));
  				    	
  	                    if (checkNode.smsResponse.contentEquals(Constants.SMS_YES_Indicator)){
  					    	  SmsManager smsManager = SmsManager.getDefault();
  					    	  checkNode.yourMessage = Global.mMap.get(checkNode.numberStr);
  							  smsManager.sendTextMessage(msg_from, null, checkNode.yourMessage  , null, null);
  							  Log.d("SMS", " ----------------  SENT SMS -----------------");
  							  Toast.makeText(context, "Terminated sms*** " + msg_from, 1000).show();
  					    	  Log.d("SMS RESPONSE : ", "YES");
  					    	  Log.d("-----YOUR MSG:-------- ", checkNode.yourMessage);
  					   }
  				       break;
                   }  
            	  } //for loop
            	  
  				  if(!AC_Match)  {
  				         ContentResolver contentResolver = context.getContentResolver();
  				        ContentValues values = new ContentValues();
  			            values.put(Constants.COLUMN_ADDRESS, msg_from);
  			            values.put(Constants.COLUMN_BODY, body);
  				    	contentResolver.insert(smsUri, values);
  				    	 start_blue_bubble_dark(context);
  				    	
  				    }
                
              }  // Global.AreaCodeChecker
              else {
                   for (int i = 0; i < Global.mAreaCodeRejectedList.size(); i++){
					    OneNode checkNode = Utilities.GetNode(Global.mAreaCodeRejectedList.get(i), "");
					    formatedNumberStr = checkNode.numberStr;
					    formatedCallStr = getDigitsOnly(msg_from);
					    if (formatedCallStr.contains(formatedNumberStr)) {
					    	AC_Match = true;
		                    if (checkNode.smsResponse.contentEquals(Constants.SMS_YES_Indicator)){
						    	  SmsManager smsManager = SmsManager.getDefault();
						    	  checkNode.yourMessage = Global.mMap.get(checkNode.numberStr);
								  smsManager.sendTextMessage(msg_from, null, checkNode.yourMessage  , null, null);
								  Log.d("SMS", " ----------------  SENT SMS -----------------");
								  Toast.makeText(context, "Terminated sms*** " + msg_from, Toast.LENGTH_SHORT).show();
						    	  Log.d("SMS RESPONSE : ", "YES");
						    	  Log.d("-----YOUR MSG:-------- ", checkNode.yourMessage);
						   }
					       break;
	                   }  
                   } 
                   
                   if (!AC_Match){
					    	   ContentResolver contentResolver = context.getContentResolver();
		  				        ContentValues values = new ContentValues();
		  			            values.put(Constants.COLUMN_ADDRESS, msg_from);
		  			            values.put(Constants.COLUMN_BODY, body);
		  				    	contentResolver.insert(smsUri, values);
		  				    	 start_blue_bubble_dark(context);
					}
                    
              } 
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
       
       
       private int deleteSMS(Context con, String originalAddress, String msgs_time) 
       { 
    	  int no_of_messages_deleted=0;
    	  no_of_messages_deleted=  con.getContentResolver().delete(smsUri, "address=? and date=?",new String[] {originalAddress, msgs_time});
       	  return no_of_messages_deleted;
         
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
			Intent notificationIntent = new Intent(context, ComposeSmsActivity.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent contentIntent = 
				PendingIntent.getActivity(context, 0, notificationIntent,0);
		    mBuilder.setContentIntent(contentIntent);
		    Notification notification = mBuilder.build();
			Global.NotifyManager.notify(Global.Notify_ID, notification);
			
		
	  }



} 

