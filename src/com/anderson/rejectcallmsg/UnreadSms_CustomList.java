package com.anderson.rejectcallmsg;

    /**
     * Created by Anderson_Mai on 1/28/2016.
     */

    import android.content.Context;
    import android.content.res.Resources;
    import android.support.v4.content.ContextCompat;
    import android.text.method.ScrollingMovementMethod;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.TextView;

    import java.text.DateFormat;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;

public class UnreadSms_CustomList extends ArrayAdapter<Sms> {

    customButtonListener customListner;
    ViewHolder viewHolder;
    boolean reset_flag = true;

    public interface customButtonListener {
        public void onButtonOneClickListener(int position, Sms smsObj);
        public void onButtonTwoClickListener(int position, Sms smsObj);
        public void onButtonThreeClickListener(int position, Sms smsObj);
    }

    public void setCustomButtonListener(customButtonListener listener) {
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

    public UnreadSms_CustomList(Context context, ArrayList<Sms> dataItem) {
        // super(context, R.layout.list_single_one, dataItem);
        super(context, R.layout.flexible_sms_list_single, dataItem);
        this.context = context;
        this.data = dataItem;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.flexible_sms_list_single, null);
            viewHolder = new ViewHolder();
            viewHolder.address_Text = (TextView) convertView
                    .findViewById(R.id.addressText);
            viewHolder.body_Text = (TextView) convertView
                    .findViewById(R.id.smsText);
            viewHolder.button_Zoom_Text = (Button) convertView
                    .findViewById(R.id.zoomTextButton);

            viewHolder.date_Text = (TextView) convertView
                    .findViewById(R.id.dateText);
            viewHolder.button_Del = (Button) convertView
                    .findViewById(R.id.deleteButton);
            viewHolder.button_Next = (Button) convertView
                    .findViewById(R.id.nextButton);
            viewHolder.in_or_out_Img = (TextView) convertView
                    .findViewById(R.id.in_or_out);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        final Sms temp = getItem(position);
        viewHolder.address_Text.setText(temp.getAddress());
        String adjusted_body = temp.getMsg();
        if (adjusted_body.length() > 30) {
            adjusted_body = adjusted_body.substring(0, 29) + " ...";
        }

        viewHolder.body_Text.setText(adjusted_body);

        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        //get current date time with Date()
        Date date = new Date();
        String month = dateFormat.format(date).substring(0, 2);
        String day = dateFormat.format(date).substring(3, 5);
        String month_one = temp.getTime().substring(0, 2);
        String day_one = temp.getTime().substring(3, 5);
        String date_Str = null;
        if (Integer.parseInt(month_one) < Integer.parseInt(month)) {
            date_Str = temp.getTime().substring(0, 5);
        } else if (Integer.parseInt(month_one) == Integer.parseInt(month)) {
            if (Integer.parseInt(day_one) == Integer.parseInt(day)) {
                date_Str = temp.getTime().substring(6);
            } else if (Integer.parseInt(day_one) < Integer.parseInt(day)) {
                date_Str = temp.getTime().substring(0, 5);
            }
        }
        viewHolder.date_Text.setText(date_Str);

        viewHolder.button_Zoom_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    //   viewHolder.in_or_out_Img.setVisibility(View.GONE);
                    customListner.onButtonThreeClickListener(position, temp);
                }
            }
        });

        viewHolder.button_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonOneClickListener(position, temp);
                }
            }

        });

        viewHolder.button_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonTwoClickListener(position, temp);
                }
            }

        });
        return convertView;
    }

    public class ViewHolder {
        TextView body_Text;
        TextView date_Text;
        Button button_Del;
        Button button_Next;
        TextView in_or_out_Img;
        TextView address_Text;
        Button button_Zoom_Text;

    }
}