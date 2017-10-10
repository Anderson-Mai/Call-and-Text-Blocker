package com.anderson.rejectcallmsg;

//import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;


/**
 * Responsible for maintaining the life cycle methods of activity. 
 */

public class BaseActivity extends Activity {
	/**
	 * Context object.
	 */
	public Context mContext;
	/**
	 * Resources.
	 */
	public Resources mResources;
	/**
	 * 
	 */
	public LayoutInflater mInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mResources = getResources();
		mInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		if (mContext == null) {
			mContext = getApplicationContext();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mContext == null) {
			mContext = getApplicationContext();
		}
	}

	@Override
	protected void onDestroy() {
		try {
			super.onDestroy();
			mResources = null;
			mInflater = null;
		} catch (Exception e) {
			// Do nothing
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//	}

}
