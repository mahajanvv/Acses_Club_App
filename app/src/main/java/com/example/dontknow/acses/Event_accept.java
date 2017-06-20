package com.example.dontknow.acses;

/**
 * Created by Dont know on 30-05-2017.
 */

public class Event_accept {

    private String Event_Type;
    private String Event_Topic;
    private String Event_Description;
    private String Event_Date;
    private String Event_Time;
    private String Event_Post_Time;
    private String Event_userName;
    private String Event_userUID;

    public String getEvent_userUID() {
        return Event_userUID;
    }

    public void setEvent_userUID(String event_userUID) {
        Event_userUID = event_userUID;
    }

    public String getEvent_userName() {
        return Event_userName;
    }

    public void setEvent_userName(String event_userName) {
        Event_userName = event_userName;
    }

    public Event_accept()
    {

    }
    public Event_accept(String event_Type, String event_Topic, String event_Description, String event_Date, String event_Time, String event_Post_Time,String event_userName,String event_userUID) {
        Event_Type = event_Type;
        Event_Topic = event_Topic;
        Event_Description = event_Description;
        Event_Date = event_Date;
        Event_Time = event_Time;
        Event_Post_Time = event_Post_Time;
        Event_userName = event_userName;
        Event_userUID = event_userUID;
    }

    public String getEvent_Type() {
        return Event_Type;
    }

    public void setEvent_Type(String event_Type) {
        Event_Type = event_Type;
    }

    public String getEvent_Topic() {
        return Event_Topic;
    }

    public void setEvent_Topic(String event_Tittle) {
        Event_Topic = event_Tittle;
    }

    public String getEvent_Description() {
        return Event_Description;
    }

    public void setEvent_Description(String event_Description) {
        Event_Description = event_Description;
    }

    public String getEvent_Date() {
        return Event_Date;
    }

    public void setEvent_Date(String event_Date) {
        Event_Date = event_Date;
    }

    public String getEvent_Time() {
        return Event_Time;
    }

    public void setEvent_Time(String event_Time) {
        Event_Time = event_Time;
    }

    public String getEvent_Post_Time() {
        return Event_Post_Time;
    }

    public void setEvent_Post_Time(String event_Post_Time) {
        Event_Post_Time = event_Post_Time;
    }
}
