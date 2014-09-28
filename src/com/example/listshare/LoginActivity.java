package com.example.listshare;

import android.os.Bundle;
import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listshare.objects.ListItemsObject;
import com.example.listshare.objects.ListObject;
import com.example.listshare.objects.SharesObject;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	EditText t1, t2;
	Button b1, b2;
	Intent i;
	Intent homeActivity;
	Intent signUpActivity;
	String name, password;
	Boolean error = false;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		ParseObject.registerSubclass(ListItemsObject.class);
		ParseObject.registerSubclass(ListObject.class);
		ParseObject.registerSubclass(SharesObject.class);
		
		Parse.initialize(this, "nW4RoU4uXcAd0jZ0yWzqfO0rwAqu8MtSbLdpYw7m", "yd0xuMmvr7ekL0wENpSi5yrbGDYrfCe3oD7ZCoKl");
		ParseAnalytics.trackAppOpened(getIntent());
		homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
		signUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser != null){
			startActivity(homeActivity);
			finish();
		}
		
		
		t1 = (EditText) findViewById(R.id.editText1);
		t2 = (EditText) findViewById(R.id.editText2);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					name = t1.getText().toString();
					password = t2.getText().toString();
					if (name.isEmpty()) {
						t1.setError("Name can not be empty.");
						error = true;
					}
					if (password.isEmpty()) {
						t2.setError("Password can not be empty.");
						error = true;
					}
				} catch (Exception e) {

				}
				if (error == false) {
					login(name, password, homeActivity);
				}
			}
		});

		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(signUpActivity);
				finish();
			}
		});
		
	}

	public void login(String username, String pwd, final Intent intent) {
		ParseUser.logInInBackground(username, pwd, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(LoginActivity.this, "Log in failed. Invalid Credentials", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
