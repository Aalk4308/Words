package words.test;

import org.junit.Test;

import words.ast.*;
import words.environment.*;
import words.exceptions.*;

public class TestWaitAction extends TestINode {
	
    @Test
    public void testWorkingWordsWait() throws WordsRuntimeException, WordsProgramException {
        environment.createObject("Fred", "thing", new Position(0, 0));
        Action action1 = new WaitAction(environment.getCurrentScope(), new LNodeNum(2));
        action1.expand(environment.getVariable("Fred").objValue, environment);
        Action action2 = new WaitAction(environment.getCurrentScope(), new LNodeString("2.3"));
        action2.expand(environment.getVariable("Fred").objValue, environment);
    }

    @Test (expected = WordsProgramException.class)
    public void onlyWaitPositiveNumberOfTurns() throws WordsRuntimeException, WordsProgramException {
        environment.createObject("Fred", "thing", new Position(0, 0));
        Action action2 = new WaitAction(environment.getCurrentScope(), new LNodeNum(-1));
        action2.expand(environment.getVariable("Fred").objValue, environment);
    }

    @Test (expected = WordsProgramException.class)
    public void onlyWaitPositiveIntegerNumberOfTurns() throws WordsRuntimeException, WordsProgramException {
        environment.createObject("Fred", "thing", new Position(0, 0));
        Action action1 = new WaitAction(environment.getCurrentScope(), new LNodeNum(0.2));
        action1.expand(environment.getVariable("Fred").objValue, environment);
    }

    @Test (expected = WordsProgramException.class)
    public void onlyOperatesOnStringsAndNumbers() throws WordsRuntimeException, WordsProgramException {
        environment.createObject("Fred", "thing", new Position(0, 0));
        Action action1 = new WaitAction(environment.getCurrentScope(), nothingLeaf);
        action1.expand(environment.getVariable("Fred").objValue, environment);
        Action action2 = new WaitAction(environment.getCurrentScope(), trueLeaf);
        action2.expand(environment.getVariable("Fred").objValue, environment);
    }

}
