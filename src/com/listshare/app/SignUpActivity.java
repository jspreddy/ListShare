package com.listshare.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import helpers.InputHelper;

public class SignUpActivity extends Activity {

	ParseUser user;
	EditText etSignUpUsername, etSignUpPassword, etSignUpPasswordRetype;
	Button btnSignUp, btnSignUpToLogin;
	ProgressDialog pdMain;
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		etSignUpUsername = (EditText) findViewById(R.id.etSignUpUsername);
		etSignUpPassword = (EditText) findViewById(R.id.etSignUpPassword);
		etSignUpPasswordRetype = (EditText) findViewById(R.id.etSignUpPasswordRetype);
		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignUpToLogin = (Button) findViewById(R.id.btnSignUpToLogin);
		pdMain=new ProgressDialog(SignUpActivity.this);
		
		etSignUpUsername.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				InputHelper.isEmpty(etSignUpUsername, getString(R.string.error_editText_empty));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		etSignUpPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				InputHelper.isEmpty(etSignUpPassword, getString(R.string.error_editText_empty));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		etSignUpPasswordRetype.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				InputHelper.isEmpty(etSignUpPasswordRetype, getString(R.string.error_editText_empty));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!InputHelper.isEmpty(etSignUpUsername, getString(R.string.error_editText_empty)) 
						&& !InputHelper.isEmpty(etSignUpPassword, getString(R.string.error_editText_empty)) 
						&& InputHelper.areMatching(
								etSignUpPassword,
								etSignUpPasswordRetype, 
								getString(R.string.error_editText_matching),
								getString(R.string.error_editText_matching)
								)) {
					String uname, password;
					uname = etSignUpUsername.getText().toString();
					password = etSignUpPassword.getText().toString();

					user = new ParseUser();
					user.setUsername(uname);
					user.setPassword(password);

					pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					pdMain.setCancelable(false);
					pdMain.setMessage("Saving..");
					pdMain.show();
					
					user.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e) {
							if(pdMain != null) pdMain.dismiss();
							if (e == null) {
								Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
								startActivity(i);
								finish();
							} else {
								Toast.makeText(SignUpActivity.this, "Failure: "+e.getMessage(), Toast.LENGTH_LONG).show();
							}
						}
					});
				}
			}
		});
		
		btnSignUpToLogin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
}
