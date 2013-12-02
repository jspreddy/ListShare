package com.example.listshare;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
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
import android.widget.TextView;
import android.widget.Toast;

public class AddItemActivity extends Activity {

	EditText t1,t2,t3;
	Button b1,b2;
	String name, quantity, count,unit,listId, itemId;
	Spinner spinner;
	String [] values;
	int flag;
	ParseUser user;
	TextView t;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		t1 = (EditText) findViewById(R.id.editText1);
		t2 = (EditText) findViewById(R.id.editText2);
		t3 = (EditText) findViewById(R.id.editText3);
		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
		t = (TextView) findViewById(R.id.textView6);
		
		values = new String[]{"liter","pound","lb","OZ","grams"};
		spinner = (Spinner) findViewById(R.id.spinner1);
		adapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_item,values);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		if (getIntent().getExtras() != null) {
			listId = getIntent().getExtras().getString("ListId");
			flag = getIntent().getExtras().getInt("flag");
			itemId = getIntent().getExtras().getString("ItemId");
		}
		
		if(flag == 2){
			ParseQuery<ParseObject> query = ParseQuery.getQuery("ListItems");
			query.getInBackground(itemId, new GetCallback<ParseObject>() {
			  public void done(ParseObject object, ParseException e) {
			    if (e == null) {
			    	t1.setText(object.getString("item_name"));
			    	t2.setText(object.getString("quantity"));
			    	t3.setText(object.getString("count"));
			    	unit = object.getString("units");
			    	int position = adapter.getPosition(unit);
			    	spinner.setSelection(position);
			    	
			    	// set user
			    	
			    } else {
			      // something went wrong
			    }
			  }
			});
		}
		
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
		user = ParseUser.getCurrentUser();
		
		if(flag == 1){
			// Insert
			ParseObject item = new ParseObject("ListItems");
			item.put("quantity", quantity);
			item.put("units", unit);
			item.put("count", count);
			item.put("item_name", name);
			item.put("ListId_fk", listId);
			item.put("editedBy", user.getString("objectId"));
			item.saveInBackground();
			Toast.makeText(this, name+" "+" "+quantity+" "+count+" "+ unit, Toast.LENGTH_SHORT).show();
		}else if(flag == 2){
			//update			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("ListItems");
			query.getInBackground(itemId, new GetCallback<ParseObject>() {
				@Override
				public void done(ParseObject item, ParseException e) {
			    if (e == null) {
					item.put("quantity", quantity);
					item.put("units", unit);
					item.put("count", count);
					item.put("item_name", name);
					item.put("ListId_fk", listId);
					item.put("editedBy", user.getString("objectId"));
					item.saveInBackground();
			    }
			  }
			});
		}
		Toast.makeText(this, name+" "+" "+quantity+" "+count+" "+ unit, Toast.LENGTH_SHORT).show();
		//Intent i = new Intent();
		//setResult(RESULT_OK, i);
		onBackPressed();	
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}	
}
