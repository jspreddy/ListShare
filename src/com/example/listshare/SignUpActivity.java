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
	EditText t1, t2, t3;
	Button b1, b2;

	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		t1 = (EditText) findViewById(R.id.editText1);
		t2 = (EditText) findViewById(R.id.editText2);
		t3 = (EditText) findViewById(R.id.editText3);
		b1 = (Button) findViewById(R.id.button1);

		t1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				isEmpty(t1);
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

		t2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				isEmpty(t2);
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

		t3.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				isEmpty(t3);
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

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isEmpty(t1) && !isEmpty(t2) && isMatching(t2, t3)) {
					String uname, password;
					uname = t1.getText().toString();
					password = t2.getText().toString();

					user = new ParseUser();
					user.setUsername(uname);
					user.setPassword(password);

					user.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e) {
							if (e == null) {
								Intent i = new Intent(SignUpActivity.this, MainActivity.class);
								startActivity(i);
								finish();
							} else {
								Log.d("DEBUG", "Sign up didn't succeed. Look at the ParseException to figure out what went wrong");
								Toast.makeText(SignUpActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
								Toast.makeText(SignUpActivity.this, ""+e, Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		});
	}

	public boolean isEmpty(EditText et) {
		String text = et.getText().toString();
		if (text.isEmpty()) {
			et.setError("This cannot be empty.");
			return true;
		}
		et.setError(null);
		return false;
	}

	public boolean isMatching(EditText et_pwd1, EditText et_pwd2) {
		String pwd1 = et_pwd1.getText().toString();
		String pwd2 = et_pwd2.getText().toString();

		if (!pwd1.equals(new String(pwd2))) {
			et_pwd2.setError("re typed Password does not match.");
			return false;
		}
		et_pwd2.setError(null);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

}
