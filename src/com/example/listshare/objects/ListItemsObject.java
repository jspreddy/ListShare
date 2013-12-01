package com.example.listshare.objects;

import com.parse.*;

@ParseClassName("ListItems")
public class ListItemsObject extends ParseObject{
	public ListItemsObject(){}
	
	
	public String getId(){
		return getObjectId();
	}
	
	public static ParseQuery<ListItemsObject> getQuery() {
		return ParseQuery.getQuery(ListItemsObject.class);
	}
}
