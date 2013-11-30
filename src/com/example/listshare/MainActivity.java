package com.example.listshare;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
public class MainActivity extends Activity {

	EditText t1,t2;
	Button b1, b2;
	Intent i;

	String name, password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Parse.initialize(this, "nW4RoU4uXcAd0jZ0yWzqfO0rwAqu8MtSbLdpYw7m", "yd0xuMmvr7ekL0wENpSi5yrbGDYrfCe3oD7ZCoKl");
		ParseAnalytics.trackAppOpened(getIntent());
		
		t1 = (EditText) findViewById(R.id.editText1);
		t2 = (EditText) findViewById(R.id.editText2);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
				
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				i = new Intent(MainActivity.this, HomeActivity.class);
				name = t1.getText().toString();
				password = t2.getText().toString();
				ParseUser.logInInBackground(name, password, new LogInCallback() {
					  public void done(ParseUser user, ParseException e) {
					    if (user != null) {
					      startActivity(i);
					    } else {
					      Log.d("DEBUG","Signup failed. Look at the ParseException to see what happened.");
					      Toast.makeText(MainActivity.this, "Log in failed. Username password does not match", Toast.LENGTH_SHORT).show();
					    }
					  }
					});
			}
		});
		
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				i = new Intent(MainActivity.this, SignUpActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
