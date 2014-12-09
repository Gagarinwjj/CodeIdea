package wjj;
/**
 * Project Name:  Test
 * File Name:     Algorithm.java
 * Package Name:  
 * @Date:         2014年10月23日
 * Copyright (c)  2014, wulian All Rights Reserved.
 */

/**
 * @ClassName: Algorithm
 * @Function: 算法参考
 * @Date: 2014年10月23日
 */
public class Algorithm {
	/**
	 * 二行制转字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}

	private static char sHexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * @Function 2进制转16进制
	 * @author Wangjj
	 * @date 2014年10月23日
	 * @param source
	 * @return
	 */
	public static String byte2hex2(byte[] source) {
		if (source == null || source.length <= 0) {
			return "";
		}

		final int size = source.length;
		final char str[] = new char[size * 2];
		int index = 0;
		byte b;
		for (int i = 0; i < size; i++) {
			b = source[i];
			str[index++] = sHexDigits[b >>> 4 & 0xf];
			str[index++] = sHexDigits[b & 0xf];
		}
		return new String(str);
	}

	/**
	 * @Function 16进制转2进制 待测
	 * @author Wangjj
	 * @date 2014年10月23日
	 * @param hex
	 * @return
	 */
	private static byte[] hex2byte(String hex) {
		byte[] b = hex.getBytes();
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException();
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	public static void main(String[] args) {
		System.out.println("0".getBytes()[0]);
	}
}
