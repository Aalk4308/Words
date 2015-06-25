package words.test;

import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.environment.*;
import words.exceptions.*;

public class TestINodeSubject extends TestINode {

	@Test
	public void properlyHandlesClassName() throws WordsRuntimeException {
		AST subject = new INodeSubject(thingStringLeaf, null, null);
		assertEquals("Returns the class name", subject.eval(environment).stringValue, "thing");
	}
	
	@Test
	public void properlyHandlesObject() throws WordsRuntimeException {
		WordsObject fredObject = environment.createObject("Fred", "thing", new Position(0, 0));
		WordsObject georgeObject = environment.createObject("George", "thing", new Position(0, 0));
		LNodeReference fredRef = new LNodeReference("Fred's");
		fredObject.setProperty("brother", new Variable(georgeObject));
		INodeReferenceList fredRefList = new INodeReferenceList(fredRef);
		LNodeIdentifier id = new LNodeIdentifier("brother");
		AST subject = new INodeSubject(null, fredRefList, id);
		assertEquals("Follows reference list and returns object", subject.eval(environment).objValue, georgeObject);
	}

}
