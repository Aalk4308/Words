package words.ast;

import words.Variable;
import words.environment.*;

/**
 * A syntax tree leaf node for a direction.
 */
public class LNodeDirection extends LNode {
	public Direction direction;
	
	public LNodeDirection(Direction d) {
		super();
		this.direction = d;
	}
	
	@Override
	protected String valueAsString() {
		return direction.toString();
	}
	
	@Override
	public Variable eval(Environment environment) {
		return new Variable(this.direction);
	}
}