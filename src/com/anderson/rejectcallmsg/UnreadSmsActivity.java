package com.anderson.rejectcallmsg;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class UnreadSmsActivity extends Activity implements UnreadSms_CustomList.customButtonListener {
	
	   /** For instance, needs to implement the followings
	      - UI for composing message
	      - Send SMS/MMS
	      - Store Sent SMS/MMS*/
	  private EditText m_compose_new_sms = null ;
	  private TextView m_character_counter = null;
	  private String text_Str = null;
	  private int turn = 0;
	  private ListView lvMsg;
	  TextView lblMsg, lblNo, m_sms_body ;
	  Context mContext;
	  RelativeLayout m_RLayout;
	  private Button mHome;
	  LinearLayout m_sms_zoom_section;
	  protected UnreadSms_CustomList m_adapter;
	  Uri SMS_INBOX_CONTENT_URI;
	  Cursor c = null;
	  LinearLayout mComposeSection;

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
	        setContentView(R.layout.new_sms);

		    mContext = this;
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
	public void onButtonOneClickListener(int position, Sms smsObj) {

		Global.unread_sms.remove(position);
		m_adapter.notifyDataSetChanged();

		Uri message = Uri.parse("content://sms");
		ContentResolver cr = getContentResolver();
		cr.delete(message, "_id=?", new String[]{smsObj.getId()});
	}

	@Override
	public void onButtonTwoClickListener(final int position,  final Sms smsObj) {

		mComposeSection =  (LinearLayout)findViewById(R.id.Compose_sms_section);
		mComposeSection.setVisibility(View.VISIBLE);
		m_character_counter = (TextView) findViewById(R.id.character_counter);
		m_compose_new_sms = (EditText) findViewById(R.id.compose_new_sms);
		m_compose_new_sms.addTextChangedListener(mTextWatcher);
		Button  m_btn_send =(Button) findViewById(R.id.btn_send);

		/** Setting an onClick event listener */
		m_btn_send.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				String text_data = Utilities.reduceSpacesInString(m_compose_new_sms.getText().toString().trim());
				SmsManager smsManager = SmsManager.getDefault();
				String sendTo = Utilities.getDigitsOnly(smsObj.getAddress());
				if (turn > 1) {
					ArrayList<String> my_text = smsManager.divideMessage(text_Str);
					smsManager.sendMultipartTextMessage(sendTo, null, my_text, null, null);
					Log.d("SMS", " ----------------  SENT SMS PARTS -----------------");
				} else {
					smsManager.sendTextMessage(sendTo, null, text_Str, null, null);
					Log.d("SMS", " ----------------  SENT SMS -----------------");
				}
				Utilities.putToast("Succesfully sent your text", 0, 300, mContext);
				Utilities.setSMSRead(mContext, smsObj.getId());
				Global.unread_sms.remove(position);
				m_adapter.notifyDataSetChanged();
				mComposeSection.setVisibility(View.INVISIBLE);

			}
		});

	}

	@Override
	public void onButtonThreeClickListener(final int position, final Sms smsObj) {
		m_sms_zoom_section = (LinearLayout) findViewById(R.id.sms_zoom_section);
		m_sms_zoom_section.setVisibility(View.VISIBLE);
		m_sms_body = (TextView) findViewById(R.id.sms_body);
		m_sms_body.setMovementMethod(new ScrollingMovementMethod());
		m_sms_body.setText(smsObj.getAddress() + "     " + smsObj.getTime() + "\n" + smsObj.getMsg());
		Utilities.setSMSRead(mContext, smsObj.getId());
		lvMsg.setVisibility(View.GONE);
		Button m_closeButton = (Button) findViewById(R.id.closeButton);
		m_closeButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				m_sms_zoom_section.setVisibility(View.GONE);
				lvMsg.setVisibility(View.VISIBLE);
			}
		});

		Button respondSMS =   (Button) findViewById(R.id.respondSMSButton);
		respondSMS.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				mComposeSection = (LinearLayout) findViewById(R.id.Compose_sms_section);
				mComposeSection.setVisibility(View.VISIBLE);
				m_character_counter = (TextView) findViewById(R.id.character_counter);
				m_compose_new_sms = (EditText) findViewById(R.id.compose_new_sms);
				m_compose_new_sms.addTextChangedListener(mTextWatcher);
				Button m_btn_send = (Button) findViewById(R.id.btn_send);

				/** Setting an onClick event listener */
				m_btn_send.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						String text_data = Utilities.reduceSpacesInString(m_compose_new_sms.getText().toString().trim());
						SmsManager smsManager = SmsManager.getDefault();
						String sendTo = Utilities.getDigitsOnly(smsObj.getAddress());
						if (turn > 1) {
							ArrayList<String> my_text = smsManager.divideMessage(text_Str);
							smsManager.sendMultipartTextMessage(sendTo, null, my_text, null, null);
							Log.d("SMS", " ----------------  SENT SMS PARTS -----------------");
						} else {
							smsManager.sendTextMessage(sendTo, null, text_Str, null, null);
							Log.d("SMS", " ----------------  SENT SMS -----------------");
						}
						Utilities.putToast("Succesfully sent your text", 0, 300, mContext);
						Utilities.setSMSRead(mContext, smsObj.getId());
						Global.unread_sms.remove(position);
						m_adapter.notifyDataSetChanged();
						mComposeSection.setVisibility(View.GONE);
						m_sms_zoom_section.setVisibility(View.GONE);
						lvMsg.setVisibility(View.VISIBLE);
					}
				});
			}
		});

	}
	public ArrayList<Sms> getUnreadSms() {
				ArrayList<Sms> lstSms = new ArrayList<Sms>();
				Sms objSms;
				Uri message = Uri.parse("content://sms/inbox");
				ContentResolver cr = getContentResolver();

				Cursor c = cr.query(message, null, "read = 0", null, null);
				startManagingCursor(c);
				int totalSMS = c.getCount();

				if (c.moveToFirst()) {
					for (int i = 0; i < totalSMS; i++) {
						Log.i("Counter : ", String.valueOf(i));
						objSms = new Sms();
						objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
						objSms.setAddress(c.getString(c
								.getColumnIndexOrThrow("address")));
						objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
						objSms.setReadState(c.getString(c.getColumnIndex("read")));
						long milliSeconds = Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")));
						DateFormat formatter = new SimpleDateFormat("MM/dd hh:mm:ss");
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(milliSeconds);
						String finalDateString = formatter.format(calendar.getTime());
						objSms.setTime(finalDateString);
						//if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
						//	objSms.setType("1");
						//} else {
						//	objSms.setType("sent");
						//}
						objSms.setType(c.getString(c.getColumnIndexOrThrow("type")));
						lstSms.add(objSms);
						c.moveToNext();
					}
				}
		        c.close();
				return lstSms;
			}

	@Override
	protected void onResume() {
				super.onResume();

				Global.unread_sms = Utilities.revertOrder(getUnreadSms());
				Log.i("Testing --------", "-------ONE--------");
				m_adapter = new UnreadSms_CustomList(this, Global.unread_sms);
				Log.i("Testing --------", "-------TWO--------");
				m_adapter.setCustomButtonListener(this);
				Log.i("Testing --------", "-------THREE--------");

				lvMsg.setAdapter(m_adapter);

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
				super.onDestroy();
	}


}
