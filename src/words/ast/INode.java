package words.ast;
import java.util.ArrayList;
import java.lang.StringBuilder;

import words.Variable;
import words.environment.*;
import words.exceptions.*;

/**
 * An abstract syntax tree internal node.
 */
public abstract class INode extends AST {
	public ArrayList<AST> children;
	
	public INode(Object... children) {
		super();
		
		this.children = new ArrayList<AST>(children.length);
		
		for (int i = 0; i < children.length; i++) {
			if (children[i] == null)
				this.children.add(null);
			else
				this.children.add((AST) children[i]);
		}
	}
	
	/**
	 * Appends a given list of nodes to this node's list of children. 
	 * 
	 * @param nodes the list of nodes to be added
	 * @return this node
	 */
	public INode add(ArrayList<AST> nodes) {
		for (AST node : nodes)
			this.children.add(node);
		
		return this;
	}
	
	private void indent(int level) {
		for (int i = 0; i < level; i++)
			System.err.printf("  ");		
	}
	
	@Override
	public void dump(int level) {
		indent(level);
		System.err.println(this.getClass().toString());
		
		if (children.size() > 0) {
			for (AST child : children) {
				if (child != null) {
					child.dump(level + 1);
				}
			}
		} else {
			indent(level + 1);
			System.err.println("  empty");
		}		
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append("[" + this.lineNumber + ": " + this.getClass().toString() + ": ");
		
		for (AST child : children)
			if (child != null)
				s.append(child.toString());
		
		s.append("]");

		return s.toString();
	}
	
	/**
	 * Given a reference list and an identifier, looks up the corresponding Property for it.
	 * If the reference list is not empty, it is evaluated and the corresponding Property is that on the final object in the reference list.
	 * If the reference list is empty, the corresponding Property is a variable directly from the environment.
	 */
	public static Variable lookupProperty(Environment environment, AST referenceObjectAST, AST identifierAST) throws WordsRuntimeException {
		Variable referenceObject = referenceObjectAST.eval(environment);
		Variable identifier = identifierAST.eval(environment);
		
		if (referenceObject.type.equals(Variable.Type.OBJ)){
			return referenceObject.objValue.getProperty(identifier.stringValue);
		} else {
			return environment.getVariable(identifier.stringValue);
		}
	}
}
