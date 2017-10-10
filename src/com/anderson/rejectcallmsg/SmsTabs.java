

package com.anderson.rejectcallmsg;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

// import com.phonepaybeta.util.Utils;

/**
 * Responsible for: Displaying default account screen.
 * 
 * @author Anderson Mai
 * 
 */
public class SmsTabs extends TabActivity{
	TabHost tabHost;
	TabHost.TabSpec tabs;
	Intent intent;
	Resources res;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.sub_tabs_layout);
		 
		 this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 tabHost = getTabHost();

		res = getResources();

		// TabHost tabHost = getTabHost();
	   ///  TabHost.TabSpec tabs;
	   //  Intent intent;
	   //  Resources res = getResources();
	     
	     View view1 = createTabView(tabHost.getContext(), "All SMS");
	     intent = new Intent().setClass( this, SmsSingleAccount.class);
	     //intent = new Intent().setClass( this, UnreadSMSprocess.class);
	 	 tabs =tabHost.newTabSpec("Options").setIndicator(view1).setContent(intent);
		 tabHost.addTab(tabs);

	     View view2 = createTabView(tabHost.getContext(), "Unread SMS");
		 intent = new Intent().setClass( this, UnreadSmsActivity.class);
		 tabs = tabHost.newTabSpec("Set Up").setIndicator(view2).setContent(intent);
	     tabHost.addTab(tabs);

		 View view3 = createTabView(tabHost.getContext(), "New SMS");
		 intent = new Intent().setClass( this,  UnreadSmsActivity.class);
	 	 tabs =tabHost.newTabSpec("Notifier").setIndicator(view3).setContent(intent);
		 tabHost.addTab(tabs);

		 setTabColor(tabHost);
	     tabHost.setCurrentTab(0) ;   
		 tabHost.getCurrentTab();

	}	
	
	@Override
	protected void onResume() {
		super.onResume();


	}
	
	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setTextSize(18);
		tv.setText(text);
		return view;
	}
	
	private void setTabColor(TabHost tabhost) {  
		for(int i=0;i<tabhost.getTabWidget().getChildCount();i++) {      
			// Global.tabselected = i;
			switch(i){
				case 0:
					tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab1updown);					
					break;
				case 1:
					tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab2updown);
					break;
				case 2:
					tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab3updown);
					//tabhost.getTabWidget().getChildAt(i).setBackgroundResource(0x7f020091);
					break;
		   }
		}
    } 

	    
}

