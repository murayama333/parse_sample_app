package com.example.nowhereman;

import java.util.Date;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nowhereman.app.AppException;
import com.example.nowhereman.model.Event;
import com.example.nowhereman.model.Message;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class MessageListFragment extends ListFragment{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		setListAdapter(null);
		ParseQuery.getQuery(Event.class).getFirstInBackground(new GetCallback<Event>() {
			@Override
			public void done(Event ev, ParseException ex) {
				final Event event;
				if(ev == null){
					event = newEvent();
				}else{
					event = ev;
				}
				
				((MainActivity)getActivity()).getActionBar().setTitle(event.getTitle());
				((MainActivity)getActivity()).setEvent(event);
				
				ParseQueryAdapter<Message> adapter = new ParseQueryAdapter<Message>(getActivity(), new ParseQueryAdapter.QueryFactory<Message>() {
					@Override
					public ParseQuery<Message> create() {
						ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
						query2.whereEqualTo("event", event);
						query2.orderByDescending("createdAt");
						return query2;
					}
				}){
					@Override
					public View getItemView(Message object, View v, ViewGroup parent) {
						if (v == null) {
						    v = View.inflate(getContext(), R.layout.list_item_card, null);
						  }
						  super.getItemView(object, v, parent);
						 
						  // Do additional configuration before returning the View.
						  TextView descriptionView = (TextView) v.findViewById(R.id.message);
						  descriptionView.setText(object.getString("message"));
						  return v;
					}
					
				};
				adapter.setTextKey("name");
				adapter.setImageKey("imageFile");
				setListAdapter(adapter);
				
				int padding = (int)(getResources().getDisplayMetrics().density * 8); // 8dp
				ListView listView = getListView();
				listView.setPadding(padding, 0, padding, 0);
				listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
				listView.setDivider(null);
				
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				View header = inflater.inflate(R.layout.list_header_footer, listView, false);
				View footer = header = inflater.inflate(R.layout.list_header_footer, listView, false);
				listView.addHeaderView(header);
				listView.addFooterView(footer);
				listView.setItemsCanFocus(false);
			}

			private Event newEvent() {
				Event event = new Event();
				event.setTitle(getString(R.string.event_title));
				event.setDate(new Date());
				try {
					event.save();
					return event;
				} catch (ParseException e) {
					throw new AppException(e);
				}
			}
		});
	}
	
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	    	
		if (item.getItemId() == R.id.action_entry) {
			getActivity().getFragmentManager()
			.beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)  
				.replace(R.id.container, 
						Fragment.instantiate(getActivity(), MessageEntryFragment.class.getName()), 
						MessageEntryFragment.class.getName())
				.addToBackStack(null)
			.commit();
		}
        return super.onOptionsItemSelected(item);
    }

}
