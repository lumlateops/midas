package com.lumlate.midas.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.nio.cs.ext.MS932DB.Encoder;

public class PasswordUtility
{
	private static final char[]	PASSWORD	= new String("").toCharArray();
	private static final byte[]	SALT			= new String("").getBytes();

	public static String encrypt(String property) throws GeneralSecurityException
	{
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
		pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
		return base64Encode(pbeCipher.doFinal(property.getBytes()));
	}

	private static String base64Encode(byte[] bytes)
	{
		// NB: This class is internal, and you probably should use another impl
		return new BASE64Encoder().encode(bytes);
	}

	public static String decrypt(String property) throws GeneralSecurityException, IOException
	{
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
		pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
		return new String(pbeCipher.doFinal(base64Decode(property)));
	}

	private static byte[] base64Decode(String property) throws IOException
	{
		// NB: This class is internal, and you probably should use another impl
		return new BASE64Decoder().decodeBuffer(property);
	}
}
