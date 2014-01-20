package com.example.nowhereman;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.example.nowhereman.model.Event;
import com.parse.ParseAnalytics;

public class MainActivity extends Activity {

	private Event mEvent;
	
	public Event getEvent() {
		return mEvent;
	}

	public void setEvent(Event event) {
		mEvent = event;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().show();
		getActionBar().setDisplayShowHomeEnabled(false);
		
		// アプリ起動
		ParseAnalytics.trackAppOpened(getIntent());

		getFragmentManager()
			.beginTransaction()
				.add(R.id.container, 
						Fragment.instantiate(this, MessageListFragment.class.getName()), 
						MessageListFragment.class.getName())
			.commit();
	}

}
