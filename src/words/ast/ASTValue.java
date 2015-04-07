package words.ast;

import words.environment.Direction;
import words.environment.WordsObject;
import words.environment.WordsPosition;

/**
 * A dynamically-typed value that an AST node can return as its result.
 */
public class ASTValue {
	public enum ValueType {
		BOOLEAN,
		NUM,
		STRING,
		OBJ,
		DIRECTION,
		POSITION,
		NOW,
		NOTHING
	}
	public ValueType type;
	
	public boolean booleanValue;
	public double numValue;
	public String stringValue;
	public WordsObject objValue;
	public Direction directionValue;
	public WordsPosition positionValue;
	
	public ASTValue(boolean b) {
		this.type = ValueType.BOOLEAN;
		this.booleanValue = b;
	}
	
	public ASTValue(double num) {
		this.type = ValueType.NUM;
		this.numValue = num;
	}	

	public ASTValue(String s) {
		this.type = ValueType.STRING;
		this.stringValue = s;
	}
	
	public ASTValue(WordsObject obj) {
		this.type = ValueType.OBJ;
		this.objValue = obj;
	}
	
	public ASTValue(Direction d) {
		this.type = ValueType.DIRECTION;
		this.directionValue = d;
	}

	public ASTValue(WordsPosition p) {
		this.type = ValueType.POSITION;
		this.positionValue = p;
	}
	
	public ASTValue(ValueType type) {
		this.type = type;
	}
	
	/**
	 * Attempts to coerce this ASTValue to the given type, if possible.  Regardless of whether or not it succeeds,
	 * returns this ASTValue.  The caller should then check the type to determine if the result of the coercion resulted
	 * in a suitable type for its purposes.
	 * 
	 * @return self
	 */
	public ASTValue tryCoerceTo(ValueType newType) {
		switch(newType) {
			case NUM:
				if (type == ValueType.STRING) {
					try {  
						double val = Double.parseDouble(stringValue);
						this.numValue = val;
						this.type = newType;
					} catch (NumberFormatException nfe) {}  
				}
				break;
			case STRING:
				if (type == ValueType.NUM) {
					this.stringValue = String.format("%f", numValue);
					this.type = newType;
				}
				break;
			default:
				break;
		}
		
		return this;
	}
}
