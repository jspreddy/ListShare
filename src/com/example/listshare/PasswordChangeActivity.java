package com.example.listshare;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import helpers.InputHelper;

public class PasswordChangeActivity extends BaseActivity {

	EditText etPasswordChangeOldPassword, etPasswordChangeNewPassword, etPasswordChangeRetypeNewPassword;
	Button btnChangePasswordSave;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_change);
		
		etPasswordChangeOldPassword = (EditText) findViewById(R.id.etPasswordChangeOldPassword);
		etPasswordChangeNewPassword = (EditText) findViewById(R.id.etPasswordChangeNewPassword);
		etPasswordChangeRetypeNewPassword = (EditText) findViewById(R.id.etPasswordChangeRetypeNewPassword);
		btnChangePasswordSave = (Button) findViewById(R.id.btnChangePasswordSave);
		
		btnChangePasswordSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputHelper.isEmpty(etPasswordChangeNewPassword, getString(R.string.error_editText_empty));
				InputHelper.isEmpty(etPasswordChangeOldPassword, getString(R.string.error_editText_empty));
				InputHelper.isEmpty(etPasswordChangeRetypeNewPassword, getString(R.string.error_editText_empty));
				InputHelper.areMatching(
						etPasswordChangeNewPassword, etPasswordChangeRetypeNewPassword,
						getString(R.string.error_editText_matching), getString(R.string.error_editText_matching));
				
			}
		});
		
	}
}
