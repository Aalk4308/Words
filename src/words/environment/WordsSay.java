package words.environment;
import java.util.LinkedList;

public class WordsSay extends WordsAction {
	private String message;
	// TODO: Should also allow expression
	
	public WordsSay(String message) {
		this.message = message;
	}
	
	@Override
	public boolean isExecutable() {
		return true;
	}

	@Override
	protected void doExecute(WordsObject object, WordsEnvironment environment) {
		object.setMessage(message);
	}

	@Override
	protected LinkedList<WordsAction> doExpand(WordsObject object, WordsEnvironment environment) { return null; }
}