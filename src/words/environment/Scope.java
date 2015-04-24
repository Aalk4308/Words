package words.environment;

import java.util.HashMap;

/**
 * A scope is a collection of variables whose value can be accessed by name. 
 */
public class Scope {
	public Scope parent;		// The access link to enclosing scope
	public HashMap<String, Property> variables;
	
	public Scope(Scope parent) {
		this.parent = parent;
		variables = new HashMap<String, Property>(); 
	}
	
	public int getDepth() {
		if (parent == null)
			return 1;
		else
			return 1 + parent.getDepth();
	}
}