/**
 * Project Name:  Test
 * File Name:     TestLink.java
 * Package Name:  wjj
 * @Date:         2014年11月25日
 * Copyright (c)  2014, wulian All Rights Reserved.
 */

package wjj;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: TestLink
 * @Function: TODO
 * @Date: 2014年11月25日
 * @author Wangjj
 * @email wangjj@wuliangroup.cn
 */
public class TestLink {
	public static void main(String[] args) {
		String http = "http://xxx/qr/icam?device_id=xxxx&ssid=xxx&spwd=45_1_d";
		System.out.println(getRequest(http));

	}

	public static HashMap<String, String> getRequest(String url) {
		Pattern p = Pattern.compile("(\\w+)=(\\w+)");
		Matcher matcher = p.matcher(url);
		HashMap<String, String> params = new HashMap<String, String>();
		while (matcher.find()) {
			params.put(matcher.group(1), matcher.group(2));
		}
		return params;

	}
}