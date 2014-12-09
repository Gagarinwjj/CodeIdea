package wjj;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Project Name:  Test
 * File Name:     TestEncrypt.java
 * Package Name:  
 * @Date:         2014年10月9日
 * Copyright (c)  2014, wulian All Rights Reserved.
 */

/**
 * @ClassName: DESHelper
 * @Function: DES加密解密助手
 * @Date: 2014年10月9日
 * @author Wangjj
 * @email wangjj@wuliangroup.cn
 */
public class DESHelper {
	// 算法名称
	public static final String KEY_ALGORITHM = "DES";
	// 算法名称/加密模式/填充方式 "AES/ECB/PKCS5Padding"
	public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

	public static void main(String[] args) {
		initkey("mypassword");

		String content = "待加密内容";
		String password = "12345678"; // 密码，长度要大于8 。可以md5一下 得到32位。

		System.out.println("加密前：" + content);
		String encryptResult = encrypt(content, password);
		System.out.println("加密后：" + encryptResult);
		System.out.println("解密后：" + decrypt(encryptResult, password));
	}

	public static String encrypt(String datasource, String password) {
		try {

			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(KEY_ALGORITHM);// 不支持aes算法，可以用KeyGenerator
			Key key = keyFactory.generateSecret(new DESKeySpec(password
					.getBytes()));
			cipher.init(Cipher.ENCRYPT_MODE, key, new SecureRandom());
			// return new String(cipher.doFinal(datasource.getBytes()));//new
			// String 是乱码，报错。
			return Base64.getEncoder().encodeToString(
					cipher.doFinal(datasource.getBytes()));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String decrypt(String src, String password) {
		try {

			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(KEY_ALGORITHM);
			Key key = keyFactory.generateSecret(new DESKeySpec(password
					.getBytes()));
			cipher.init(Cipher.DECRYPT_MODE, key, new SecureRandom());

			return new String(cipher.doFinal(Base64.getDecoder().decode(src)));
		} catch (Exception e) {

			e.printStackTrace();

		}
		return "";
	}

	/**
	 * @Function 产生key
	 * @author Wangjj
	 * @date 2014年10月9日
	 */
	public static void initkey(String pwd) {
		try {
			KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM); // 实例化密钥生成器
			// 初始化密钥生成器:DES为56,AES要求密钥长度为128,192,256位
			// kg.init(56);//没有指定SecureRandom，每次生成都不一样
			kg.init(56, new SecureRandom(pwd.getBytes())); // 指定SecureRandom，每次生成都一样
			SecretKey secretKey = kg.generateKey(); // 生成密钥
			System.out.println(Base64.getEncoder().encodeToString(
					secretKey.getEncoded()));
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		}

	}

}
