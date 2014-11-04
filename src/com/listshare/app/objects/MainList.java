package com.listshare.app.objects;

public class MainList {
	String name, owner, id;
	
	public MainList(String name, String owner, String id)
	{
		this.name = name;
		this.owner = owner;
		this.id=id;
	}

	public String getTitle() {
		return this.name;
	}

	public String getOwner() {
		return this.owner;
	}

	public String getId() {
		return this.id;
	}
	
}
