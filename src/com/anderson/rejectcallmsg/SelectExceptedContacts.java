package com.anderson.rejectcallmsg;

import java.util.ArrayList;

import com.anderson.include.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SelectExceptedContacts extends Activity {
	private final int RESULT_OK = 2;
	private ListView mContactList;
	private Button m_submit_button;
	protected ArrayAdapter<String> contactListAdapter;
	Context mContext;
	private  final String TAG = "SelectPhoneContact";
	private ArrayList<String> m_exceptedNumbersList = new ArrayList<String>();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_contact_list);
        
        Intent passedContactList = getIntent();
        final String [] phoneContactList = passedContactList.getStringArrayExtra(Constants.PHONE_LIST);
    	mContext = this;
    	
    	mContactList = (ListView)findViewById(R.id.contactList);
    	contactListAdapter = new ArrayAdapter<String>(this,
		        	R.layout.simple_list_item_5, phoneContactList );
	
    	mContactList.setAdapter(contactListAdapter);	
        mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?>parent, View view, int position, long id){
				 if (phoneContactList == null){
					 Log.i(TAG, "phoneContactList is empty");
					 return;
				 }
				 m_exceptedNumbersList.add(phoneContactList[position]);
				 
				 
				 Intent returnIntent = new Intent();
		         returnIntent.putExtra(Constants.PHONE_NUMBER, phoneContactList[position]);
		         setResult(RESULT_OK, returnIntent);
		         finish();
		   }	
	   });	  
        
        m_submit_button = (Button)findViewById(R.id.submit_button);
        m_submit_button.setOnClickListener(new Button.OnClickListener(){
           	
     	 	 public void onClick(View v) {
     	 		 
     	 	     String NumbersStr = "";
     	 		 Intent returnIntent = new Intent();
     	 		 for(int i = 0; i < m_exceptedNumbersList.size(); i++){
     	 		    String tMumber = m_exceptedNumbersList + ", ";
     	 			NumbersStr += tMumber;
     	 		 }
		         returnIntent.putExtra(Constants.PHONE_LIST,NumbersStr );
		         setResult(RESULT_OK, returnIntent);
		         finish(); 
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
		