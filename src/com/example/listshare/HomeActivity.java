package com.example.listshare;

import java.util.ArrayList;
import java.util.List;

import com.example.listshare.objects.ListObject;
import com.example.listshare.objects.MainList;
import com.example.listshare.objects.SharesObject;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class HomeActivity extends Activity {

	ListView list;
	Button b;
	Intent i; 
	ArrayAdapter<MainList> adapter;
	ArrayList<MainList> listOfList;
	ProgressDialog pdMain;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		pdMain=new ProgressDialog(HomeActivity.this);
		listOfList=new ArrayList<MainList>();
		
		list = (ListView) findViewById(R.id.listView1);
		//new GetMainListItems(HomeActivity.this).execute();
		DisplayListContents();
		b = (Button) findViewById(R.id.button1);
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				i = new Intent(HomeActivity.this,EditListDetailsActivity.class);
				startActivity(i);
			}
		});
		
	}

	public void DisplayListContents() {
		// Get current User
		pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdMain.setCancelable(false);
		pdMain.setMessage("Loading List");
		pdMain.show();
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			
			ParseQuery<SharesObject> query = SharesObject.getQuery();
			
			query.whereEqualTo("UserId_fk", currentUser);
			query.include("UserId_fk");
			query.include("ListId_fk");
			
			query.findInBackground(new FindCallback<SharesObject>() {
				@Override
				public void done(List<SharesObject> arg0, ParseException arg1) {
					for(SharesObject obj : arg0){
						ParseUser user = obj.getParseUser("UserId_fk");
						ListObject listObj = (ListObject) obj.getParseObject("ListId_fk");
						listOfList.add( new MainList(listObj.getName(), user.getUsername(), listObj.getId()) );
					}
					
					pdMain.dismiss();
				}
			});
			
		} else {
			Log.d("DEBUG", "No user loggrd in");
			i = new Intent(HomeActivity.this, MainActivity.class);
			finish();
			startActivity(i);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
