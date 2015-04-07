package words.environment;
public class WordsPosition {
	public int x;
	public int y;
	
	public WordsPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public WordsPosition(double x, double y) {
		this.x = (int) Math.round(x);
		this.y = (int) Math.round(y);
	}
	
	@Override 
	public String toString() {
		return Integer.toString(x) + " , " + Integer.toString(y);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof WordsPosition)) {
			return false;
		}
		WordsPosition otherPos = (WordsPosition) other;
		return this.x == otherPos.x && this.y == otherPos.y;
	}
}
