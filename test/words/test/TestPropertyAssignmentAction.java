package words.test;

import org.junit.Test;

import words.ast.*;
import words.environment.*;
import words.exceptions.*;

public class TestPropertyAssignmentAction extends TestINode {
    @Test
    public void testWorkingWordsPropertyAssignment() throws WordsRuntimeException, WordsProgramException {
        environment.createObject("Fred", "thing", new Position(0, 0));
        Action action = new PropertyAssignAction(environment.getCurrentScope(), new INodeQueueAssignPropertyList(new INodeQueueAssignProperty(stringLeaf, numLeaf)));
        action.execute(environment.getVariable("Fred").objValue, environment);
    }
}
