package com.anderson.rejectcallmsg;

import java.io.IOException;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NumberBlackList extends Activity implements CustomList_Two.customButtonListener {
	private int ret_val = 0;
	private final int RESULT_OK = 2;
	private final int CODE_ONE = 1;
	private ListView mList;
	private LinearLayout mListView;
	private Button mHome;
    private Button mButton_add;
	private Button mButton_DeteleAll;
    private  RelativeLayout mTopHeaderLayout = null;
	protected CustomList_Two adapter;
	private TextView mNumberLabel;
	private String phoneNumberStr = "";
	Context mContext;
	private String msgBody = null;
    private int k_pos = 0;
	private boolean on_Pause = false;

	/**
	 * Text watcher for detecting price fields changed
	 */

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numberblacklist);
        
    	mContext = this;
    	mHome =  (Button)findViewById(R.id.homeButton);	
				
    	mHome.setOnClickListener(new Button.OnClickListener(){
           	
       	 	 public void onClick(View v) {
       	 		// BackUpUSA_NumberRecords(Constants.mRejectedList);
       	 		 finish();
       	 	 }
        });
    	mTopHeaderLayout = (RelativeLayout) findViewById(R.id.topHeaderLayout);
    	
    	mButton_add =  (Button)findViewById(R.id.button_add);	
    	
    	mButton_add.setVisibility(View.GONE);
    	mButton_add.setOnClickListener(new Button.OnClickListener(){
           	
      	 	 public void onClick(View v) {
      	 		 Intent button_next_intent = new Intent ( getApplicationContext(), InsertBlackListNumber.class);
      	 		 startActivity(button_next_intent);
      	 	 }
       });

        mList = (ListView) findViewById(R.id.gmailContactInbox);
        mListView = (LinearLayout)findViewById(R.id.WrappedViewTwo);

		mButton_DeteleAll = (Button)findViewById(R.id.empty_list_button);
		mButton_DeteleAll.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				Global.mRejectedList.clear();
				adapter.notifyDataSetChanged();
				deleteFile(Constants.REJECTED_LIST_FILE);
				mButton_DeteleAll.setVisibility(View.GONE);
			}
		});
	}

	 @Override
		protected void onResume() {
			super.onResume();
			
		    try {
					Utilities.get_MessageList(mContext, Constants.MESSAGE_MAP_FILE);
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}

		    try {
				    Global.mRejectedList.clear();
					ret_val = Utilities.getListFromFile(mContext, Global.mRejectedList, Constants.REJECTED_LIST_FILE);
			} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}

			 if (Global.mRejectedList.isEmpty()) {
				 mButton_DeteleAll.setVisibility(View.GONE);
			 }
			 else{
				 mButton_DeteleAll.setVisibility(View.VISIBLE);
			 }

		    adapter = new CustomList_Two(this, Global.mRejectedList);
		    adapter.setCustomButtonListner(this);
           
		    // adapter = new ArrayAdapter<String>(this,
			//        	R.layout.simple_list_item_4, Global.mRejectedList);
		
            mList.setAdapter(adapter);
        
            mButton_add.setVisibility(View.VISIBLE);
            mButton_add.setClickable(true);
		}
	   protected void onStop() {
	        super.onStop();
	 
	   }
	
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (data != null) {
				Log.d("REQUESTED CODE", String.valueOf(requestCode));
				if (requestCode == CODE_ONE) {
					if (resultCode == RESULT_OK) {
						adapter.notifyDataSetChanged();
					}
				}
			}
		}

	@Override
	public void onButtonClickListener(int position, String value) {

		Log.d("MODIFY POS :", String.valueOf(position));
		Intent mModifier = new Intent(getApplicationContext(), ModifyBlackList.class);
		mModifier.putExtra(Constants.POSITION, position);
		startActivityForResult(mModifier, CODE_ONE);
	}

	@Override
	public void onButtonTwoClickListner(int position, String value) {

		OneNode t_node = Utilities.GetNode(Global.mRejectedList.get(position), "");
		Global.mRejectedList.remove(position);

		adapter.notifyDataSetChanged();
		Global.mMap.remove(t_node.numberStr);
		Utilities.BackUpAList(Global.mRejectedList, Constants.REJECTED_LIST_FILE, mContext);
		Utilities.BackUpMsgMap(mContext);

		if (Global.mRejectedList.isEmpty()) {
			mButton_DeteleAll.setVisibility(View.GONE);
		}
	}
}
		