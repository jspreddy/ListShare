package com.example.listshare;

import java.util.List;

import com.example.listshare.objects.ListObject;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditListDetailsActivity extends Activity {

	Button btnAddShare, btnEditListName;
	ParseUser currentUser;
	
	String list_id;
	ListObject listObject;
	ProgressDialog pdMain;
	Boolean nameChanged = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_list_details);

		Intent intent = getIntent();
		list_id = intent.getStringExtra("list_id");
		currentUser = ParseUser.getCurrentUser();
		pdMain=new ProgressDialog(EditListDetailsActivity.this);
		
		
		btnEditListName = (Button) findViewById(R.id.btnEditListName);
		btnAddShare = (Button) findViewById(R.id.btnAddShare);
		if(list_id!=null)
		{	
			ParseQuery<ListObject> listQuery = ListObject.getQuery();
			listQuery.include("createdBy");
			listQuery.getInBackground(list_id, new GetCallback<ListObject>(){
				@Override
				public void done(ListObject arg0, ParseException e) {
					if(e==null){
						listObject = arg0;
						btnEditListName.setText(listObject.getName());
						ParseUser createdBy =listObject.getParseUser("createdBy"); 
						if(createdBy.getObjectId().equals(currentUser.getObjectId())){
							btnEditListName.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									
									AlertDialog.Builder alert = new AlertDialog.Builder(EditListDetailsActivity.this);
		
									alert.setTitle("List Name");
		
									// Set an EditText view to get user input
									final EditText input = new EditText(EditListDetailsActivity.this);
									input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
									alert.setView(input);
		
									alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											String listName = input.getText().toString();
											if(!listName.isEmpty()){
												pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
												pdMain.setCancelable(false);
												pdMain.setMessage("Loading List");
												pdMain.show();
												listObject.setName(listName);
												listObject.saveInBackground(new SaveCallback(){
													@Override
													public void done(ParseException e) {
														pdMain.dismiss();
														if(e==null){
															btnEditListName.setText(listObject.getName());
															nameChanged=true;
														}
														else{
															Toast.makeText(EditListDetailsActivity.this, "Error Saving: Try again.", Toast.LENGTH_SHORT).show();
														}
													}
												});
											}
										}
									});
		
									alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											// Canceled.
										}
									});
		
									alert.show();
								}
							});
						}
						else{
							btnEditListName.setOnClickListener(new OnClickListener() {
	
								@Override
								public void onClick(View v) {
									Toast.makeText(getApplicationContext(), "You dont own this list. You can't change it.", Toast.LENGTH_SHORT).show();
								}
								
							});
						}
					}
					else{
						Toast.makeText(getApplicationContext(), "Error Retreiving: Try again.", Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			});
		}
		else{
			btnEditListName.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					AlertDialog.Builder alert = new AlertDialog.Builder(EditListDetailsActivity.this);

					alert.setTitle("List Name");

					// Set an EditText view to get user input
					final EditText input = new EditText(EditListDetailsActivity.this);
					input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
					alert.setView(input);

					alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String listName = input.getText().toString();
							if(!listName.isEmpty()){
								pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								pdMain.setCancelable(false);
								pdMain.setMessage("Loading List");
								pdMain.show();
								
								listObject = new ListObject();
								listObject.setName(listName);
								listObject.setUser(currentUser);
								listObject.saveInBackground(new SaveCallback(){
									@Override
									public void done(ParseException e) {
										pdMain.dismiss();
										if(e==null){
											btnEditListName.setText(listObject.getName());
											nameChanged=true;
										}
										else{
											Toast.makeText(EditListDetailsActivity.this, "Error Saving: Try again.", Toast.LENGTH_SHORT).show();
										}
									}
								});
							}
						}
					});

					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							// Canceled.
						}
					});

					alert.show();
				}
			});
		}
		
		btnAddShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	@Override
	public void onBackPressed() {
		if(nameChanged==true){
			Intent i = new Intent();
			i.putExtra("nameChanged", true);
			setResult(RESULT_OK, i);
		}
		finish();
	}

}
