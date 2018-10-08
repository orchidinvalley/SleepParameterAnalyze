package com.taixin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

	public static final int YEAR = 1;
	public static final int MONTH = 2;
	public static final int DAY = 3;
	public static final long _1M =  1048576;
	public static final long _1K = 1024;
	public static final long _1G = 1073741824L;
	
	
	private final static char[] HEX = "0123456789abcdef".toCharArray();

	public Utils() {
	}
	/* ArrayList<Integer> converse to String */
	public static String arrayListToString(ArrayList<Integer> arraylist) {
		String ss = null;
		byte [] bb = new byte[arraylist.size()];
		for (int i = 0; i < arraylist.size(); i++) {
			bb[i] = (byte) (arraylist.get(i) & 0xff);
		}
		ss = byteArrayToHexString(bb);
		return ss;
	}
	/**
	 * Using some super basic byte array &lt;-&gt; hex conversions so we don't
	 * have to rely on any large Base64 libraries. Can be overridden if you
	 * like!
	 * 
	 * @param bytes
	 *            byte array to be converted
	 * @return string containing hex values
	 */
	static public String byteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte element : bytes) {
			int v = element & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase(Locale.US);
	}
}
