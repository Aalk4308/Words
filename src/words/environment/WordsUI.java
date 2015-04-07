package words.environment;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

/**
 * A display to present a running Words program, along with UI elements to allow the user to customize the display (e.g., pan, zoom).
 */
public class WordsUI {
	JFrame window;						// The entire window containing the UI
	JPanel panel;						// The entire content of the window
	JPanel grid;						// The portion of the content showing the grid of cells
	JPanel buttons;						// The portion of the content showing the buttons

	int windowWidth, windowHeight;		// Dimensions of the window in pixels
	int boardSize; 						// Dimensions (square) of the board in pixels
	int numCells;						// Number of rows (columns) in the board (must be odd)
	static final int minCells = 5;		// Must be odd so that there is a cell in the center
	static final int maxCells = 25;		// Must be odd so that there is a cell in the center
	static final int zoomStep = 2;		// Must be even so that there is a cell in the center
	static final int moveStep = 2;
	int xCenterCell, yCenterCell;		// The board coordinates of the cell depicted in the center

	static final int topPadding = 20;	// Padding at the top of the window before the board in pixels

	static final Font fObj = new Font("SansSerif", Font.BOLD, 12);
	static final Font fClass = new Font("SansSerif", Font.PLAIN, 9);
	static final Font fMsg = new Font("SansSerif", Font.PLAIN, 8);

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
			return className + " " + objName + (message != null ? " " + message : "");
		}
	}

	/**
	 * Convert a position into a string suitable to serve as a hash key.
	 *
	 * @return a string representing the coordinate
	 */
	private String positionToKey(WordsPosition p) {
		return Integer.toString(p.x) + "_" + Integer.toString(p.y);
	}

	private class Grid extends JPanel {
		int cellSize;					// Dimensions of a cell in pixels

		/**
		 * Draws a string centered at given coordinates.
		 * Adapted from http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.htm
		 *
		 * @param g2 The Graphics2D context in which to draw
		 * @param string The text that should be drawn
		 * @param xCenter The x pixel position where the string should be centered
		 * @param yCenter The y pixel position where the string should be centered
		 * @param font The font the string should be drawn in
		 * @param color The color the string should be drawn in
		 */
		private void drawCenteredString(Graphics2D g2, String string, int x, int y, Font font, Color color) {
			g2.setFont(font);
			g2.setPaint(color);

			FontMetrics fm = g2.getFontMetrics();
			int w = fm.stringWidth(string);
			int h = fm.getAscent() + fm.getDescent();
			g2.drawString(string, x - w/2, y - h/2 + fm.getAscent());
		}

		/**
		 * Renders a given cell from the grid at given pixel coordinates.
		 *
		 * @param g2 The Graphics2D context in which to draw
		 * @param xCenter The x pixel position where the cell should be centered
		 * @param yCenter The y pixel position where the cell should be centered
		 * @param p The position in the grid that should be rendered
		 */
		private void renderCell(Graphics2D g2, int xCenter, int yCenter, WordsPosition p) {
			g2.setPaint(new Color(128, 128, 128));
			g2.setStroke(new BasicStroke());

			g2.drawRect(xCenter - cellSize/2, yCenter - cellSize/2, cellSize, cellSize);

			LinkedList<RenderData> list = content.get(positionToKey(p));
			if (list != null) {
				// Simple rendering of an object
				// TODO: More gracefully render situations where multiple objects are on the same cell
				int fillSize = cellSize - 4;
				for (RenderData r : list) {
					g2.setPaint(new Color(64, 64, 64));
					g2.fillRect(xCenter - fillSize/2, yCenter - fillSize/2, fillSize, fillSize);

					drawCenteredString(g2, r.objName, xCenter, yCenter - fillSize/3, fObj, Color.WHITE);
					drawCenteredString(g2, r.className, xCenter, yCenter, fClass, Color.GRAY);

					if (r.message != null)
						drawCenteredString(g2, r.message, xCenter, yCenter + fillSize/4, fMsg, Color.GRAY);
				}
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;

			// Background region
			int x1, y1, x2, y2;			// Boundaries of the board in pixels
			int cx, cy;					// Center of the board in pixel

			x1 = (windowWidth - boardSize)/2;
			y1 = topPadding;
			x2 = x1 + boardSize;
			y2 = y1 + boardSize;
			cx = (x1 + x2)/2;
			cy = (y1 + y2)/2;
			cellSize = (x2 - x1) / numCells;

			g2.setPaint(new Color(255, 255, 255));
			g2.fillRect(x1, y1, x2-x1, y2-y1);

			// Draw each cell
			for (int i = -(numCells-1)/2; i <= (numCells-1)/2; i++)
				for (int j = -(numCells-1)/2; j <= (numCells-1)/2; j++)
					renderCell(g2, cx + i*cellSize, cy + j*cellSize, new WordsPosition(xCenterCell + i, yCenterCell + j));
		}
	}

	private class Buttons extends JPanel {
		Button up, down, left, right, zoomIn, zoomOut;
		Handler handler;

		private class Handler implements ActionListener {
		   public void actionPerformed(ActionEvent e) {
			   if (e.getSource() == up) {
				   yCenterCell += moveStep;
			   } else if (e.getSource() == down) {
				   yCenterCell -= moveStep;
			   } else if (e.getSource() == left) {
				   xCenterCell += moveStep;
			   } else if (e.getSource() == right) {
				   xCenterCell -= moveStep;
			   } else if (e.getSource() == zoomIn) {
				   numCells -= zoomStep;
				   numCells = numCells < minCells ? minCells : numCells;
			   } else if (e.getSource() == zoomOut) {
				   numCells += zoomStep;
				   numCells = numCells > maxCells ? maxCells : numCells;
			   }

			   grid.repaint();
		   }
		}

		public Buttons() {
			handler = new Handler();

			up = new Button("Up");
			this.add(up);
			up.addActionListener(handler);

			down = new Button("Down");
			this.add(down);
			down.addActionListener(handler);

			left = new Button("Left");
			this.add(left);
			left.addActionListener(handler);

			right = new Button("Right");
			this.add(right);
			right.addActionListener(handler);

			zoomIn = new Button("Zoom In");
			this.add(zoomIn);
			zoomIn.addActionListener(handler);

			zoomOut = new Button("Zoom Out");
			this.add(zoomOut);
			zoomOut.addActionListener(handler);
		}
	}

	public WordsUI() {
		window = new JFrame();
		panel = new JPanel();
		grid = new Grid();
		buttons = new Buttons();

		windowWidth = 750;		// Default windowWidth
		windowHeight = 750;		// Default windowHeight
		boardSize = 650;		// Default boardSize
		numCells = 9;			// Default numCells
		xCenterCell = 0;
		yCenterCell = 0;

		content = new HashMap<String, LinkedList<RenderData>>();

		panel.setLayout(new BorderLayout());
		panel.add(grid, BorderLayout.CENTER);
		panel.add(buttons, BorderLayout.SOUTH);

		window.setContentPane(panel);
		window.setSize(windowWidth,windowHeight);
		window.setLocation(500,100);
		window.setVisible(true);
		window.setResizable(false);
	}

	/**
	 * Clears all objects from the UI.  After calling this method, the user will see only an empty grid.
	 * Typically, the caller will want to immediately repopulate the grid with new objects.
	 */
	public void clear() {
		content.clear();
		grid.repaint();
	}

	/**
	 * Adds an object to the UI at a given position.
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
	 * Immediately updates the UI to show the objects that have been added.
	 */
	public void render() {
		grid.repaint();
	}

	/**
	 * Retrieves the position of the cell where the grid is currently centered.
	 *
	 * @return the position of the cell currently at the center of the grid
	 */
	public WordsPosition getCenter() {
		return new WordsPosition(xCenterCell, yCenterCell);
	}
}