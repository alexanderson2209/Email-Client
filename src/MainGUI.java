/*
 * Author: Alexander Anderson
 * Spring 2014
 */
package a09;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/*
 * Main GUI window created using Swing.
 */
public class MainGUI extends JFrame {
	private static final long serialVersionUID = -8775887591444721347L;

	private CardLayout cardLayout = new CardLayout();
	private JPanel downloadPanel, mapPanel, missionPanel, objectivePanel, contentPane, panel1, usernamePassword;
	private JTextField usernameTextField, codeTextField;
	private JPasswordField passwordTextField;
	private JDialog userPass;
	private static MainGUI frame;
	private InboxPanel panel2;

	private Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	private ObjectivesEnum chosenEnum;
	private String code;
	private String filePath = "src/a09TEAM/resources/downloadedmission.txt";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new MainGUI();
					frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creates the main panel and links to the other panel classes (NewMessagePanel and InboxPanel)
	 */
	public MainGUI() {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.
					getAudioInputStream(getClass().getResource("../resources/sounds/bond.wav"));

			clip.open(inputStream);
			clip.start();
		} catch (LineUnavailableException | UnsupportedAudioFileException
				| IOException e1) {
			e1.printStackTrace();
		} 

		int windowWidth = 675;
		int windowHeight = 475;
		userPass = new JDialog();
		userPass.setLayout(new BorderLayout());
		userPass.setVisible(true);
		userPass.setBounds(center.x - windowWidth / 2, center.y - windowHeight / 2, windowWidth, windowHeight);
		usernamePassword = usernamePasswordPanel();
		userPass.add(usernamePassword, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
				try {
					Clip goodLuckClip = AudioSystem.getClip();
					AudioInputStream goodLuckAudioStream = AudioSystem.
							getAudioInputStream(getClass().getResource("../resources/sounds/good luck.wav"));
					goodLuckClip.open(goodLuckAudioStream);
					goodLuckClip.start();
					Thread.sleep(1500);

					Clip bondOutClip = AudioSystem.getClip();
					AudioInputStream bondOutInputStream = AudioSystem.
							getAudioInputStream(getClass().getResource("../resources/sounds/bondOut.wav"));
					bondOutClip.open(bondOutInputStream);
					bondOutClip.start();
					Thread.sleep(10000);
				} catch (LineUnavailableException
						| UnsupportedAudioFileException | IOException
						| InterruptedException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});

		setBounds(center.x - 800 / 2, center.y - 450 / 2, 800, 450);		

		JMenuBar menuBar = menuBarCreator();
		setJMenuBar(menuBar);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(cardLayout);

