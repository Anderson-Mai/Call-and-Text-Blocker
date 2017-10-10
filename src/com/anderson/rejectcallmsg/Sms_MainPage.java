package com.anderson.rejectcallmsg;

/**
 * Created by Anderson_Mai on 1/28/2016.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.anderson.include.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Sms_MainPage extends ArrayAdapter<Sms>{

    customButtonListener customListner;
    ViewHolder viewHolder;
    boolean reset_flag = true;

    public interface customButtonListener {
        public void onButtonDelClickListener(int position, Sms smsObj);
        public void onButtonProcessClickListener(int position, Sms smsObj);
        public void onButtonViewClickListener(String address, String name);
        public void onButtonAddressClickListener(String address);
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

    public Sms_MainPage(Context context, ArrayList<Sms> dataItem) {
       // super(context, R.layout.sms_list_single_o, dataItem);
        super(context, R.layout.sms_list_mainpage, dataItem);
        this.context = context;
        this.data = dataItem;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater =  LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.sms_list_mainpage, null);
            viewHolder = new ViewHolder();
            viewHolder.address_Text = (TextView) convertView
                    .findViewById(R.id.addressText);
            viewHolder.body_Text = (TextView) convertView
                    .findViewById(R.id.smsText);
            viewHolder.date_Text = (TextView) convertView
                    .findViewById(R.id.dateText);
            viewHolder.button_Del = (Button) convertView
                    .findViewById(R.id.deleteButton);
            viewHolder.button_Package = (Button) convertView
                    .findViewById(R.id.firstLetter);
            viewHolder.button_View = (Button) convertView
                    .findViewById(R.id.viewingButton);

            convertView.setTag(viewHolder);
        } else {
             viewHolder = (ViewHolder) convertView.getTag();
        }
       // final int sdk = android.os.Build.VERSION.SDK_INT;
        final Sms temp = getItem(position);
        String m_address = temp.getAddress();
        String m_Name = temp.getName();
        String StringToSet = "";
        if (!m_Name.contains(Constants.anonymous)){
                    StringToSet = m_Name;
                    String firstLetter = m_Name.substring(0, 1);
                    viewHolder.button_Package.setText(firstLetter);

                    switch (firstLetter) {
                        case "A":
                        case "a":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_blue_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_blue_circle));
                            }
                            break;
                        case "B":
                        case "b":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_orange_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_orange_circle));
                            }
                            break;
                        case "C":
                        case "c":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_purple_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_purple_circle));
                            }
                        case "D":
                        case "d":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.aqua_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.aqua_circle));
                            }
                            break;
                        case "E":
                        case "e":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.black_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.black_circle));
                            }
                            break;
                        case "F":
                        case "f":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.blue_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.blue_circle));
                            }
                            break;
                        case "G":
                        case "g":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.fuchsia_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.fuchsia_circle));
                            }
                            break;
                        case "Z":
                        case "z":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.gray_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.gray_circle));
                            }
                            break;
                        case "H":
                        case "h":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.green_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.green_circle));
                            }
                            break;
                        case "J":
                        case "j":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_teal_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_teal_circle));
                            }
                            break;
                        case "K":
                        case "k":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.lime_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.lime_circle));
                            }
                            break;
                        case "L":
                        case "l":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.navy_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.navy_circle));
                            }
                            break;
                        case "M":
                        case "m":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.orange_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.orange_circle));
                            }
                            break;
                        case "N":
                        case "n":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.purple_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.purple_circle));
                            }
                            break;
                        case "O":
                        case "o":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.red_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.red_circle));
                            }
                            break;
                        case "P":
                        case "p":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.silver_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.silver_circle));
                            }
                            break;

                        case "Q":
                        case "q":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.teal_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.teal_circle));
                            }
                            break;
                        case "R":
                        case "r":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.yellow_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.yellow_circle));
                            }
                            break;
                        case "S":
                        case "s":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.dark_silver_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.dark_silver_circle));
                            }
                            break;
                        case "T":
                        case "t":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_yellow_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_yellow_circle));
                            }
                            break;
                        case "U":
                        case "u":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_yellow_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_yellow_circle));
                            }
                            break;
                        case "V":
                        case "v":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_fuchsia_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_fuchsia_circle));
                            }
                            break;
                        case "X":
                        case "x":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_navy_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_navy_circle));
                            }
                            break;
                        case "Y":
                        case "y":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_gray_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_gray_circle));
                            }
                            break;

                        case "W":
                        case "w":
                            if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_red_circle, context.getTheme()));
                            } else {
                                viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.light_red_circle));
                            }
                            break;

                    }

        }
        else {

                if (Global.sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.anonymous_icon, context.getTheme()));
                    // viewHolder.in_or_out_Img.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.arrow_right));
                } else {
                    viewHolder.button_Package.setBackground(context.getResources().getDrawable(R.drawable.anonymous_icon));
                }

                StringToSet = m_address;
        }

        viewHolder.address_Text.setText(StringToSet);
        String adjusted_body = temp.getMsg();
        if (adjusted_body.length()> 27){
            adjusted_body = adjusted_body.substring(0, 26 ) + " ...";
        }

        viewHolder.body_Text.setText(adjusted_body);

        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        //get current date time with Date()
        Date date = new Date();
        String month = dateFormat.format(date).substring(0,2);
        String day = dateFormat.format(date).substring(3,5);
        String month_one = temp.getTime().substring(0,2);
        String day_one = temp.getTime().substring(3, 5);
        String date_Str = null;
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


        viewHolder.button_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonDelClickListener(position, temp);
                }
            }

        });

        viewHolder.button_Package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonProcessClickListener(position, temp);
                }
            }

        });

        viewHolder.button_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonViewClickListener(temp.getAddress(), temp.getName());
                }
            }

        });
        viewHolder.address_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonAddressClickListener(temp.getAddress());
                }
            }

        });

        return convertView;
    }

     class ViewHolder {
        TextView address_Text;
        TextView body_Text;
        TextView date_Text;
        Button button_Package;
        Button button_View;
        Button button_Del;

    }


}