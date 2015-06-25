package words.environment;

import java.util.HashMap;

import words.Variable;

/**
 * A scope is a collection of variables whose value can be accessed by name. 
 */
public class Scope {
	public Scope parent;		// The access link to enclosing scope
	public HashMap<String, Variable> variables;
	
	public Scope(Scope parent) {
		this.parent = parent;
		variables = new HashMap<String, Variable>(); 
	}
	
	public int getDepth() {
		if (parent == null)
			return 1;
		else
			return 1 + parent.getDepth();
	}
}
