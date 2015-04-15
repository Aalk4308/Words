package words.environment;
import java.util.LinkedList;

import java.util.Random;
import words.ast.AST;
import words.ast.ASTValue;
import words.ast.ASTValue.ValueType;
import words.exceptions.WordsInvalidTypeException;
import words.exceptions.WordsProgramException;
import words.exceptions.WordsRuntimeException;

public class WordsMove extends WordsAction {
	private Direction direction;
	private AST distanceExpression;		// The expression whose value will be the number of moves to make; used only before the move has been expanded

	/**
	 * Create a new WordsMove action.  distanceExpression should evaluate to a number (or be coercible into a number)
	 * else an exception will be thrown to the user when the action is executed.
	 * 
	 * distanceExpression may be null, in which case the WordsMove will be treated as a 1-unit move.
	 */
	public WordsMove(Direction direction, AST distanceExpression) {
		if (direction.type == Direction.Type.ANYWHERE) {
			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(4);
			this.direction = new Direction(Direction.explicit[randomInt]);
		} else {
			this.direction = direction;
		}
		this.distanceExpression = distanceExpression;
	}

	/**
	 * Private constructor used to create a 1-unit move action.
	 */
	private WordsMove(Direction direction) {
		this.direction = direction;
		this.distanceExpression = null;
	}

	@Override
	public boolean isExecutable() {
		if (distanceExpression != null)
			return false;

		return true;
	}

	@Override
	public void doExecute(WordsObject object, WordsEnvironment environment) throws WordsProgramException {
		// We know that the distanceValue is 1
		// ANYWHERE directions will already have been replaced to be a real direction
		switch(direction.type) {
			case DOWN:
				object.moveDown();
				break;
			case LEFT:
				object.moveLeft();
				break;
			case RIGHT:
				object.moveRight();
				break;
			case UP:
				object.moveUp();
				break;
			default:
				throw new AssertionError("Attempted to execute direction type " + direction.type.toString());
		}
	}

	@Override
	public LinkedList<WordsAction> doExpand(WordsObject object, WordsEnvironment environment) throws WordsProgramException {
		ASTValue value;
		try {
			value = distanceExpression.eval(environment).tryCoerceTo(ASTValue.ValueType.NUM);
		} catch (WordsRuntimeException e) {
			throw new WordsProgramException(distanceExpression, e);
		}

		if (value.type != ASTValue.ValueType.NUM) {
			throw new WordsProgramException(distanceExpression, new WordsInvalidTypeException(value.type.toString(), ASTValue.ValueType.NUM.toString()));
		}

		int distanceValue = (int) Math.round(value.numValue);

		if (distanceValue < 0) {
			distanceValue = -distanceValue;
			direction.type = Direction.getOpposite(direction.type);
		}

		LinkedList<WordsAction> list = new LinkedList<WordsAction>();

		// Decompose into executable 1-unit moves, or a wait action if distanceValue is zero
		if (distanceValue > 0) {
			for (int i = 0; i < distanceValue; i++) {
				// Use the private constructor to create the 1-unit move
				list.add(new WordsMove(direction));
			}
		} else {
			list.add(new WordsWait(1));
		}

		return list;
	}
}