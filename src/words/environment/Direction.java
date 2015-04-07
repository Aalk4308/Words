package words.environment;
/**
 * An orthogonal direction, possibly random.
 */
public class Direction {
	/**
	 * One of the four orthogonal directions or an indication that any direction is permitted.
	 */
	public enum Type {
		ANYWHERE,
		DOWN,
		LEFT,
		RIGHT,
		UP
	}
	
	public Type type;
	
	public Direction(Type type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.type.toString();
	}
}
