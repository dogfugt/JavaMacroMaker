

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.jnativehook.*;



public class ScreenDrawFrame extends JFrame {
	
	private JPanel contentPane;
	static 	ScreenDraw panel = new ScreenDraw();
	
	Color 	startC = new Color(0,170,255);
	Color	endC = new Color(255, 0, 0);

	double	radius = (panel.getScale() * 8);
	double 	dLine = panel.getScale() * 8;
	
	int 	sclRadius = (int) radius;
	int 	line = (int)dLine;
	int 	crosshairlength = 8, rectSize = 10;

	
	/**
	 * Launch the application.
	 */
	public static void ScreenDrawFrame() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScreenDrawFrame frame = new ScreenDrawFrame();
					frame.setSize(panel.getWidth() + 16, panel.getHeight() + 39);
					System.out.println(panel.getWidth() + 16 + ", " + (panel.getHeight() -1));
					frame.setVisible(true);
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public ScreenDrawFrame() throws IOException {
		setTitle("Screen Displayer");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.add(panel, BorderLayout.CENTER);
	}
	
	/**
	 * Paint the cool stuff that shows where your clicks will be and that...
	 * Paints all your commands
	 */
	public void paint(Graphics g) {
		// Drawing clicks or mouse movements currently only works with .5 and .25 scales.
		Graphics2D g2d = (Graphics2D) g;
		try {
			panel.draw(g);

			for (int i = 0; i < Main.runAction.size() - 1; i++) {

				if (Main.runX.size() > 0 && Main.runY.size() > 0) {  //so it doesnt pull out an array that isnt there
					if (Main.runX.get(i) != Main.runX.get(i + 1) || Main.runY.get(i) != Main.runY.get(i + 1)) { //FIXME, doesnt draw the most recent click
						// If the current action isnt in the same coordinates as the next one after, draw mouse movement.
						drawMouseMoveNoX(g2d, Main.runX.get(i), Main.runY.get(i), Main.runX.get(i + 1), Main.runY.get(i + 1));
					}
				}

				switch (Main.runAction.get(i)) {
				case 0: // left click
					drawX(g, Main.runX.get(i), Main.runY.get(i), Color.green);
					drawColouredBox(g, Main.runX.get(i), Main.runY.get(i));

					break;

				case 1: // right click
					drawClick(g, Main.runX.get(i), Main.runY.get(i)/*, Color.blue*/);
					break;

				case 2: // middle click
					drawClick(g, Main.runX.get(i), Main.runY.get(i)/*, Color.cyan*/);
					break;

				case 3: // mouse move
					drawMouseMove(g2d, Main.runX.get(i - 1), Main.runY.get(i - 1), Main.runX.get(i), Main.runY.get(i));
					break;

				case 4: // shift click
					drawClick(g, Main.runX.get(i), Main.runY.get(i));
					break;

				case 5: // Type
					drawType(g, Main.runType.get(i).toString(), Main.runX.get(i), Main.runY.get(i), Color.black);
					break;

				default:
//					drawCursor(g, 0, 0, Color.white);
					break;

				}
			}

			if (Main.debug)
				if (!(screenConv(panel.getWidth()) != panel.getWidth() * panel.getScale()))
					System.out.println("Looks like it converted correctly!");

		} catch (IOException e) {e.printStackTrace();}

	}
	
	
	// Drawing action stuff
	public void drawMouseMove(Graphics2D g, int x, int y, int newX, int newY) { // mouseMove
		x = screenConv(x) + panel.xFix;
		y = screenConv(y) + panel.yFix;
		newX = screenConv(newX) + panel.xFix;
		newY = screenConv(newY) + panel.yFix;
		
		GradientPaint gradient = new GradientPaint(x, y, startC, newX, newY, endC);
		g.setPaint(gradient);

		g.drawLine(x, y, newX, newY);
		drawX(g,x,y);
		drawX(g,newX,newY);
	}
	
	public void drawMouseMoveNoX(Graphics2D g, int x, int y, int newX, int newY) { // mouseMove with no X in middle
		x = screenConv(x) + panel.xFix;
		y = screenConv(y) + panel.yFix;
		newX = screenConv(newX) + panel.xFix;
		newY = screenConv(newY) + panel.yFix;
		
		GradientPaint gradient = new GradientPaint(x, y, startC, newX, newY, endC);
		g.setPaint(gradient);

		g.drawLine(x, y, newX, newY);
	}
	
