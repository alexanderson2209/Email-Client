/*
 * Author: Alexander Anderson
 * Spring 2014
 */
package a09;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
 * Mail Class.
 * Handles all functions of sending/receiving emails including encryption/decryption via Encryption class.
 * Only works with gmail email accounts.
 * Specify email and password in variables email and pw.
 */
public class Mail {
	private ArrayList<String> subjectComponentOfMessage = new ArrayList<>();
	private ArrayList<String> fromComponentOfMessage = new ArrayList<>();
	private ArrayList<String> recipientComponentOfMessage = new ArrayList<>();
	private ArrayList<String> dateComponentOfMessage = new ArrayList<>();
	private ArrayList<String> contentComponentOfMessage = new ArrayList<>();
	private static Properties props = new Properties();
	private Message[] msgs = null;
	private String email = "agent.9352@gmail.com";
	private String pw = "93529352";

	/*
	 * Grabs Emails from gmail inbox
	 */
	public void getEmails() {
		fromComponentOfMessage.clear();
		recipientComponentOfMessage.clear();
		dateComponentOfMessage.clear();
		subjectComponentOfMessage.clear();
		contentComponentOfMessage.clear();

		Session session = addPropsAndCreateSession(); 

		try {
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", email, pw);
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			msgs = inbox.getMessages();

			for (int i = 0; i < msgs.length; i++) {
				Address[] addressFrom = msgs[i].getFrom();
				Address[] addressTo = msgs[i].getAllRecipients();
				fromComponentOfMessage.add(addressFrom[0].toString());
				recipientComponentOfMessage.add(addressTo[0].toString());
				dateComponentOfMessage.add(msgs[i].getReceivedDate().toString());
				subjectComponentOfMessage.add(Encryption.decrypt(msgs[i].getSubject()));
				if (msgs[i].getContent() instanceof MimeMultipart) {
					contentComponentOfMessage.add(((MimeMultipart)msgs[i].getContent()).getBodyPart(0)
							.getContent().toString());
				}
				else 
					contentComponentOfMessage.add(Encryption.decrypt(msgs[i].getContent().toString()));
			}
		} catch (MessagingException
				| IOException e) {
			e.printStackTrace();
		}


	}

	private static Session addPropsAndCreateSession() {
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.store.protocol", "imaps");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, pw);
			}
		});
		return session;
	}

	/*
	 * Sends email to recipient.
	 * subject argument specifies what the email subject will be.
	 * content argument specifies what the email content will be.
	 */
	public static synchronized void sendEmail(final String recipient, final String subject, 
			final String content) {
		new Thread() {
			public void run() {
				try {
					Session session = addPropsAndCreateSession();
					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress(email));
					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(recipient));
					message.setSubject(Encryption.encrypt(subject));
					message.setText(Encryption.encrypt(content));
					
					Transport.send(message);
					Clip clip = AudioSystem.getClip();
				    
		    			AudioInputStream inputStream = AudioSystem.
					getAudioInputStream(getClass().getResource("../resources/sounds/messagesent.wav"));
		    
		    			clip.open(inputStream);
		    			clip.start(); 
					System.out.println("sent");
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
	}

	public ArrayList<String> getSubjectComponentOfMessage() {
		return subjectComponentOfMessage;
	}

	public ArrayList<String> getFromComponentOfMessage() {
		return fromComponentOfMessage;
	}

	public ArrayList<String> getRecipientComponentOfMessage() {
		return recipientComponentOfMessage;
	}

	public ArrayList<String> getDateComponentOfMessage() {
		return dateComponentOfMessage;
	}

	public ArrayList<String> getContentComponentOfMessage() {
		return contentComponentOfMessage;
	}

	/*
	 * Deletes all emails in the Inbox folder of the gmail account.
	 */
	public static void deleteAllEmails() {
		Session session = addPropsAndCreateSession(); 

		try {
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", email, pw);
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			Flags deleted = new Flags(Flags.Flag.DELETED);
			inbox.setFlags(inbox.getMessages(), deleted, true);
			inbox.expunge();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}


