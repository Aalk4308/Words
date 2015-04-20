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
	Environment environment = new Environment();
		
	/****************************************
	 * Object Creation Section
	 ****************************************/
	@Test
	public void basicObjectCreation() {
		WordsObject newObject = null;
		WordsObject receivedObject = null;

		try {
			// Objects are created directly with object literals instead of evaluated leaf nodes
			// If we evaluated leaf nodes, we would be testing multiple things in the same method. 
			newObject = environment.createObject("Alex", "thing", new Position(0,0));
			receivedObject = environment.getVariable("Alex").objProperty;
		} catch (WordsRuntimeException e) {
			// If we have an exception, immediately fail the run.
			fail();
		}
		
		// Test that the object received is the same as the object we tried to create. 
		// Always give a test a descriptive name so, if it fails, we know what went wrong
		assertEquals("An object can successfully be created", newObject, receivedObject);
		assertEquals("Object is placed at correct position", receivedObject.getCurrentPosition(), new Position(0,0));
	}
	
	// If an operation can throw exceptions, check that it always throws that exception
	@Test (expected = ObjectAlreadyExistsException.class)
	public void objectCreationWhenObjectAlreadyExists() throws WordsRuntimeException {
		environment.createObject("Alex", "thing", new Position(0,0));
		environment.createObject("Alex", "thing", new Position(2,0));
	}
	
	@Test (expected = WordsClassNotFoundException.class)
	public void objectCreationWhenClassDoesNotExist() throws WordsRuntimeException {
		environment.createObject("Alex", "person", new Position(0,0));
	}
	
	@Test
	public void localScopeIteratedObjectCreation() {
		// Arbitrarily try this 5 times. 
		for (int i = 0; i < 5; i++) {
			environment.enterNewLocalScope();
			WordsObject newObject = null;
			WordsObject receivedObject = null;
	
			try {
				// Objects are created directly with object literals instead of evaluated leaf nodes
				// If we evaluated leaf nodes, we would be testing multiple things in the same method. 
				newObject = environment.createObject("Alex", "thing", new Position(0,0));
				receivedObject = environment.getVariable("Alex").objProperty;
			} catch (WordsRuntimeException e) {
				// If we have an exception, immediately fail the run.
				fail();
			}
			
			// Test that the object received is the same as the object we tried to create. 
			// Always give a test a descriptive name so, if it fails, we know what went wrong
			assertEquals("A local object can successfully be created", newObject, receivedObject);
			assertEquals("Object is placed at correct position", receivedObject.getCurrentPosition(), new Position(0,0));
			environment.exitLocalScope();
		}
	}
	
	@Test
	public void localScopeVariablePersists() throws WordsRuntimeException {
		environment.enterNewLocalScope();
		try {
			environment.createObject("Alex", "thing", new Position(0,0));
			environment.createObject("James", "thing", new Position(0,0));
		} catch (WordsRuntimeException e) {
			fail();
		}
		environment.exitLocalScope();
		assertEquals("Object out of local scope still exists", 2, environment.getObjects().size());
	}
	
	@Test
	public void localScopeVariableOnlyExistsLocally() throws WordsRuntimeException {
		environment.enterNewLocalScope();
		try {
			environment.createObject("Alex", "thing", new Position(0,0));
			environment.createObject("James", "thing", new Position(0,0));
		} catch (WordsRuntimeException e) {
			fail();
		}
		environment.exitLocalScope();
		Property property = environment.getVariable("Alex");
		assertEquals("Variable did not exist after popping local scope", property.type, Property.PropertyType.NOTHING);
	}

	/****************************************
	 * Class Creation Section
	 ****************************************/
	
	@Test
	public void basicClassCreation() throws WordsRuntimeException {
		WordsClass newClass = environment.createClass("Person", "thing");
		WordsClass receivedClass = environment.getClass("Person");
		
		assertEquals("Class created and retrieved", newClass, receivedClass);
	}
	
	@Test
	public void basicClassWithObject() throws WordsRuntimeException {
		environment.createClass("Person", "thing");
		WordsObject newObject = environment.createObject("James", "Person", new Position(0.0, 0.0));
		WordsObject receivedObject = environment.getVariable("James").objProperty;
		
		assertEquals("Object of class created and retrieved", newObject, receivedObject);
		assertEquals("Object is of correct class", newObject.getClassName(), "Person");
	}

	@Test
	public void singleInheritance() throws WordsRuntimeException {
		WordsClass parentClass = environment.createClass("Person", "thing");
		parentClass.setProperty("height", new Property(5.0));
		
		environment.createClass("Man", "Person");
		WordsObject childObject = environment.createObject("Dude", "Man", new Position(0.0, 0.0));
		
		assertEquals("Child class inherits property", childObject.getProperty("height").numProperty, 5.0, .0001);
	}
	
	@Test
	public void skipGenerationInheritance() throws WordsRuntimeException {
		WordsClass grandParentClass = environment.createClass("Person", "thing");
		grandParentClass.setProperty("height", new Property(5.0));
		
		environment.createClass("Man", "Person");
		environment.createClass("Boy", "Man");
		WordsObject grandChildObject = environment.createObject("Sonnie", "Boy", new Position(0.0, 0.0));
		
		assertEquals("Grandchild class inherits property", grandChildObject.getProperty("height").numProperty, 5.0, .0001);
	}
	
	@Test
	public void compoundInheritance() throws WordsRuntimeException {
		WordsClass grandParentClass = environment.createClass("Person", "thing");
		grandParentClass.setProperty("height", new Property(5.0));
		
		WordsClass parentClass = environment.createClass("Man", "Person");
		parentClass.setProperty("language", new Property("wordz"));
		
		environment.createClass("Boy", "Man");
		WordsObject grandChildObject = environment.createObject("Sonnie", "Boy", new Position(0.0, 0.0));
		
		assertEquals("Grandchild class inherits grandparent property", grandChildObject.getProperty("height").numProperty, 5.0, .0001);
		assertEquals("Grandchild class inherits parent property", grandChildObject.getProperty("language").stringProperty, "wordz");
	}
	
	@Test
	(expected = ClassAlreadyExistsException.class)
	public void classNameTaken() throws WordsRuntimeException {
		environment.createClass("Person", "thing");
		environment.createClass("Person", "thing");
	}
	
	@Test
	(expected = WordsClassNotFoundException.class)
	public void parentClassDoesntExist() throws WordsRuntimeException {
		environment.createClass("Person", "wing");
	}
		
	
}
