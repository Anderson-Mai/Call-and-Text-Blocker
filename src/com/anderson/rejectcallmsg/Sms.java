package com.anderson.rejectcallmsg;

import android.content.Context;

import com.anderson.include.Utilities;

import java.util.ArrayList;

/**
 * Created by Anderson_Mai on 3/6/2016.
 */
public class Sms{
    private String _id;
    private String _address;
    private String _msg;
    private String _readState; //"0" for have not read sms and "1" for have read sms
    private String _time;
    private String _name;
    private String _type;

    public String getId(){
        return _id;
    }
    public String getAddress(){
        return _address;
    }
    public String getMsg(){
        return _msg;
    }
    public String getReadState(){
        return _readState;
    }
    public String getTime(){
        return _time;
    }
    public String getType(){
        return _type;
    }
    public String getName(){
        return _name;
    }


    public void setId(String id){
        _id = id;
    }
    public void setAddress(String address){
        _address = address;
    }
    public void setMsg(String msg){
        _msg = msg;
    }
    public void setReadState(String readState){
        _readState = readState;
    }
    public void setTime(String time){
        _time = time;
    }
    public void setType(String type){
        _type = type;
    }
    public void setName(String address, String [] mPhoneContacts, String [] mRejectList){
        _name = Utilities.lookForName(address, mPhoneContacts,  mRejectList );
    }

}
