package com.example.listshare;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddItemActivity extends Activity {

	EditText t1,t2,t3;
	Button b1,b2;
	String name, quantity, count,unit;
	Spinner spinner;
	String [] values;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		t1 = (EditText) findViewById(R.id.editText1);
		t2 = (EditText) findViewById(R.id.editText2);
		t3 = (EditText) findViewById(R.id.editText3);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
		spinner = (Spinner) findViewById(R.id.spinner1);
		values = new String[]{"liter","pound","lb","OZ","grams"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_item,values);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
				// TODO Auto-generated method stub		
			}
		});
		
		b1.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(!isEmpty(t1) && !isEmpty(t3)){
					executeQuery();
				}
			}
		});
		
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
		return true;
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
	
	private void executeQuery() {
		//get all values
		name = t1.getText().toString();
		quantity = t2.getText().toString();
		count = t3.getText().toString();
		unit = spinner.getSelectedItem().toString();
		//Execute query
		Toast.makeText(this, name+" "+" "+quantity+" "+count+" "+ unit, Toast.LENGTH_SHORT).show();
		Intent i = new Intent();
		setResult(RESULT_OK, i);
		onBackPressed();	
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}	
}
