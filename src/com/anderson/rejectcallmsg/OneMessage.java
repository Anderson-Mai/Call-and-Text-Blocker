package com.anderson.rejectcallmsg;

public class OneMessage {
	String recent_sender =  "";
	long recent_sms_time = 0L;
	String recent_sms_body = "";

	void set_Address(String address){
		this.recent_sender = address;
	}
	
    void set_Time (long time){
		recent_sms_time = time;
	}

	void set_Msg_Body ( String body ){
		recent_sms_body = body;
	}
	String get_Address(){
		return this.recent_sender;
	}

	String get_Time (){
		return String.valueOf(recent_sms_time);
	}

	String  get_Msg_Body (){
		return recent_sms_body;
	}


}
