package com.listshare.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.listshare.app.objects.ListItemsObject;
import com.listshare.app.objects.ListObject;
import com.listshare.app.objects.SharesObject;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	EditText etLoginUsername, etLoginPassword;
	Button btnLogin, btnSignUp;
	Intent homeActivity;
	Intent signUpActivity;
	String name, password;
	Boolean error = false;

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
		
		
		etLoginUsername = (EditText) findViewById(R.id.etLoginUsername);
		etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					name = etLoginUsername.getText().toString();
					password = etLoginPassword.getText().toString();
					if (name.isEmpty()) {
						etLoginUsername.setError("Name can not be empty.");
						error = true;
					}
					if (password.isEmpty()) {
						etLoginPassword.setError("Password can not be empty.");
						error = true;
					}
				} catch (Exception e) {

				}
				if (error == false) {
					login(name, password, homeActivity);
				}
			}
		});

		btnSignUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(signUpActivity);
				finish();
			}
		});
		
	}

	private void login(String username, String pwd, final Intent intent) {
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
