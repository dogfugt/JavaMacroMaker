import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JInternalFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JProgressBar;
import com.jgoodies.forms.factories.DefaultComponentFactory;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import org.jnativehook.GlobalScreen;
import org.jnativehook.*;
import org.jnativehook.mouse.*;

@SuppressWarnings({ "serial", "unused" })
public class Main extends JFrame implements ActionListener {

	/* TODO
	 * Save array to textfiles.. load textfile to arrays.
	 * 
	 * make it repeat the script, not repeat individual commands.. put repeat thing next to the run button
	 */
	
	// Classes
	static HumanClickArray hca = new HumanClickArray();

	// Variables
	static boolean 	cheat = false; // green text in humanarray console
	static boolean 	debug = true; // DEBUG WHAT GETS ADDED AND STUFF

	int				x, y, repeatClicks, action, mouseX, mouseY;
	int				intervalOne = 100, intervalTwo = 105;
	int				setInterval;
	
	private int 	i;
	
	String debugStr = "DEBUG: ";

	// Action
	String[] 	actionTypes = { "Left click", "Right click", "Middle click", "Mouse move", "Shift click", "Type" }; // Types of action in choice combobox
	JComboBox 	actionTypeChoice = new JComboBox(actionTypes);

	// Arrays, storing values of commands
	public static List<Integer> runAction = new ArrayList<Integer>();
	public static List<Integer> runX = new ArrayList<Integer>();
	public static List<Integer> runY = new ArrayList<Integer>();
	public static List<Integer> runRepeat = new ArrayList<Integer>();
	public static List<Integer> humanArray = new ArrayList<Integer>();
	public static List<Integer> runInterval = new ArrayList<Integer>();
	public static List<String> runType = new ArrayList<String>();
	public static List<String> runRGBstr = new ArrayList<String>();
	public static List<Color>  runRGB = new ArrayList<Color>();

	// Major components
	JButton 	btnAddAction = new JButton("Add action");
	JButton 	btnRun = new JButton("Run"); //Execute commands chronologically
	JButton 	btnHumanArraySetup = new JButton("Setup Human Interval"); //Open human interval setup
	JButton 	btnScreenDraw = new JButton("What I see"); //ScreenDraw window opener
	JButton 	btnSettings = new JButton("Settings"); //Open settings

	JCheckBox 	hasXY = new JCheckBox("Use mouse as coordinate"); //If enabled, the user will have 1 second to move the mouse to a location, then the program will automatically set the X and Y values.

	JSpinner 	repeatTimesSpinner = new JSpinner();
	JSpinner 	intervalHardOne = new JSpinner();
	JSpinner 	intervalHardTwo = new JSpinner();
	JSpinner	simpleIntervalSpinner = new JSpinner();

	JPanel 		cp;
	
	JTextField	moveXTxt, moveYTxt, txtTypeString;
	
	// Interval radio buttons
	ButtonGroup intervalBtns = new ButtonGroup();
	JRadioButton rdbtnHardInterval = new JRadioButton("Random Intervals");
	JRadioButton rdbtnHumanInterval = new JRadioButton("Human Interval Array");
	JRadioButton rdbtnSimpleInterval = new JRadioButton("Simple Interval");
	

	// Table
	TableCellRenderer RENDERER = new DefaultTableCellRenderer();
	DefaultTableModel model = new DefaultTableModel();
	String[] 	tableHeaders = { "Action", "Location (x, y)", "RGB", "Repeat action(s)", "Interval"};
	
