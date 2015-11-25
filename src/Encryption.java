/*
 * Author: Alexander Anderson
 * Spring 2014
 */
package a09;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class Encryption {
	final static String algorithmToUse = "AES";
	static byte[] keyValue = "TheBestSecretKey".getBytes();

	/**
	 * @param data
	 * @return
	 * @throws Exception
	 * 
	 * Encrypts string data.
	 * returns encrypted data as a string.
	 */
	public static String encrypt(String data) {
		try {
			//		creates a new key based on the key I chose and the Algorithm I chose
			//		in this case, I chose AES with a 128bit encryption
			Key key = new SecretKeySpec(keyValue, "AES");

			//		the Cipher is used to do the actual encryption
			Cipher cipher = Cipher.getInstance(algorithmToUse);


			//		I initialize this cipher with a key and a mode. If I wanted to 
			//		decrypt, I would use Cipher.DECRYPT_MODE instead.
			cipher.init(Cipher.ENCRYPT_MODE, key);

			//		I encrypt the data with the doFinal method of cipher.
			//		I have to encode the data using its byte representation.

			byte[] encVal = cipher.doFinal(data.getBytes());

			//		the strange method call BASE64Encoder translates the raw binary data into
			//		a seemingly random string of characters, not random to the decoder though :)
			String encryptedData = new BASE64Encoder().encode(encVal);
			return encryptedData;
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
			return "Encryption Error";
		}
	}

	/**
	 * @param encryptedData
	 * @return
	 * @throws Exception
	 * 
	 * Decrypts string encryptedData.
	 * returns String of the decrypted data.
	 */
	public static String decrypt(String encryptedData)  {
		// the decrypt method is just reversing the encrypt method
		Key key = new SecretKeySpec(keyValue, "AES");
		try {
			Cipher cipher = Cipher.getInstance(algorithmToUse);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decodedValue = new BASE64Decoder().decodeBuffer(encryptedData);
			byte[] decValue = cipher.doFinal(decodedValue);
			String decryptedData = new String(decValue);
			return decryptedData;
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			return encryptedData;
		}
	}
}