	public void drawClick(Graphics g, int x, int y) {
		g.setColor(Color.green); //crosshair on click
		
		x = screenConv(x) + panel.xFix;
		y = screenConv(y) + panel.yFix;;

//		g.setColor(Color.red);
		g.drawLine(x, y + crosshairlength, x, y - crosshairlength);
		g.drawLine(x - crosshairlength, y, x + crosshairlength, y);
//		drawX(g,x,y,Color.cyan);
		
//		g.setColor(Color.green); //circle on click
//		g.drawOval(x - line/2, y - line/2, sclRadius , sclRadius );
		
//		g.setColor(Color.red); //dot on click
//		g.drawLine(x, y, x, y);
//		g.setColor(Color.green);
		
		/*g.setColor(new Color(0,0,0));
		g.fillOval(x - line, y - line, line*2, line*2);*/
	}
	
	

	public void drawX(Graphics g, int x, int y, Color c) {
		g.setColor(c);
		x = screenConv(x) + panel.xFix;
		y = screenConv(y) + panel.yFix;
		g.drawLine(x - crosshairlength, y + crosshairlength, x + crosshairlength, y - crosshairlength);
		g.drawLine(x - crosshairlength, y - crosshairlength, x + crosshairlength, y + crosshairlength);

	}
	
	public void drawX(Graphics g, int x, int y) {
		x = screenConv(x) + panel.xFix;
		y = screenConv(y) + panel.yFix;
		
		g.drawLine(x - crosshairlength, y + crosshairlength, x + crosshairlength, y - crosshairlength);
		g.drawLine(x - crosshairlength, y - crosshairlength, x + crosshairlength, y + crosshairlength);

	}

	private void drawCursor(Graphics g, int x, int y/* , int times */, Color c) {
		
		// drawCursor is not drawing at the screenConv (REAL COORDINATE)
		int newX = x;
		int times = 25;
		int yold = y;
		int modulo = 2;
		

		
		System.out.println("X: " + x + ", Y: " + y + ", Times: " + times);

		for (int i = 0; i <= times; i++) {
//			g.setColor(Color.white);
			g.setColor(c);
			g.drawLine(x, y, newX, y);
			y++;
			if (y % modulo == 0)
				newX++;

		}
		g.setColor(Color.black);
		g.drawLine(x + 1, yold, x + (times / modulo) + 1, y); // 0,0 to bottom right triangle.
		g.drawLine(x, yold, x, (int) (y + pythagNotC(pythagC(times, times)) / screenConv(y))); // 0,0 to bottom left triangle.
		g.drawLine(x, (int) (y + pythagNotC(pythagC(times, times)) / screenConv(y)), x + (times / modulo) + 1, y); // bottom left to bottom right triangle.
		
		for(int i = 0; i <= 5; i++) { //draw the spine of cursor
			g.setColor(Color.white);
//			g.drawLine(x + times / 2, (int) (y + pythagNotC(pythagC(times, times)) / screenConv(y)), x + times + 5, (int) (y + pythagNotC(pythagC(times, times)) / screenConv(y)) * 2);
			
		}

	}
	
	public void drawType(Graphics g, String str, int x, int y, Color c) {
		x = screenConv(x) + panel.xFix;
		y = screenConv(y) + panel.yFix;
		

		g.setColor(Color.white);
		g.fillRoundRect(x, y, str.length() * 6, 20, 10, 10); //draw a white textbox behind
		
		g.setColor(c);
		g.drawString(str, x + 2, y + 16);
		
		g.setColor(Color.black);
		g.drawRoundRect(x, y, str.length() * 6, 20, 10, 10); //black border around white textbox
	}
	
	public void drawColouredBox(Graphics g, int x, int y) {
		try {
			Robot r = new Robot();

		} catch (Exception e) {}
		
		x = screenConv(x) + panel.xFix;
		y = screenConv(y) + panel.yFix;
		
		Color col = Action.getColour(x, y);
		g.setColor(col);
		g.fillRect(x, y, rectSize, rectSize);
		
		//border of box
		g.setColor(Color.black);
		g.drawRect(x - 1, y - 1, rectSize + 1, rectSize + 1);
		
	}
	
	public int screenConv(int x) {
		//Converts the pixels on original screen over to the scaled new image
		double scaleInverse = 1 / panel.scale;
		return x = x / (int)scaleInverse;
	}
	
	// Stuff for the cursor click method
	public double pythagC(double a, double b) {
		double c = (Math.pow(a, 2) + Math.pow(b, 2));
		return Math.sqrt(c);
	}

	public double pythagNotC(double c) {
		double a = (c * Math.sqrt(2) / 2);
		return a;
	}

}
