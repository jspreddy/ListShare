package com.listshare.app.objects;

import com.parse.*;

@ParseClassName("List")
public class ListObject extends ParseObject{
	
	public ListObject(){}
	
	public String getName(){
		return getString("list_name");
	}
	
	public void setName(String value){
		put("list_name", value);
	}
	
	public String getId(){
		return getObjectId();
	}
	
	public void setUser(ParseUser user){
		put("createdBy",user);
	}
	
	public static ParseQuery<ListObject> getQuery() {
		return ParseQuery.getQuery(ListObject.class);
	}
	
	
}
