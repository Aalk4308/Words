package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.environment.*;
import words.exceptions.*;

public class TestINodeQueueAssign extends TestINode {
	String prop1Name = "prop1";
	String prop2Name = "prop2";
	String prop3Name = "prop3";
	
	String prop1Value = "prop1Value";
	String prop2Value = "prop2Value";
	String prop3Value = "prop3Value";	
	
	INodeQueueAssignProperty prop1 = new INodeQueueAssignProperty(new LNodeString(prop1Name), new LNodeString(prop1Value));
	INodeQueueAssignProperty prop2 = new INodeQueueAssignProperty(new LNodeString(prop2Name), new LNodeString(prop2Value));
	INodeQueueAssignProperty prop3 = new INodeQueueAssignProperty(new LNodeString(prop3Name), new LNodeString(prop3Value));
	
	INodeQueueAssignPropertyList onePropList = new INodeQueueAssignPropertyList(prop1);
	INodeQueueAssignPropertyList threePropList = new INodeQueueAssignPropertyList(prop1, prop2, prop3);
	
	INodeReferenceList refList = new INodeReferenceList(new LNodeReference("Fred's"));
	
	@Test
	public void queueAssignShouldWorkForOneProperty() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0,0));
		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up.

		assertEquals("Property does not currently exist", environment.getVariable("Fred").objValue.getProperty(prop1Name).type, Variable.Type.NOTHING);
		
		INodeQueueAssign testNode = new INodeQueueAssign(refList, onePropList, null);
		loop.enqueueAST(testNode);
		assertEquals("Property does not exist after enqueueing", environment.getVariable("Fred").objValue.getProperty(prop1Name).type, Variable.Type.NOTHING);		
		
		loop.fastForwardEnvironment(1);
		assertEquals("Property exists after executing", environment.getVariable("Fred").objValue.getProperty(prop1Name).stringValue, prop1Value);
	}
	
	@Test
	public void queueAssignShouldSimultaneouslyAssignMultipleProperties() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0,0));
		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up.
		assertEquals("Property 1 does not currently exist", environment.getVariable("Fred").objValue.getProperty(prop1Name).type, Variable.Type.NOTHING);
		assertEquals("Property 2 does not currently exist", environment.getVariable("Fred").objValue.getProperty(prop2Name).type, Variable.Type.NOTHING);
		assertEquals("Property 3 does not currently exist", environment.getVariable("Fred").objValue.getProperty(prop3Name).type, Variable.Type.NOTHING);
		
		INodeQueueAssign testNode = new INodeQueueAssign(refList, threePropList, null);
		loop.enqueueAST(testNode);
		assertEquals("Property 1 does not exist after enqueueing", environment.getVariable("Fred").objValue.getProperty(prop1Name).type, Variable.Type.NOTHING);
		assertEquals("Property 2 does not exist after enqueueing", environment.getVariable("Fred").objValue.getProperty(prop2Name).type, Variable.Type.NOTHING);
		assertEquals("Property 3 does not exist after enqueueing", environment.getVariable("Fred").objValue.getProperty(prop3Name).type, Variable.Type.NOTHING);
		
		loop.fastForwardEnvironment(1);
		assertEquals("Property 1 exists after executing", environment.getVariable("Fred").objValue.getProperty(prop1Name).stringValue, prop1Value);
		assertEquals("Property 2 exists after executing", environment.getVariable("Fred").objValue.getProperty(prop2Name).stringValue, prop2Value);
		assertEquals("Property 3 exists after executing", environment.getVariable("Fred").objValue.getProperty(prop3Name).stringValue, prop3Value);
	}
	
	@Test
	public void testQueueAssignWithNow() throws WordsRuntimeException {
		environment.createObject("Fred", "thing", new Position(0,0));
		loop.fastForwardEnvironment(1); //object is created with a 1 frame wait, so use it up.
		assertEquals("Property 1 does not currently exist", environment.getVariable("Fred").objValue.getProperty(prop1Name).type, Variable.Type.NOTHING);
		assertEquals("Property 2 does not currently exist", environment.getVariable("Fred").objValue.getProperty(prop2Name).type, Variable.Type.NOTHING);
		
		INodeQueueAssignPropertyList propList1 = new INodeQueueAssignPropertyList(new INodeQueueAssignProperty(new LNodeString(prop1Name), new LNodeString(prop1Value)));
		INodeQueueAssignPropertyList propList2 = new INodeQueueAssignPropertyList(new INodeQueueAssignProperty(new LNodeString(prop2Name), new LNodeString(prop2Value)));
		
		INodeQueueAssign testNode1 = new INodeQueueAssign(refList, propList1, null);
		INodeQueueAssign testNode2 = new INodeQueueAssign(refList, propList2, new LNodeNow());
		
		loop.enqueueAST(testNode1);
		loop.enqueueAST(testNode2);
		
		assertEquals("Property 1 does not exist after enqueueing", environment.getVariable("Fred").objValue.getProperty(prop1Name).type, Variable.Type.NOTHING);
		assertEquals("Property 2 does not exist after enqueueing", environment.getVariable("Fred").objValue.getProperty(prop2Name).type, Variable.Type.NOTHING);
		
		loop.fastForwardEnvironment(1);
		assertEquals("Property 1 does not exist after one frame", environment.getVariable("Fred").objValue.getProperty(prop1Name).type, Variable.Type.NOTHING);
		assertEquals("Property 2 exists after one frame", environment.getVariable("Fred").objValue.getProperty(prop2Name).stringValue, prop2Value);
		
		loop.fastForwardEnvironment(1);
		assertEquals("Property 1 exists after two frames", environment.getVariable("Fred").objValue.getProperty(prop1Name).stringValue, prop1Value);
		assertEquals("Property 2 exists after two frames", environment.getVariable("Fred").objValue.getProperty(prop2Name).stringValue, prop2Value);
	}
}
