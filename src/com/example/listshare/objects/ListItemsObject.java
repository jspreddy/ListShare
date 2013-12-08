package com.example.listshare.objects;

import com.parse.*;
import com.parse.ParseFacebookUtils.Permissions.User;

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

	public int getState() {
		return getInt("done");
	}

	public ParseUser getUser(){
		return getParseUser("editedBy");
	}
	
	public void setName(String val) {
		put("item_name", val);
	}

	public void setUnit(String val) {
		put("units", val);
	}

	public void setQuantity(double val) {
		put("quantity", val);
	}

	public void setCount(int val) {
		put("count", val);
	}

	public void setUser(ParseUser user) {
		put("editedBy", user);
	}

	public void setList(ListObject list){
		put("ListId_fk",list );
	}

	public void setState(int val) {
		put("done", val);
	}

	public static ParseQuery<ListItemsObject> getQuery() {
		return ParseQuery.getQuery(ListItemsObject.class);
	}

}
