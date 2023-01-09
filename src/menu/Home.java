package menu;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import data.FileManager;

public class Home {

	private JFrame frmChecksumMaker;
	private JTextField textFolder;
	private File directory;
	private JComboBox<String> hashSelect;
	private JButton btnStart;
	private JLabel lblProgressCurrent;
	private JProgressBar progressBar;
	private JButton btnFolder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home window = new Home();
					window.frmChecksumMaker.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChecksumMaker = new JFrame();
		frmChecksumMaker.setTitle("CheckSum Maker");
		frmChecksumMaker.setBounds(100, 100, 450, 200);
		frmChecksumMaker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmChecksumMaker.getContentPane().setLayout(springLayout);

		// TODO: Make second window for checking

		textFolder = new JTextField();
		textFolder.setFont(new Font("Tahoma", Font.PLAIN, 14));
		springLayout.putConstraint(SpringLayout.NORTH, textFolder, 25, SpringLayout.NORTH,
				frmChecksumMaker.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textFolder, 34, SpringLayout.WEST,
				frmChecksumMaker.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textFolder, 284, SpringLayout.WEST,
				frmChecksumMaker.getContentPane());
		frmChecksumMaker.getContentPane().add(textFolder);
		textFolder.setColumns(10);

		btnFolder = new JButton("Browse");
		btnFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fc.showOpenDialog(frmChecksumMaker) == JFileChooser.APPROVE_OPTION)
					directory = fc.getSelectedFile();

				textFolder.setText(directory.getPath());

			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnFolder, 0, SpringLayout.NORTH, textFolder);
		springLayout.putConstraint(SpringLayout.EAST, btnFolder, -25, SpringLayout.EAST,
				frmChecksumMaker.getContentPane());
		frmChecksumMaker.getContentPane().add(btnFolder);

		progressBar = new JProgressBar();
		progressBar.setMaximum(10000);
		springLayout.putConstraint(SpringLayout.WEST, progressBar, 50, SpringLayout.WEST,
				frmChecksumMaker.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, progressBar, -35, SpringLayout.SOUTH,
				frmChecksumMaker.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, progressBar, -50, SpringLayout.EAST,
				frmChecksumMaker.getContentPane());
		progressBar.setForeground(new Color(0, 0, 255));
		progressBar.setStringPainted(true);
		progressBar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		frmChecksumMaker.getContentPane().add(progressBar);

		lblProgressCurrent = new JLabel("");
		springLayout.putConstraint(SpringLayout.NORTH, lblProgressCurrent, 25, SpringLayout.SOUTH, progressBar);
		lblProgressCurrent.setHorizontalAlignment(SwingConstants.CENTER);
		lblProgressCurrent.setFont(new Font("Tahoma", Font.PLAIN, 16));
		springLayout.putConstraint(SpringLayout.WEST, lblProgressCurrent, 0, SpringLayout.WEST, progressBar);
		springLayout.putConstraint(SpringLayout.EAST, lblProgressCurrent, 0, SpringLayout.EAST, progressBar);
		frmChecksumMaker.getContentPane().add(lblProgressCurrent);

		btnStart = new JButton("Start");
		btnStart.addActionListener((event) -> {
			btnStart.setEnabled(false);
			FileManager fileManager = new FileManager(directory, hashSelect.getSelectedIndex());
			Thread hashmapThread = new Thread(() -> {
				Map<File, String> m = fileManager.getFilemap();
				try {
					final String[] ext = { "md5", "sha1", "sha512" };
					FileWriter writer = new FileWriter(String.format("%s\\%s.%s", directory, directory.getName(),
							ext[hashSelect.getSelectedIndex()]));
					for (Entry<File, String> entry : m.entrySet()) {
						File k = entry.getKey();
						String v = entry.getValue();
						writer.write(String.format("%s *..%s%s\n", v, k.getName(),
								k.toString().split(directory.getName(), 2)[1]));
					}
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});

			Thread progressThread = new Thread(() -> {
				int current = 0;
				final int MAX = progressBar.getMaximum();
				while (current < MAX) {
					progressBar.setValue(current);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						System.out.println("Something really bad happened to get here");
						e1.printStackTrace();
					}
					current = Math.round(((float) fileManager.getFileComplete() / fileManager.getSize()) * MAX);
				}
				progressBar.setValue(MAX);
				btnStart.setEnabled(true);
			});

			hashmapThread.start();
			progressThread.start();
		});
		springLayout.putConstraint(SpringLayout.WEST, btnStart, 150, SpringLayout.WEST,
				frmChecksumMaker.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnStart, -20, SpringLayout.NORTH, progressBar);
		springLayout.putConstraint(SpringLayout.EAST, btnStart, -150, SpringLayout.EAST,
				frmChecksumMaker.getContentPane());
		frmChecksumMaker.getContentPane().add(btnStart);

		hashSelect = new JComboBox<>();
		springLayout.putConstraint(SpringLayout.WEST, hashSelect, 25, SpringLayout.EAST, btnStart);
		hashSelect.setModel(new DefaultComboBoxModel<String>(new String[] { "MD5", "SHA-1", "SHA-512" }));
		hashSelect.setFont(new Font("Tahoma", Font.PLAIN, 12));
		springLayout.putConstraint(SpringLayout.NORTH, hashSelect, 0, SpringLayout.NORTH, btnStart);
		frmChecksumMaker.getContentPane().add(hashSelect);
	}
}
