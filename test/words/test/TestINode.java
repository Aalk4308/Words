package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.FrameLoop;
import words.ast.*;
import words.environment.*;


public class TestINode {
	// Some generic variables useful for all tests
	Environment environment = new Environment();
	FrameLoop loop = new FrameLoop(environment);

	AST nothingLeaf = new LNodeNothing();
	AST numLeaf = new LNodeNum(0.0);
	AST twoLeaf = new LNodeNum(2.0);
	AST negTwoLeaf = new LNodeNum(-2.0);
	AST fiveLeaf = new LNodeNum(5.0);
	AST stringLeaf = new LNodeString("string");
	AST thingStringLeaf = new LNodeString("thing");
	AST position00 = new INodePosition(numLeaf, numLeaf);
	
	AST fredStringLeaf = new LNodeString("Fred");
	AST georgeStringLeaf = new LNodeString("George");
	AST leftDirectionLeaf = new LNodeDirection(Direction.LEFT);
	AST rightDirectionLeaf = new LNodeDirection(Direction.RIGHT);
	AST anywhereDirectionLeaf = new LNodeDirection(Direction.ANYWHERE);
	
	AST moveFredLeft2 = new INodeQueueMove(nothingLeaf, fredStringLeaf, leftDirectionLeaf, twoLeaf, null);
	AST moveGeorgeLeft2 = new INodeQueueMove(nothingLeaf, georgeStringLeaf, leftDirectionLeaf, twoLeaf, null);
	AST moveGeorgeRight2 = new INodeQueueMove(nothingLeaf, georgeStringLeaf, rightDirectionLeaf, twoLeaf, null);
	AST moveFredRight2 = new INodeQueueMove(nothingLeaf, fredStringLeaf, rightDirectionLeaf, twoLeaf, null);
	AST moveFredLeftNegative2 = new INodeQueueMove(nothingLeaf, fredStringLeaf, leftDirectionLeaf, negTwoLeaf, null);
	AST moveFredAnywhere2 = new INodeQueueMove(nothingLeaf, fredStringLeaf, anywhereDirectionLeaf, twoLeaf, null);
	
	AST statementsAboutFred = new INodeStatementList(moveFredLeft2, moveFredRight2, moveFredAnywhere2);
	
	AST helloWorldLeaf = new LNodeString("Hello World");
	AST makeFredSayHelloWorld = new INodeQueueSay(new INodeReferenceList(), fredStringLeaf, helloWorldLeaf, null);
	AST makeGeorgeSayHelloWorld = new INodeQueueSay(new INodeReferenceList(), georgeStringLeaf, helloWorldLeaf, null);
	AST makeFredSayString = new INodeQueueSay(new INodeReferenceList(), fredStringLeaf, stringLeaf, null);


	AST trueLeaf = new INodeEquals(nothingLeaf, nothingLeaf);
	AST falseLeaf = new INodeEquals(nothingLeaf, numLeaf);
	
	AST createObjectFred = new INodeCreateObject(fredStringLeaf, thingStringLeaf, null, position00);
	AST createObjectGeorge = new INodeCreateObject(georgeStringLeaf, thingStringLeaf, null, position00);
	
	@Test
	public void canAddNullToINode() {
		INode testNode = new INodeStatementList(null, null, null, null);
		assertEquals("INode has correct number of children", testNode.children.size(), 4);
	}
}
