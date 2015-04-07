package words;
import java.util.concurrent.LinkedBlockingDeque;

import words.ast.AST;
import words.environment.*;
import words.exceptions.WordsRuntimeException;

public class FrameLoop extends Thread {
	private static int counter = 1;

	private LinkedBlockingDeque<AST> ASTQueue;
	private WordsEnvironment environment;
	private WordsUI GUI;

	/**
	 * Initialize the Frame Loop with a GUI -- generally used for a real run of the program
	 */
	public FrameLoop(WordsUI GUI) {
		this.environment = new WordsEnvironment();
		this.ASTQueue = new LinkedBlockingDeque<AST>();
		this.GUI = GUI;
	}

	/**
	 * Initialize the frame loop with a specific environment -- generally used for testing
	 */
	public FrameLoop(WordsEnvironment environment) {
		this.environment = environment;
		this.ASTQueue = new LinkedBlockingDeque<AST>();
		this.GUI = null;
	}

	public void enqueueAST(AST ast) {
		ASTQueue.add(ast);
	}


	public void run() {
		boolean finished = false;
		while (!finished) {
			long timeToSleep = Option.TIME_TO_WAIT;
		    long start, end, slept;
			while (timeToSleep > 0){
		        start = System.currentTimeMillis();
		        try {
		            Thread.sleep(timeToSleep);
		            break;
		        } catch (InterruptedException e) {
					//work out how much more time to sleep for
					end = System.currentTimeMillis();
					slept = end-start;
					timeToSleep -= slept;
		        }
			}
			finished = executeSingleFrame();
		}
	}

	private boolean executeSingleFrame() {
		boolean finished = false;
		
		// Phase 1: Statement Translation and Execution
		while (!ASTQueue.isEmpty()) {
			AST ast = ASTQueue.pop();
			try {
				ast.eval(environment);
			} catch (WordsRuntimeException e) {
				// Note: this should never actually be caught here; it should be caught earlier at the statement level.
				System.err.println();
				System.err.println(e.toString());
				System.out.println("> ");
			}
		}
		
		// Phase 2: Action Queue Processing
		for (WordsObject object : environment.getObjects()) {
			try {
				object.executeNextAction(environment);
			} catch (WordsProgramException e) {
				System.err.println("Error executing action on object " + object.getObjectName() + ": \n" + e.toString());
				System.err.println("Action will not be performed");
				System.out.println("> ");
			}
		}

		// Phase 3: Listener Evaluation
		for (WordsEventListener eventListener : environment.getEventListeners()) {
			eventListener.execute();
		}

		if (GUI != null) {
			GUI.clear();
			for (WordsObject object : environment.getObjects()) {
				GUI.add(object.getCurrentCell(), object.getClassName(), object.getObjectName(), object.getCurrentMessage());
			}
			GUI.render();
		}  else {
			System.out.println("counter = " + counter);
			WordsLog log = new WordsLog();
			for (WordsObject object : environment.getObjects()) {
				log.add(object.getCurrentCell(), object.getClassName(), object.getObjectName(), object.getCurrentMessage());
			}
			log.log();
			if (counter >= Option.MAX_COUNTER)
				finished = true;
		}

		++counter;
		return finished;
	}

	public void fastForwardEnvironment(int numOfFrames) {
		for (int i = 0; i < numOfFrames; i++) {
			executeSingleFrame();
		}
	}
}
