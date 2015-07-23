/**
 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.jeannot.becauseyouarehere.dao;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

public class EmbeddedNeo4j
{
    private static final String DB_PATH = "target/neo4j-becauseyouarehere-db";

    public String greeting;

    // START SNIPPET: vars
    public GraphDatabaseService db;
    Node firstNode;
    Node secondNode;
    Relationship relationship;

    private static enum RelTypes implements RelationshipType{
        KNOWS
    }

    private static enum MyLabels implements Label {
    	PERSON,KNOWN_PERSON 
    }
    
    //When embedded in the Spring Boot app, can't have a main() method
//    public static void main( final String[] args ) throws IOException
//    {
//        EmbeddedNeo4j hello = new EmbeddedNeo4j();
//        hello.createDb();
//        hello.removeData();
//        hello.shutDown();
//    }

    public void createDb() throws IOException
    {
        FileUtils.deleteRecursively( new File( DB_PATH ) );

        // START SNIPPET: startDb
        db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        registerShutdownHook( db );
        // END SNIPPET: startDb

//        // START SNIPPET: transaction
//        try ( Transaction tx = db.beginTx() )
//        {
//            // Database operations go here
//            // END SNIPPET: transaction
//            // START SNIPPET: addData
//            firstNode = db.createNode();
//            firstNode.setProperty( "message", "Hello, " );
//            secondNode = db.createNode();
//            secondNode.setProperty( "message", "World!" );
//            
//            Map<String,List<String>> people = new HashMap<String,List<String>>();
//            people.put("Paul", Arrays.<String>asList("Helen","Penny"));
//            people.put("Helen", Arrays.<String>asList("Paul","Jezz"));
//            people.put("Penny", Arrays.<String>asList("Helen","Jezz"));
//            people.put("Jezz", Arrays.<String>asList("Penny"));
//            
//            for (String person : people.keySet()) {
//				Node personNode = db.createNode(MyLabels.PERSON);
//				personNode.setProperty("name",person);
//			}
//
//            for (String person : people.keySet()) {
//            	List<String> knows = people.get(person);
//            	for (String knownPerson : knows) {
//					Node personNode = db.findNode(MyLabels.PERSON, "name", person);
//					Node knownPersonNode = db.createNode(MyLabels.KNOWN_PERSON);
//					knownPersonNode.setProperty("name", knownPerson);
//					personNode.createRelationshipTo(knownPersonNode, RelTypes.KNOWS);
//            	}
//			}
//
//            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
//            relationship.setProperty( "message", "brave Neo4j " );
//            // END SNIPPET: addData
//
//            // START SNIPPET: readData
////            System.out.print( firstNode.getProperty( "message" ) );
////            System.out.print( relationship.getProperty( "message" ) );
////            System.out.print( secondNode.getProperty( "message" ) );
//            // END SNIPPET: readData
//
////            greeting = ( (String) firstNode.getProperty( "message" ) )
////                       + ( (String) relationship.getProperty( "message" ) )
////                       + ( (String) secondNode.getProperty( "message" ) );
//
//            //read my data
//            ResourceIterator<Node> personNodes = db.findNodes(MyLabels.PERSON);
//            while (personNodes.hasNext()) {
//            	Node personNode = personNodes.next();
//            	System.out.println("Person: " + personNode.getProperty("name"));
//            	Iterable<Relationship> relationships = personNode.getRelationships();
//            	for (Relationship relationship : relationships) {
//					System.out.println(relationship.getType().name() + " " + relationship.getEndNode().getProperty("name"));
//				}
//            }
//            
//            // START SNIPPET: transaction
//            tx.success();
//        }
//        // END SNIPPET: transaction
    }

    void removeData()
    {
        try ( Transaction tx = db.beginTx() )
        {
            // START SNIPPET: removingData
            // let's remove the data
            firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
            firstNode.delete();
            secondNode.delete();
            // END SNIPPET: removingData

            tx.success();
        }
    }

    void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        db.shutdown();
        // END SNIPPET: shutdownServer
    }

    // START SNIPPET: shutdownHook
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
    // END SNIPPET: shutdownHook
}