public class INodeReferenceList extends INode {
	public INodeReferenceList(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		// TODO
		//throw new AssertionError("Not yet implemented");
		return new ASTValue(ASTValue.ValueType.NOTHING);
	}
}