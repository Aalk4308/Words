package words;

import words.environment.Direction;
import words.environment.Position;
import words.environment.WordsObject;

/**
 * A dynamically-typed value that an AST node can return as its result.
 */
public class Variable {
	public enum Type {
		BOOLEAN,
		NUM,
		STRING,
		OBJ,
		DIRECTION,
		POSITION,
		NOW,
		VARIABLE,
		NOTHING
	}
	public Type type;
	
	public boolean booleanValue;
	public double numValue;
	public String stringValue;
	public WordsObject objValue;
	public Direction directionValue;
	public Position positionValue;
	
	public Variable(boolean b) {
		this.type = Type.BOOLEAN;
		this.booleanValue = b;
	}
	
	public Variable(double num) {
		this.type = Type.NUM;
		this.numValue = num;
	}	

	public Variable(String s) {
		this.type = Type.STRING;
		this.stringValue = s;
	}
	
	public Variable(WordsObject obj) {
		this.type = Type.OBJ;
		this.objValue = obj;
	}
	
	public Variable(Direction d) {
		this.type = Type.DIRECTION;
		this.directionValue = d;
	}

	public Variable(Position p) {
		this.type = Type.POSITION;
		this.positionValue = p;
	}
	
	public Variable(Type type) {
		this.type = type;
	}
	
	/**
	 * Attempts to coerce this ASTValue to the given type, if possible.  Regardless of whether or not it succeeds,
	 * returns this ASTValue.  The caller should then check the type to determine if the result of the coercion resulted
	 * in a suitable type for its purposes.
	 * 
	 * @return self
	 */
	public Variable tryCoerceTo(Type newType) {
		switch(newType) {
			case NUM:
				if (type == Type.STRING) {
					try {  
						double val = Double.parseDouble(stringValue);
						this.numValue = val;
						this.type = newType;
					} catch (NumberFormatException nfe) {}  
				}
				break;
			case STRING:
				if (type == Type.NUM) {
					this.stringValue = String.format("%f", numValue);
					this.type = newType;
				}
				if (type == Type.OBJ) {
					this.stringValue = objValue.getObjectName();
					this.type = newType;
				}
				break;
			default:
				break;
		}
		return this;
	}
	
	public void copyOtherVariable(Variable other) {
		this.type = other.type;
		this.booleanValue = other.booleanValue;
		this.directionValue = other.directionValue;
		this.numValue = other.numValue;
		this.objValue = other.objValue;
		this.positionValue = other.positionValue;
		this.stringValue = other.stringValue;
	}
}
