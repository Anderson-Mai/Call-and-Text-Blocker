package com.anderson.rejectcallmsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SelectFromAreaCodeList extends Activity{

	 private ListView  mlistView = null;
	 private ArrayAdapter<String> adapter = null;
	 private Button mbutton_back = null;
	 public static ArrayList<String> mUSA_AreaCodeList = new ArrayList<String>();
	 
	 @Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				
				 setContentView(R.layout.selectfromareacodelist);
				 openAndRead(mUSA_AreaCodeList, R.raw.areacode);
				 adapter = new ArrayAdapter<String>(this,
							        	R.layout.simple_list_item_5, mUSA_AreaCodeList);
				 mbutton_back = (Button)findViewById(R.id.button_back);
				 mbutton_back.setOnClickListener(new Button.OnClickListener(){
			           	public void onClick(View v) {
			           		finish();
			           	}
			     });
				 
				 mlistView = (ListView)findViewById(R.id.symbolsearch);
				 mlistView.setAdapter(adapter);
				 
				 mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?>parent, View view, int position, long id){
							String selected_Str = mUSA_AreaCodeList.get(position);
							String selected_AreaCodeStr  = selected_Str.substring(0,3);
							String selected_CityStr  = selected_Str.substring(selected_Str.indexOf("-") + 2);
							if (!Utilities.checkDuplicatedAreaCode(selected_AreaCodeStr, Global.mAreaCodeRejectedList, getApplicationContext())) {
								String FullString = Constants.CITY + selected_CityStr + "\n"+ Constants.AREA_CODE_TITLE +
										selected_AreaCodeStr + "\n" + FormMonthDateYearStr("01", "01", "2019") + "\n" + Constants.SMS_NO_Indicator + "\n" + Constants.SMS_NO_OPTION;
								Utilities.insertInOrder(Global.mAreaCodeRejectedList, FullString);
								Utilities.putToast(selected_AreaCodeStr + "is added to Area Code BlackList", 0, 400, getBaseContext());
								Utilities.BackUpAList(Global.mAreaCodeRejectedList, Constants.AREA_CODE_REJECTED_LIST_FILE, getApplicationContext());
								Intent ret_intent = new Intent();
								ret_intent.putExtra(Constants.PHONE_NUMBER, selected_AreaCodeStr);
								setResult(RESULT_OK, ret_intent);
								finish();
							}
						}
				 });	 
		 }
		
		protected void openAndRead( ArrayList<String> symbolFile, int rawFileId ){
		   
			 InputStream inputStream = getApplicationContext().getResources().openRawResource(rawFileId);

             InputStreamReader inputreader = new InputStreamReader(inputStream);
             BufferedReader buffreader = new BufferedReader(inputreader);
              
             String line = "";
              
              try {
                while (( line = buffreader.readLine()) != null) {
                	/* int first_coma =  line.indexOf(coma);
                	// Log.d("FIRST COMMA: ", String.valueOf(first_coma));
                	 int second_coma =  line.substring(first_coma + 1).indexOf(coma) + 1;
                	// Log.d("SECOND COMMA: ", String.valueOf(first_coma + second_coma + 2));
                	 String compName = line.substring(0, first_coma - 1);
                     String symbol = line.substring(first_coma + 1, first_coma + second_coma + 1);
                     String fString = symbol + "  " + compName;
                    // Log.d("FULL STRING ", fString);
                     
                     */
                     symbolFile.add(line);
                     line = "";
                   
                  }
            } catch (IOException e) {
            	return;
            }		
		}
		
		@Override
		protected void onResume() {
			super.onResume();
		}

	public String FormMonthDateYearStr(String monthStr, String dateStr, String yearStr){

		String formedString = "Block until : " + monthStr + "/" + dateStr + "/" + yearStr;

		return formedString;

	}
			
}
