package com.listshare.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.listshare.app.objects.ListObject;
import com.listshare.app.objects.MainList;
import com.listshare.app.objects.SharesObject;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

	ListView lvMainList;
	Button btnAddList;
	Intent i; 
	ArrayAdapter<MainList> adapter;
	ArrayList<MainList> mainList;
	ProgressDialog pdMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		pdMain=new ProgressDialog(HomeActivity.this);
		mainList=new ArrayList<MainList>();
		
		lvMainList = (ListView) findViewById(R.id.lvMainList);
		lvMainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent i = new Intent(HomeActivity.this,ViewListActivity.class);
				i.putExtra("list_id", mainList.get(arg2).getId());
				startActivity(i);
			}
			
		});
		lvMainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//TODO: ask for edit or delete?
				Intent i = new Intent(HomeActivity.this,ListInfoActivity.class);
				i.putExtra("list_id", mainList.get(arg2).getId());
				startActivityForResult(i,0);
				return true;
			}
			
		});
		
		DisplayListContents();
		
		btnAddList = (Button) findViewById(R.id.btnListAdd);
		
		btnAddList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				i = new Intent(HomeActivity.this,ListInfoActivity.class);
				startActivityForResult(i,0);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==0 && resultCode==RESULT_OK){
			Boolean nameChanged = data.getBooleanExtra("nameChanged", false);
			if(nameChanged){
				DisplayListContents();
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void DisplayListContents() {
		mainList.clear();
		pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdMain.setCancelable(false);
		pdMain.setMessage("Loading List");
		pdMain.show();
		
		ParseQuery<SharesObject> sharesQuery = SharesObject.getQuery();
		
		sharesQuery.whereEqualTo("UserId_fk", getCurrentUser());
		sharesQuery.include("UserId_fk");
		sharesQuery.include("ListId_fk");
		
		sharesQuery.findInBackground(new FindCallback<SharesObject>() {
			@Override
			public void done(List<SharesObject> arg0, ParseException arg1) {
				if(arg1 == null){
					for(SharesObject obj : arg0){
						ListObject listObj = (ListObject) obj.getParseObject("ListId_fk");
						ParseUser owner = listObj.getParseUser("createdBy");
						try {
							owner.fetchIfNeeded();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						mainList.add( new MainList(listObj.getName(), owner.getUsername(), listObj.getId()) );
					}
				}
				
				ParseQuery<ListObject> listQuery = ListObject.getQuery();
				listQuery.whereEqualTo("createdBy", getCurrentUser());
				listQuery.include("createdBy");
				
				listQuery.findInBackground(new FindCallback<ListObject>(){
					@Override
					public void done(List<ListObject> arg0, ParseException arg1) {
						if(arg1 == null){
							for(ListObject obj : arg0){
								ParseUser user = obj.getParseUser("createdBy");
								mainList.add(new MainList(obj.getName(), user.getUsername(), obj.getId()));
							}
						}
						if(pdMain != null) pdMain.dismiss();
						
						if(mainList!=null && mainList.size() != 0){
							ListAdapter la = new ListAdapter(HomeActivity.this, mainList);
							lvMainList.setAdapter(la);
						}
						else{
							Toast.makeText(HomeActivity.this, "Nothing to Show", Toast.LENGTH_SHORT).show();
						}
					}
				});
				//*/
			}
		});
	}
	
	class ListAdapter extends ArrayAdapter<MainList>{
		Context context;
		ArrayList<MainList> localList;
		
		public ListAdapter(Context context, ArrayList<MainList> list){
			super(context,R.layout.main_list_item, R.id.tvMainTitle, list);
			this.context = context;
			this.localList = list;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			MainViewHolder holder = null;
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.main_list_item, parent, false);
				holder = new MainViewHolder(row);
				row.setTag(holder);
			}
			else{
				holder = (MainViewHolder) row.getTag();
			}
			
			MainList item = this.localList.get(position);
			holder.tvTitle.setText(item.getTitle());
			holder.tvOwner.setText(item.getOwner());
			
			return row;
		}
	}
	
	class MainViewHolder{
		public TextView tvTitle;
		public TextView tvOwner;
		
		MainViewHolder(View row){
			tvTitle = (TextView) row.findViewById(R.id.tvMainTitle);
			tvOwner = (TextView) row.findViewById(R.id.tvMainOwner);
		}
	}
}
