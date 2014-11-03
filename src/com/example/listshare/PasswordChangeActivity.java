package com.example.listshare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import helpers.InputHelper;

public class PasswordChangeActivity extends BaseActivity {

	EditText etPasswordChangeOldPassword, etPasswordChangeNewPassword, etPasswordChangeRetypeNewPassword;
	Button btnChangePasswordSave;
	ProgressDialog pdMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_change);
		
		etPasswordChangeOldPassword = (EditText) findViewById(R.id.etPasswordChangeOldPassword);
		etPasswordChangeNewPassword = (EditText) findViewById(R.id.etPasswordChangeNewPassword);
		etPasswordChangeRetypeNewPassword = (EditText) findViewById(R.id.etPasswordChangeRetypeNewPassword);
		btnChangePasswordSave = (Button) findViewById(R.id.btnChangePasswordSave);
		pdMain=new ProgressDialog(PasswordChangeActivity.this);
		
		btnChangePasswordSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(InputHelper.isEmpty(etPasswordChangeOldPassword, getString(R.string.error_editText_empty)) ||
						InputHelper.isEmpty(etPasswordChangeNewPassword, getString(R.string.error_editText_empty)) ||
						InputHelper.isEmpty(etPasswordChangeRetypeNewPassword, getString(R.string.error_editText_empty))){
					return;
				}
				
				if(!InputHelper.areMatching(
						etPasswordChangeNewPassword, etPasswordChangeRetypeNewPassword,
						getString(R.string.error_editText_matching), getString(R.string.error_editText_matching))){
					return;
				}
				
				ParseUser currentUser = getCurrentUser();
				pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pdMain.setCancelable(false);
				pdMain.setMessage("Saving..");
				pdMain.show();
				
				ParseUser.logInInBackground(currentUser.getUsername(), etPasswordChangeOldPassword.getText().toString(), new LogInCallback(){

					@Override
					public void done(ParseUser user, ParseException error) {
						if(user == null || error != null){
							pdMain.dismiss();
							Toast.makeText(PasswordChangeActivity.this, "Authentication failed. Try again.", Toast.LENGTH_SHORT).show();
							return;
						}
						user.setPassword(etPasswordChangeNewPassword.getText().toString());
						user.saveInBackground(new SaveCallback(){

							@Override
							public void done(ParseException error) {
								pdMain.dismiss();
								if(error != null){
									Toast.makeText(PasswordChangeActivity.this, "Saving Failed. Try again.", Toast.LENGTH_SHORT).show();
									return;
								}
								Toast.makeText(PasswordChangeActivity.this, "Password change successful.", Toast.LENGTH_SHORT).show();
							}
							
						});
					}
				});
			}
		});
	}
}
