package com.jeannot.becauseyouarehere.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeannot.becauseyouarehere.Application;
import com.jeannot.becauseyouarehere.dto.Item;
import com.jeannot.becauseyouarehere.dto.Location;

@RestController
public class LocationController {
	
	private static Logger LOG = Logger.getLogger(LocationController.class);
	
    private static enum RelTypes implements RelationshipType{
        KNOWS
    }

    private static enum MyLabels implements Label {
    	PERSON,KNOWN_PERSON 
    }
    
    @Autowired
    Application app; //TODO sort out the object usages!

    private GraphDatabaseService db;	
	
    @RequestMapping(value="/location",method=RequestMethod.POST)
    public Location createLocation(
    						@RequestParam(value="lat", defaultValue="51.1") float latitude,
    						@RequestParam(value="long", defaultValue="0") float longitude) {
    	
    	Location location = null;
		Set<Item> items = new HashSet<Item>();
		location = new Location(latitude,longitude,items);
        return location;
    }

    @RequestMapping(value="/location",method=RequestMethod.GET)
    public Location getLocation(@RequestParam(value="id") long id) {
    	
    	Location location = null;
		Set<Item> items = new HashSet<Item>();
		location = new Location(0,0,items);
        return location;
    }

    @RequestMapping(value="/location",method=RequestMethod.DELETE)
    public void deleteLocation(@RequestParam(value="id") long id) {
    	//TODO
    }

}