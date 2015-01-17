package com.listshare.app.objects;

public class Items {

	String name, editedBy, unit,id ;
	double quantity;
	int count, state;
	
	public Items(String id, String name, String editedBy, String unit, double quantity, int count, int state) {
		this.id = id;
		this.name = name;
		this.editedBy = editedBy;
		this.unit = unit;
		this.quantity = quantity;
		this.count = count;
		this.state = state;
	}

	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return name;
	}

	public String getEditedBy() {
		return editedBy;
	}

	public String getUnit() {
		return unit;
	}

	public double getQuantity() {
		return quantity;
	}

	public int getCount() {
		return count;
	}
	
	public int getState() {
		return state;
	}

}
