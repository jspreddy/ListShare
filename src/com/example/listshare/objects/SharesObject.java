package com.example.listshare.objects;

import com.parse.*;

@ParseClassName("Shares")
public class SharesObject extends ParseObject{
	public SharesObject(){}

	
	public String getId(){
		return getObjectId();
	}
	
	public static ParseQuery<SharesObject> getQuery() {
		return ParseQuery.getQuery(SharesObject.class);
	}
}
