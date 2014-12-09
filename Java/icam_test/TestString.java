package wjj;

import java.util.Date;

/**
 * Project Name:  Test
 * File Name:     TestString.java
 * Package Name:  
 * @Date:         2014年11月3日
 * Copyright (c)  2014, wulian All Rights Reserved.
 */

/**
 * @ClassName: TestString
 * @Function: TODO
 * @Date: 2014年11月3日
 * @author Wangjj
 * @email wangjj@wuliangroup.cn
 */
public class TestString {

	public static void main(String[] args) {
		System.out.println("字符串比较");
		String a = new String("a");
		String b = new String("a");
		System.out.println(a == b);
		System.out.println(a.equals(b));
		String c = "fd";
		String d = "fd";
		System.out.println(c == d);
		System.out.println();

		System.out.println("秒时间*1000转换毫秒，int越界");
		System.out.println(System.currentTimeMillis());
		System.out.println(1415093048 * 1000);// int越界
		System.out.println(1415093048 * 1000L);
		System.out.println();

		System.out.println("强制类型转换");
		System.out.println(4 / 7);
		System.out.println((float) 4 / 7);
		System.out.println((double) 4 / 7);
		System.out.println(4.0 / 7);
		System.out.println();

		System.out.println("当前时间与强制转换");
		System.out.println(System.currentTimeMillis());
		System.out.println(System.currentTimeMillis() / 1000);
		System.out.println((int) System.currentTimeMillis());
		System.out.println((int) System.currentTimeMillis() / 1000);// 强制转换优先除法'/'
		System.out.println((int) (System.currentTimeMillis() / 1000));
		System.out.println();

		System.out.println(deviceIdToMac("cmic0110000023344566"));
		
		
		System.out.println("4567dfs".toUpperCase());
		
		System.out.println(Integer.toHexString(12));
		
		System.out.println(1136/17*9);
		System.out.println(1136f/17*9);
		System.out.println((int)(1136f/17*9));
	}

	enum aa {
		a, b, c;
	}

	public static String deviceIdToMac(String deviceId) {
		if (deviceId.length() != 20) {
			return "";
		}
		String macStr = deviceId.substring(8, 20);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 12; i++) {
			sb.append(macStr.charAt(i));
			if (i % 2 == 1) {
				sb.append(":");
			}
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}