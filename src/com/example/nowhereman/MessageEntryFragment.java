package com.example.nowhereman;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nowhereman.app.AppException;
import com.example.nowhereman.model.Event;
import com.example.nowhereman.model.Message;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

public class MessageEntryFragment extends Fragment{

	int REQUEST_CAPTURE_IMAGE = 1;

	private File mPictureFile;
	private EditText mEditTextEntryMessage;
	private EditText mEditTextEntryName;
	private ImageButton mImageButtonEntryIcon;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.item_card, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		mEditTextEntryMessage = (EditText)getActivity().findViewById(R.id.entryMessage);
		mEditTextEntryMessage.setHint(getString(R.string.hint_message));
		
		mEditTextEntryName = (EditText)getActivity().findViewById(R.id.entryName);
		mEditTextEntryName.setHint(getString(R.string.hint_name));
		
		mImageButtonEntryIcon = (ImageButton)getActivity().findViewById(R.id.entryIcon);
		mImageButtonEntryIcon.setImageDrawable(getResources().getDrawable(R.drawable.camera));
		
		((ImageButton)getActivity().findViewById(R.id.entryIcon)).setOnClickListener(new CameraListener());
	}
	
	
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.entry, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
    		save();
        }
        return true;
    }

	private void back() {
		getActivity().getFragmentManager().popBackStack();
	}

	private void save() {
		getActivity().findViewById(R.id.view_actionbar_progress).setVisibility(View.VISIBLE);

		final Event event = ((MainActivity)getActivity()).getEvent();
		final Message message = new Message();
		message.setEvent(event);
		message.setMessage(mEditTextEntryMessage.getText().toString());
		message.setName(mEditTextEntryName.getText().toString());
		message.setLocalUpdateAt(new Date());
		// imageFile
		Bitmap bmp = ((BitmapDrawable) mImageButtonEntryIcon.getDrawable()).getBitmap();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 70, bos);
		final ParseFile parseFile = new ParseFile("image.jpg", bos.toByteArray());
		message.setImageFile(parseFile);
		parseFile.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e2) {
				if(e2 != null) throw new AppException(e2);
				message.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e3) {
						if(e3 != null) throw new AppException(e3);
						Toast.makeText(getActivity(), getString(R.string.save_success), Toast.LENGTH_LONG).show();
						getActivity().findViewById(R.id.view_actionbar_progress).setVisibility(View.INVISIBLE);
						back();
					}
				});
			}
		});
	}
  
    class CameraListener implements OnClickListener{
		@Override
    	public void onClick(View v) {
    		mPictureFile = new File(Environment.getExternalStorageDirectory(), "tmp_image.png");
    		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
    		startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
    	}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (REQUEST_CAPTURE_IMAGE == requestCode
				&& resultCode == Activity.RESULT_OK) {
			try {
				FileInputStream in = new FileInputStream(mPictureFile);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap capturedImage = BitmapFactory.decodeStream(in, null,	options);
				((ImageView) getActivity().findViewById(R.id.entryIcon)).setImageBitmap(capturedImage);
			} catch (FileNotFoundException e) {
				throw new AppException(e);
			}
		}
    }
}

