/**
 * An abstract syntax tree node, which may be either an internal node or leaf node.
 */
public abstract class AST {
	public class ASTValue {
		public ValueType type;
		
		public double numValue;
		public String stringValue;
		public WordsObject objValue;
		public Direction directionValue;
		public WordsPosition positionValue;
		
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
		 * Coerces the ASTValue into a num if it can be coerced.
		 * 
		 * @return self
		 **/
		public ASTValue getNumCoercedVal() {
			if (type == ValueType.NUM) {
				return this;
			} else if (type == ValueType.STRING) {
				  try  
				  {  
				    double val = Double.parseDouble(stringValue); 
				    this.type = ValueType.NUM;
				    this.numValue = val;
				  }  
				  catch(NumberFormatException nfe)  
				  {  
				    return this;
				  }  
			}
			return this;
		}
		
		/**
		 * Coerces the ASTValue into a String if it can be coerced.
		 * 
		 * @return ASTValue this
		 **/
		public ASTValue getStringCoercedVal() {
			if (type == ValueType.STRING) {
				return this;
			} else if (type == ValueType.NUM) {
				this.stringValue = String.format("%f", numValue);
				this.type = ValueType.STRING;
				return this;
			}
			return this;
		}
	}

	public enum ValueType {
		NUM,
		STRING,
		OBJ,
		DIRECTION,
		POSITION,
		NOW,
		NOTHING
	}
	
	/**
	 * The type of the node, which determines how the node is evaluated.
	 */
	public enum ASTType { 
		// Leaf node types
		DIRECTION,
		NOTHING,
		NOW,
		NUM,
		IDENTIFIER,
		REFERENCE,
		STRING,
		
		// Internal node types
		STATEMENT_LIST,
		
		RESET,
		EXIT,
		
		CREATE_CLASS,
		CLASS_STATEMENT_LIST,
		DEFINE_PROPERTY,
		DEFINE_ACTION,
		CREATE_OBJ,
		REMOVE,
		ASSIGN,
		
		REPEAT,
		WHILE,
		IF,
		
		LISTENER_PERM,
		LISTENER_TEMP,
		
		QUEUE_MOVE,
		QUEUE_SAY,
		QUEUE_WAIT,
		QUEUE_STOP,
		QUEUE_ASSIGN,
		QUEUE_ACTION,
		
		MOVES_PREDICATE,
		SAYS_PREDICATE,
		WAITS_PREDICATE,
		TOUCHES_PREDICATE,
		
		NOT,
		AND,
		OR,
		
		EQUALS,
		LESS,
		GREATER,
		LEQ,
		GEQ,
		
		NEGATE,
		ADD,
		SUBTRACT,
		MULTIPLY,
		DIVIDE,
		EXPONENTIATE,
		
		POSITION,
		RETRIEVE_PROPERTY,
		
		REFERENCE_LIST,
		IDENTIFIER_LIST,
		PARAMETER_LIST,
		QUEUE_ASSIGN_PROPERTY_LIST,
		
		PARAMETER,
		SUBJECT,
		ALIAS,
		QUEUE_ASSIGN_PROPERTY,
	};
	
	public ASTType type;
	public int lineNo;
	
	public AST(ASTType type, int lineNo) {
		this.type = type;
		this.lineNo = lineNo;
	}
	
	/**
	 * Debugging method to dump this node and all its children, if any, to standard error for inspection.
	 * 
	 * @param level the indentation level
	 */
	abstract public void dump(int level);
	
	/**
	 * Evaluate an AST node, possibly by having side effects on the passed environment.
	 */
	public abstract ASTValue eval(WordsEnvironment environment) throws WordsProgramException;
}