package com.jeannot.becauseyouarehere.dto;

import java.util.Set;

public class Location {

    private final float latitude;
    private final float longitude;
    private final Set<Item> items;
    
    public Location(float latitude, float longitude, Set<Item> items) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.items = items;
    }

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}
	
	public Set<Item> getItems() {
		items.add(new Item(1L,"ID001",""));
		return items;
	}

}
