package com.anderson.rejectcallmsg;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.anderson.include.Constants;
import com.anderson.include.Utilities;
import com.anderson.rejectcallmsg.Global;
import com.anderson.rejectcallmsg.InsertBlackListNumber;
import com.anderson.rejectcallmsg.ModifyBlackList;
import com.anderson.rejectcallmsg.OneNode;
import com.anderson.rejectcallmsg.R;
import com.anderson.rejectcallmsg.R.color;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReviewRecord extends Activity {
    private int ret_val = 0;
    private final int RESULT_OK = 2;
    private final int CODE_ONE = 1;
    private ListView mGmailInbox;
    private LinearLayout mListView;
    private Button mButton_add;
    private Button mButton_back;

    private LinearLayout eEditViewer = null;
    private TextView mMessageBody = null;
    private RelativeLayout mModifyHeader = null;
    private  RelativeLayout mTopHeaderLayout = null;
    private TextView mNumberLabel;
    private String phoneNumberStr = "";
    Context mContext;
    private String msgBody = null;
    private int k_pos = 0;
    private int blacklist_type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_record);

        mContext = this;
        Intent received_intent = getIntent();
        k_pos = received_intent.getIntExtra(Constants.POSITION, -1);
        blacklist_type = received_intent.getIntExtra(Constants.BLACKLIST_TYPE, -1);

        eEditViewer = (LinearLayout)findViewById(R.id.EditViewer);
        String fullStr = null;
        switch(blacklist_type){

            case 0: fullStr  = Global.mWhiteList.get(k_pos);
                break;
            case 1: fullStr  = Global.mRejectedList.get(k_pos);
                break;
            case 2: fullStr = Global.mAreaCodeRejectedList.get(k_pos);
                break;
            case 3: fullStr = Global.mInternationalRejectedList.get(k_pos);
                break;
        }

        mNumberLabel = (TextView) findViewById(R.id.numberLabel);
        mNumberLabel.setText(fullStr);
        mMessageBody = (TextView) findViewById(R.id.messageBody);
        String [] getFields = fullStr.split("\n");

        phoneNumberStr = getFields[1].substring(Constants.NUMBER_TITLE.length());
        String yesORno = getFields[3];
        Log.i("YES OR NO :  ", yesORno);
        if (yesORno.contains(Constants.SMS_YES_Indicator)){
            mMessageBody.setVisibility(View.VISIBLE);
            Log.i("YES OR NO :  ", "I COME TO YES");
            msgBody = Global.mMap.get(phoneNumberStr);
            mMessageBody.setText(msgBody);
        }
        else {
            mMessageBody.setVisibility(View.GONE);
            Log.i("YES OR NO :  ", "I COME TO YES");
        }

        mModifyHeader = (RelativeLayout)findViewById(R.id.modify_header);
        mTopHeaderLayout = (RelativeLayout) findViewById(R.id.topHeaderLayout);

        Button mButton_back = (Button) findViewById(R.id.okButton);
        mButton_back.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        //   mRejectedList = new ArrayList<String>();

        Button mModifier = (Button) findViewById(R.id.modifyButton);
        mModifier.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v) {

                Log.d("MODIFY POS :", String.valueOf(k_pos));
                Intent mModifyIntent = null;
                switch(blacklist_type){
                    case 0 :
                        break;
                    case 1:  mModifyIntent = new Intent(getApplicationContext(), ModifyBlackList.class);
                        break;
                    case 2: mModifyIntent = new Intent(getApplicationContext(), ModifyAreaCodeBlackList.class);
                        break;
                    case 3: mModifyIntent = new Intent(getApplicationContext(), Modify_World_BlackList.class);
                        break;
                }

                mModifyIntent.putExtra(Constants.POSITION, k_pos);
                startActivityForResult(mModifyIntent, CODE_ONE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.d("REQUESTED CODE",  String.valueOf(requestCode));
            if (requestCode == CODE_ONE) {
                if (resultCode == RESULT_OK) {
                    finish();
                }
            }
        }
    }
}
