package com.anderson.rejectcallmsg;

/**
 * Created by Anderson_Mai on 1/28/2016.
 */

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anderson.include.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Sms_CustomList extends ArrayAdapter<Sms>{

    customButtonListener customListner;
    ViewHolder viewHolder;
    boolean reset_flag = true;

    public interface customButtonListener {
        public void onButtonOptionClickListener(int position, Sms smsObj);
        public void onButtonDelClickListener(int position, Sms smsObj);
        public void onButtonAddress_TextClickListener(String address);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
   // public interface customButtonTwoListener {
   //     public void onButtonTwoClickListner(int position,String value);
  //  }
 //   public void setCustomButtonTwoListner(customButtonTwoListener listener) {
  //      this.customTwoListner = listener;
 //   }


    private ArrayList<Sms> data = new ArrayList<Sms>();
    private Context context;

    public Sms_CustomList(Context context, ArrayList<Sms> dataItem) {
       // super(context, R.layout.list_single_one, dataItem);
        super(context, R.layout.sms_list_single, dataItem);
        this.context = context;
        this.data = dataItem;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater =  LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.sms_list_single, null);

            viewHolder = new ViewHolder();
            viewHolder.layout_Pos = (LinearLayout)convertView
                    .findViewById(R.id.layout_pos);
            viewHolder.panel_View = (TextView)convertView
                    .findViewById(R.id.panel);
            viewHolder.address_Text = (TextView) convertView
                    .findViewById(R.id.addressText);
            viewHolder.body_Text = (TextView) convertView
                    .findViewById(R.id.smsText);
            viewHolder.date_Text = (TextView) convertView
                    .findViewById(R.id.dateText);
            viewHolder.button_Del = (Button) convertView
                   .findViewById(R.id.deleteButton);
          //  viewHolder.button_Next = (Button) convertView
          //          .findViewById(R.id.nextButton);
            viewHolder.in_or_out_Img = (TextView) convertView
                    .findViewById(R.id.in_or_out);
            viewHolder.button_Option = (Button) convertView
                    .findViewById(R.id.optionButton);


            convertView.setTag(viewHolder);
        } else {
             viewHolder = (ViewHolder) convertView.getTag();
        }
        final int sdk = android.os.Build.VERSION.SDK_INT;
        final Sms temp = getItem(position);
        if (temp.getType().contentEquals("2") ){
           // viewHolder.layout_Pos.setGravity(Gravity.LEFT);
            viewHolder.panel_View.setVisibility(View.GONE);
            viewHolder.button_Option.setVisibility(View.GONE);
            if (sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.in_or_out_Img.setBackground(context.getResources().getDrawable(R.drawable.arrow_right, context.getTheme()));
            }
            else {
                 viewHolder.in_or_out_Img.setBackground(context.getResources().getDrawable(R.drawable.arrow_right));
            }
        }
        else if (temp.getType().contentEquals("1") ){
            viewHolder.panel_View.setVisibility(View.VISIBLE);
            viewHolder.button_Option.setVisibility(View.VISIBLE);
            if (sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                viewHolder.in_or_out_Img.setBackground(context.getResources().getDrawable(R.drawable.arrow_left_one, context.getTheme()));
            }
            else {
                viewHolder.in_or_out_Img.setBackground(context.getResources().getDrawable(R.drawable.arrow_left_one));
            }
        }
        String display_Str  = "";
        if (temp.getName().contains(Constants.anonymous)){
            display_Str = temp.getAddress();
        }
        else{
            display_Str = temp.getName();
        }

        viewHolder.address_Text.setText(display_Str);
        String adjusted_body = temp.getMsg();

        viewHolder.body_Text.setText(adjusted_body);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date date = new Date();
        String month = dateFormat.format(date).substring(0,2);
        String day = dateFormat.format(date).substring(3,5);
        String month_one = temp.getTime().substring(0,2);
        String day_one = temp.getTime().substring(3, 5);
        String date_Str = null;
        final String address_text = temp.getAddress();
        if (Integer.parseInt(month_one) < Integer.parseInt(month) ) {
            date_Str = temp.getTime().substring(0, 5);
        }
        else
        if (Integer.parseInt(month_one) == Integer.parseInt(month) ){
            if (Integer.parseInt(day_one) == Integer.parseInt(day) ){
                date_Str = temp.getTime().substring(6);
            }
            else
            if (Integer.parseInt(day_one) < Integer.parseInt(day) ){
                date_Str = temp.getTime().substring(0, 5);
            }
        }
        viewHolder.date_Text.setText(date_Str);

     viewHolder.button_Option.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if (customListner != null) {
                 customListner.onButtonOptionClickListener(position, temp);
             }
         }

     });

        viewHolder.button_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonDelClickListener(position, temp);
                }
            }

        });

        viewHolder.address_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonAddress_TextClickListener(address_text);
                }
            }

        });

        viewHolder.address_Text = (TextView) convertView
                .findViewById(R.id.addressText);



        return convertView;
    }



    public class ViewHolder {
        LinearLayout layout_Pos;
        TextView body_Text;
        TextView date_Text;
        Button button_Option;
        Button button_Del;
        TextView in_or_out_Img;
        TextView address_Text;
        TextView panel_View;
    }


}