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
	private AST distanceExpression;
	private int distanceValue;

	public WordsMove(Direction direction, AST distanceExpression) {
		if (direction.type == Direction.Type.ANYWHERE) {
			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(4);
			this.direction.type = Direction.explicit[randomInt];
		} else {
			this.direction = direction;
		}
		this.distanceExpression = distanceExpression;
	}

	/**
	 * Create a new WordsMove action.  distanceValue must round to a positive or negative integer.
	 * distanceValue cannot round to zero.
	 */
	public WordsMove(Direction direction, double distanceValue) {
		if (direction.type == Direction.Type.ANYWHERE) {
			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(4);
			this.direction = new Direction(Direction.explicit[randomInt]);
		} else {
			this.direction = direction;
		}

		if (distanceValue < 0) {
			distanceValue = distanceValue * -1;
			this.direction.type = Direction.getOpposite(this.direction.type);
		}
		this.distanceValue = (int) Math.round(distanceValue);
	}

	@Override
	public boolean isExecutable() {
		if (distanceExpression != null)
			return false;

		if (distanceValue != 1)
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
		if (distanceExpression != null) {
			ASTValue value;
			try {
				value = distanceExpression.eval(environment).tryCoerceTo(ASTValue.ValueType.NUM);
			} catch (WordsRuntimeException e) {
				throw new WordsProgramException(distanceExpression, e);
			}

			if (value.type != ASTValue.ValueType.NUM) {
				throw new WordsProgramException(distanceExpression, new WordsInvalidTypeException(value.type.toString(), ASTValue.ValueType.NUM.toString()));
			}

			distanceValue = (int) Math.round(value.numValue);
			distanceExpression = null;						// Not necessary, but including for clarity since once the expression is evaluated, it is no longer needed
		}

		//TODO: flip if distanceValue is negative

		LinkedList<WordsAction> list = new LinkedList<WordsAction>();

		// Decompose into executable 1-unit moves, or a wait action if distanceValue is zero
		if (distanceValue > 0) {
			for (int i = 0; i < distanceValue; i++) {
				list.add(new WordsMove(direction, 1));
			}
		} else {
			list.add(new WordsWait(1));
		}

		return list;
	}
}