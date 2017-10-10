package com.anderson.rejectcallmsg;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import android.content.ComponentName;
import android.os.Bundle;
import android.provider.Telephony;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	protected Context mContext = null;
	private ListView mSelectionView;
	private Button startButton;
	private Button closeButton;
	private Button resetButton;
	private Button smsButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    mContext = this;

        String [] selectionList = { 
        		
				//"International number Black list",
				//"International Country Code Black list",
				//"   Start Blocking",
				//"   Close App",
			//	"Reset App",
				"White List",
				"International Blacklist",
				"USA Phone Blacklist",
				"USA Area Code Blacklist",
				"Blocked Call & Text Log"
				};
	  
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_3, selectionList);
      mSelectionView = (ListView)findViewById(R.id.SelectionList);
      mSelectionView.setAdapter(adapter);
      
      
      // Select a spot from list
      mSelectionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?>parent, View view, int position, long id){
   
   						switch(position){


   					         case 0:    Intent mWhiteList = new Intent (getApplicationContext(), WhiteList.class);
			                            startActivity(mWhiteList);
		 			                    break;

							 case 1:    Intent INTERnumber = new Intent (getApplicationContext(), World_BlackList.class);
								        startActivity(INTERnumber);
								        break;
   						     case 2:
   							            Intent USnumber = new Intent (getApplicationContext(), NumberBlackList.class);
					                    startActivity(USnumber);
				 			            break;
					
							case 3:     Intent USareaCode = new Intent (getApplicationContext(), AreaCodeBlackList.class);
								        startActivity(USareaCode);
			                            break;

							case 4:     Intent BlockedList = new Intent (getApplicationContext(), BlockedCallTextList.class);
										startActivity(BlockedList);
										break;
   					  }		
			    }
     	  });

		smsButton = (Button)findViewById(R.id.smsButton);
		smsButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				Intent smsIntent = new Intent(mContext, SmsMainPageActivity.class);
				//Intent smsIntent = new Intent(mContext, SmsTabs.class);
				startActivityForResult(smsIntent, Constants.REFRESH);
			}
		});


		startButton = (Button)findViewById(R.id.startButton);

		startButton.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if ((Global.mRejectedList.size() == 0) &&
						(Global.mAreaCodeRejectedList.size() == 0) &&
						(Global.mInternationalRejectedList.size() == 0)) {
					Utilities.putToast("Your black lists is empty.", 0, 100, mContext);
					return false;
				}

				if (Global.mRejectedList.size() > 0) {
					Global.NumberChecker = true;
				}

				if (Global.mAreaCodeRejectedList.size() > 0) {
					Global.AreaCodeChecker = true;
				}

				if (Global.mInternationalRejectedList.size() > 0) {
					Global.InterChecker = true;
				}

				Global.mWhiteList.clear();
				try {
					Utilities.getListFromFile(mContext, Global.mWhiteList, Constants.WHITE_LIST_FILE);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int iconImage = 0;
				if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
					iconImage= R.drawable.phone_blocker;
				}
				else {
					iconImage= R.drawable.cs_blocker;
				}

				displayNotification(iconImage, true);
				moveTaskToBack(true);
				return true;

			}
		});



		closeButton = (Button)findViewById(R.id.closeButton);
		closeButton.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (Global.NotifyManager != null){
					Global.NotifyManager.cancelAll();
					Global.NotifyManager = null;
				}
				finish();
				return true;
			}
		});


		resetButton = (Button)findViewById(R.id.resetButton);
		resetButton.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Global.mMap.clear();
				Global.mRejectedList.clear();
				Global.mAreaCodeRejectedList.clear();
				Global.mInternationalRejectedList.clear();
				Global.mWhiteList.clear();

				if (Global.NotifyManager != null) {
					Global.NotifyManager.cancelAll();
					Global.NotifyManager = null;
				}

				deleteFile(Constants.AREA_CODE_REJECTED_LIST_FILE);
				deleteFile(Constants.REJECTED_LIST_FILE);
				deleteFile(Constants.INTERNATIONAL_REJECTED_LIST_FILE);
				deleteFile(Constants.MESSAGE_MAP_FILE);
				deleteFile(Constants.WHITE_LIST_FILE);
				String msg = "App is completely reseted.";
				Utilities.putToast(msg, 0 , 300, mContext);
				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/* public void displayNotification(int iconImage, boolean runInBackGround){

         String ns = Context.NOTIFICATION_SERVICE;
         Global.NotifyManager= (NotificationManager)getSystemService(ns);
		int icon = -1;
		CharSequence tickerText = "";
		long when = System.currentTimeMillis();
		icon = iconImage;

		Notification notification = new Notification(icon, tickerText, when);
		CharSequence contentTitle = "Call & Text Blocker";
		CharSequence contentText = "";
		
		//Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
		//notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 Intent it = new Intent("intent.my.action");
		 it.setComponent(new ComponentName(getPackageName(), MainActivity.class.getName()));
		 it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = 
						PendingIntent.getActivity(getApplicationContext(), 0, it ,0);
		//finish();
		notification.setLatestEventInfo(getApplicationContext(), contentTitle, contentText, contentIntent);
		Global.NotifyManager.notify(Global.Notify_ID, notification);



	}
	*/


         public void displayNotification(int m_icon_image,boolean runInBackGround) {
             int icon = -1;

             String ns = Context.NOTIFICATION_SERVICE;
             Global.NotifyManager = (NotificationManager)getSystemService(ns);
             icon = m_icon_image;

             Notification.Builder mBuilder =
                     new Notification.Builder(getApplicationContext())
                             .setSmallIcon(icon)
                             .setContentTitle("Call & Text Blocker");
             Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
             notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

             PendingIntent contentIntent =
                     PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
             mBuilder.setContentIntent(contentIntent);
             Notification notification = mBuilder.build();
             Global.NotifyManager.notify(Global.Notify_ID, notification);
	}



	   // Remove all elements created for the table
	    @Override protected void onResume(){
	    	super.onResume();
	    	
	    	 if (Global.NotifyManager != null){
	    		    Global.NotifyManager.cancelAll();
	    	 }
	    	 
	    	 try {
				    Global.mRejectedList.clear();
					Utilities.getListFromFile(mContext, Global.mRejectedList, Constants.REJECTED_LIST_FILE);
				    Global.mAreaCodeRejectedList.clear();
					Utilities.getListFromFile(mContext, Global.mAreaCodeRejectedList, Constants.AREA_CODE_REJECTED_LIST_FILE);
				    Global.mInternationalRejectedList.clear();
					Utilities.getListFromFile(mContext, Global.mInternationalRejectedList, Constants.INTERNATIONAL_REJECTED_LIST_FILE);
				    Global.mWhiteList.clear();
				    Utilities.getListFromFile(mContext, Global.mWhiteList, Constants.WHITE_LIST_FILE);
					
			 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 }
	    	 
	    	 try {
					Utilities.get_MessageList(mContext, Constants.MESSAGE_MAP_FILE);
					
		  	 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 }
	    	 
	    	/* if (Utilities.needAdjustmentForKidKat(Global.mRejectedList, Global.mAreaCodeRejectedList)){
	    		 checkForKITKAT();
	    		 return;
	    		 
	    	 }
	    	*/    
	    }
		
	    // Remove all elements created for the table
	    @Override protected void onDestroy(){
	    	super.onDestroy();

	    	 if (Global.NotifyManager != null)
	    		    Global.NotifyManager.cancelAll();
	    	 System.exit(0);
		   
	    }

	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (data != null) {
				if (requestCode == Constants.REFRESH) {
			  	  if (resultCode == Activity.RESULT_OK) {
					//  int pos = data.getIntExtra("REFRESH", -1);
					//   Global.all_inbox.remove(pos);
					//  m_adapter.notifyDataSetChanged();
					// Intent smsIntent = new Intent(mContext, SmsMainPageActivity.class);
					// startActivityForResult(smsIntent, Constants.REFRESH);
					}
				 }
			}
		}

	  
	    public int getACRejectedList( ArrayList<String> tGmailList, String filePath) throws IOException {
			
			String str = null;
		    String tempStr = null; 	
		
		    File file = getFileStreamPath(filePath);
		
			if  (file.exists()){	
				FileInputStream fs = openFileInput(filePath);
			
				if (fs == null){
					return -1;
				}
				else
				if (fs.available() == 0){
					return -2;
					
				}
				else
				if ((fs != null) && (fs.available() != 0)){
					 
					byte[] buffer = new byte[fs.available()];
					
						fs.read(buffer, 0, fs.available());
						str = new String(buffer);
						Log.d("backup Old message", "str = " + str);
					
					fs.close();
					int start = 0, end = 0;
					while ((end = str.indexOf(Constants.END_MARK, start)) != -1){
							tempStr = str.substring(start, end);			
						//	Global.mACNode.add(Global.GetACNode(tempStr, ""));
							tGmailList.add( tempStr);
							start = end + 1 ;
							str = str.substring(start);
							start = 0;
				   }
				 return 1;
			   }
			  
		  }
		  return -3;
	 }	
    
 public int getINTERRejectedList( ArrayList<String> tGmailList, String filePath) throws IOException {
			
			String str = null;
		    String tempStr = null; 	
		
		    File file = getFileStreamPath(filePath);
		
			if  (file.exists()){	
				FileInputStream fs = openFileInput(filePath);
			
				if (fs == null){
					return -1;
				}
				else
				if (fs.available() == 0){
					return -2;
					
				}
				else
				if ((fs != null) && (fs.available() != 0)){
					 
					byte[] buffer = new byte[fs.available()];
					
						fs.read(buffer, 0, fs.available());
						str = new String(buffer);
						Log.d("Get data frpm backup Old message", "str = " + str);
					
					fs.close();
					int start = 0, end = 0;
					while ((end = str.indexOf(Constants.END_MARK, start)) != -1){
							tempStr = str.substring(start, end);			
							//Global.mINTERNode.add(Global.GetINTERNode(tempStr, ""));
							tGmailList.add( tempStr);
							start = end + 1 ;
							str = str.substring(start);
							start = 0;
				   }
				 return 1;
			   }
			  
		  }
		  return -3;
	 }	
  
	    public int get_ACMessageList(String filePath) throws IOException {
			
			String str = null;
		    String tempStr = null; 	
		
		    
		    File file = getFileStreamPath(filePath);
		
			if  (file.exists()){	
				FileInputStream fs = openFileInput(filePath);
			
				if (fs == null){
					return -1;
				}
				else
				if (fs.available() == 0){
					return -2;
					
				}
				else
				if ((fs != null) && (fs.available() != 0)){
					 
					byte[] buffer = new byte[fs.available()];
					
						fs.read(buffer, 0, fs.available());
						str = new String(buffer);
						Log.d("Get data frpm backup Old message", "str = " + str);
					
					fs.close();
					int start = 0, end = 0;
					while ((end = str.indexOf(Constants.END_MARK, start)) != -1){
							 
							tempStr = str.substring(start, end);
						    int  keyPos  = tempStr.indexOf("@");
						    String key = tempStr.substring(0, keyPos);
						    String message = tempStr.substring(keyPos + 1);
							Global.mMap.put(key, message);  
							start = end + 1 ;
							str = str.substring(start);
							start = 0;
				   }
				 return 1;
			   }
			  
		  }
		  return -3;
	 }	
	    
  public int get_INTERMessageList(String filePath) throws IOException {
			
			String str = null;
		    String tempStr = null; 	
		
		    
		    File file = getFileStreamPath(filePath);
		
			if  (file.exists()){	
				FileInputStream fs = openFileInput(filePath);
			
				if (fs == null){
					return -1;
				}
				else
				if (fs.available() == 0){
					return -2;
					
				}
				else
				if ((fs != null) && (fs.available() != 0)){
					 
					byte[] buffer = new byte[fs.available()];
					
						fs.read(buffer, 0, fs.available());
						str = new String(buffer);
						Log.d("Get data frpm backup Old message", "str = " + str);
					
					fs.close();
					int start = 0, end = 0;
					while ((end = str.indexOf(Constants.END_MARK, start)) != -1){
							 
							tempStr = str.substring(start, end);
						    int  keyPos  = tempStr.indexOf("@");
						    String key = tempStr.substring(0, keyPos);
						    String message = tempStr.substring(keyPos + 1);
							Global.mMap.put(key, message);  
							start = end + 1 ;
							str = str.substring(start);
							start = 0;
				   }
				 return 1;
			   }
			  
		  }
		  return -3;
	 }	
		
	    public void checkForKITKAT(){
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){    
		    	  final String myPackageName = getPackageName();
		          if (!Telephony.Sms.getDefaultSmsPackage(mContext).equals(myPackageName)) {
		                      Intent intent =
		                              new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
		                      intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, 
		                              myPackageName);
		                      startActivity(intent);
		                  }
		     	 }
	 	 }
}
