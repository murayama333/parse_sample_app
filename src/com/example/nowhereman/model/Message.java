package com.example.nowhereman.model;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject{

	private Date localUpdateAt;

	public String getName() {
		return getString("name");
	}
	public void setName(String name) {
		put("name", name);
	}
	public String getMessage() {
		return getString("message");
	}
	public void setMessage(String message) {
		put("message", message);
	}
	public ParseFile getImageFile() {
		return getParseFile("imageFile");
	}
	public void setImageFile(ParseFile file) {
		put("imageFile", file);
	}
	
	public void setEvent(Event event){
		put("event", event);
	}

	public Event getEvent(){
		return (Event)get("event");
	}

	
	public void setLocalUpdateAt(Date date) {
		this.localUpdateAt = date;
	}
	
	public Date getUpdatedAt2() {
		Date date = getUpdatedAt();
		if(date != null){
			return date;
		}
		return this.localUpdateAt;
	}
}
