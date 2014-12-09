package wjj;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project Name:  Test
 * File Name:     TestRegx.java
 * Package Name:  
 * @Date:         2014年11月12日
 * Copyright (c)  2014, wulian All Rights Reserved.
 */

/**
 * @ClassName: TestRegx
 * @Function: TODO
 * @Date: 2014年11月12日
 * @author Wangjj
 * @email wangjj@wuliangroup.cn
 */
public class TestRegx {

	public static void main(String[] args) {
		System.out.println('a' - 0);
		System.out.println(isMatcher("", "123"));
		psa("啊1wqer2rweqr3".split("\\d"));
		System.out.println(addSplitPoint("adbc2343"));
		System.out.println(isContiStr("54321"));
		System.out.println(isMatcher("^(\\w)(\\1)+$", "bbbb"));// 向后引用
		System.out.println("abc,123".replaceAll("(\\w+)\\s*,\\s*(\\w+)",
				"$2 $1"));// 分组捕获
		System.out.println(isMatcher(".{6,18}", "fdasfsf"));
	}

	private static boolean isMatcher(String regex, String str) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean result = m.find();
		return result;
	}

	// print_string_array
	public static void psa(String[] ss) {
		for (String s : ss) {
			System.out.println(s);
		}

	}

	// abc123=>a,b,c,1,2,3
	private static String addSplitPoint(String str) {
		int len = str.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(str.charAt(i)).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	// 连续字符的判断，正序或倒序
	public static boolean isContiStr(String str) {
		if (isMatcher("[a-zA-Z0-9]+", str)) {
			int len = str.length();
			int[] paddings = new int[len - 1];// 字符间隔
			char preChar = str.charAt(0);
			for (int i = 1; i < len; i++) {
				paddings[i - 1] = str.charAt(i) - preChar;
				preChar = str.charAt(i);
			}

			for (int j = 1; j < paddings.length; j++) {
				if (paddings[0] != paddings[j]) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

}
