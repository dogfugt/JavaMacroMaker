import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScreenDraw extends JPanel {

	ImageIcon 		image1, image2;
	JLabel 			label1, label2;

	BufferedImage 	screen;
	Rectangle 		screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	Image 			image;

	int 			xPosition, yPosition;
	int				width = 50, height = 50;
	int 			xFix = 8, yFix = 31, imgFix = 32;

	double 			scale = .5; //use .5 or .25
	double 			scaleWidth = (screenRect.getWidth() * scale);
	double 			scaleHeight = (screenRect.getHeight() * scale);
	
	String 			documents = System.getProperty("user.dir");
	String 			newFile = "screenDisplay.png";
	
	
	/**
	 *  Draw monitor image
	 */
	public void draw(Graphics g) throws IOException {
		try {	
			
			BufferedImage capture = new Robot().createScreenCapture(screenRect);
			System.out.println(getWidth() + ", " + getHeight());

			ImageIO.write(capture, "jpg", new File(documents + "screenDisplay.jpg"));

			g.drawImage(capture, xPosition + xFix, yPosition + yFix, (int) scaleWidth, (int) scaleHeight, this); //WORKING USE THIS
			

		} catch (AWTException e) {
			e.printStackTrace();
			System.out.println("I could not draw a screen");
		}
	}
	
	public int getInverse() {
		double scaleInverse = 1 / scale;
		return (int)scaleInverse;
	}
	
	public int getHeight() {
		return (int)Math.round(scaleHeight) ;
	}
	
	public int getWidth() {
		return (int)Math.round(scaleWidth); //fiximg used to be 8
	}
		
	public double getScale() {
		return scale;
	}
}
