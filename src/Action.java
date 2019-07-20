import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Date;

public class Action extends Main {
	
	/*
	 * This class is just for telling the program how to exactly "Left click"..
	 */
	
	static boolean boolInterval = true;
	static boolean colourDebugging = true;

	public static boolean check(int x, int y, int r, int g, int b, String str) throws AWTException {
		Date date = new Date();
		Robot robot = new Robot();
		Color color = robot.getPixelColor(x, y);

		if ((r == color.getRed()) && (g == color.getGreen()) && (b == color.getBlue())) {

			if (colourDebugging) {
				System.out.println("Found colour - " + str + "\n" + "  Red   = " + r + "\n" + "  Green = " + g + "\n"
						+ "  Blue  = " + b + "\n\n");
			}
			return true;
		} else {
			if (colourDebugging)
				System.out.println("Didn't find colour of " + str + " \n" + "-------------------" + "\n\n");
			return false;
		}
	}
	
	public static Color getColour(int x, int y) {
		Color col = null;

		try {
			Robot r = new Robot();
			
			col = r.getPixelColor(x, y);
			if(Main.debug)
			System.out.println(col.toString());
		}
		
		catch(Exception e) {}
		return col;
		
	}
	

	
	public static void keyPress(char c) throws AWTException {
		Robot r = new Robot();

		r.keyPress((int) c);
		r.keyRelease((int) c);
	}

	public static void keyPress(int c) throws AWTException {
		Robot r = new Robot();

		r.keyPress(c);
		r.keyRelease(c);
	}

	public static void stringType(String s) throws AWTException { // cant type in capitals
		Robot r = new Robot();
		char key;
		boolean keyShift = false;

		for (int i : s.toCharArray()) { // loop through the strings length


			if (i >= 65 && i <= 90) { // if key is between capital letters in ascii, hold shift
				keyShift = true;
				key = (char) ((int) i);

			}
			if (i > 90 && i <= 122) {
				keyShift = false;
				key = (char) ((int) i - 32);

			} else
				key = (char) i;

			//to convert noncapital letters to capital , simply have the integer  - 32.
			
			if (KeyEvent.CHAR_UNDEFINED == key) {
				throw new RuntimeException("Key code not found for character " + i + "");
			}

			if (key == 0x00) { // if the character is null, change it to space.
				key = 32;
			}

//			System.out.println(key + " " + (int) key + " key pressed");

			if (keyShift) {
				r.keyPress(KeyEvent.VK_SHIFT);
				keyPress(key);
				r.keyRelease(KeyEvent.VK_SHIFT);
				System.out.print(key);

			} else {
				keyPress(key);
				key = (char) ((int) key - 32);
				System.out.print(key);

			}
		}
	}
	
	//simple actions
	public static void left() throws AWTException {
		Robot r = new Robot();
		r.mousePress(InputEvent.BUTTON1_MASK);
		r.mouseRelease(InputEvent.BUTTON1_MASK);
		
	}
	
	public static void right() throws AWTException {
		Robot r = new Robot();
		r.mousePress(InputEvent.BUTTON3_MASK);
		r.mouseRelease(InputEvent.BUTTON3_MASK);
		
	}
	
	public static void middle() throws AWTException {
		Robot r = new Robot();
		r.mousePress(InputEvent.BUTTON2_MASK);
		r.mouseRelease(InputEvent.BUTTON2_MASK);
		
	}

	public static void shiftClick() throws AWTException {
		Robot r = new Robot();
		r.keyPress(InputEvent.SHIFT_MASK);
		r.mousePress(InputEvent.BUTTON1_MASK);
		r.mouseRelease(InputEvent.BUTTON1_MASK);
		r.keyRelease(InputEvent.SHIFT_MASK);
		
	}
	
	
	
	//Mouse move simple actions 
	
	//Left click at coordinate
	public static void left(int x, int y) throws AWTException {
		Robot r = new Robot();
		r.mouseMove(x, y);
		r.mousePress(InputEvent.BUTTON1_MASK);
		r.mouseRelease(InputEvent.BUTTON1_MASK);
		
	}
	
	//right click at coordinate
	public static void right(int x, int y) throws AWTException {
		Robot r = new Robot();
		r.mouseMove(x, y);
		r.mousePress(InputEvent.BUTTON3_MASK);
		r.mouseRelease(InputEvent.BUTTON3_MASK);
		
	}
	
	//middle mouse click at coordinate
	public static void middle(int x, int y) throws AWTException {
		Robot r = new Robot();
		r.mouseMove(x, y);
		r.mousePress(InputEvent.BUTTON2_MASK);
		r.mouseRelease(InputEvent.BUTTON2_MASK);
		
	}
	
	//Move mouse at coordinate
	public static void mouseMove(int x , int y) throws AWTException {
		Robot r = new Robot();
		r.mouseMove(x, y);
	}
	
	public static void mouseMove(int x2, int y2, int t, int n) throws AWTException, InterruptedException {
		Robot r = new Robot();
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		
		int x1 = (int) b.getX();
		int y1 = (int) b.getY();
		
		 double dx = (x2 - x1) / ((double) n);
	        double dy = (y2 - y1) / ((double) n);
	        double dt = t / ((double) n);
	        for (int step = 1; step <= n; step++) {
	            Thread.sleep((int) dt);
	            r.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
	        }
	}

	//Holds shift and left clicks at coordinate
	public static void shiftClick(int x, int y) throws AWTException {
		Robot r = new Robot();
		r.mouseMove(x, y);
		r.keyPress(InputEvent.SHIFT_MASK);
		left(x, y);
		r.keyRelease(InputEvent.SHIFT_MASK);

	}
		
	//Delay actions for a certain amount of (ms)
	public static void delay(int ms) throws AWTException {
		Robot r = new Robot();
		r.delay(ms);
	}
	
	
}
