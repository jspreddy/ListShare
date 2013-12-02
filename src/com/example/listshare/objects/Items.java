package com.example.listshare.objects;

public class Items {

	String name, editedBy, unit,id ;
	double quantity;
	int count;
	
	public Items(String id, String name, String editedBy, String unit, double quantity, int count) {
		this.id = id;
		this.name = name;
		this.editedBy = editedBy;
		this.unit = unit;
		this.quantity = quantity;
		this.count = count;
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
	
	

}
