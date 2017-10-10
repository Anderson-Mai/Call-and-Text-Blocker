package com.anderson.rejectcallmsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.widget.ListView;

public class ValidateAreaCode{
	 
	 ValidateAreaCode(String areaCode){
	}
		
	protected boolean readAndCheck(int rawFileId, String mAreaCodeStr, Context mContext ){
		     int  mAreaCodeInt =  Integer.parseInt(mAreaCodeStr);
			 InputStream inputStream = mContext.getResources().openRawResource(rawFileId);

             InputStreamReader inputreader = new InputStreamReader(inputStream);
             BufferedReader buffreader = new BufferedReader(inputreader);
              
             String line = "";
              
             try {
                while (( line = buffreader.readLine()) != null) {
                	 String temp_AreaCodeStr =   line.substring(0, 4);
                	
                     if (temp_AreaCodeStr.contains(mAreaCodeStr)){
                    	 return true;
                     }
                     else if (  Integer.parseInt(temp_AreaCodeStr) > mAreaCodeInt){
                    	 return false;	 
                     }
                     line = "";
                }
            } catch (IOException e) {
            	return false;
            }	
             return false;
		}
			
}
