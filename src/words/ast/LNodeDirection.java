package words.ast;

import words.environment.Direction;
import words.environment.WordsEnvironment;
import words.environment.Direction.Type;

/**
 * A syntax tree leaf node for a direction.
 */
public class LNodeDirection extends LNode {
	public Direction direction;
	
	public LNodeDirection(Direction.Type d) {
		super();
		this.direction = new Direction(d);
	}
	
	@Override
	protected String valueAsString() {
		return direction.toString();
	}
	
	@Override
	public ASTValue eval(WordsEnvironment environment) {
		return new ASTValue(this.direction);
	}
}