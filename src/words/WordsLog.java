package words;
import java.util.*;

import words.environment.WordsPosition;

public class WordsLog {
	HashMap<String, LinkedList<RenderData>> content;

	/**
	 * A structure to hold rendering information about a single Words object.
	 */
	private class RenderData {
		public String className;
		public String objName;
		public String message;

		public RenderData(String className, String objName, String message) {
			this.className = className;
			this.objName = objName;
			this.message = message;
		}

		@Override
		public String toString() {
			return "<" + className + ">" + " " + objName + (message != null ? (" \"" + message + "\"") : "");
		}
	}

	/**
	 * Convert a position into a string suitable to serve as a hash key.
	 *
	 * @return a string representing the coordinate
	 */
	private String positionToKey(WordsPosition p) {
		return Integer.toString(p.x) + "," + Integer.toString(p.y);
	}

	private void logCell(String key) {
		LinkedList<RenderData> list = content.get(key);
		if (list != null) {
			System.out.println("(" + key + "):");
			ArrayList<String> renderDataStr = new ArrayList<String>();
			for (RenderData r : list) {
				renderDataStr.add(r.toString());
			}
			Collections.sort(renderDataStr);
			for (int i = 0; i < renderDataStr.size(); ++i)
				System.out.println(renderDataStr.get(i));
		}
	}

	public WordsLog() {
		content = new HashMap<String, LinkedList<RenderData>>();
	}

	/**
	 * Adds an object to the log at a given position.
	 *
	 * @param p The position of the object
	 * @param className The name of the class of this object
	 * @param objName The name of the object
	 * @param message The message that the object should say.  May be null.
	 */
	public void add(WordsPosition p, String className, String objName, String message) {
		String key = positionToKey(p);
		LinkedList<RenderData> list = content.get(key);

		if (list == null) {
			list = new LinkedList<RenderData>();
			content.put(key, list);
		}

		list.add(new RenderData(className, objName, message));
	}

        /**
         * Output logs of frameloop run.
         */
	public void log() {
                SortedSet<String> keys = new TreeSet<String>(content.keySet());
                for (String key : keys) {
                        logCell(key);
                }
	}
}
