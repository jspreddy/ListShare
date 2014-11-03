package com.example.listshare;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	
	public ParseUser getCurrentUser(){
		authCheck();
		return ParseUser.getCurrentUser();
	}
	
	public boolean authCheck() {
		if (ParseUser.getCurrentUser() == null)
		{
			Log.d("DEBUG", "No user logged in");
			Intent intnt = new Intent(BaseActivity.this, LoginActivity.class);
			intnt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intnt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			finish();
			startActivity(intnt);
			return false;
		}
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		authCheck();
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
			Intent intnt = new Intent(BaseActivity.this, LoginActivity.class);
			intnt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intnt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intnt);
			finish();
		}
		if (item.getItemId() == R.id.action_change_password) {
			Intent intnt = new Intent(BaseActivity.this, PasswordChangeActivity.class);
			intnt.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intnt.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intnt);
		}
		return true;
	}
}
