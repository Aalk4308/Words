package words.test;

import org.junit.Test;

import words.environment.*;
import words.exceptions.*;

public class TestMoveAction extends TestINode {
    @Test
    public void testWorkingWordsMove() throws WordsRuntimeException, WordsProgramException {
        environment.createObject("Fred", "thing", new Position(0, 0));
        Action action = new MoveAction(environment.getCurrentScope(), Direction.LEFT, twoLeaf);
        action.expand(environment.getVariable("Fred").objValue, environment);
    }

    @Test (expected = WordsProgramException.class)
    public void onlyOperatesOnStringsAndNumbers() throws WordsRuntimeException, WordsProgramException {
        environment.createObject("Fred", "thing", new Position(0, 0));
        Action action1 = new MoveAction(environment.getCurrentScope(), Direction.LEFT, nothingLeaf);
        action1.expand(environment.getVariable("Fred").objValue, environment);
        Action action2 = new MoveAction(environment.getCurrentScope(), Direction.LEFT, trueLeaf);
        action2.expand(environment.getVariable("Fred").objValue, environment);
    }

}
