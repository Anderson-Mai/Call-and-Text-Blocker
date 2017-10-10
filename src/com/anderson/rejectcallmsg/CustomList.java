package com.anderson.rejectcallmsg;

/**
 * Created by Anderson_Mai on 1/28/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anderson.include.Utilities;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String>{

    customButtonListener customListner;
    ViewHolder viewHolder;
    boolean reset_flag = true;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
        public void onButtonTwoClickListner(int position,String value);
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

    public CustomList(Context context, ArrayList<String> dataItem) {
        super(context, R.layout.list_single, dataItem);
        this.context = context;
        this.data = dataItem;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater =  LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_single, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.data_txt);
            viewHolder.button_Call = (Button) convertView
                    .findViewById(R.id.makecallButton);
            viewHolder.button_Del = (Button) convertView
                    .findViewById(R.id.deleteButton);

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
        TextView text;
        Button button_Call;
        Button button_Del;
    }

}