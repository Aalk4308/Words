package words.test;
import static org.junit.Assert.*;

import org.junit.Test;

import words.environment.WordsPosition;

public class TestWordsPosition {
	@Test
	public void testDoubleParams() {
		WordsPosition pos = new WordsPosition(3.7, 5.2);
		assertEquals("X position correctly rounded", pos.x, 4);
		assertEquals("Y position correctly rounded", pos.y, 5);
	}
	
	@Test
	public void testIntParams() {
		WordsPosition pos = new WordsPosition(2,4);
		assertEquals("X position correctly rounded", pos.x, 2);
		assertEquals("Y position correctly rounded", pos.y, 4);
	}
}
