/*
 * Author: Alexander Anderson
 * Spring 2014
 */
package a09;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/*
 * New Message panel for the email client using Swing.
 */
public class NewMessagePanel extends JPanel {
	private static final long serialVersionUID = -338134169983611403L;
	private JTextField recipientText;
	private JTextField subjectText;
	private JLabel newMessageLabel;
	private JTextArea contentText;
	private JButton sendButton;
	private JButton cancelButton;

	/**
	 * Creates the panel that has the new message functionality and the reply functionality.
	 */
	public NewMessagePanel(String recipient) {
		setOpaque(false);
		setLayout(null);

		JPanel messagePanel = new JPanel();
		messagePanel.setOpaque(false);
		messagePanel.setBounds(0, 0, 400, 321);
		add(messagePanel);
		messagePanel.setLayout(null);

		createNewMessageLabel();
		messagePanel.add(newMessageLabel);

		createRecipientText(recipient);
		messagePanel.add(recipientText);

		createSubjectText();
		messagePanel.add(subjectText);

		createContentText();
		messagePanel.add(contentText);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setBounds(0, 321, 400, 29);
		add(buttonPanel);

		createSendButton(buttonPanel);
		buttonPanel.add(sendButton);

		createCancelButton();
		buttonPanel.add(cancelButton);
	}
	
	private void createCancelButton() {
		cancelButton = new JButton("cancel");
		cancelButton.setFont(new Font("Courier New", Font.PLAIN, 13));
		cancelButton.setBounds(150, 0, 84, 29);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.getWindowAncestor(getParent()).dispose();
			}
		});
	}
	
	private void createSendButton(JPanel buttonPanel) {
		sendButton = new JButton("send");
		sendButton.setFont(new Font("Courier New", Font.PLAIN, 13));
		sendButton.setBounds(0, 0, 150, 29);
		sendButton.addActionListener(new SendButtonAction());
		buttonPanel.setLayout(null);
		sendButton.setMaximumSize(new Dimension(150, 29));
	}
	
	private void createNewMessageLabel() {
		newMessageLabel = new JLabel("New message");
		newMessageLabel.setForeground(Color.WHITE);
		newMessageLabel.setBounds(6, 6, 117, 20);
		newMessageLabel.setFont(new Font("Courier New", Font.PLAIN, 16));
		newMessageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		newMessageLabel.setHorizontalAlignment(SwingConstants.LEFT);
	}
	
	private void createContentText() {
		contentText = new JTextArea();
		contentText.setLineWrap(true);
		contentText.setWrapStyleWord(true);
		contentText.setForeground(Color.BLACK);
		contentText.setFont(new Font("Courier New", Font.PLAIN, 16));
		contentText.setBounds(6, 94, 388, 227);
		contentText.setText("Content");
		contentText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (contentText.getText().equals("Content"))
					contentText.setText("");
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (contentText.getText().equals(""))
					contentText.setText("Content");
			}
		});
	}
	
	private void createSubjectText() {
		subjectText = new JTextField();
		subjectText.setFont(new Font("Courier New", Font.PLAIN, 13));
		subjectText.setBounds(0, 61, 400, 28);
		subjectText.setText("Subject");
		subjectText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (subjectText.getText().equals("Subject"))
					subjectText.setText("");
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (subjectText.getText().equals(""))
					subjectText.setText("Subject");
			}
		});
	}
	
	private void createRecipientText(String recipient) {
		recipientText = new JTextField();
		recipientText.setFont(new Font("Courier New", Font.PLAIN, 13));
		recipientText.setBounds(0, 28, 400, 28);
		recipientText.setText(recipient);
		recipientText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (recipientText.getText().equals("Recipient"))
					recipientText.setText("");
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (recipientText.getText().equals(""))
					recipientText.setText("Recipient");
			}
		});
	}

	public class SendButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Mail.sendEmail(recipientText.getText(), subjectText.getText(), contentText.getText());
			SwingUtilities.getWindowAncestor(getParent()).dispose();
		}

	}
	
	@Override
	protected void paintComponent(Graphics g) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResource("images/newMessage.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(image, 0, 0, this);
		super.paintComponent(g);
	}
}
