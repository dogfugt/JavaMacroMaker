import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.*;
import java.awt.event.*;

public class Settings extends JFrame implements ActionListener {

	JPanel					cp;
	
	JCheckBox 				showRGBValueBox = new JCheckBox("Show RGB values in table", false);
	
	public static boolean 	showRGBValuesOnTable = false;
	
	/**
	 * Launch the application.
	 */
	public static void Settings() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Settings frame = new Settings();
					frame.setSize(400,200);
					frame.setVisible(true);
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

				} catch (Exception e) {e.printStackTrace();}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public Settings() throws IOException {
		setTitle("Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		cp = new JPanel();
		setContentPane(cp);
		cp.setLayout(null);

		showRGBValueBox.setBounds(8, 8, 180, 20);
		showRGBValueBox.addActionListener(this);
		cp.add(showRGBValueBox);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == showRGBValueBox) {
			if (showRGBValueBox.isSelected())
				showRGBValuesOnTable = true;
			else
				showRGBValuesOnTable = false;
		}
	}

}
