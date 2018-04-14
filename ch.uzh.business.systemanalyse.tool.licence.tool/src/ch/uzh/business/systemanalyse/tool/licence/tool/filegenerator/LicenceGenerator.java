/**
 * 
 */
package ch.uzh.business.systemanalyse.tool.licence.tool.filegenerator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Krzysztof Dabkowski (kdabko@yahoo.com)
 * 
 */
public class LicenceGenerator {

	private static String KEY_PHRASE = "c01f6ae2-aa21-47aa-a6b7-6c7469fc3112";

	public void generateLicence(String location, Calendar validityDate)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidKeyException, ShortBufferException,
			IllegalBlockSizeException, BadPaddingException {
		// validityDate.ser

		SecretKeySpec key = new SecretKeySpec(KEY_PHRASE.getBytes(), "DES");
		for (Provider p : Security.getProviders()) {
			System.out.println(p.getName());
		}

		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		byte[] input = String.valueOf(validityDate.getTimeInMillis())
				.getBytes();
		System.out.println("input text : " + validityDate.getTimeInMillis());

		// encryption pass

		byte[] cipherText = new byte[input.length];
		cipher.init(Cipher.ENCRYPT_MODE, key);
		int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
		ctLength += cipher.doFinal(cipherText, ctLength);
		System.out.println("cipher text: " + new String(cipherText)
				+ " bytes: " + ctLength);

		// decryption pass

		byte[] plainText = new byte[ctLength];
		cipher.init(Cipher.DECRYPT_MODE, key);
		int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
		ptLength += cipher.doFinal(plainText, ptLength);
		System.out.println("plain text : " + new String(plainText) + " bytes: "
				+ ptLength);
	}
}
