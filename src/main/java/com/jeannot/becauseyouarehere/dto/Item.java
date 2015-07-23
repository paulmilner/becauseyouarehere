package com.jeannot.becauseyouarehere.dto;

public class Item {

	private final long id; //local ID
	private final String itemId; //natural key for the provider
	private final String name;

    public Item(long id, String itemId, String name) {
    	this.id = id;
    	this.itemId = itemId;
    	this.name = name;
    }

	public long getId() {
		return id;
	}

	public String getItemId() {
		return itemId;
	}

	public String getName() {
		return name;
	}

}
