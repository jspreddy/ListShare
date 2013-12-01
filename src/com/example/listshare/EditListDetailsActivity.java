package com.example.listshare;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditListDetailsActivity extends Activity {

	EditText t;
	Button b1, b2;
	int flag;
	ParseUser currentUser;
	List<ParseObject> objectlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_list_details);

		// flag =1 if add button is pressed and flag = 0 if long press on list
		// item.

		// Intent intent = getIntent();
		// int list_id = intent.getIntExtra("list_id", -1);
		t = (EditText) findViewById(R.id.editText1);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
		
		if (getIntent().getExtras() != null) {
			flag = getIntent().getExtras().getInt("flag");
			currentUser = ParseUser.getCurrentUser();
		}
		
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateDatabase();
				onBackPressed();
				finish();
			}
		});
		
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//show dialogue box or dropdown box.
				//store names into an arraylist
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		setResult(RESULT_OK, i);
		super.onBackPressed();
	}

	private void updateDatabase() {
		if(flag == 0){
			//update
			
		}else if(flag == 1){
			//insert list
			
			ParseObject object = new ParseObject("List");
			object.put("list_name", t.getText().toString());
			object.put("createdBy", currentUser.getString("objectId"));
			object.saveInBackground();
			
			// insert into shared
			ParseQuery<ParseObject> query = ParseQuery.getQuery("List");
			query.whereEqualTo("list_name", t.getText().toString());
			query.whereEqualTo("createdBy", currentUser.getString("objectId"));
			query.findInBackground(new FindCallback<ParseObject>() {
				
				@Override
				public void done(List<ParseObject> list, ParseException e) {
					if (e == null) {
			            Log.d("score", "Retrieved " + list.size() + " scores");
			            objectlist = list;
			        } else {
			            Log.d("score", "Error: " + e.getMessage());
			        }
			    }
			});
			
			
			// for each shared user list id start for loop
			{
				ParseObject sharedObject = new ParseObject("Shares");
				sharedObject.put("ListId_fk", objectlist.get(0).getString("objectId"));
				sharedObject.put("UserId_fk","");
			}	
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_list_details, menu);
		return true;
	}

}
