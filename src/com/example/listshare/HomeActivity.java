package com.example.listshare;

import java.util.ArrayList;

import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class HomeActivity extends Activity {

	ListView list;
	Button b;
	Intent i; 
	ArrayAdapter<MainList> adapter;
	ArrayList<MainList> listItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		list = (ListView) findViewById(R.id.listView1);
		new GetMainListItems(HomeActivity.this).execute();
		
		b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				i = new Intent(HomeActivity.this,EditListDetailsActivity.class);
				startActivity(i);
			}
		});
		
	}

	public void DisplayListContents(ArrayList<MainList> mainList) {
		// Get current User
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			adapter = new MainListAdapter(this,mainList);
			list.setAdapter(adapter);

			
		} else {
			Log.d("DEBUG", "No user loggrd in");
			i = new Intent(HomeActivity.this, MainActivity.class);
			finish();
			startActivity(i);
		}
		
		// Get all the lists of current user
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
