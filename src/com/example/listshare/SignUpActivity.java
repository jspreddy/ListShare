package com.example.listshare;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
import android.widget.Toast;

public class SignUpActivity extends Activity {

	ParseUser user;
	EditText t1,t2,t3;
	Button b1, b2;
	String uname, password, rePassword;
	Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		t1 = (EditText) findViewById(R.id.editText1);
		t2 = (EditText) findViewById(R.id.editText2);
		t3 = (EditText) findViewById(R.id.editText3);
		b1 = (Button) findViewById(R.id.button1);
				
		/*t1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}		
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void afterTextChanged(Editable s) {
				uname = t1.getText().toString();
				if(uname.isEmpty()){
					t1.setError("Enter valid username");
					return;
				}	
			}
		});
			
		t2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}		
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void afterTextChanged(Editable s) {
				password = t2.getText().toString();
				if(password.isEmpty()){
					t2.setError("Password cannot be empty");
					return;
				}	
			}
		});

		t3.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}		
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void afterTextChanged(Editable s) {
				rePassword = t3.getText().toString();
				if(password.isEmpty()&& !password.equals(new String(rePassword))){
					t3.setError("Re-entered password does not match Password");
					return;
				}	
			}
		});

		*/
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				uname =t1.getText().toString();
				password = t2.getText().toString();
				rePassword = t3.getText().toString();
				/*if(uname.matches(""))
					t1.setError("Uname Null");
				if(password.matches(""))
					t2.setError("Pwd Null");*/			
				if(password.matches(rePassword) ){//&& password.isEmpty() && uname.isEmpty()){
					user = new ParseUser();
					user.setUsername(uname);
					user.setPassword(password);
					user.signUpInBackground(new SignUpCallback() {
						  public void done(ParseException e) {
						    if (e == null) {
						    	Intent i = new Intent(SignUpActivity.this, MainActivity.class);
						    	finish();
						    	startActivity(i);
						    } else {
						      Log.d("DEBUG","Sign up didn't succeed. Look at the ParseException to figure out what went wrong");
						      Toast.makeText(SignUpActivity.this,"Signup failed", Toast.LENGTH_SHORT).show();
						    }
						  }
						});
					}
				}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

}
