package com.example.listshare.objects;

import com.parse.*;

@ParseClassName("ListItems")
public class ListItemsObject extends ParseObject{
	public ListItemsObject(){}
	
	
	public String getId(){
		return getObjectId();
	}
	
	public String getName() {
		return getString("item_name");
	}

	public String getUnit() {
		return getString("units");
	}

	public double getQuantity() {
		return getDouble("quantity");
	}

	public int getCount() {
		return getInt("count");
	}

	
	public static ParseQuery<ListItemsObject> getQuery() {
		return ParseQuery.getQuery(ListItemsObject.class);
	}
}
