package words.test;

import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.environment.*;
import words.exceptions.*;

public class TestINodeReferenceList extends TestINode {
	
	@Test
	public void testSizeZero() throws WordsRuntimeException {
		INodeReferenceList refList = new INodeReferenceList();
		Variable nothingValue = refList.eval(environment);
		
		assertEquals(nothingValue.type, Variable.Type.NOTHING);
	}
	
	@Test
	public void testSizeOne() throws WordsRuntimeException {
		WordsObject alexObject = environment.createObject("Alex", "thing", new Position(0, 0));
		LNodeReference alexRef = new LNodeReference("Alex's");
		INodeReferenceList alexRefList = new INodeReferenceList(alexRef);
		
		Variable alexValue = alexRefList.eval(environment);
		assertEquals(alexValue.objValue, alexObject);
	}
	
	@Test
	public void testSizeTwo() throws WordsRuntimeException {
		WordsObject alexObject = environment.createObject("Alex", "thing", new Position(0, 0));
		LNodeReference alexRef = new LNodeReference("Alex's");
		
		WordsObject bobObject = environment.createObject("Bob", "thing", new Position(0, 0));
		alexObject.setProperty("friend", new Variable(bobObject));
		
		LNodeReference friendRef = new LNodeReference("friend");
		INodeReferenceList refList = new INodeReferenceList(alexRef, friendRef);
		
		Variable alexFriendValue = refList.eval(environment);
		assertEquals(alexFriendValue.objValue, bobObject);
	}
	
	@Test
	public void testSizeThree() throws WordsRuntimeException {
		WordsObject alexObject = environment.createObject("Alex", "thing", new Position(0, 0));
		LNodeReference alexRef = new LNodeReference("Alex's");
		
		WordsObject bobObject = environment.createObject("Bob", "thing", new Position(0, 0));
		alexObject.setProperty("friend", new Variable(bobObject));
		LNodeReference friendRef = new LNodeReference("friend's");
		
		WordsObject chrisObject = environment.createObject("Chris", "thing", new Position(0, 0));
		bobObject.setProperty("enemy", new Variable(chrisObject));
		LNodeReference enemyRef = new LNodeReference("enemy");
		
		INodeReferenceList refList = new INodeReferenceList(alexRef, friendRef, enemyRef);
		
		Variable alexFriendValue = refList.eval(environment);
		assertEquals(alexFriendValue.objValue, chrisObject);
	}
	
	@Test
	public void testSizeFour() throws WordsRuntimeException {
		WordsObject alexObject = environment.createObject("Alex", "thing", new Position(0, 0));
		LNodeReference alexRef = new LNodeReference("Alex's");
		
		WordsObject bobObject = environment.createObject("Bob", "thing", new Position(0, 0));
		alexObject.setProperty("friend", new Variable(bobObject));
		LNodeReference friendRef = new LNodeReference("friend's");
		
		WordsObject chrisObject = environment.createObject("Chris", "thing", new Position(0, 0));
		bobObject.setProperty("enemy", new Variable(chrisObject));
		LNodeReference enemyRef = new LNodeReference("enemy's");
		
		WordsObject dennisObject = environment.createObject("Dennis", "thing", new Position(0, 0));
		chrisObject.setProperty("dad", new Variable(dennisObject));
		LNodeReference dadRef = new LNodeReference("dad");

		INodeReferenceList refList = new INodeReferenceList(alexRef, friendRef, enemyRef, dadRef);
		
		Variable alexFriendValue = refList.eval(environment);
		assertEquals(alexFriendValue.objValue, dennisObject);
	}
	
	@Test (expected = ReferenceException.class)
	public void firstNotAnObject() throws WordsRuntimeException {
		LNodeReference friendRef = new LNodeReference("friend");
		INodeReferenceList refList = new INodeReferenceList(friendRef);
		
		refList.eval(environment);
	}
	
	@Test (expected = ReferenceException.class)
	public void secondNotAnObject() throws WordsRuntimeException {
		WordsObject alexObject = environment.createObject("Alex", "thing", new Position(0, 0));
		LNodeReference alexRef = new LNodeReference("Alex's");
		
		alexObject.setProperty("friend", new Variable(6.1));
		
		LNodeReference friendRef = new LNodeReference("friend");
		INodeReferenceList refList = new INodeReferenceList(alexRef, friendRef);
		
		refList.eval(environment);
	}

}
