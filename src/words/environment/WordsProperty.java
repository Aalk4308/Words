package words.environment;
/**
 * A property that can be assigned to a WordsObject.
 */
public class WordsProperty {
	public enum PropertyType {
		NOTHING, NUM, STRING, OBJECT
	}
	
	public PropertyType type;
	
	public double numProperty;
	public String stringProperty;
	public WordsObject objProperty;
	
	public WordsProperty(int n) {
		this.type = PropertyType.NUM;
		this.numProperty = (double) n;
	}
	
	public WordsProperty(double n) {
		this.type = PropertyType.NUM;
		this.numProperty = n;
	}

	public WordsProperty(String s) {
		this.type = PropertyType.STRING;
		this.stringProperty = s;
	}
	
	public WordsProperty(WordsObject o) {
		this.type = PropertyType.OBJECT;
		this.objProperty = o;
	}
	
	public WordsProperty(PropertyType type) {
		this.type = type;
	}
}