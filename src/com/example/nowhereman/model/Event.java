package com.example.nowhereman.model;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Event")
public class Event extends ParseObject{

	public Event(){
		//setMessageList(new ArrayList<Message>());
	}
	
	public String getTitle() {
		return getString("title");
	}
	public void setTitle(String title) {
		put("title", title);
	}
	public Date getDate() {
		return getDate("date");
	}
	public void setDate(Date date) {
		put("date", date);
	}
	public double getLat() {
		return getDouble("lat");
	}
	public void setLat(double lat) {
		put("lat", lat);
	}
	public double getLng() {
		return getDouble("lng");
	}
	public void setLng(double lng) {
		put("lng", lng);
	}
	
}
