package com.example.listshare;

import java.util.ArrayList;
import java.util.List;

import com.example.listshare.HomeActivity.ListAdapter;
import com.example.listshare.HomeActivity.MainViewHolder;
import com.example.listshare.objects.Items;
import com.example.listshare.objects.ListItemsObject;
import com.example.listshare.objects.ListObject;
import com.example.listshare.objects.MainList;
import com.example.listshare.objects.SharesObject;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewListActivity extends Activity {

	ListView listView;
	Button b;
	String listId;
	Intent i;
	ParseUser currentUser;
	ProgressDialog pdMain;
	ArrayList<Items> listofItem;
	ListObject listObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_list);

		if(getIntent().getExtras()!= null){
			listId = getIntent().getExtras().getString("list_id");
		}
		else{
			finish();
		}
		currentUser = ParseUser.getCurrentUser();
		pdMain=new ProgressDialog(ViewListActivity.this);
		listofItem=new ArrayList<Items>();
		
		listView = (ListView) findViewById(R.id.listView1);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				i = new Intent(ViewListActivity.this, AddItemActivity.class);
				i.putExtra("ListId", listId);
				i.putExtra("flag", 2);
				i.putExtra("ItemId", listofItem.get(arg2).getId());
				startActivity(i);
			}
		});

		DisplayListContents();

		b = (Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ViewListActivity.this, AddItemActivity.class);
				i.putExtra("ListId", listId);
				i.putExtra("flag", 1);
				i.putExtra("ItemId","");
				startActivity(i);
			}
		});
	}

	private void DisplayListContents() {
		pdMain.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdMain.setCancelable(false);
		pdMain.setMessage("Loading List");
		pdMain.show();
		
		if (currentUser != null) {
			
			
			ParseQuery<ListItemsObject> itemsQuery = ListItemsObject.getQuery();
			itemsQuery.include("editedBy");
			ListObject lo = new ListObject();
			lo.setObjectId(listId);
			try {
				lo.fetch();
			} catch (ParseException e2) {
				e2.printStackTrace();
			}
			itemsQuery.whereEqualTo("ListId_fk", lo);
			
			itemsQuery.findInBackground(new FindCallback<ListItemsObject>() {
				
				@Override
				public void done(List<ListItemsObject> item, ParseException e) {
					for(ListItemsObject obj : item){
						String id=obj.getId();
						String name = obj.getName();
						ParseUser editedBy = obj.getParseUser("editedBy");
						String username = editedBy.getUsername();
						String unit = obj.getUnit();
						Double qty = obj.getQuantity();
						int count = obj.getCount();
						Log.d("DEBUG","test");
						listofItem.add(new Items(obj.getId(),obj.getName(),editedBy.getUsername(),obj.getUnit(),obj.getQuantity(),obj.getCount()));
					}
					
					pdMain.dismiss();
					if(listofItem !=null && listofItem.size() != 0){
						ListItemAdapter adapter = new ListItemAdapter(ViewListActivity.this, listofItem);
						listView.setAdapter(adapter);
					}
					else{
						Toast.makeText(ViewListActivity.this, "No itemsto display", Toast.LENGTH_SHORT).show();
					}
				}
			});
		} else {
			Log.d("DEBUG", "No user logged in");
			i = new Intent(ViewListActivity.this, MainActivity.class);
			finish();
			startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_list, menu);
		return true;
	}

	class ListItemAdapter extends ArrayAdapter<Items>{

		
		Context context;
		ArrayList<Items> localList;
		
		public ListItemAdapter(Context context, ArrayList<Items> list) {
			super(context,R.layout.view_list_item, R.id.textView1, list);
			this.context = context;
			this.localList = list;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			ListItemViewHolder holder = null;
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.view_list_item, parent, false);
				holder = new ListItemViewHolder(row);
				row.setTag(holder);
			}
			else{
				holder = (ListItemViewHolder) row.getTag();
			}

			Items item = this.localList.get(position);
			holder.t1.setText(item.getName());
			holder.t2.setText(""+item.getCount());
			holder.t3.setText(""+item.getQuantity());
			holder.t4.setText(item.getUnit());
			holder.t5.setText(item.getEditedBy());
			return row;
			
		}

	}
	
	class ListItemViewHolder{
		public TextView t1,t2,t3,t4,t5;
		
		ListItemViewHolder(View row){
			t1= (TextView) row.findViewById(R.id.textView1);
			t2= (TextView) row.findViewById(R.id.textView2);
			t3= (TextView) row.findViewById(R.id.textView3);
			t4= (TextView) row.findViewById(R.id.textView4);
			t5= (TextView) row.findViewById(R.id.textView5);
		}
	}
}

