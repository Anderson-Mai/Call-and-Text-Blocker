package com.anderson.rejectcallmsg;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Text watcher for detecting price fields changed
 */



public class ComposeSmsActivity extends Activity {
	
	   /** For instance, needs to implement the followings
	      - UI for composing message
	      - Send SMS/MMS
	      - Store Sent SMS/MMS*/
	  private EditText m_compose_new_sms = null ;  
	  private TextView m_character_counter = null;
	  private String text_Str = null;
	  private int turn = 0;
	      
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
	        setContentView(R.layout.compose_sms); Intent mIntent = getIntent();

		  final String smsBody = mIntent.getStringExtra(Constants.BODY);
		  final String smsAddress = mIntent.getStringExtra(Constants.ADDRESS);
		  final String smsTime = mIntent.getStringExtra(Constants.DATE);

		   // checkForKITKAT();


	        Log.i("I AM IN COMPOSE---", "------------------------");
	        if (Global.NotifyManager != null){
				Global.NotifyManager.cancelAll();
			}
	        m_character_counter = (TextView) findViewById(R.id.character_counter);
	        
	        TextView  m_recentMsg = (TextView) findViewById(R.id.recentMsg);
	        m_recentMsg.setText("\n" + Global.recent_sms_body);
		    m_recentMsg.setMovementMethod(new ScrollingMovementMethod());
	        
	        m_compose_new_sms = (EditText) findViewById(R.id.compose_new_sms);
	        m_compose_new_sms.addTextChangedListener(mTextWatcher);
	        
	        TextView m_recentMsgTime = (TextView) findViewById(R.id.recent_sms_time);
	        String timeStamp = new SimpleDateFormat("MMM dd yyyy  HH:mm:ss").format(Calendar.getInstance().getTime());
	        m_recentMsgTime.setText(timeStamp);
	        
	        /** Getting a reference to the button "btn_compose" of the main.xml */
	        Button btn_compose = (Button) findViewById(R.id.btn_compose);
	        btn_compose.setText(Global.recent_sender);
	        
	        /** Setting an onClick event listener */
	        btn_compose.setOnClickListener(new Button.OnClickListener(){
	      	 	 public void onClick(View v) {
	      	 	  Intent intent = new Intent("android.intent.action.VIEW");
	      		 
	                /** creates an sms uri */
	                Uri data = Uri.parse("sms:");
	 
	                /** Setting sms uri to the intent */
	                intent.setData(data);
	 
	                /** Initiates the SMS compose screen, because the activity contain ACTION_VIEW and sms uri */
	                startActivity(intent);
	      	 	 }
	    });
	        
	     Button m_btn_send = (Button) findViewById(R.id.btn_send);
	    
	     /** Setting an onClick event listener */
	     m_btn_send.setOnClickListener(new Button.OnClickListener(){
	      	 	 public void onClick(View v) {
	      	 	
				    SmsManager smsManager = SmsManager.getDefault();
				    if(turn > 1){
				    	ArrayList<String> my_text =  smsManager.divideMessage(text_Str);
				    	smsManager.sendMultipartTextMessage(Global.recent_sender, null, my_text, null, null);
				    	Log.d("SMS", " ----------------  SENT SMS PARTS -----------------");
				    }
				    else {
							smsManager.sendTextMessage(Global.recent_sender, null,text_Str , null, null);
							Log.d("SMS", " ----------------  SENT SMS -----------------");
				    }
					 Utilities.putToast("Succesfully sent your text", 0, 300, getApplicationContext());
					 finish();
	      	 	 }
	     });
	       
	}


	public void checkForKITKAT(){
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
			final String myPackageName = getPackageName();
			if (!Telephony.Sms.getDefaultSmsPackage(getApplicationContext()).equals(myPackageName)) {
				Intent intent =
						new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
				intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
						myPackageName);
				startActivity(intent);
			}
		}
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
				long milliSeconds =  Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")));
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
		// else {
		// throw new RuntimeException("You have no SMS");
		// }
		//c.close();

		return lstSms;
	}
	@Override
	protected void onResume() {
		super.onResume();
		Global.unread_sms =  getUnreadSms();
		Log.i("Testing --------", "-------ONE--------");
		//m_adapter = new Sms_CustomList(this, Global.all_sms  );
		Log.i("Testing --------", "-------TWO--------");
	//	m_adapter.setCustomButtonListner(this);
		Log.i("Testing --------", "-------THREE--------");

		//lvMsg.setAdapter(m_adapter);

	}
}
