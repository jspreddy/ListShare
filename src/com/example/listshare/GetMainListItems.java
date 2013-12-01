package com.example.listshare;

import java.util.ArrayList;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GetMainListItems extends AsyncTask<Void, Void, ArrayList<MainList>>{

	ProgressDialog pdMain;
	Activity act;
	
	public GetMainListItems(HomeActivity homeActivity) {
		this.act = homeActivity;
	}
	
	@Override
	protected void onPreExecute() {
		pdMain = new ProgressDialog(this.act);
		pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdMain.setCancelable(false);
		pdMain.setMessage("Loading List");
		pdMain.show();
	}

	@Override
	protected ArrayList<MainList> doInBackground(Void... params) {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			//ParseQuery<ParseObject> q1 = ParseQuery.getQuery("User");
			//q1.whereEqualTo("objectId", currentUser.getString("objectId"));
			ParseQuery<ParseObject> q2 = ParseQuery.getQuery("Shares");
			q2.whereEqualTo("uid_fk", currentUser.getString("objectId"));
			
		}
		return null;
	}

	@Override
	protected void onPostExecute(ArrayList<MainList> result) {
		pdMain.dismiss();
		((HomeActivity) act).DisplayListContents(result);
	}
}
