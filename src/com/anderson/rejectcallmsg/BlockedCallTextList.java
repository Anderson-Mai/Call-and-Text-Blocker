package com.anderson.rejectcallmsg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BlockedCallTextList extends Activity {
	private final int RESULT_OK_SELECT = 2;
	private ListView mBlockedList;
	private Button mBackButton;
	private Button mButton_DeteleAll;
	protected ArrayAdapter<String> blockedListAdapter;
	Context mContext;
	private  final String TAG = "BlockedCallTextList";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blocked_call_text_list);

		mContext = this;
		mBackButton = (Button)findViewById(R.id.backButton);
		mBackButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		mButton_DeteleAll = (Button) findViewById(R.id.empty_list_button);
		mButton_DeteleAll.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Global.mBlockedList.clear();
				blockedListAdapter.notifyDataSetChanged();
				Utilities.BackUpAList(Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE, mContext);
				mButton_DeteleAll.setVisibility(View.GONE);
			}
		});


		Global.mBlockedList.clear();
		try {
			Utilities.getListFromFile(mContext, Global.mBlockedList, Constants.BLOCKED_CALL_SMS_RECORD_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (Global.mBlockedList == null) {
			Utilities.putToast("No blocked call or text.", 0, 300, mContext);
			return;
		}

		mBlockedList = (ListView) findViewById(R.id.blockedOneList);
		blockedListAdapter = new ArrayAdapter<String>(this,
				R.layout.simple_list_item_5, Global.mBlockedList);

		mBlockedList.setAdapter(blockedListAdapter);
		if (!Global.mBlockedList.isEmpty()) {
			mButton_DeteleAll.setVisibility(View.VISIBLE);
		} else {
			mButton_DeteleAll.setVisibility(View.GONE);
		}
	}
		@Override
		protected void onResume(){
		  super.onResume();
		}
	 
	 protected void onStop()
	{
	        super.onStop();
	}


}