		threadedInbox();

	}
	/**
	 * This method creates a new InboxPanel on a different thread. The inbox panel has a method that
	 * lenghthens the overall start time of the application and is put on another thread for that reason.
	 */
	private synchronized void threadedInbox() {
		new Thread() {
			public void run() {
				panel2 = new InboxPanel();
				contentPane.add(panel2, "2");

			}
		}.start();
	}
	
	private JMenuBar menuBarCreator() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenuBar = new JMenu("File");
		menuBar.add(fileMenuBar);

		JMenuItem exitMenuBarButton = new JMenuItem("Exit");
		exitMenuBarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});
		fileMenuBar.add(exitMenuBarButton);

		JMenu viewMenuBar = new JMenu("View");
		menuBar.add(viewMenuBar);


		JMenuItem newMessageMenuButton = new JMenuItem("New message");
		newMessageMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int windowWidthOfMessage = 400;
				int windowHeightOfMessage = 375;

				JDialog newMessageContainer = new JDialog();
				newMessageContainer.setBounds(center.x - windowWidthOfMessage / 2, center.y - windowHeightOfMessage / 2,
						windowWidthOfMessage, windowHeightOfMessage);
				NewMessagePanel newMessage = new NewMessagePanel("Recipient");
				newMessageContainer.setVisible(true);
				newMessageContainer.add(newMessage);
			}
		});
		viewMenuBar.add(newMessageMenuButton);

		JMenuItem inboxMenuButton = new JMenuItem("Inbox");
		inboxMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(contentPane, "2");
			}
		});
		viewMenuBar.add(inboxMenuButton);

		JMenuItem missionControlMenuButton = new JMenuItem("Mission Control");
		missionControlMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(contentPane, "1");
			}
		});
		viewMenuBar.add(missionControlMenuButton);
		return menuBar;
	}

	private JPanel createMissionPanel() {
		JPanel missionPanel = new JPanel();
		missionPanel.setLayout(new BorderLayout(0, 0));

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout(0, 0));
		titlePanel.setOpaque(false);
		JLabel lblOperationTitle = new JLabel();
		lblOperationTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblOperationTitle.setFont(new Font("Courier New", Font.PLAIN, 24));
		titlePanel.add(lblOperationTitle, BorderLayout.CENTER);
		lblOperationTitle.setText("Operation: " + code);
		lblOperationTitle.setOpaque(false);

		JLabel lblTitleOfMission = new JLabel();
		lblTitleOfMission.setFont(new Font("Courier New", Font.PLAIN, 1));
		lblTitleOfMission.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(lblTitleOfMission, BorderLayout.SOUTH);
		lblTitleOfMission.setText(chosenEnum.getTitle());
		lblTitleOfMission.setOpaque(false);
		missionPanel.setOpaque(false);
		missionPanel.add(titlePanel, BorderLayout.NORTH);
		return missionPanel;
	}

	private JPanel createObjectivePanel() {
		objectivePanel = new JPanel();
		objectivePanel.setBorder(new EmptyBorder(20, 0, 0, 0));
		objectivePanel.setLayout(new BorderLayout(0, 0));

		JLabel lblMainAndSide = new JLabel();
		lblMainAndSide.setFont(new Font("Courier New", Font.BOLD, 16));
		lblMainAndSide.setText(ObjectivesEnum.sideObjective(chosenEnum));
		lblMainAndSide.setVerticalAlignment(SwingConstants.TOP);
		lblMainAndSide.setHorizontalAlignment(SwingConstants.CENTER);
		objectivePanel.add(lblMainAndSide, BorderLayout.CENTER);

		return objectivePanel;
	}

	private JPanel createMapPanel() {
		mapPanel = new JPanel();
		mapPanel.setBackground(Color.LIGHT_GRAY);
		mapPanel.setLayout(new BorderLayout(0, 0));

		JLabel lblMap = new JLabel();
		lblMap.setIcon(chosenEnum.getImg());
		lblMap.setFont(new Font("Courier New", Font.PLAIN, 11));
		JLabel lblMapCoordinates = new JLabel();
		lblMapCoordinates.setFont(new Font("Courier New", Font.PLAIN, 20));
		lblMapCoordinates.setHorizontalAlignment(SwingConstants.CENTER);
		lblMapCoordinates.setText(ObjectivesEnum.enumInformationForGui(chosenEnum));	
		lblMap.setHorizontalAlignment(SwingConstants.CENTER);
		mapPanel.add(lblMapCoordinates, BorderLayout.SOUTH);

		mapPanel.add(lblMap);
		return mapPanel;
	}

	private JPanel createDownloadPanel() {
		downloadPanel = new JPanel();

		JButton btnDownloadMission = new JButton("Download Mission");
		btnDownloadMission.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try (PrintWriter writer = new PrintWriter(filePath)) {
					writer.print(chosenEnum.toString());
					JOptionPane.showMessageDialog(null, "Download Successful!");	
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		downloadPanel.add(btnDownloadMission);
		return downloadPanel;
	}

	public JPanel usernamePasswordPanel() {
		class UsernamePanel extends JPanel {

			private static final long serialVersionUID = 5089481693194835806L;
			BufferedImage image;
			public UsernamePanel() {
				setOpaque(false);
				setLayout(null);

				JLabel usernameLabel = new JLabel("UserName:");
				usernameLabel.setForeground(Color.WHITE);
				usernameLabel.setFont(new Font("Courier New", Font.PLAIN, 15));
				add(usernameLabel);

				usernameTextField = new JTextField();
				usernameTextField.setFont(new Font("Courier New", Font.PLAIN, 15));
				add(usernameTextField);

				JLabel passwordLabel = new JLabel("Password:");
				passwordLabel.setForeground(Color.WHITE);
				passwordLabel.setFont(new Font("Courier New", Font.PLAIN, 15));
				add(passwordLabel);

				passwordTextField = new JPasswordField();
				passwordTextField.setFont(new Font("Courier New", Font.PLAIN, 15));
				add(passwordTextField);

				JLabel lblCodeword = new JLabel("Codeword:");
				lblCodeword.setForeground(Color.WHITE);
				lblCodeword.setFont(new Font("Courier New", Font.PLAIN, 15));
				add(lblCodeword);

				codeTextField = new JTextField();
				codeTextField.setFont(new Font("Courier New", Font.PLAIN, 15));
				add(codeTextField);
				ActionOfLoginButton loginAction = new ActionOfLoginButton();
				codeTextField.addActionListener(loginAction);

				JButton loginButton = new JButton("Login");
				loginButton.addActionListener(loginAction);
				loginButton.setFont(new Font("Courier New", Font.PLAIN, 15));
				add(loginButton);

				JButton btnCancel = new JButton("Cancel");
				btnCancel.setFont(new Font("Courier New", Font.PLAIN, 15));
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(ABORT);
					}
				});
				usernameLabel.setBounds(175, 280, 81, 16);
				codeTextField.setBounds(257, 354, 241, 28);			
				passwordLabel.setBounds(175, 320, 88, 16);
				usernameTextField.setBounds(257, 274, 241, 28);
				lblCodeword.setBounds(175, 360, 87, 16);
				passwordTextField.setBounds(257, 314, 241, 28);
				loginButton.setBounds(170, 404, 228, 29);
				btnCancel.setBounds(410, 404, 88, 29);
				add(btnCancel);
			}
			@Override
			protected void paintComponent(Graphics g) {
				try {
					image = ImageIO.read(getClass().getResource("../resources/images/cia.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				g.drawImage(image, 0, 0, null);
				super.paintComponent(g);
			}
		}

		UsernamePanel j = new UsernamePanel();
		return j;

	}
	
	/**
	 *This class handles the logic of the login button and creates the main panel based on the variable "String code"
	 *specified at the login screen.(codeword)
	 */
	public class ActionOfLoginButton implements ActionListener {
		int tryCount = 0;

		class Panel1 extends JPanel {
			/**
			 * This Inner class creates the Mission panel.
			 * The reason this is an inner class is because it needs to override the paintComponent method
			 * to paint the background.
			 */
			private static final long serialVersionUID = -6412578352809943590L;
			private BufferedImage image;
			public Panel1() {
				setLayout(new BorderLayout(0, 0));

				missionPanel = createMissionPanel();
				missionPanel.setOpaque(false);
				add(missionPanel, BorderLayout.CENTER);

				objectivePanel = createObjectivePanel();
				objectivePanel.setOpaque(false);
				missionPanel.add(objectivePanel, BorderLayout.CENTER);

				mapPanel = createMapPanel();
				mapPanel.setOpaque(false);
				add(mapPanel, BorderLayout.EAST);

				downloadPanel = createDownloadPanel();
				downloadPanel.setOpaque(false);
				add(downloadPanel, BorderLayout.SOUTH);
			}

			@Override
			protected void paintComponent(Graphics g) {
				try {
					image = ImageIO.read(getClass().getResource("../resources/images/parchment.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);

			}
		}
		/**
		 * Checks whether the codeword matches one of the specified codewords.
		 * @param codeword
		 * @return
		 */
		public boolean codewordValidationCheck(String codeword) {
			if (codeword.equalsIgnoreCase("GOLDENEYE") ||
					codeword.equalsIgnoreCase("GHOST") || 
					codeword.equalsIgnoreCase("JUGGERNAUT") || 
					codeword.equalsIgnoreCase("TIGER") || 
					codeword.equalsIgnoreCase("RABBITHOLE"))
				return true;
			else
				return false;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Scanner input = null;
			InputStream is = getClass().getResourceAsStream("resources/UPass.csv");
			input = new Scanner(is);
			//				Usernames: 			Passwords:
			//				agent9352 			MyPassword
			//				agentzero			zero
			//				agent007			007
			//				austin				powers
			ArrayList<String> userNames = new ArrayList<>();
			ArrayList<String> passwords = new ArrayList<>();

			while (input.hasNext()) {
				String tempStr = input.nextLine();
				String[] strings = tempStr.split(",");
				userNames.add(strings[0]);
				passwords.add(strings[1]);
			}
			input.close();

			boolean loginAuthentication = false;
			for(int i = 0; i < userNames.size(); i++){
				if (Encryption.encrypt(usernameTextField.getText()).equals(userNames.get(i))
						&& Encryption.encrypt(new String(passwordTextField.getPassword())).equals(passwords.get(i))
						&& codewordValidationCheck(codeTextField.getText())) {
					loginAuthentication = true;
					break;
				}
			}

			if (loginAuthentication) {

				Clip clip = null;
				try {
					clip = AudioSystem.getClip();
					AudioInputStream inputStream;
					inputStream = AudioSystem.getAudioInputStream(getClass().getResource("../resources/sounds/goodafternoon.wav"));
					clip.open(inputStream);
				} catch (LineUnavailableException
						| UnsupportedAudioFileException | IOException e1) {
					e1.printStackTrace();
				}
				clip.start();

				userPass.setVisible(false);
				frame.setVisible(true);

				code = codeTextField.getText().toUpperCase();
				switch(code){
				case "GOLDENEYE":
					chosenEnum = ObjectivesEnum.GOLDENEYE;
					break;
				case "GHOST":
					chosenEnum = ObjectivesEnum.GHOST;
					break;
				case "JUGGERNAUT":
					chosenEnum = ObjectivesEnum.JUGGERNAUT;
					break;
				case "TIGER":
					chosenEnum = ObjectivesEnum.TIGER;
					break;
				case "RABBITHOLE":
					chosenEnum = ObjectivesEnum.RABBITHOLE;
					break;
				default:
					break;
				}

				panel1 = new Panel1();
				contentPane.add(panel1, "1");
				cardLayout.show(contentPane, "1");
			}
			else {
				tryCount++;
				codeTextField.setText("");
				usernameTextField.setText("");
				passwordTextField.setText("");
				if (tryCount == 3) {
					JOptionPane.showMessageDialog(frame, "Access denied");
					System.exit(ABORT);
				}
				JOptionPane.showMessageDialog(frame, 
						String.format("Incorrect Username/Password/codeword combination%nTries left: %d", (3 - tryCount)));
			}
		}
	}
}



