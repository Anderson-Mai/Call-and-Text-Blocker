package com.anderson.rejectcallmsg;

/**
 * Created by Anderson_Mai on 1/28/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anderson.include.Constants;
import com.anderson.include.Utilities;

import java.util.ArrayList;

public class CustomList_Three extends ArrayAdapter<String>{

    customButtonListener customListner;
    ViewHolder viewHolder;
    boolean reset_flag = true;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
        public void onButtonTwoClickListner(int position, String value);
    }
    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

  // public interface customButtonTwoListener {
  //      public void onButtonTwoClickListner(int position,String value);
  //  }
 //   public void setCustomButtonTwoListner(customButtonTwoListener listener) {
  //      this.customTwoListner = listener;
 //   }


    private ArrayList<String> data = new ArrayList<String>();
    private Context context;

    public CustomList_Three(Context context, ArrayList<String> dataItem) {
        super(context, R.layout.list_single, dataItem);
        this.context = context;
        this.data = dataItem;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater =  LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_single_three, null);
            viewHolder = new ViewHolder();

            viewHolder.msg_sect = (LinearLayout) convertView
                    .findViewById(R.id.msg_section);
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.data_txt);
            viewHolder.msg = (TextView) convertView
                    .findViewById(R.id.messageBody);
            viewHolder.button_Review = (Button) convertView
                    .findViewById(R.id.firstLetter);
            viewHolder.button_Del = (Button) convertView
                    .findViewById(R.id.deleteButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String temp = getItem(position);
        if (temp == null){
            return null;
        }
        // viewHolder.text.setText(temp.substring(0, temp.indexOf("\n",30)));

        viewHolder.text.setText(temp);
        String [] getFields = temp.split("\n");
        String phoneNumberStr = getFields[1].substring(Constants.AREA_CODE_TITLE.length());
        String yesORno = getFields[3];
        if (yesORno.contains(Constants.SMS_YES_Indicator)){
            viewHolder.msg_sect.setVisibility(View.VISIBLE);
            String msgBody = Global.mMap.get(phoneNumberStr);
            Log.i(" -------- PHONE :  ---------    ", phoneNumberStr );
            Log.i(" -------- MSG: ---------    ",  msgBody);
            viewHolder.msg.setText(msgBody);
        }
        else {
            Log.i("YesOrNo = ", "NO");
        }

        String firstLetter =  temp.substring(Constants.CITY.length(), Constants.CITY.length() + 1);
        String colorCode = Utilities.getColorForLetter(firstLetter);
        if (colorCode.contains("ANONYMOUS")){
         //   final int sdk = android.os.Build.VERSION.SDK_INT;
            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.button_Review.setBackground(context.getResources().getDrawable(R.drawable.question_mark, context.getTheme()));
                // viewHolder.in_or_out_Img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.question_mark));
            } else {
                viewHolder.button_Review.setBackground(context.getResources().getDrawable(R.drawable.question_mark));
            }
        }
        else{
            viewHolder.button_Review.setText(firstLetter);
            viewHolder.button_Review.setBackgroundColor(Color.parseColor(colorCode));
        }

        viewHolder.button_Review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, temp);
                }
            }
         });


        viewHolder.button_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (customListner != null) {
                        customListner.onButtonTwoClickListner(position, temp);
                    }
            }

        });
        return convertView;
    }

    public class ViewHolder {
        LinearLayout msg_sect;
        TextView text;
        TextView msg;
        Button button_Review;
        Button button_Del;
    }

}