package com.listshare.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.listshare.app.objects.ListObject;
import com.listshare.app.objects.SharesObject;
import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ListInfoActivity extends BaseActivity {

	Button btnAddShare;
	TextView tvEditListName;
	
	String list_id;
	ListObject listObject;
	ParseUser shareWithUser;
	ProgressDialog pdMain;
	Boolean nameChanged = false;
	ListView lvSharesList;
	ParseQueryAdapter<SharesObject> sharesListadapter;
	ParseQueryAdapter.QueryFactory<SharesObject> sharesListFactory; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_info);
		
		Intent intent = getIntent();
		list_id = intent.getStringExtra("list_id");
		pdMain=new ProgressDialog(ListInfoActivity.this);
		
		tvEditListName = (TextView) findViewById(R.id.btnEditListName);
		btnAddShare = (Button) findViewById(R.id.btnAddShare);
		lvSharesList = (ListView) findViewById(R.id.lvSharesList);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		if(list_id==null)
		{
			// activity is being used to create new list.
			tvEditListName.setOnClickListener(new AddListNameListener());
			btnAddShare.setOnClickListener(new ShareButtonListner());
			return;
		}
		
		pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdMain.setCancelable(false);
		pdMain.setMessage("Loading list details.");
		pdMain.show();
		
		ParseQuery<ListObject> listQuery = ListObject.getQuery();
		listQuery.include("createdBy");
		listQuery.getInBackground(list_id, new GetCallback<ListObject>(){
			@Override
			public void done(ListObject arg0, ParseException exception) {
				if(pdMain != null) pdMain.dismiss();
				if(exception != null || arg0 == null){
					Toast.makeText(getApplicationContext(), "Error Retreiving: Try again.", Toast.LENGTH_SHORT).show();
					finish();
				}
				
				listObject = arg0;
				tvEditListName.setText(listObject.getName());
				
				loadSharesData();
				
				ParseUser createdBy = listObject.getParseUser("createdBy"); 
				if(createdBy.getObjectId().equals(getCurrentUser().getObjectId())){
					tvEditListName.setOnClickListener(new EditListNameListner());
					btnAddShare.setOnClickListener(new ShareButtonListner());
					lvSharesList.setOnItemLongClickListener(new SharesListItemLongClockListener());
				}
				else{
					ViewGroup parentView_btnAddShare = (ViewGroup) btnAddShare.getParent();
					parentView_btnAddShare.removeView(btnAddShare);
					
					tvEditListName.setBackgroundColor(Color.LTGRAY);
					tvEditListName.setTextColor(Color.BLACK);
					tvEditListName.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(ListInfoActivity.this, "You don't own this list. You can't change it.", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		});
	}
	
	public void loadSharesData(){
		sharesListFactory = new ParseQueryAdapter.QueryFactory<SharesObject>() {
			@Override
			public ParseQuery<SharesObject> create() {
				ParseQuery<SharesObject> sharesQuery = SharesObject.getQuery();
				sharesQuery.whereEqualTo("ListId_fk", listObject);
				sharesQuery.include("UserId_fk");
				return sharesQuery;
			}
		};
		
		sharesListadapter = new ParseQueryAdapter<SharesObject>(ListInfoActivity.this, sharesListFactory){
			@Override
			public View getItemView(SharesObject object, View v, ViewGroup parent) {
				
				if (v == null) {
					v = View.inflate(getBaseContext(), R.layout.share_list_item, null);
				}
				TextView descriptionView = (TextView) v.findViewById(R.id.tvSharedWithUserName);
				descriptionView.setText(object.getSharedWithUsername());
				return v;
			}
		};
		lvSharesList.setAdapter(sharesListadapter);
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

	class ShareButtonListner implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(listObject != null){
				AlertDialog.Builder alert = new AlertDialog.Builder(ListInfoActivity.this);

				alert.setTitle("Share with User:");
				alert.setMessage("Enter the username of the person you want to share the list: "+listObject.getName());

				// Set an EditText view to get user input
				final EditText input = new EditText(ListInfoActivity.this);
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				
				alert.setView(input);
				
				alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String uname = input.getText().toString();
						
						if(uname.equals(getCurrentUser().getUsername())){
							Toast.makeText(ListInfoActivity.this, "You can't share your list with yourself.", Toast.LENGTH_LONG).show();
						}
						else if(!uname.isEmpty()){
							pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							pdMain.setCancelable(false);
							pdMain.setMessage("Sharing");
							pdMain.show();
							
							ParseQuery<ParseUser> shareUserQuery = ParseUser.getQuery();
							shareUserQuery.whereEqualTo("username", uname);
							
							shareUserQuery.getFirstInBackground(new GetCallback<ParseUser>(){
								@Override
								public void done(ParseUser arg0, ParseException arg1) {
									if(arg0 != null && arg1==null){
										//user exists.
										shareWithUser=arg0;
										ParseQuery<SharesObject> shareQuery = SharesObject.getQuery();
										shareQuery.whereEqualTo("ListId_fk", listObject);
										shareQuery.whereEqualTo("UserId_fk", shareWithUser);
										
										shareQuery.countInBackground(new CountCallback(){
											@Override
											public void done(int arg0, ParseException arg1) {
												if(arg0 == 0){
													//share doesn't exist. Fresh Share.
													SharesObject sharesObject= new SharesObject();
													sharesObject.setList(listObject);
													sharesObject.setUser(shareWithUser);
													sharesObject.saveInBackground(new SaveCallback(){
														@Override
														public void done(ParseException e) {
															if(pdMain != null) pdMain.dismiss();
															if(e==null){
																Toast.makeText(ListInfoActivity.this, "List has been shared.", Toast.LENGTH_SHORT).show();
																sharesListadapter.loadObjects();
																sharesListadapter.notifyDataSetChanged();
															}
															else{
																Toast.makeText(ListInfoActivity.this, "Error Saving: Try again.", Toast.LENGTH_SHORT).show();
															}
														}
													});
												}
												else{
													if(pdMain != null) pdMain.dismiss();
													Toast.makeText(ListInfoActivity.this, "You have already shared the list with this user.", Toast.LENGTH_SHORT).show();
												}
											}
										});
										
									}
									else if(arg0==null){
										if(pdMain != null) pdMain.dismiss();
										Toast.makeText(ListInfoActivity.this, "User doesn't exist.", Toast.LENGTH_LONG).show();
									}
									else{
										if(pdMain != null) pdMain.dismiss();
										Toast.makeText(ListInfoActivity.this, "Error. Try again.", Toast.LENGTH_LONG).show();
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
			else{
				Toast.makeText(ListInfoActivity.this, "You can only share an existing list.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	class EditListNameListner implements OnClickListener{

		@Override
		public void onClick(View v) {
			AlertDialog.Builder alert = new AlertDialog.Builder(ListInfoActivity.this);
			
			alert.setTitle("Change list name to:");

			// Set an EditText view to get user input
			final EditText input = new EditText(ListInfoActivity.this);
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
			input.setText(listObject.getName());
			alert.setView(input);

			alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String listName = input.getText().toString();
					if(!listName.isEmpty()){
						pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						pdMain.setCancelable(false);
						pdMain.setMessage("Changing list name");
						pdMain.show();
						listObject.setName(listName);
						listObject.saveInBackground(new SaveCallback(){
							@Override
							public void done(ParseException e) {
								if(pdMain != null) pdMain.dismiss();
								if(e==null){
									tvEditListName.setText(listObject.getName());
									nameChanged=true;
								}
								else{
									Toast.makeText(ListInfoActivity.this, "Error Saving: Try again.", Toast.LENGTH_SHORT).show();
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
		
	}

	class AddListNameListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			AlertDialog.Builder alert = new AlertDialog.Builder(ListInfoActivity.this);

			alert.setTitle("Add List with name:");

			// Set an EditText view to get user input
			final EditText input = new EditText(ListInfoActivity.this);
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
			alert.setView(input);

			alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String listName = input.getText().toString();
					if(!listName.isEmpty()){
						pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						pdMain.setCancelable(false);
						pdMain.setMessage("Adding new List");
						pdMain.show();
						
						listObject = new ListObject();
						listObject.setName(listName);
						listObject.setUser(getCurrentUser());
						listObject.saveInBackground(new SaveCallback(){
							@Override
							public void done(ParseException e) {
								if(pdMain != null) pdMain.dismiss();
								if(e==null){
									tvEditListName.setText(listObject.getName());
									nameChanged=true;
									Toast.makeText(ListInfoActivity.this, "List Created", Toast.LENGTH_SHORT).show();
								}
								else{
									Toast.makeText(ListInfoActivity.this, "Error Saving: Try again.", Toast.LENGTH_SHORT).show();
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
	}

	class SharesListItemLongClockListener implements AdapterView.OnItemLongClickListener {
		SharesObject item;
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			item = sharesListadapter.getItem(position);
			
			AlertDialog.Builder alert = new AlertDialog.Builder(ListInfoActivity.this);
			alert.setTitle("Revoke share from: "+item.getSharedWithUsername()+" ?");
			
			alert.setPositiveButton("Revoke", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					item.deleteInBackground(new DeleteCallback(){
						@Override
						public void done(ParseException arg0) {
							if(arg0==null){
								sharesListadapter.loadObjects();
								sharesListadapter.notifyDataSetChanged();
								Toast.makeText(ListInfoActivity.this, "Removed successfully", Toast.LENGTH_SHORT).show();
							}
							else{
								Toast.makeText(ListInfoActivity.this, "Error. Refresh and try again.", Toast.LENGTH_SHORT).show();
							}
						}
				    });
				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {}
			});

			alert.show();

			return true;
		}
	}
}
