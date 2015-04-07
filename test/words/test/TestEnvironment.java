package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.environment.*;
import words.exceptions.*;

/*
 * This class contains more documentation that most test classes. It is meant as an
 * example template for how JUnit tests should be performed. 
 * 
 * Each test class should test a single class. The name of the test class should be Test[Classname].
 */
public class TestEnvironment {
	
	// Between each test case in JUnit, all instance variables are reinitialized. This means that
	// each test case is run with a new, clean environment that has not been modified by previous test cases.
	WordsEnvironment environment = new WordsEnvironment();
		
	/****************************************
	 * Object Creation Section
	 ****************************************/
	@Test
	public void basicObjectCreation() {
		WordsObject newObject = null;
		try {
			// Objects are created directly with object literals instead of evaluated leaf nodes
			// If we evaluated leaf nodes, we would be testing multiple things in the same method. 
			newObject = environment.createObject("Alex", "thing", new WordsPosition(0,0));
		} catch (WordsRuntimeException e) {
			// If we have an exception, immediately fail the run.
			fail();
		}
		WordsObject receivedObject = environment.getObject("Alex");
		
		// Test that the object received is the same as the object we tried to create. 
		// Always give a test a descriptive name so, if it fails, we know what went wrong
		assertEquals("An object can successfully be created", newObject, receivedObject);
		assertEquals("Object is placed at correct position", receivedObject.getCurrentCell(), new WordsPosition(0,0));

	}
	
	// If an operation can throw exceptions, check that it always throws that exception
	@Test (expected = WordsObjectAlreadyExistsException.class)
	public void objectCreationWhenObjectAlreadyExists() throws WordsRuntimeException {
		environment.createObject("Alex", "thing", new WordsPosition(0,0));
		environment.createObject("Alex", "thing", new WordsPosition(2,0));
	}
	
	@Test (expected = WordsClassNotFoundException.class)
	public void objectCreationWhenClassDoesNotExist() throws WordsRuntimeException {
		environment.createObject("Alex", "person", new WordsPosition(0,0));
	}

}
