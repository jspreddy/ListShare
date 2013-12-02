package com.example.listshare.objects;

import java.io.ObjectInputStream.GetField;

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

	public String getEditedBy() {
		return getString("editedBy");
	}

	public String getUnit() {
		return getString("unit");
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
