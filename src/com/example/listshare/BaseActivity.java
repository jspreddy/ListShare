package com.example.listshare;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	ParseUser currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		currentUser = ParseUser.getCurrentUser();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		if (currentUser == null)
		{
			Log.d("DEBUG", "No user logged in");
			Intent i = new Intent(BaseActivity.this, LoginActivity.class);
			finish();
			startActivity(i);
			return;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		if (item.getItemId() == R.id.action_logout) {
			ParseUser.logOut();
			Intent i = new Intent(BaseActivity.this, LoginActivity.class);
			startActivity(i);
			finish();
		}
		return true;
	}
}
