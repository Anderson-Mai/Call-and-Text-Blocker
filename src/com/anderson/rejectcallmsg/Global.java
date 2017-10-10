package com.anderson.rejectcallmsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.NotificationManager;

public class Global {
	public static int Notify_ID = 101;
	public static boolean sms = false;
    public static NotificationManager NotifyManager = null;
	
	public static Map<String, String> mMap = new HashMap<String, String>();
	public static Map<String, String> mMap_ExceptedNumber = new HashMap<String, String>();
	public static boolean NumberChecker = false;
	public static boolean AreaCodeChecker = false;
	public static boolean InterChecker = false;
	public static ArrayList<String> mRejectedList = new ArrayList<String>();
	public static ArrayList<String> mAreaCodeRejectedList = new ArrayList<String>();
	public static ArrayList<String> mInternationalRejectedList = new ArrayList<String>();
	public static ArrayList<String> mWhiteList = new ArrayList<String>();
	public static ArrayList<String> mCheckMarkList = new ArrayList<String>();
	public static ArrayList<String> mMissedCallList = new ArrayList<String>();
	public static ArrayList<String> mBlockedList = new ArrayList<String>();
	public static int icon = -1;
	public static String recent_sender = "";
	public static String recent_sms_time = "";
	public static String recent_sms_body = "";
	public static ArrayList<Sms> all_sms = null;
	public static ArrayList<Sms> unread_sms = null;
	public static ArrayList<Sms> all_inbox = null;
	public static final int sdk = android.os.Build.VERSION.SDK_INT;
}