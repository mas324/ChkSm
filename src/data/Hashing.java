package data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Hashing {

	protected static String fast(File in) {
		return hash(in, "MD5");
	}

	protected static String slow(File in) {
		return hash(in, "SHA-1");
	}

	protected static String enhanced(File in) {
		return hash(in, "SHA-512");
	}

	private static String hash(File in, String algorithm) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(in));
			StringBuilder builder = new StringBuilder();

			while (inputStream.available() > 0)
				digest.update((byte) inputStream.read());

			inputStream.close();

			for (byte b : digest.digest())
				builder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "Error occured in hashing";
	}

}