	JTable 		table = new JTable(model) {
		
		/*
		 * This makes the table, 'RGB' column have a background colour of the colour that it was clicked on
		 * 
		 * e.g. if the entire screen is white, and i clicked somewhere on the screen, the rgb row would appear white for that action.
		*/
		
		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			JLabel label = (JLabel) super.prepareRenderer(renderer, row, column);

			for (int i2 = 0; i2 < runX.size(); i2++) {

				if (column == 2) {
					if (row == i2) {
						if (runRGB.size() >= 0) {
							if(Settings.showRGBValuesOnTable) {
								label.setBackground(runRGB.get(i2));
								label.setText(runRGBstr.get(i2));
							} else
							label.setBackground(runRGB.get(i2));
						}
					}

				} else
					label.setBackground(Color.WHITE);
			}
			return label;
		}
	};
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
		
			public void run() {
				try {					
					hca.loadHumanArray();

					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //Give window the operating system look
					Main frame = new Main();
					frame.setVisible(true);
					
				} catch (Exception e) {e.printStackTrace();}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		
		setTitle("Brendan's macro-based automation program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 621, 551);

		cp = new JPanel();
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(cp);
		cp.setLayout(null);

		JLabel xCoordlbl = new JLabel("Enter x coord:");
		xCoordlbl.setBounds(10, 11, 95, 14);
		cp.add(xCoordlbl);

		moveXTxt = new JTextField();
		moveXTxt.setHorizontalAlignment(SwingConstants.RIGHT);
		moveXTxt.setText("50");
		moveXTxt.setBounds(100, 8, 70, 20);
		cp.add(moveXTxt);
		moveXTxt.setColumns(10);

		moveYTxt = new JTextField();
		moveYTxt.setHorizontalAlignment(SwingConstants.RIGHT);
		moveYTxt.setText("255");
		moveYTxt.setBounds(100, 36, 70, 20);
		moveYTxt.setColumns(10);
		cp.add(moveYTxt);

		JLabel YCoordinateLbl = new JLabel("Enter y coord:");
		YCoordinateLbl.setBounds(10, 39, 95, 14);
		cp.add(YCoordinateLbl);

		repeatTimesSpinner.setBounds(100, 67, 70, 20);
		repeatTimesSpinner.setValue(1);
		cp.add(repeatTimesSpinner);

		JLabel lblRepeatTimes = new JLabel("Repeat time(s):");
		lblRepeatTimes.setBounds(10, 70, 95, 14);
		lblRepeatTimes.setToolTipText("How many times the action will be repeated");
		cp.add(lblRepeatTimes);

		JLabel lblNewLabel = new JLabel("Additional actions");
		lblNewLabel.setBounds(46, 224, 84, 14);
		lblNewLabel.setToolTipText("Any of the features used is typically for avoiding bans in games.");
		cp.add(lblNewLabel);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 219, 160, 31);
		cp.add(separator);
		actionTypeChoice.setModel(new DefaultComboBoxModel(actionTypes));

		actionTypeChoice.setBounds(10, 98, 160, 20);
		cp.add(actionTypeChoice);
		actionTypeChoice.addActionListener(this);

		btnAddAction.addActionListener(this);

		btnAddAction.setBounds(10, 154, 160, 23);
		cp.add(btnAddAction);

		JPanel panel = new JPanel();
		panel.setBounds(10, 240, 160, 212);
		cp.add(panel);
		panel.setLayout(null);

		JLabel lblIntervalBetweenActions = new JLabel("Action Interval (ms):");
		lblIntervalBetweenActions.setBounds(10, 11, 132, 14);
		panel.add(lblIntervalBetweenActions);

		// rdbtnHardInterval.setSelected(true);
		rdbtnHardInterval.setBounds(10, 89, 132, 23);
		panel.add(rdbtnHardInterval);
		intervalBtns.add(rdbtnHardInterval);
		rdbtnHumanInterval.setSelected(true);

		rdbtnHumanInterval.setBounds(10, 146, 142, 23);
		intervalBtns.add(rdbtnHumanInterval);
		panel.add(rdbtnHumanInterval);


		JLabel lblNewLabel_1 = new JLabel("-");
		lblNewLabel_1.setBounds(75, 119, 46, 14);
		panel.add(lblNewLabel_1);

		intervalHardTwo.setBounds(83, 119, 63, 20);
		intervalHardTwo.setValue(intervalTwo);
		panel.add(intervalHardTwo);

		intervalHardOne.setValue(intervalOne);
		intervalHardOne.setBounds(10, 119, 63, 20);
		panel.add(intervalHardOne);

		rdbtnSimpleInterval.setBounds(6, 32, 136, 23);
		intervalBtns.add(rdbtnSimpleInterval);
		panel.add(rdbtnSimpleInterval);


		// simple interval spinner
		simpleIntervalSpinner.setBounds(10, 62, 132, 20);
		simpleIntervalSpinner.setValue(1);
		panel.add(simpleIntervalSpinner);
		btnHumanArraySetup.setBounds(10, 176, 142, 23);
		btnHumanArraySetup.addActionListener(this);

		panel.add(btnHumanArraySetup);

		// Table
		
		table.setDefaultRenderer(Object.class, RENDERER);

		table.setDoubleBuffered(true);

		for (int i = 0; i < tableHeaders.length; i++) {
			model.addColumn(tableHeaders[i]);
		}
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setSize(415, 459);
		scrollPane.setLocation(180, 10);
//		scrollPane.setViewportView(table);
		scrollPane.setEnabled(false);
		cp.add(scrollPane);

		// Run button, execute commands
		btnRun.addActionListener(this);
		btnRun.setBounds(506, 480, 89, 23);
		cp.add(btnRun);
		
		// Open screen display window
		btnScreenDraw.setBounds(16, 480, 89, 23);
		btnScreenDraw.addActionListener(this);
		cp.add(btnScreenDraw);
		
		// Open screen display window
		btnSettings.setBounds(110, 480, 89, 23);
		btnSettings.addActionListener(this);
		cp.add(btnSettings);
		
		
		// Type area for the type string command
		txtTypeString = new JTextField();
		txtTypeString.setEnabled(false);

		txtTypeString.setText("Type string");
		txtTypeString.setBounds(10, 123, 160, 20);
		cp.add(txtTypeString);
		txtTypeString.setColumns(10);

		table.setVisible(true);

		// Image of screen

		cp.setFocusTraversalPolicy(new FocusTraversalOnArray(
				new Component[] { panel, xCoordlbl, moveXTxt, moveYTxt, YCoordinateLbl, repeatTimesSpinner,
						lblRepeatTimes, lblNewLabel, separator, actionTypeChoice, btnAddAction, lblIntervalBetweenActions,
						rdbtnHardInterval, rdbtnHumanInterval, lblNewLabel_1, intervalHardTwo, intervalHardOne }));

		// Toggle XY coordinate checkbox (So it can choose to click at x, y.. otherwise,
		// click at mouse position)
		hasXY.setBounds(10, 176, 142, 23);
		cp.add(hasXY);

	}

	protected void updateTypeTxt() { // this makes it so you can type in the textfield box
		boolean enabledType = actionTypeChoice.getSelectedItem().equals(actionTypes[5]);
		txtTypeString.setEnabled(enabledType);
		moveXTxt.setEnabled(!enabledType);
		moveYTxt.setEnabled(!enabledType);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Robot r = new Robot();

			if (e.getSource() == btnAddAction) { // This processes one action/command to be added to the script
				if (/* !moveXTxt.getText().isEmpty() && !moveYTxt.getText().isEmpty() && */ (int) repeatTimesSpinner
						.getValue() > 0) {

					// checkbox to click at mouse position or at x, y
					if (hasXY.isSelected()) {
						print("- You have 1 second to move to the coordinate -");
						r.delay(1500);
						PointerInfo a = MouseInfo.getPointerInfo();
						Point b = a.getLocation();
						x = (int) b.getX();
						y = (int) b.getY();
						moveXTxt.setText(x +"");
						moveYTxt.setText(y +"");
						
						print("timer ended");
//						x = mousex;
//						y = mousey;

					} else { // if the checkbox isnt checked...

						// X and Y equal to the x,y textboxes AND remove all characters that are not
						// numbers

						x = Integer.parseInt(moveXTxt.getText().toString().replaceAll("[^\\d.]", ""));
						y = Integer.parseInt(moveYTxt.getText().toString().replaceAll("[^\\d.]", ""));
					}

					repeatClicks = (int) (repeatTimesSpinner.getValue());
					intervalOne = (int) (intervalHardOne.getValue());
					intervalTwo = (int) (intervalHardTwo.getValue());

					setInterval();

					action = actionTypeChoice.getSelectedIndex();

					if (!moveXTxt.isEnabled()) { // if the type option is selected
						addArrays(action, x, y, Integer.parseInt(getRepeat()), setInterval(), txtTypeString.getText());

						addRow(getTypeAction(), // add table
								runType.get(i).toString(), /*runRGBstr.get(i).toString()*/ "", runRepeat.get(i) + " times",
								runInterval.get(i) + " ms");
						
						i++;
					} else {
						// add vales to arrays
						addArrays(action, x, y, Integer.parseInt(getRepeat()), setInterval(), txtTypeString.getText());

						// Add a new row to the table
						addRow(getTypeAction(), // add table
								runX.get(i) + ", " + runY.get(i), /*runRGBstr.get(i).toString()*/ "", runRepeat.get(i) + " times",
								runInterval.get(i) + " ms");
						i++;

					}

				}
			}

			if (e.getSource() == btnRun) {

				for (int i = 0; i < runX.size(); i++) {
					if (debug)
						System.out.println("Action: " + actionTypes[i] + ", X " + runX.get(i) + ", Y " + runY.get(i)
								+ ", RGB: " + runRGBstr.get(i) + ", Repeats " + runRepeat.get(i) + ", Interval "
								+ runInterval.get(i));

					switch (runAction.get(i)) {
					case 0:
						try {
							Action.left(runX.get(i), runY.get(i));
							Action.delay(runInterval.get(i));

						} catch (AWTException e1) {}
						break;

					case 1:
						try {
							Action.right(runX.get(i), runY.get(i));
							Action.delay(runInterval.get(i));

						} catch (AWTException e1) {}
						break;

					case 2:
						try {
							Action.middle(runX.get(i), runY.get(i));
							Action.delay(runInterval.get(i));

						} catch (AWTException e1) {}
						break;

					case 3:
						try {
							Action.mouseMove(runX.get(i), runY.get(i));
							Action.delay(runInterval.get(i));

						} catch (AWTException e1) {}
						break;

					case 4:
						try {
							Action.shiftClick(runX.get(i), runY.get(i));
							Action.delay(runInterval.get(i));

						} catch (AWTException e1) {}
						break;

					case 5:
						try {
							Action.stringType(runType.get(i));
							Action.delay(runInterval.get(i));
							// print(runType.get(i)); //debug what the program has written in the array
						} catch (AWTException e1) {}
						break;

					default:
						//do nothing
						break;

					}
				}
			}

			if (e.getSource() == btnHumanArraySetup) { //Open Human Array setup
				HumanClickArray hca = new HumanClickArray();
				hca.HumanClickArr();
			}

			if (e.getSource() == btnScreenDraw) { //Open "What I see" or screendraw
				ScreenDrawFrame sdf;
				try {
					sdf = new ScreenDrawFrame();
					sdf.ScreenDrawFrame();
					
				} catch (IOException e1) {e1.printStackTrace();}

			}
			
			if (e.getSource() == btnSettings) { //Open settings
				Settings stngs;
				try {
					stngs = new Settings();
					stngs.Settings();
					
				} catch (IOException e1) {e1.printStackTrace();}
			}

			updateTypeTxt();
		} catch (AWTException e2) {
			System.out.println("Couldnt make robot");
		}
	}
	
	public void addArrays(int action, int x, int y, int repeat, int interval, String type) {
		runAction.add(action);
		runType.add(type);

		runX.add(x); // so it doesnt mess up
		runY.add(y); // so it doesnt mess up //FIXME
		runRepeat.add(repeat);
		runInterval.add(interval);
		// add rgb through methods, because it needs another class to detect RGB colours
		addRGBstr(x, y);
		addRGBarr(x, y);
	}
	
	public int getIntervalofArray(int i) {
		return runInterval.get(i);
	}

	public int getHumanInterval(int i) {
		// Algorithm to change up the interval if the max is lower than the minimal...
		try {
			// If first cell is less than second cell, have it setRandomInt as first cell,
			// second cell.
			if (humanArray.get(i - 1) < humanArray.get(i)) {
				setInterval = setRandomInt((humanArray.get(i - 1)), humanArray.get(i));
			}

			// If first array cell is more than second cell, have the second cell be the
			// smaller int in the setRandomInt method
			else if (humanArray.get(i - 1) > humanArray.get(i)) {
				setInterval = setRandomInt((humanArray.get(i) + 1), humanArray.get(i - 1));
			}
			// If the array of two numbers equal eachother, make the second int (max) +1
			else if (humanArray.get(i - 1) == humanArray.get(i)) {
				humanArray.set(i - 1, (i - 1) - 100);
				setInterval = setRandomInt(humanArray.get(i - 1), (humanArray.get(i)));

			}

			else {
				if (debug)
					print(debugStr + "trying HumanInterval: \n	" + humanArray.get(i - 1) + ",  " + humanArray.get(i) + " failed");
				throw new IllegalArgumentException("something wrong with getting an interval in GUI.getHumanInterval");
			}
		} catch (Exception e) {}
		if (debug)
			print(debugStr + "trying HumanInterval: \n	 " + i + " " + humanArray.get(i - 1) + ",  " + humanArray.get(i) + " SUCCESS");
		return setInterval;

	}



	public int setInterval() {

		if (rdbtnSimpleInterval.isSelected())
			return setInterval = Math.round((int) simpleIntervalSpinner.getValue());

		if (rdbtnHardInterval.isSelected())
			return setInterval = setRandomInt(intervalOne, intervalTwo);

		if (rdbtnHumanInterval.isSelected())
			return setInterval = getHumanInterval(setRandomInt(1, humanArray.size() - 1)); 
		// - 1 because it will sometimes try to get -1 in array, and it will go out of range.. causing error

		return setInterval;
	}

	public int setRandomInt(int min, int max) {
		if (min > max) {
			print("max must be greater than min");
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	
	
	// Table methods
	public String getRepeat() {
		return String.valueOf(repeatClicks);
	}

	public String getTypeAction() {
		return actionTypes[actionTypeChoice.getSelectedIndex()];
	}

	public void print(String s) {
		System.out.println(s);
	}
	
		// Adds a new row to the table
	public void addRow(String action, String xy, String color, String repeats, String interval) { 
		model.addRow(new Object[] { action, xy, color, repeats, interval });
	}
	
	public void addRGBstr(int x, int y) { //add RGB string value to table
		try {
			Robot rob = new Robot();
			Color col = Action.getColour(x, y);
			int r, g, b;

			col = rob.getPixelColor(x, y);
			r = col.getRed();
			g = col.getGreen();
			b = col.getBlue();


			String s = ("r " + r + " g " + g + " b " + b);
			runRGBstr.add(s);
		} catch (Exception e) {
		}
	}
	
	public Color addRGBarr(int x, int y) {
		Color col = Action.getColour(x, y);
		try {
			Robot r = new Robot();
			runRGB.add(col);

		} catch (Exception e) {}
		return col;
	}
	
	public Color getRGBarr(int x, int y) {
		try {
			Robot r = new Robot();
		} catch (Exception e) {}	
		
		Color col = Action.getColour(x, y);
		return col;
	}

}

