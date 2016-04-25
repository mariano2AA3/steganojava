package tmi.steganojava.utils;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;


public class Security {

	// AES specification - changing will break existing encrypted streams!
	private static final String CIPHER_SPEC = "AES/CBC/PKCS5Padding";

	// Key derivation specification - changing will break existing streams!
	private static final String KEYGEN_SPEC = "PBKDF2WithHmacSHA1";
	private static final int SALT_LENGTH = 16; // in bytes
	private static final int AUTH_KEY_LENGTH = 8; // in bytes
	private static final int ITERATIONS = 32768;

	// Process input/output streams in chunks - arbitrary
	private static final int BUFFER_SIZE = 1024;


	/**
	 * @return a new pseudorandom salt of the specified length
	 */
	private static byte[] generateSalt(int length) {
		Random r = new SecureRandom();
		byte[] salt = new byte[length];
		r.nextBytes(salt);
		return salt;
	}
	
	private static Keys keygen(char[] password, byte[] salt) {
		SecretKeyFactory factory;
		int keyLength = 256;
		try {
			factory = SecretKeyFactory.getInstance(KEYGEN_SPEC);
		} catch (NoSuchAlgorithmException impossible) { return null; }
		// derive a longer key, then split into AES key and authentication key
		KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, keyLength + AUTH_KEY_LENGTH * 8);
		SecretKey tmp = null;
		try {
			tmp = factory.generateSecret(spec);
		} catch (InvalidKeySpecException impossible) { }
		byte[] fullKey = tmp.getEncoded();
		SecretKey authKey = new SecretKeySpec( // key for password authentication
				Arrays.copyOfRange(fullKey, 0, AUTH_KEY_LENGTH), "AES");
		SecretKey encKey = new SecretKeySpec( // key for AES encryption
				Arrays.copyOfRange(fullKey, AUTH_KEY_LENGTH, fullKey.length), "AES");
		return new Keys(encKey, authKey);
	}

	public static byte[] encrypt(byte[] input, String password)
			throws IOException, IllegalBlockSizeException, BadPaddingException {
		int keyLength = 256;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		// generate salt and derive keys for authentication and encryption
		byte[] salt = generateSalt(SALT_LENGTH);
		Keys keys = keygen(password.toCharArray(), salt);
		
		// initialize AES encryption
		Cipher encrypt = null;
		try {
			encrypt = Cipher.getInstance(CIPHER_SPEC);
			encrypt.init(Cipher.ENCRYPT_MODE, keys.encryption);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException impossible) { }
		  catch (InvalidKeyException e) { }
		
		// get initialization vector
		byte[] iv = null;
		try {
			iv = encrypt.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
		} catch (InvalidParameterSpecException impossible) { }
		
		// write authentication and AES initialization data
		output.write(keyLength / 8);
		output.write(salt);
		output.write(keys.authentication.getEncoded());
		output.write(iv);
		
		// escribe los bytes ya encriptados del archivo
		output.write(encrypt.update(input, 0, input.length));
		output.write(encrypt.doFinal());
		
		return output.toByteArray();
	}

	public static byte[] decrypt(byte[] input, String password) throws InvalidPasswordException, InvalidAESStreamException, IOException {

		ByteArrayInputStream in = new ByteArrayInputStream(input);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		// read salt, generate keys, and authenticate password
		byte[] salt = new byte[SALT_LENGTH];
		in.read(salt);
		Keys keys = keygen(password.toCharArray(), salt);
		byte[] authRead = new byte[AUTH_KEY_LENGTH];
		in.read(authRead);
		if (!Arrays.equals(keys.authentication.getEncoded(), authRead)) {
			throw new InvalidPasswordException();
		}
		
		// initialize AES decryption
		byte[] iv = new byte[16]; // 16-byte I.V. regardless of key size
		in.read(iv);
		Cipher decrypt = null;
		try {
			decrypt = Cipher.getInstance(CIPHER_SPEC);
			decrypt.init(Cipher.DECRYPT_MODE, keys.encryption, new IvParameterSpec(iv));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException impossible) { }
		  catch (InvalidKeyException e) { }
		
		// read data from input into buffer, decrypt and write to output
		byte[] buffer = new byte[BUFFER_SIZE];
		int numRead;
		byte[] decrypted;
		while ((numRead = in.read(buffer)) > 0) {
			decrypted = decrypt.update(buffer, 0, numRead);
			if (decrypted != null) {
				output.write(decrypted);
			}
		}
		try { // finish decryption - do final block
			decrypted = decrypt.doFinal();
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new InvalidAESStreamException(e);
		}
		if (decrypted != null) {
			output.write(decrypted);
		}

		return output.toByteArray();
	}

	private static class Keys {
		public final SecretKey encryption, authentication;
		public Keys(SecretKey encryption, SecretKey authentication) {
			this.encryption = encryption;
			this.authentication = authentication;
		}
	}
	
	
	//******** EXCEPTIONS thrown by encrypt and decrypt ********
	
	public static class InvalidPasswordException extends Exception {
		private static final long serialVersionUID = -739414573736270785L;
	}

	public static class InvalidKeyLengthException extends Exception {
		private static final long serialVersionUID = 7102100427839254460L;

		InvalidKeyLengthException(int length) {
			super("Invalid AES key length: " + length);
		}
	}
	
	public static class StrongEncryptionNotAvailableException extends Exception {
		private static final long serialVersionUID = -2873912089451057375L;

		public StrongEncryptionNotAvailableException(int keySize) {
			super(keySize + "-bit AES encryption is not available on this Java platform.");
		}
	}
	
	public static class InvalidAESStreamException extends Exception {
		private static final long serialVersionUID = -5131044354036355952L;
		public InvalidAESStreamException() { super(); };
		public InvalidAESStreamException(Exception e) { super(e); }
	}

}
