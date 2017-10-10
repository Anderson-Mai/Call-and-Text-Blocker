package com.anderson.rejectcallmsg;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anderson.include.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//import android.widget.SimpleCursorAdapter;

/**
 * Text watcher for detecting price fields changed
 */

public class SmsSingleAccount extends Activity implements Sms_CustomList.customButtonListener {
	
	   /** For instance, needs to implement the followings
	      - UI for composing message
	      - Send SMS/MMS
	      - Store Sent SMS/MMS*/
	  private EditText m_compose_new_sms = null ;
	  private TextView m_character_counter = null;
	  private String text_Str = null;
	  private int turn = 0;
	  private ListView lvMsg;
	  TextView lblMsg, lblNo;
	  Context mContext;
	  RelativeLayout m_Compose_sms_section;
	  RelativeLayout m_RLayout;
	  private Button mHome;
	  private Button mText;
	  protected Sms_CustomList m_adapter;
	  Uri SMS_INBOX_CONTENT_URI;
	  Cursor c = null;
	  String m_address = "";
	  String m_name = "";

	  String [] m_phoneContactList = null;
	  String [] m_rejectList = null;
	private TextWatcher mTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			// character_counter
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
			// TODO Auto-generated method stub
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			text_Str = m_compose_new_sms.getText().toString().trim();
			int len = text_Str.length() % 160;
			turn = (text_Str.length() / 160) + 1;
			int textRemain = 160 - len ;
			String full_counter = String.valueOf(textRemain) + "/" + String.valueOf(turn);
			m_character_counter.setText(full_counter);
		}
	};
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.sms_single_account);

		    mContext = this;
            Intent m_intent = getIntent();
		    m_address = m_intent.getStringExtra("ADDRESS");
		    m_name = m_intent.getStringExtra("NAME");
          //  Log.i("ADDRESS:     ", m_address);
		    m_character_counter = (TextView) findViewById(R.id.character_counter);
		    m_compose_new_sms = (EditText) findViewById(R.id.compose_new_sms);
		    m_compose_new_sms.addTextChangedListener(mTextWatcher);

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

		  mText =  (Button)findViewById(R.id.send_text_Button);
		  mText.setOnClickListener(new Button.OnClickListener() {

			  public void onClick(View v) {
				  m_Compose_sms_section = (RelativeLayout) findViewById(R.id.wrapper);
				  m_Compose_sms_section.setVisibility(View.VISIBLE);
				  Button m_btn_send = (Button) findViewById(R.id.button_send);

				  /** Setting an onClick event
				   *  */
				  m_btn_send.setOnClickListener(new Button.OnClickListener() {
					  public void onClick(View v) {

						  SmsManager smsManager = SmsManager.getDefault();
						  if (turn > 1) {
							  ArrayList<String> my_text = smsManager.divideMessage(text_Str);
							  smsManager.sendMultipartTextMessage(Utilities.getDigitsOnly(m_address), null, my_text, null, null);
							  Utilities.putToast(" SENT SMS PARTS", 0, 300, getApplicationContext());
							  Log.d("SMS", " ----------------  SENT SMS PARTS -----------------");
						  } else {
							  smsManager.sendTextMessage(Utilities.getDigitsOnly(m_address), null, text_Str, null, null);
							  Utilities.putToast(" SENT ONE PART", 0, 300, getApplicationContext());
						  }
						 // Utilities.putToast("Succesfully sent your text", 0, 300, getApplicationContext());
						  m_Compose_sms_section.setVisibility(View.GONE);
					  }
				  });
			  }
		  });

		  TextView mHeader =  (TextView)findViewById(R.id.header);
		  mHeader.setText(m_name + "  " + m_address);
	}
	@Override
	public void onButtonDelClickListener(int position, Sms smsObj) {

		Global.all_sms.remove(position);
		m_adapter.notifyDataSetChanged();

		Uri message = Uri.parse("content://sms");
		ContentResolver cr = getContentResolver();
		Log.i("ID:    ", smsObj.getId());
		cr.delete(message, "_id=?", new String[]{smsObj.getId()});
	}

	@Override
	public void onButtonAddress_TextClickListener(String address) {
		//check if it is a name or an address.
		// If it is a name, look for its number and call intent to make a call.
		// Otherwise, make a call

	}

	@Override
	public void onButtonOptionClickListener(int position, Sms smsObj) {

		/*Global.all_sms.remove(position);
		if (Global.all_sms.isEmpty()) {
		//	mButton_DeteleAll.setVisibility(View.GONE);
		}
		m_adapter.notifyDataSetChanged();
	//	Utilities.BackUpAList(Global.mWhiteList, Constants.WHITE_LIST_FILE, mContext);
	*/
	}

	/* @Override
	public void onButtonTwoClickListner(Sms smsObj) {

		Intent smsIntent = new Intent(mContext, ComposeSmsActivityInMenu.class);
		smsIntent.putExtra(Constants.ADDRESS, smsObj.getAddress());
		smsIntent.putExtra(Constants.BODY, smsObj.getMsg());
		smsIntent.putExtra(Constants.DATE, smsObj.getTime());
		startActivity(smsIntent);
	}

    */
	public ArrayList<Sms> getSMSForPerson() {
		if (m_address == null){
			return null;
		}
		ArrayList<Sms> lstSms = new ArrayList<Sms>();
		Sms objSms;
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
						objSms = new Sms();
						objSms.setAddress(infile_address);
						Log.i("ADDRESS IN FILE :    ", objSms.getAddress());
						objSms.setName(m_address, m_phoneContactList, m_rejectList);
						objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
						Log.i("ID:    ", objSms.getId());
						objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
						objSms.setReadState(c.getString(c.getColumnIndex("read")));
						long milliSeconds = Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")));
						DateFormat formatter = new SimpleDateFormat("MM/dd hh:mm:ss");
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(milliSeconds);
						String finalDateString = formatter.format(calendar.getTime());
						objSms.setTime(finalDateString);
						objSms.setType(c.getString(c.getColumnIndexOrThrow("type")));
						Log.i("TYPE:    ", objSms.getType());
						lstSms.add(objSms);
					}
				    else {
						Log.i("NOT MATCH ADDRESS :    ", infile_address);
					}
			}while (c.moveToNext());
		}

		return lstSms;
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i("I AM IN ", "--------------- BUTTON VIEW - MAIN   ON RESUME____");
		Global.all_sms =  Utilities.revertOrder(getSMSForPerson());

		Log.i("Testing --------", "-------ONE--------");
		m_adapter = new Sms_CustomList(this, Global.all_sms  );
		Log.i("Testing --------", "-------TWO--------");
		m_adapter.setCustomButtonListner(this);
		Log.i("Testing --------", "-------THREE--------");
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




}
