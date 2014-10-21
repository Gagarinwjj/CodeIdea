import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * @ClassName: AndroidUtils
 * @Function: 常用辅助类
 */
public class AndroidUtils {
	public static final String KEY_ALGORITHM = "AES";
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	// public static final String CIPHER_ALGORITHM =
	// "AES/ECB/ZeroBytePadding";//解决4.3以上的bug？

	/**
	 * 如果键盘没有收回 自动关闭键盘
	 *
	 * @param activity
	 *            Activity
	 * @param v
	 *            控件View
	 */
	public static void autoCloseKeyboard(Activity activity, View v) {
		/** 收起键盘 */
		View view = activity.getWindow().peekDecorView();
		if (view != null && view.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}

	}

	/**
	 * @Function 获取屏幕尺寸
	 * @author Wangjj
	 * @date 2014年10月5日
	 * @param context
	 * @return
	 */
	public DisplayMetrics getViewSize(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics;

	}

	/**
	 * @Function 加密数据
	 * @author Wangjj
	 * @date 2014年10月9日
	 * @param originStr
	 *            原始字符串
	 * @param pwd
	 *            加密密码
	 * @return base64编码的加密数据
	 */
	public static String encrypt(String originStr, String pwd) {

		try {
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);

			SecretKey secretKey = generateKey(pwd);

			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			// string->utf8 bytes->final->base64
			return Base64
					.encodeToString(
							cipher.doFinal(originStr.getBytes("UTF-8")),
							Base64.DEFAULT);
		} catch (Exception e) {

			e.printStackTrace();

		}
		return "";
	}

	/**
	 * 
	 * @Function 解密数据
	 * @author Wangjj
	 * @date 2014年10月9日
	 * @param encryptedStr
	 *            已经使用base64编码的加密数据
	 * @param pwd
	 *            解密密码
	 * @return
	 */
	public static String decrypt(String encryptedStr, String pwd) {
		try {
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			SecretKey secretKey = generateKey(pwd);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			// base64->final->utf8 bytes->string
			// TODO: 华为手机4.2.2 BadPaddingException
			// 在4.2.1及之前，都用的是好好的，但一到4.2.2及其以后的版本后就抛出Bad-Padding Exception.这个异常。

			return new String(cipher.doFinal(Base64.decode(encryptedStr,
					Base64.DEFAULT)), "UTF-8");
		} catch (Exception e) {
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
	 * @throws NoSuchProviderException
	 */
	private static SecretKey generateKey(String pwd)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);

		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");// 解决4.2.2及之后的bug
		sr.setSeed(pwd.getBytes());

		// SecureRandom sr =new SecureRandom(pwd.getBytes())//4.2.1及之前可以
		kgen.init(128, sr);
		SecretKey secretKey = kgen.generateKey();
		return secretKey;
		// return new SecretKeySpec(pwd.getBytes(), KEY_ALGORITHM);
	}

}
