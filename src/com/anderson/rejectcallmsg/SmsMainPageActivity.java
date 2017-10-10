package com.anderson.rejectcallmsg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//import android.widget.SimpleCursorAdapter;

/**
 * Text watcher for detecting price fields changed
 */

public class SmsMainPageActivity extends Activity implements Sms_MainPage.customButtonListener {
	
	   /** For instance, needs to implement the followings
	      - UI for composing message
	      - Send SMS/MMS
	      - Store Sent SMS/MMS*/
	  private EditText m_compose_new_sms = null ;
	  private TextView m_character_counter = null;
	  private String text_Str = null;
	  private int turn = 0;
	  private SimpleCursorAdapter adapter;
	  private ListView lvMsg;
	  TextView lblMsg, lblNo;
	  Context mContext;
	  RelativeLayout m_RLayout;
	  private Button mHome;
	  protected Sms_MainPage m_adapter;
	  Uri SMS_INBOX_CONTENT_URI;
	  Cursor c = null;
	  String [] m_phoneContactList = null;
	  String [] m_rejectList = null;
	   
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.sms_in_menu);

		    mContext = this;

		    m_phoneContactList = Utilities.getPhoneContactList(mContext);
		    m_rejectList = Utilities.getPhoneRejectList(mContext);

	  	    lvMsg = (ListView) findViewById(R.id.lvMsg);

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


	@Override
	public void onButtonDelClickListener(final int position, Sms smsObj) {

		confirnDeleting(smsObj, position);

		//Intent mIntent = new Intent();
		//mIntent.putExtra("REFRESH", position );
		//setResult(Activity.RESULT_OK, mIntent);
		//finish();//	m_adapter = new Sms_MainPage(mContext, Global.all_inbox  );
		//m_adapter.setCustomButtonListner(mContext);
		//	lvMsg.setAdapter(m_adapter);
		//m_adapter.notifyDataSetChanged();
		//finish();


	}

	@Override
	public void onButtonProcessClickListener(int position, Sms smsObj) {

		Intent processIntent = new Intent(mContext, SmsSingleAccount.class );
        startActivity(processIntent);
	}

	@Override
	public void onButtonViewClickListener(String address, String  name) {

		Utilities.putToast("-- BUTTON VIEW - MAIN  _",0, 300, mContext);
		Intent viewIntent = new Intent(mContext, SmsSingleAccount.class );
		viewIntent.putExtra("ADDRESS", address );
		viewIntent.putExtra("NAME", name );
		startActivity(viewIntent);
	}

	@Override
	public void onButtonAddressClickListener(String address) {
		// Call intent to make a call
	}

	public ArrayList<Sms> getAllInbox() {
		ArrayList<Sms> lstSms = new ArrayList<Sms>();
		Sms objSms;
		Uri message = Uri.parse("content://sms/inbox");
		ContentResolver cr = getContentResolver();

		c = cr.query(message, null, null, null, null);
		startManagingCursor(c);
		if (c.moveToFirst()) {
			do {
				objSms = new Sms();
				String m_address = c.getString(c
						.getColumnIndexOrThrow("address"));
				boolean duplicate = false;
				for (int ii = 0; ii < lstSms.size(); ii++){
					if (m_address.contains(lstSms.get(ii).getAddress()) ||
							lstSms.get(ii).getAddress().contains(m_address)) {
						duplicate = true;
						break;
					}
				}
				if (!duplicate) {
					 objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
					 objSms.setAddress(m_address);
					 objSms.setName(m_address, m_phoneContactList, m_rejectList);
					 objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
					 objSms.setReadState(c.getString(c.getColumnIndex("read")));
					 long milliSeconds = Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")));
					 DateFormat formatter = new SimpleDateFormat("MM/dd hh:mm:ss");
					 Calendar calendar = Calendar.getInstance();
					 calendar.setTimeInMillis(milliSeconds);
					 String finalDateString = formatter.format(calendar.getTime());
					 objSms.setTime(finalDateString);
					 objSms.setType(c.getString(c.getColumnIndexOrThrow("type")));
					 lstSms.add(objSms);
				 }
			}while (c.moveToNext());
		}

		return lstSms;
	}


	@Override
	protected void onResume() {
		super.onResume();
		Global.all_inbox =  getAllInbox();
		m_adapter = new Sms_MainPage(this, Global.all_inbox  );
		m_adapter.setCustomButtonListner(this);
		lvMsg.setAdapter(m_adapter);

	}
	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		c.close();
	}

	public void confirnDeleting(final Sms smsObj, final int pos){
		String nName_Address = smsObj.getName();
		final String n_Address = smsObj.getAddress();

		if (nName_Address.contains(Constants.anonymous)){
			nName_Address = smsObj.getAddress();
		}

		AlertDialog.Builder alertbox = new AlertDialog.Builder(SmsMainPageActivity.this);
				alertbox.setTitle("Delete all sms for " + nName_Address);
					alertbox.setIcon(R.drawable.track_container);
					alertbox.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							deleteAllForAddress(Utilities.getDigitsOnly(n_Address));
							Utilities.putToast("All sms belong to this account has been removed", 0, 300, mContext);
							Global.all_inbox.remove(pos);
							m_adapter.notifyDataSetChanged();
						}
					});

					alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							Utilities.putToast("You cancelled the delete", 0, 300, mContext);
							return;
						}
					});
		alertbox.show();
	}

	public void deleteAllForAddress(String m_address) {
		if (m_address == null){
			return;
		}
		Uri message = Uri.parse("content://sms/");
		ContentResolver cr = getContentResolver();
		c = cr.query(message, null, null, null, null);
		startManagingCursor(c);
		if (c.moveToFirst()) {
			do {

				String infile_address = c.getString(c
						.getColumnIndexOrThrow("address"));
				if ((infile_address == null) || (infile_address.isEmpty())){
					continue;
				}
				if (m_address.contains(Utilities.getDigitsOnly(infile_address))) {
					//objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
					cr.delete(message, "_id=?", new String[]{c.getString(c.getColumnIndexOrThrow("_id"))});
					Log.i("MATCH ADDRESS- DELETE :    ", infile_address);
				}
				else {
					Log.i("NOT MATCH ADDRESS :    ", infile_address);
				}
			}while (c.moveToNext());
		}
	}

}
