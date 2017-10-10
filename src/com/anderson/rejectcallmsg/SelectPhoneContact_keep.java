package com.anderson.rejectcallmsg;

import java.util.ArrayList;

import com.anderson.include.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SelectPhoneContact_keep extends Activity {
	private final int RESULT_OK = 2;
	private ListView mContactList;
	protected ArrayAdapter<String> contactListAdapter;
	protected  ArrayList<String> contactList = new ArrayList<String>();
	Context mContext;
	private Button mbackButton = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_contact_list);
        
        Intent passedContactList = getIntent();
        final String [] phoneContactList = passedContactList.getStringArrayExtra("PHONE_CONTACT_LIST");
    	mContext = this;
    	
    	mbackButton= (Button) findViewById(R.id.submit_button);
    	mbackButton.setOnClickListener(new Button.OnClickListener(){
           	
      	 	 public void onClick(View v) {
      	 		 Intent returnIntent = new Intent();
		         returnIntent.putExtra(Constants.PHONE_LIST, contactList);
		         setResult(RESULT_OK, returnIntent);
		         finish();
			}
	});
    	
    	mContactList = (ListView)findViewById(R.id.contactList);
    	contactListAdapter = new ArrayAdapter<String>(this,
		        	R.layout.simple_list_item_5, phoneContactList );
	
    	mContactList.setAdapter(contactListAdapter);	
        mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?>parent, View view, int position, long id){
				contactList.add(phoneContactList[position]);
		   }	
	   });	  	   		
	}
		
	 @Override
		protected void onResume() {
		  super.onResume();
		}
	 
	 protected void onStop() {
	        super.onStop();
	    }
	}
		