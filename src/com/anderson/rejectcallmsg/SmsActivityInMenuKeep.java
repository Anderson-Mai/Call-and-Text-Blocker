package com.anderson.rejectcallmsg;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import android.widget.SimpleCursorAdapter;

/**
 * Text watcher for detecting price fields changed
 */



public class SmsActivityInMenuKeep extends Activity {
	
	   /** For instance, needs to implement the followings
	      - UI for composing message
	      - Send SMS/MMS
	      - Store Sent SMS/MMS*/ private EditText m_compose_new_sms = null ;
	  private TextView m_character_counter = null;
	  private String text_Str = null;
	  private int turn = 0;
	  private SimpleCursorAdapter adapter;
	  private ListView lvMsg;
	  TextView lblMsg, lblNo;
	  Context mContext;
	  RelativeLayout m_RLayout;
	  private Button mHome;
	   
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.sms_in_menu);


		    mContext = this;
	  	    lvMsg = (ListView) findViewById(R.id.lvMsg);

		    int[] to = new int[] { R.id.msg_Address, R.id.msg_Body, R.id.msg_Date };
		  // Create Inbox box URI
		 	 Uri inboxURI = Uri.parse("content://sms/inbox");

			// List required columns
		 	 String[] reqCols = new String[] {"address","body", "date", "_id"};

		// Get Content Resolver object, which will deal with Content Provider
		 	 ContentResolver cr = getContentResolver();

			// Fetch Inbox SMS Message from Built-in Content Provider
		 	 Cursor c = cr.query(inboxURI, reqCols, null, null, null);
		  // Attached Cursor with adapter and display in listview

			  adapter = new SimpleCursorAdapter(this, R.layout.row, c, reqCols , to, 0 );
			  lvMsg.setAdapter(adapter);


		      lvMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					  Cursor c = (Cursor) parent.getAdapter().getItem(position);
					  String sendTo = Utilities.getDigitsOnly(c.getString(c.getColumnIndex("address")));
					  String sms_body = c.getString(c.getColumnIndex("body"));
                      long milliSeconds =  Long.parseLong(c.getString(c.getColumnIndex("date")));
					  DateFormat formatter = new SimpleDateFormat("MM/dd hh:mm:ss");
					  Calendar calendar = Calendar.getInstance();
					  calendar.setTimeInMillis(milliSeconds);
					  String finalDateString = formatter.format(calendar.getTime());
					  Intent smsIntent = new Intent(mContext, ComposeSmsActivityInMenu.class);
					  smsIntent.putExtra(Constants.ADDRESS, sendTo);
					  smsIntent.putExtra(Constants.BODY, sms_body);
					  smsIntent.putExtra(Constants.DATE, finalDateString);
					  startActivity(smsIntent);
				  }
			  });



		    if (Global.NotifyManager != null) {
				Global.NotifyManager.cancelAll();
			}

		   mHome =  (Button)findViewById(R.id.homeButton);
		   mHome.setOnClickListener(new Button.OnClickListener() {

			  public void onClick(View v) {
				  // BackUpUSA_NumberRecords(Constants.mRejectedList);
				  finish();
			  }
		  });
	}

}
