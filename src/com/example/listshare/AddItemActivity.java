package com.example.listshare;

import com.example.listshare.objects.ListItemsObject;
import com.example.listshare.objects.ListObject;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

	EditText t1, t2, t3;
	Button b1, b2;
	String name, unit, listId, itemId;
	double quantity;
	int count;
	ListItemsObject currentItem;
	Spinner spinner;
	String[] values;
	int flag;
	ParseUser user;
	TextView t;
	ArrayAdapter<String> adapter;
	Boolean itemModification = false;
	ProgressDialog pdMain;

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

		values = new String[] { "", "Liter", "pound", "lb", "OZ", "Grams", "KG", "qty", "ct. pk." };
		spinner = (Spinner) findViewById(R.id.spinner1);
		adapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_item, values);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		if (getIntent().getExtras() != null) {
			listId = getIntent().getExtras().getString("ListId");
			flag = getIntent().getExtras().getInt("flag");
			itemId = getIntent().getExtras().getString("ItemId");
		}

		if (flag == 2) {
			pdMain=new ProgressDialog(AddItemActivity.this);
			pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pdMain.setCancelable(false);
			pdMain.setMessage("Loading Item details");
			pdMain.show();
			ParseQuery<ListItemsObject> query = ListItemsObject.getQuery();
			query.include("editedBy");
			query.getInBackground(itemId, new GetCallback<ListItemsObject>() {
				public void done(ListItemsObject object, ParseException e) {
					if(pdMain != null) pdMain.dismiss();
					if (e == null) {
						currentItem = object;
						Log.d("DEBUG", object.getName());
						t1.setText(object.getName());
						t2.setText(Double.toString(object.getQuantity()));
						t3.setText(Integer.toString(object.getCount()));
						unit = object.getUnit();
						int position = adapter.getPosition(unit);
						spinner.setSelection(position);
						t.setText(object.getUser().getUsername());
					} else {
						Toast.makeText(getApplicationContext(), "Error. Try again.", Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			});
		}else
			t.setText(ParseUser.getCurrentUser().getUsername());

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
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
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
				if (!isEmpty(t1) && !isEmpty(t3)) {
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
		// get all values
		pdMain=new ProgressDialog(AddItemActivity.this);
		pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdMain.setCancelable(false);
		pdMain.setMessage("Saving");
		pdMain.show();
		
		name = t1.getText().toString();
		Log.d("DEBUG", "double "+t2.getText().toString());
		if(t2.getText().toString().equals("") ||t2.getText().toString().equals(null)){
			quantity = 0;
			unit = "";
		}else{
			quantity = Double.parseDouble(t2.getText().toString());
			unit = spinner.getSelectedItem().toString();
		}
		count = Integer.parseInt(t3.getText().toString());
		user = ParseUser.getCurrentUser();
		ListObject lo = new ListObject();
		lo.setObjectId(listId);
		
		try {
			lo.fetch();
		} catch (ParseException e2) {
			e2.printStackTrace();
		}

		if (flag == 1) {
			// Insert
			ListItemsObject item = new ListItemsObject();
			item.setName(name);
			item.setQuantity(quantity);
			item.setUnit(unit);
			item.setCount(count);
			item.setUser(user);
			item.setList(lo);
			item.setState(0);
			item.saveInBackground(new SaveCallback(){
				@Override
				public void done(ParseException arg0) {
					if(arg0==null){
						onBackPressed();
					}
					else{
						Toast.makeText(getApplicationContext(), "Error. Try Saving again.", Toast.LENGTH_SHORT).show();
					}
				}
			});
			itemModification = true;

		} else if (flag == 2) {
			//update
			currentItem.setName(name);
			currentItem.setQuantity(quantity);
			currentItem.setUnit(unit);
			currentItem.setCount(count);
			currentItem.setUser(user);
			currentItem.saveInBackground(new SaveCallback(){
				@Override
				public void done(ParseException arg0) {
					if(arg0==null){
						onBackPressed();
					}
					else{
						Toast.makeText(getApplicationContext(), "Error. Try Saving again.", Toast.LENGTH_SHORT).show();
					}
				}
			});
			itemModification = true;
		}
	}

	@Override
	public void onBackPressed() {
		if(itemModification==true){
			Intent i = new Intent();
			i.putExtra("itemModification", true);
			setResult(RESULT_OK, i);
		}
		finish();
		super.onBackPressed();
	}
}
