package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.Variable;
import words.ast.*;
import words.environment.*;
import words.exceptions.*;

public class TestWordsObject {
	Environment environment = new Environment();
	Position startPos = new Position(4, -9);
	WordsClass thing = new WordsClass("thing", null);
	WordsObject obj = new WordsObject("test", thing, new Position(4, -9));
	
	@Test
	public void executeNextActionShouldNotFailWhenEmpty() {
		// Any exception (which would likely be a Java null pointer exception or the like, if it occurred, is a failure
		try {
			obj.executeNextAction(environment);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void executeNextActionShouldExecuteNextMoveAction() throws WordsProgramException {
		obj.enqueueAction(new MoveAction(new Scope(null), Direction.RIGHT, new LNodeNum(1)));
		obj.enqueueAction(new MoveAction(new Scope(null), Direction.RIGHT, new LNodeNum(1)));
		obj.enqueueAction(new MoveAction(new Scope(null), Direction.LEFT, new LNodeNum(1)));
		obj.enqueueAction(new MoveAction(new Scope(null), Direction.LEFT, new LNodeNum(1)));
		
		// First use up a the fake wait that exists on new objects
		obj.executeNextAction(environment);
		
		obj.executeNextAction(environment);
		assertEquals("Object moved 1 to the right in total", obj.getCurrentPosition().x, startPos.x + 1);
		
		obj.executeNextAction(environment);
		assertEquals("Object moved 2 to the right in total", obj.getCurrentPosition().x, startPos.x + 2);
		
		obj.executeNextAction(environment);
		assertEquals("Object moved 1 to the right in total again", obj.getCurrentPosition().x, startPos.x + 1);
		
		obj.executeNextAction(environment);
		assertEquals("Object is back at its starting position", obj.getCurrentPosition().x, startPos.x);
	}
	
	@Test
	public void executeNextActionShouldExecuteNextSayAction() throws WordsProgramException {
		// This line will need to be changed when WordsSay is updated to take an AST expression
		obj.enqueueAction(new SayAction(new Scope(null), new LNodeString("first")));
		obj.enqueueAction(new SayAction(new Scope(null), new LNodeString("second")));
		obj.enqueueAction(new SayAction(new Scope(null), new LNodeString("third")));
		
		// First use up a the fake wait that exists on new objects
		obj.executeNextAction(environment);
		
		obj.executeNextAction(environment);
		assertEquals("Object's message is first message", obj.getCurrentMessage(), "first");
		
		obj.executeNextAction(environment);
		assertEquals("Object's message is second message", obj.getCurrentMessage(), "second");
		
		obj.executeNextAction(environment);
		assertEquals("Object's message is third message", obj.getCurrentMessage(), "third");
	}
	
	
	@Test
	public void enqeueAtFrontShouldBeNextActionExecuted() throws WordsProgramException {
		obj.enqueueAction(new MoveAction(new Scope(null), Direction.RIGHT, new LNodeNum(3)));
		obj.enqueueAction(new MoveAction(new Scope(null), Direction.UP, new LNodeNum(5)));
		obj.enqueueAction(new MoveAction(new Scope(null), Direction.DOWN, new LNodeNum(5)));
		
		// First use up a the fake wait that exists on new objects
		obj.executeNextAction(environment);
		// This should be first to be executed
		obj.enqueueActionAtFront(new MoveAction(new Scope(null), Direction.LEFT, new LNodeNum(1)));
		
		obj.executeNextAction(environment);
		assertEquals("Object moved 1 to the left", obj.getCurrentPosition().x, startPos.x - 1);
	}
	
	@Test
	public void objectMovesUpCorrectly() {
		obj.moveUp();
		assertEquals("Object moved up 1", obj.getCurrentPosition().y, startPos.y + 1);
	}
	
	@Test
	public void objectMovesDownCorrectly() {
		obj.moveDown();
		assertEquals("Object moved down 1", obj.getCurrentPosition().y, startPos.y - 1);
	}
	
	@Test
	public void objectMovesLeftCorrectly() {
		obj.moveLeft();
		assertEquals("Object moved let 1", obj.getCurrentPosition().x, startPos.x - 1);
	}
	
	@Test
	public void objectMovesRightCorrectly() {
		obj.moveRight();
		assertEquals("Object moved right 1", obj.getCurrentPosition().x, startPos.x + 1);
	}
	
	@Test
	public void shouldGetPreviouslySetProperty() {
		String propertyName = "height";
		Variable numProperty = new Variable(15.5);
		
		try {
			obj.setProperty(propertyName, numProperty);
		} catch (Exception e) {
			fail();
		}
		assertEquals("Retrieved property should match set property", numProperty, obj.getProperty(propertyName));
	}
	
	@Test
	public void missingPropertyShouldGetNothing() {
		String propertyName = "garbage";
		assertEquals("Retrieved property was NOTHING", Variable.Type.NOTHING, obj.getProperty(propertyName).type);
	}
	
	@Test
	public void settingMissingPropertyToNothingShouldHaveNoEffect() {
		String propertyName = "height";
		try {
			obj.setProperty(propertyName, new Variable(Variable.Type.NOTHING));
		} catch (Exception e) {
			fail();
		}	
		assertEquals("Retrieved property was NOTHING", Variable.Type.NOTHING, obj.getProperty(propertyName).type);
	}
	
	@Test
	public void settingExistingPropertyToNothingShouldRemoveIt() {
		String propertyName = "height";
		
		// Set it, then remove it by assigning NOTHING
		try {
			obj.setProperty(propertyName, new Variable(15.5));		
			obj.setProperty(propertyName, new Variable(Variable.Type.NOTHING));
		} catch (Exception e) {
			fail();
		}
		assertEquals("Retrieved property was NOTHING", Variable.Type.NOTHING, obj.getProperty(propertyName).type);
	}

	@Test
	public void gettingRowAndColumnPropertiesShouldMatchObjectPosition() {
		// Note: x is column, y is row
		assertEquals("Object's row matches y", (double) obj.getCurrentPosition().y, obj.getProperty("row").numValue, 0.0001);
		assertEquals("Object's column matches x", (double) obj.getCurrentPosition().x, obj.getProperty("column").numValue, 0.0001);
	}
	
	@Test
	public void settingRowAndColumnPropertiesShouldSetObjectPosition() {
		try {
			obj.setProperty("row", new Variable(-4));
			obj.setProperty("column", new Variable(2));
		} catch (Exception e) {
			fail();
		}
		
		// Note: x is column, y is row
		assertEquals("Object's x is 2", obj.getCurrentPosition().x, 2);
		assertEquals("Object's y is 4", obj.getCurrentPosition().y, -4);
	}
	
	@Test
	public void settingRowAndColumnPropertiesShouldBeRounded() {
		try {
			obj.setProperty("row", new Variable(-4.3));
			obj.setProperty("column", new Variable(2.9));
		} catch (Exception e) {
			fail();
		}
		// Note: x is column, y is row
		assertEquals("Object's x is 3", obj.getCurrentPosition().x, 3);
		assertEquals("Object's y is -4", obj.getCurrentPosition().y, -4);
	}

	@Test (expected = InvalidTypeException.class)
	public void setPropertyCorrectType() throws WordsRuntimeException {
		obj.setProperty("row", new Variable("String"));
	}
	
	@Test
	public void inheritanceOfClassPropertyShouldWork() {
		String propertyName = "height";
		Variable numProperty = new Variable(15.5);
		
		assertEquals("Object currently does not have the property", Variable.Type.NOTHING, obj.getProperty(propertyName).type);
		thing.setProperty(propertyName, numProperty);
		assertEquals("Object inherited property from parent", numProperty, obj.getProperty(propertyName));
	}
	
	@Test (expected = ModifyObjectPropertyException.class)
	public void cannotSetName() throws WordsRuntimeException {
		obj.setProperty("name", new Variable("String"));
	}
	
	@Test (expected = ModifyObjectPropertyException.class)
	public void cannotSetClass() throws WordsRuntimeException {
		obj.setProperty("class", new Variable("String"));
	}
	
	@Test
	public void getNameOfObject() throws WordsRuntimeException {
		assertEquals("Object name is retrieved and correct", "test", obj.getProperty("name").stringValue);
	}
	
	@Test 
	public void getClassOfObject() throws WordsRuntimeException {
		assertEquals("Class name is retrieved and correct", "thing", obj.getProperty("class").stringValue);
	}
}
