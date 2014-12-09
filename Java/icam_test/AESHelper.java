package wjj;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESHelper {

	private static final String KEY_ALGORITHM = "AES";
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static Cipher cipher = null; // 私鈅加密对象Cipher
	static {
		try {
			cipher = Cipher.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		System.out.println("AES加解密测试：");
		System.out.println("mykey:" + getKey("123"));
		System.out.println("mykey2:" + getKey("123"));

		String content = "待加密字符串";
		String password = "password";

		System.out.println("加密前：" + content);
		String encryptResult = encrypt(content, password);
		System.out.println("加密后：" + encryptResult);
		System.out.println("解密后：" + decrypt(encryptResult, password));
	}

	/**
	 * 加密
	 * 
	 * @param message
	 * @return
	 */
	public static String encrypt(String message, String pwd) {

		try {
			cipher.init(Cipher.ENCRYPT_MODE, generateKey(pwd)); // 设置工作模式为加密模式，给出密钥
			byte[] resultBytes = cipher.doFinal(message.getBytes("UTF-8")); // 正式执行加密操作
			return Base64.getEncoder().encodeToString(resultBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 解密
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String message, String pwd) {

		try {
			cipher.init(Cipher.DECRYPT_MODE, generateKey(pwd)); // 设置工作模式为解密模式，给出密钥
			byte[] resultBytes = cipher.doFinal(Base64.getDecoder().decode(
					message));
			return new String(resultBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 去掉加密字符串换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String filter(String str) {
		String output = "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int asc = str.charAt(i);
			if (asc != 10 && asc != 13) {
				sb.append(str.subSequence(i, i + 1));
			}
		}
		output = new String(sb);
		return output;
	}

	/**
	 * @Function    测试key的产生过程
	 * @author      Wangjj
	 * @date        2014年10月10日
	 * @param pwd
	 * @return
	 */
	private static String getKey(String pwd) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			SecureRandom sr = new SecureRandom();
			sr.setSeed(pwd.getBytes());
			kgen.init(128, sr);

			// 种子给定后 会按照一定的序列产生key
			System.out.println("genkey1: "
					+ Base64.getEncoder().encodeToString(
							kgen.generateKey().getEncoded()));
			System.out.println("genkey2: "
					+ Base64.getEncoder().encodeToString(
							kgen.generateKey().getEncoded()));
			System.out.println("genkey3: "
					+ Base64.getEncoder().encodeToString(
							kgen.generateKey().getEncoded()));

			return Base64.getEncoder().encodeToString(
					kgen.generateKey().getEncoded());
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		}
		return "";

	}

	/**
	 * 
	 * @Function 通过KeyGenerator 产生key
	 * @author Wangjj
	 * @date 2014年10月10日
	 * @param pwd
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static SecretKey generateKey(String pwd)
			throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
		kgen.init(128, new SecureRandom(pwd.getBytes()));
		SecretKey secretKey = kgen.generateKey();
		return secretKey;
	}

	/**
	 * 
	 * @Function 直接new一个key
	 * @author Wangjj
	 * @date 2014年10月10日
	 * @param pwd
	 * @return
	 */
	private static SecretKey generateKey2(String pwd) {
		// 由于key要求128/192/256 ；这里的pwd.getBytes()长度不满足 ；MD5为32位的
		// SecretKeySpec secretKeySpec = new SecretKeySpec(pwd.getBytes(),
		// KEY_ALGORITHM);

		SecretKeySpec secretKeySpec = null;
		try {
			secretKeySpec = new SecretKeySpec(MessageDigest.getInstance("MD5")
					.digest(pwd.getBytes()), KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		}
		return secretKeySpec;

	}

}