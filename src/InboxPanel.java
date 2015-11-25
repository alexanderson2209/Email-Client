/*
 * Author: Alexander Anderson
 * Spring 2014
 */
package a09;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;

/*
 * Panel for the inbox. Created with Swing.
 */
public class InboxPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel fromLabel, dateLabel,subjectLabel;
	private Mail mail = new Mail();
	private JTextPane contentText;
	private JPanel tempInboxPanel, messagePanel, inboxMessages, inboxGUI;
	private Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
	private BufferedImage image;
	private JButton deleteButton;

	/**
	 * Creates the inbox panel. 
	 * It is the panel that holds the inbox and the individual message view.
	 */
	public InboxPanel() {
		setFont(new Font("Courier New", Font.PLAIN, 13));
		setOpaque(false);
		setLayout(null);

		inboxGUI = createInboxPanel();
		add(inboxGUI);

		createMessagePanel();
		add(messagePanel);

		createFromLabel();
		messagePanel.add(fromLabel);

		createDateLabel();
		messagePanel.add(dateLabel);

		createSubjectLabel();
		messagePanel.add(subjectLabel);

		createContentText();
		messagePanel.add(contentText);

		JButton replyButton = createReplyButton();
		messagePanel.add(replyButton);
	}
	
	private JButton createReplyButton() {
		JButton replyButton = new JButton("Reply");
		replyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int windowWidthOfMessage = 400;
				int windowHeightOfMessage = 375;

				JDialog newMessageContainer = new JDialog();
				newMessageContainer.setBounds(center.x - windowWidthOfMessage / 2, center.y - windowHeightOfMessage / 2,
					windowWidthOfMessage, windowHeightOfMessage);
				NewMessagePanel newMessage = new NewMessagePanel(fromLabel.getText().substring(6));
				newMessageContainer.setVisible(true);
				newMessageContainer.getContentPane().add(newMessage);
			}
		});
		replyButton.setBounds(0, 370, 117, 29);
		return replyButton;
	}
	
	private void createContentText() {
		contentText = new JTextPane();
		contentText.setOpaque(false);
		contentText.setForeground(Color.WHITE);
		contentText.setBounds(0, 78, 605, 291);
		contentText.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentText.setEditable(false);
		contentText.setFont(new Font("Courier New", Font.PLAIN, 18));
	}
	
	private void createSubjectLabel() {
		subjectLabel = new JLabel("Subject: ");
		subjectLabel.setBounds(0, 55, 599, 19);
		subjectLabel.setForeground(Color.WHITE);
		subjectLabel.setOpaque(false);
		subjectLabel.setBorder(null);
		subjectLabel.setVerticalAlignment(SwingConstants.TOP);
		subjectLabel.setHorizontalAlignment(SwingConstants.LEFT);
		subjectLabel.setFont(new Font("Courier New", Font.PLAIN, 18));
	}
	
	private void createDateLabel() {
		dateLabel = new JLabel("Date: ");
		dateLabel.setBounds(0, 29, 599, 19);
		dateLabel.setForeground(Color.WHITE);
		dateLabel.setOpaque(false);
		dateLabel.setBorder(null);
		dateLabel.setVerticalAlignment(SwingConstants.TOP);
		dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
		dateLabel.setFont(new Font("Courier New", Font.PLAIN, 18));
	}
	
	private void createFromLabel() {
		fromLabel = new JLabel("From: ");
		fromLabel.setBounds(0, 3, 599, 19);
		fromLabel.setForeground(Color.WHITE);
		fromLabel.setBorder(null);
		fromLabel.setOpaque(false);
		fromLabel.setVerticalAlignment(SwingConstants.TOP);
		fromLabel.setHorizontalAlignment(SwingConstants.LEFT);
		fromLabel.setFont(new Font("Courier New", Font.PLAIN, 18));
	}
	
	private void createMessagePanel() {
		messagePanel = new JPanel();
		messagePanel.setBounds(0, 0, 605, 400);
		messagePanel.setOpaque(false);
		messagePanel.setFont(new Font("Courier New", Font.PLAIN, 13));
		messagePanel.setBorder(null);
		messagePanel.setLayout(null);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		try {
			image = ImageIO.read(getClass().getResource("../resources/images/inbox1.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(image, 0, 0, this);
		super.paintComponent(g);
	}

	private JPanel createInboxPanel() {
		tempInboxPanel = new JPanel();
		tempInboxPanel.setBounds(607, 0, 193, 400);
		tempInboxPanel.setOpaque(false);
		tempInboxPanel.setFont(new Font("Courier New", Font.PLAIN, 13));
		tempInboxPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		tempInboxPanel.setLayout(null);

		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mail.deleteAllEmails();
				removeAll();
				inboxGUI = createInboxPanel();
				add(inboxGUI);
				add(messagePanel);
				revalidate();
				repaint();
			}
		});
		deleteButton.setFont(new Font("Courier New", Font.PLAIN, 13));
		deleteButton.setMinimumSize(new Dimension(75, 29));
		deleteButton.setMaximumSize(new Dimension(75, 29));
		deleteButton.setBounds(112, 368, 75, 29);
		tempInboxPanel.add(deleteButton);

		JLabel inboxLabel = new JLabel("Inbox");
		inboxLabel.setBounds(2, 2, 185, 37);
		inboxLabel.setForeground(Color.WHITE);
		inboxLabel.setOpaque(false);
		inboxLabel.setFont(new Font("Courier New", Font.PLAIN, 22));
		inboxLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tempInboxPanel.add(inboxLabel);

		JButton refreshButton = new JButton();
		refreshButton.setBounds(2, 368, 108, 29);
		refreshButton.setOpaque(false);
		refreshButton.setFont(new Font("Courier New", Font.PLAIN, 13));
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeAll();
				inboxGUI = createInboxPanel();
				add(inboxGUI);
				add(messagePanel);
				revalidate();
				repaint();
			}
		});
		refreshButton.setText("Refresh");
		tempInboxPanel.add(refreshButton);
		inboxMessages = new JPanel();
		inboxMessages.setOpaque(false);
		inboxMessages.setLayout(new BoxLayout(inboxMessages, BoxLayout.PAGE_AXIS));

		mail.getEmails();

		for (int i = 0; i < mail.getSubjectComponentOfMessage().size(); i++) {
			JButton tempButton = new JButton();
			tempButton.setFont(new Font("Courier New", Font.PLAIN, 16));
			if (i != (mail.getSubjectComponentOfMessage().size() - 1))
				tempButton.setBorder(new MatteBorder(2, 0, 2, 0, (Color) new Color(0, 0, 0)));
			else
				tempButton.setBorder(new MatteBorder(2, 0, 4, 0, (Color) new Color(0, 0, 0)));
			tempButton.setOpaque(false);
			tempButton.setMinimumSize(new Dimension(185, 30));
			tempButton.setMaximumSize(new Dimension(185, 30));
			tempButton.addActionListener(new Action());
			tempButton.setText(mail.getSubjectComponentOfMessage().get(i));
			inboxMessages.add(tempButton);
		}
		JScrollPane inboxMessagesScrollPane = new JScrollPane(inboxMessages);
		inboxMessagesScrollPane.setBounds(2, 40, 185, 328);
		inboxMessagesScrollPane.setOpaque(false);
		tempInboxPanel.add(inboxMessagesScrollPane);

		return tempInboxPanel;
	}

	public class Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < mail.getSubjectComponentOfMessage().size(); i++) {
				if (mail.getSubjectComponentOfMessage().get(i).equals(((JButton) e.getSource()).getText())) {
					fromLabel.setText("From: " + mail.getFromComponentOfMessage().get(i));
					dateLabel.setText("Date: " + mail.getDateComponentOfMessage().get(i));
					subjectLabel.setText("Subject: " + Encryption.decrypt(mail.getSubjectComponentOfMessage().get(i)));
					contentText.setText("Content: " + Encryption.decrypt(mail.getContentComponentOfMessage().get(i)));
				}

			}
		}
	}
}

