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

/**
 * A couple of test methods to create and read some test data, to try stuff out with...
 * (Both use the GET HTTP method) 
 *
 */
@RestController
public class TestDataController {
	
	private static Logger LOG = Logger.getLogger(TestDataController.class);
	
    private static enum RelTypes implements RelationshipType{
        KNOWS
    }

    private static enum MyLabels implements Label {
    	PERSON,KNOWN_PERSON 
    }
    
    @Autowired
    Application app; //TODO sort out the object usages!

    private GraphDatabaseService db;	
	
    @RequestMapping(value="/create-data",method=RequestMethod.GET)
    public void createData() {
		//This is to see if it'll do something with Neo4j...
    	//NB this will throw NPEs if you try to do this too soon before Neo4J has had a chance
    	//to get going...
		db = app.neo4j.db; //TODO sort out...	    		
		try ( Transaction tx = db.beginTx() ) {
			LOG.debug("Creating data...");
    		Map<String,List<String>> people = new HashMap<String,List<String>>();
            people.put("Paul", Arrays.<String>asList("Helen","Penny"));
            people.put("Helen", Arrays.<String>asList("Paul","Jezz"));
            people.put("Penny", Arrays.<String>asList("Helen","Jezz"));
            people.put("Jezz", Arrays.<String>asList("Penny"));
            
            for (String person : people.keySet()) {
				Node personNode = db.createNode(MyLabels.PERSON);
				personNode.setProperty("name",person);
				LOG.debug("Creating data... person=" + person);
			}

            for (String person : people.keySet()) {
            	List<String> knows = people.get(person);
            	for (String knownPerson : knows) {
					Node personNode = db.findNode(MyLabels.PERSON, "name", person);
					Node knownPersonNode = db.createNode(MyLabels.KNOWN_PERSON);
					knownPersonNode.setProperty("name", knownPerson);
					personNode.createRelationshipTo(knownPersonNode, RelTypes.KNOWS);
					LOG.debug("Creating data... relationship=" + person + " knows " + knownPerson);
            	}
			}
			tx.success();
		}
    }

    @RequestMapping(value="/read-data",method=RequestMethod.GET)
    public Location readData() {
		//This is to see if it'll do something with Neo4j...
    	Location location = null;
		db = app.neo4j.db; //TODO sort out...	    		
		Set<Item> items = new HashSet<Item>();
		try ( Transaction tx = db.beginTx() ) {
        //read my data
		LOG.debug("Reading data...");
        ResourceIterator<Node> personNodes = db.findNodes(MyLabels.PERSON);
        while (personNodes.hasNext()) {
        	Node personNode = personNodes.next();
        	String personName = (String) personNode.getProperty("name");
        	LOG.debug("Person: " + personName);
        	Iterable<Relationship> relationships = personNode.getRelationships();
        	long counter = 0L;
        	for (Relationship relationship : relationships) {
    			LOG.debug("Reading relationship...");
				LOG.debug(relationship.getType().name() + " " + relationship.getEndNode().getProperty("name"));
				Item item = new Item(
						counter++, 
						personName + " " + relationship.getType().name(), 
						(String)relationship.getEndNode().getProperty("name")
						);
				items.add(item);
			}
        }

		location = new Location(0,0,items);
		tx.success();
		}
		return location;
    }

}