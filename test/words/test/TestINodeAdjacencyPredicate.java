package words.test;

import static org.junit.Assert.*;

import org.junit.Test;

import words.ast.*;
import words.environment.*;
import words.exceptions.*;

public class TestINodeAdjacencyPredicate extends TestINode {
	
	INodeSubject fredSubject = new INodeSubject(null, new INodeReferenceList(), fredStringLeaf);
	INodeSubject georgeSubject = new INodeSubject(null, new INodeReferenceList(), georgeStringLeaf);
	
	@Test
	public void knowsIfTwoObjectsAreAdjacent() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0,0));
		environment.createObject("George", "thing", new Position(0,0));
		loop.fastForwardEnvironment(1); //objects are created with a 1 frame wait, so use it up.
		
		INodeAdjacencyPredicate leftPredicate = new INodeAdjacencyPredicate(fredSubject, new LNodeIdentifier("alias1"), 
				leftDirectionLeaf, georgeSubject, new LNodeIdentifier("alias2"));
		INodeAdjacencyPredicate rightPredicate = new INodeAdjacencyPredicate(fredSubject, new LNodeIdentifier("alias1"), 
				rightDirectionLeaf, georgeSubject, new LNodeIdentifier("alias2"));
		INodeAdjacencyPredicate nextToPredicate = new INodeAdjacencyPredicate(fredSubject, new LNodeIdentifier("alias1"), 
				anywhereDirectionLeaf, georgeSubject, new LNodeIdentifier("alias2"));
	
		loop.enqueueAST(moveFredLeft2);
		loop.fastForwardEnvironment(1);
		assertEquals("Fred is left of Bob", leftPredicate.eval(environment, new INodeStatementList()).booleanValue, true);
		assertEquals("Fred is next to Bob", nextToPredicate.eval(environment, new INodeStatementList()).booleanValue, true);
		loop.fastForwardEnvironment(1);
		assertEquals("Fred is not left of Bob", leftPredicate.eval(environment, new INodeStatementList()).booleanValue, false);
		assertEquals("Fred is not next to Bob", nextToPredicate.eval(environment, new INodeStatementList()).booleanValue, false);
		loop.enqueueAST(moveFredRight2);
		loop.fastForwardEnvironment(2);
		
		loop.enqueueAST(moveFredRight2);
		loop.fastForwardEnvironment(1);
		assertEquals("Fred is right of Bob", rightPredicate.eval(environment, new INodeStatementList()).booleanValue, true);
		assertEquals("Fred is next to Bob", nextToPredicate.eval(environment, new INodeStatementList()).booleanValue, true);
		loop.fastForwardEnvironment(1);
		environment.getVariable("Fred").objValue.moveRight();
		assertEquals("Fred is not right of Bob", rightPredicate.eval(environment, new INodeStatementList()).booleanValue, false);
		assertEquals("Fred is not next to Bob", nextToPredicate.eval(environment, new INodeStatementList()).booleanValue, false);
		loop.enqueueAST(moveFredLeft2);
		loop.fastForwardEnvironment(2);
	}
	
	@Test
	public void knowsIfTwoThingsAreAdjacent() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0,0));
		environment.createObject("George", "thing", new Position(0,0));
		loop.fastForwardEnvironment(1); //objects are created with a 1 frame wait, so use it up.
		
		INodeAdjacencyPredicate leftPredicate = new INodeAdjacencyPredicate(thingStringLeaf, new LNodeIdentifier("alias1"), 
				leftDirectionLeaf, thingStringLeaf, new LNodeIdentifier("alias2"));
		INodeAdjacencyPredicate rightPredicate = new INodeAdjacencyPredicate(thingStringLeaf, new LNodeIdentifier("alias1"), 
				rightDirectionLeaf, thingStringLeaf, new LNodeIdentifier("alias2"));
		INodeAdjacencyPredicate nextToPredicate = new INodeAdjacencyPredicate(thingStringLeaf, new LNodeIdentifier("alias1"), 
				anywhereDirectionLeaf, thingStringLeaf, new LNodeIdentifier("alias2"));
	
		loop.enqueueAST(moveFredRight2);
		loop.fastForwardEnvironment(1);
		assertEquals("A thing is left of a thing", leftPredicate.eval(environment, new INodeStatementList()).booleanValue, true);
		assertEquals("A thing is right of a thing", rightPredicate.eval(environment, new INodeStatementList()).booleanValue, true);
		assertEquals("A thing is next to a thing", nextToPredicate.eval(environment, new INodeStatementList()).booleanValue, true);
		loop.fastForwardEnvironment(1);
		assertEquals("A thing is not left of a thing", leftPredicate.eval(environment, new INodeStatementList()).booleanValue, false);
		assertEquals("A thing is not right of a thing", rightPredicate.eval(environment, new INodeStatementList()).booleanValue, false);
		assertEquals("A thing is not next to a thing", nextToPredicate.eval(environment, new INodeStatementList()).booleanValue, false);		
	}
	
	@Test
	public void adjacencyShouldOnlyTriggerFirstTime() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(1,0));
		environment.createObject("George", "thing", new Position(0,0));
		loop.fastForwardEnvironment(1); //objects are created with a 1 frame wait, so use it up.
		
		INodeAdjacencyPredicate nextToPredicate = new INodeAdjacencyPredicate(fredSubject, new LNodeIdentifier("alias1"), 
				anywhereDirectionLeaf, georgeSubject, new LNodeIdentifier("alias2"));
	
		loop.enqueueAST(moveFredLeft2);
		loop.fastForwardEnvironment(2);
		assertEquals("Fred is next to Bob", nextToPredicate.eval(environment, new INodeStatementList()).booleanValue, true);
		loop.fastForwardEnvironment(1);
		assertEquals("Fred is not next to Bob", nextToPredicate.eval(environment, new INodeStatementList()).booleanValue, false);
	}
	
	@Test
	public void touchingObjectsAreNotAdjacent() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0,0));
		environment.createObject("George", "thing", new Position(0,0));
		loop.fastForwardEnvironment(1); //objects are created with a 1 frame wait, so use it up.
		
		INodeAdjacencyPredicate testPredicate = new INodeAdjacencyPredicate(thingStringLeaf, new LNodeIdentifier("alias1"), 
				anywhereDirectionLeaf, thingStringLeaf, new LNodeIdentifier("alias2"));
		
		assertEquals("Two things in same location eval false", testPredicate.eval(environment, new INodeStatementList()).booleanValue, false);
	}

	@Test (expected = AliasException.class)
	public void twoAliasesCannotBeSame() throws WordsRuntimeException {
		INodeAdjacencyPredicate adjacencyPred = new INodeAdjacencyPredicate(thingStringLeaf, new LNodeIdentifier("alias"), 
				anywhereDirectionLeaf, thingStringLeaf, new LNodeIdentifier("alias"));
		adjacencyPred.eval(environment, statementsAboutFred);
	}
}
