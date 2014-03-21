package org.mamute.infra;

import java.math.BigInteger;

import org.apache.commons.codec.digest.DigestUtils;

public class Digester {
	private final static String SALT = "sadifhasdu34hqo9ihadfsoivuhaewuihfasiuasiufhifaew";

	public static String encrypt(String toDigest) {
		return DigestUtils.sha256Hex(toDigest + SALT);
	}

	public static String md5(String content) {
		return DigestUtils.md5Hex(content);
	}
	
	public static String legacyMd5(String content) {
	    return MD5.crypt(content);
	}

	public static String hashFor(Long id) {
		return encrypt(id.toString())
				+ new BigInteger("" + (id * 37 + 1)).toString(16);
	}

	public static Long idFor(String hash) {
		assert (hash.length() > 64);

		String obfuscatedId = hash.substring(64);

		long id = (Integer.parseInt(obfuscatedId, 16) - 1) / 37;

		assert (hash.equals(hashFor(id)));

		return id;
	}
}