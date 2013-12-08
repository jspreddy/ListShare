package com.example.listshare.objects;

import com.parse.*;

@ParseClassName("Shares")
public class SharesObject extends ParseObject{
	public SharesObject(){}

	
	public String getId(){
		return getObjectId();
	}
	
	public String getSharedWithUsername(){
		return getParseUser("UserId_fk").getUsername();
	}
	
	public static ParseQuery<SharesObject> getQuery() {
		return ParseQuery.getQuery(SharesObject.class);
	}
	
	public void setUser(ParseUser user){
		put("UserId_fk",user);
	}
	public void setList(ListObject obj){
		put("ListId_fk",obj);
	}
}
