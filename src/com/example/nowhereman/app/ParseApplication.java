package com.example.nowhereman.app;

import android.app.Application;

import com.example.nowhereman.R;
import com.example.nowhereman.model.Event;
import com.example.nowhereman.model.Message;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application{

	@Override
	public void onCreate() {
		ParseObject.registerSubclass(Event.class);
		ParseObject.registerSubclass(Message.class);
		Parse.initialize(this, getString(R.string.application_id), getString(R.string.client_key)); 
	}
}
