package com.anderson.rejectcallmsg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

//import android.widget.SimpleCursorAdapter;

/**
 * Text watcher for detecting price fields changed
 */



public class ComposeSmsActivityInMenu extends Activity {
	
	   /** For instance, needs to implement the followings
	      - UI for composing message
	      - Send SMS/MMS
	      - Store Sent SMS/MMS*/
	  private EditText m_compose_new_sms = null ;  
	  private TextView m_character_counter = null;
	  private TextView m_recent_msg ;
	  private String text_Str = null;
	  private String date_Str = null;
	  private int turn = 0;
	  Context mContext;

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
	        setContentView(R.layout.compose_sms_in_menu);

		    mContext = this;
		    Intent mIntent = getIntent();
		    final String smsBody = mIntent.getStringExtra(Constants.BODY);
		    final String smsAddress = mIntent.getStringExtra(Constants.ADDRESS);
		    final String smsTime = mIntent.getStringExtra(Constants.DATE);
		    m_character_counter = (TextView) findViewById(R.id.character_counter);
	        m_compose_new_sms = (EditText) findViewById(R.id.compose_new_sms);
	        m_compose_new_sms.addTextChangedListener(mTextWatcher);
		    TextView m_recentMsg = (TextView) findViewById(R.id.recentMsg);
		    m_recentMsg.setText(smsBody);
		    m_recentMsg.setMovementMethod(new ScrollingMovementMethod());
		    TextView m_dateStr = (TextView) findViewById(R.id.dateStr);
		    m_dateStr.setText(smsAddress + "         " +smsTime);
	       // TextView m_recentMsgTime = (TextView) findViewById(R.id.recent_sms_time);
	       //  String timeStamp = new SimpleDateFormat("MMM dd yyyy  HH:mm:ss").format(Calendar.getInstance().getTime());
	       //  m_recentMsgTime.setText(timeStamp);

	       Button  m_btn_send =(Button) findViewById(R.id.btn_send);

	       /** Setting an onClick event listener */
	       m_btn_send.setOnClickListener(new Button.OnClickListener(){
	      	 	 public void onClick(View v) {
					 String text_data = Utilities.reduceSpacesInString(m_compose_new_sms.getText().toString().trim());
				    SmsManager smsManager = SmsManager.getDefault();
					 String sendTo = Utilities.getDigitsOnly(smsAddress);
				    if(turn > 1){
				    	ArrayList<String> my_text =  smsManager.divideMessage(text_Str);
				    	smsManager.sendMultipartTextMessage(sendTo, null, my_text, null, null);
				    	Log.d("SMS", " ----------------  SENT SMS PARTS -----------------");
				    }
				    else {
							smsManager.sendTextMessage(sendTo, null,text_Str , null, null);
							Log.d("SMS", " ----------------  SENT SMS -----------------");
				    }
					 Utilities.putToast("Succesfully sent your text", 0, 300, mContext);
					 finish();
	      	 	 }
	     });

	}


}
