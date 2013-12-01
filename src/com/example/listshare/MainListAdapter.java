package com.example.listshare;

import java.util.ArrayList;
import android.widget.ArrayAdapter;

public class MainListAdapter extends ArrayAdapter<MainList>{

	HomeActivity homeActivity;
	ArrayList<MainList> list;
	public MainListAdapter(HomeActivity homeActivity, ArrayList<MainList> list) {
		super(homeActivity, R.layout.main_list, list);
		this.homeActivity = homeActivity;
		this.list = list;
	}

}
