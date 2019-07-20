import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.TextArea;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;

import java.awt.Font;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.JEditorPane;
import javax.swing.DropMode;
import java.awt.SystemColor;

public class HumanClickArray extends JFrame implements ActionListener {

	JPanel 			contentPane;
	
	JButton 		humanArraybtn = new JButton("Click here");
	
	JTextArea 		HCAconsole = new JTextArea();
	
	JScrollPane 	jsp;
	
	long 			started = 0, elapsedTime = 0, start = 0;
	long			before, differenceA;
	
	int 			count = 0, humanInts;
	
	boolean 		greenConsole = false;
	
	String 			fileName = "humanInput.txt";
	String 			str ="         Interval Console        \n"
					+   "_________________________________\n";
	String 			line;
	
    File 			file = new File(fileName);

	/**
	 * Launch the application.
	 */
	public static void HumanClickArr() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					HumanClickArray frame = new HumanClickArray();
					frame.setTitle("Human Interval Setup");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HumanClickArray() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setBounds(100, 100, 526, 460);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		humanArraybtn.addActionListener(this);
		humanArraybtn.setBounds(10, 11, 207, 239);
		contentPane.add(humanArraybtn);

		HCAconsole.setText("         Interval Console        \r\n_________________________________");

//		jsp = new JScrollPane(HCAconsole, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		if (greenConsole) {
			HCAconsole.setFont(new Font("Lucida Console", Font.PLAIN, 13));
			HCAconsole.setForeground(Color.GREEN);
			HCAconsole.setBackground(Color.BLACK);
		}
		HCAconsole.setBounds(227, 11, 273, 400);
		contentPane.add(HCAconsole);
		
		JEditorPane dtrpnEachClickWill = new JEditorPane();
		dtrpnEachClickWill.setEditable(false);
		dtrpnEachClickWill.setBackground(SystemColor.menu);
		dtrpnEachClickWill.setText("Each click will time the milliseconds\r\nbetween each click. \r\n\r\nThe data will be used to in the intervals to make the bot seem more human.");
		dtrpnEachClickWill.setBounds(10, 261, 207, 90);
		contentPane.add(dtrpnEachClickWill);
		
		JButton btnSaveHumanArr = new JButton("Save");
		
		btnSaveHumanArr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
				FileWriter writer = new FileWriter(fileName); 
				Writer output = new BufferedWriter(writer);

	            file.setWritable(true);
	            file.setReadable(true);
		        System.out.println(file.getAbsolutePath());

				for(Integer i: Main.humanArray) {
					output.write(String.valueOf(i) + "\r\n");
//					  writer.write(String.valueOf(i + " "));
					}

				output.close();

			}catch(Exception e) {System.out.println("I couldnt save array");}}
		});
		
		btnSaveHumanArr.setBounds(10, 387, 89, 23);
		contentPane.add(btnSaveHumanArr);
		
		JButton btnLoadHumanArr = new JButton("Load");
		btnLoadHumanArr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadHumanArray();
		}});
		
		btnLoadHumanArr.setBounds(109, 387, 89, 23);
		contentPane.add(btnLoadHumanArr);
		
//		contentPane.add(jsp);
	}
	
	public void loadHumanArray() {
		try {
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			if (!input.ready()) {
				throw new IOException();
			}
			while ((line = input.readLine()) != null) {
				Main.humanArray.add(Integer.parseInt(line));
			}
			System.out.println("Loaded human intervals successfully from text file: \n" + file.getAbsolutePath() + "\n");

			input.close();

		} catch (Exception e) {
			System.out.println("I couldnt load array");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) { //algorithm
		Random rand = new Random();
		if (e.getSource() == humanArraybtn) {

			count++;

			long EndA = System.nanoTime();
			if (count > 1)
				differenceA = (EndA - start);
			else
				//make first integer a random number
				differenceA = (rand.nextInt(150) + rand.nextInt(100)* 1000000);
			
			
			int intervalMs = (int) (differenceA / 1000000); // nanoseconds to milliseconds, for the array
			Main.humanArray.add(intervalMs); 
			str += (intervalMs + " ms\n");
			consoleAdd();

			start = System.nanoTime();
			System.out.println(Main.humanArray.get(count - 1));
		}

		if (e.getSource() == HCAconsole)
			//Auto scroll down
			HCAconsole.setCaretPosition(HCAconsole.getDocument().getLength());
		
	}

	public void consoleAdd() {
		HCAconsole.setText(String.format("%s", str));
	}
}
