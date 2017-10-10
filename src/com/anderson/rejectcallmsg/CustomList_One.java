package com.anderson.rejectcallmsg;

/**
 * Created by Anderson_Mai on 1/28/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomList_One extends ArrayAdapter<String>{

    customButtonListener customListner;
    ViewHolder viewHolder;
    boolean reset_flag = true;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
        public void onButtonOneClickListner(int position, String value);
        public void onButtonTwoClickListner(int position, String value);
        public void onButtonThreeClickListner(int position, String value);
        public void onButtonFourClickListner(int position, String value);

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


    private ArrayList<String> data = new ArrayList<String>();
    private Context context;

    public CustomList_One(Context context, ArrayList<String> dataItem) {
        super(context, R.layout.list_single_one, dataItem);
        this.context = context;
        this.data = dataItem;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater =  LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_single_one, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.data_txt);
            viewHolder.button_Call = (Button) convertView
                    .findViewById(R.id.makecallButton);
            viewHolder.button_Del = (Button) convertView
                    .findViewById(R.id.deleteButton);
            viewHolder.button_sms = (Button) convertView
                    .findViewById(R.id.sms_Button);
            viewHolder.button_email = (Button) convertView
                    .findViewById(R.id.email_Button);
            viewHolder.button_Personality = (Button) convertView
                    .findViewById(R.id.person_record_Button);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String temp = getItem(position);
        viewHolder.text.setText(temp);
        viewHolder.button_Call.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (customListner != null) {
                     customListner.onButtonClickListner(position, temp);
                 }

             }
         });

        /* viewHolder.button_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( reset_flag ) {
                    reset_flag = false;
                    viewHolder.button_Del.setBackgroundResource(R.drawable.check_mark);

                    if (customListner != null) {
                        customListner.onButtonTwoClickListner(position, temp);
                    }
                }
                else {
                     reset_flag = true;
                     viewHolder.button_Del.setBackgroundResource(R.drawable.delete_icon);
                     Global.mCheckMarkList.remove(position);
                }
            }
        });
        */
        viewHolder.button_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (customListner != null) {
                        customListner.onButtonOneClickListner(position, temp);
                    }
            }

        });

        viewHolder.button_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonTwoClickListner(position, temp);
                }
            }
        });

        viewHolder.button_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonThreeClickListner(position, temp);
                }
            }
        });

        viewHolder.button_Personality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonFourClickListner(position, temp);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView text;
        Button button_Call;
        Button button_Del;
        Button button_Personality;
        Button button_sms;
        Button button_email;
    }

}