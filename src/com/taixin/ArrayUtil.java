
package com.taixin;

public class ArrayUtil {

	public static void arraySet(byte[] src, byte value, int len) {
		int i;

		if (src == null) {
			return;
		}

		if (len > src.length) {
			len = src.length;
		}

		for (i = 0; i < len; i++) {
			src[i] = value;
		}

		return;
	}

	public static void arraySet(int[] src, int value, int len) {
		int i;

		if (src == null) {
			return;
		}

		if (len > src.length) {
			len = src.length;
		}

		for (i = 0; i < len; i++) {
			src[i] = value;
		}

		return;
	}

	public static void arraySet(double[] src, double value, int len) {
		int i;

		if (src == null) {
			return;
		}

		if (len > src.length) {
			len = src.length;
		}

		for (i = 0; i < len; i++) {
			src[i] = value;
		}

		return;
	}
	
	public static void arraySet(float[] src, float value, int len) {
		int i;

		if (src == null) {
			return;
		}

		if (len > src.length) {
			len = src.length;
		}

		for (i = 0; i < len; i++) {
			src[i] = value;
		}

		return;
	}

	public static int arrayCompare(byte[] src, int srcPos, byte[] dest, int destPos, int len) {
		int i;

		if (src == null || dest == null) {
			return -1;
		}

		for (i = 0; i < len; i++) {
			if (src[srcPos + i] != dest[destPos + i]) {
				return (i + 1);
			}
		}

		return 0;
	}

	public static boolean isArrayZero(byte[] buf, int len) {
		int i;
		for (i = 0; i < len; i++)
			if (buf[i] != 0)
				return false;
		return true;
	}

	public static boolean isArrayZero(int[] buf, int len) {
		int i;
		for (i = 0; i < len; i++)
			if (buf[i] != 0)
				return false;
		return true;
	}

	/**
	 * 标准差，输入为int数组
	 * 
	 * @param srcData
	 *            要计算标准差的数组
	 * @param sraDataLen
	 *            srcData的长度 返回标准差输出，double类型
	 */
	public static double std(int[] srcData, int sraDataLen) {
		if (sraDataLen == 0) {
			return 0;
		}

		// 平均值ֵ
		double avg = mean(srcData, sraDataLen);

		double sum = 0;
		for (int i = 0; i < sraDataLen; i++) {
			sum += (srcData[i] - avg) * (srcData[i] - avg);
		}

		double stdData = 0;
		stdData = Math.sqrt(sum / (sraDataLen - 1));

		return stdData;
	}

	/**
	 * 标准差，输入为double数组
	 * 
	 * @param srcData
	 *            要计算标准差的数组
	 * @param sraDataLen
	 *            srcData的长度 返回标准差输出，double类型
	 */
	public static double std(double[] srcData, int sraDataLen) {
		if (sraDataLen == 0) {
			return 0;
		}

		// 平均值ֵ
		double avg = mean(srcData, sraDataLen);

		double sum = 0;
		for (int i = 0; i < sraDataLen; i++) {
			sum += (srcData[i] - avg) * (srcData[i] - avg);
		}

		double stdData = 0;
		stdData = Math.sqrt(sum / (sraDataLen - 1));

		return stdData;
	}

	/**
	 * 求数组的平均值，输入为int数组
	 * 
	 * @param srcData
	 *            要求平均值的原始数组
	 * @param sraDataLen
	 *            要求平均值的原始数组的长度 返回平均值输出，double
	 */
	public static double mean(int[] srcData, int sraDataLen) {
		if (sraDataLen == 0) {
			return 0;
		}

		// 求和
		int sum = 0;
		for (int i = 0; i < sraDataLen; i++) {
			sum += srcData[i];
		}

		// 求平均值ֵ
		double meanData = (double) sum / sraDataLen;
		return meanData;
	}

	/**
	 * 求数组的平均值，输入为double数组
	 * 
	 * @param srcData
	 *            要求平均值的原始数组
	 * @param sraDataLen
	 *            要求平均值的原始数组的长度 返回平均值输出，double
	 */
	public static double mean(double[] srcData, int sraDataLen) {
		if (sraDataLen == 0) {
			return 0;
		}

		double sum = 0.0;
		for (int i = 0; i < sraDataLen; i++) {
			sum += srcData[i];
		}

		double meanData = (double) sum / sraDataLen;
		return meanData;
	}

	/** 求数组最大值 */
	public static double max(int[] srcData, int srcDataLen) {
		double max = 0;

		for (int i = 0; i < srcDataLen; i++) {
			if (srcData[i] > max)
				max = srcData[i];
		}

		return max;
	}
	
	public static double max(double[] srcData, double srcDataLen) {
		double max = 0;

		for (int i = 0; i < srcDataLen; i++) {
			if (srcData[i] > max)
				max = srcData[i];
		}

		return max;
	}
	
	/** 求数组最小 值 */
	public static double min(int[] srcData, int srcDataLen) {
		double min;

		min = srcData[0];

		for (int i = 1; i < srcDataLen; i++) {
			if (srcData[i] < min)
				min = srcData[i];
		}

		return min;
	}
	
	public static double min(double[] srcData, double srcDataLen) {
		double min;

		min = srcData[0];
		for (int i = 1; i < srcDataLen; i++) {
			if (srcData[i] < min)
				min = srcData[i];
		}

		return min;
	}
	
	/** 计算一个数组中某个数出现的次数 */
	public static int calcuteDataNum(float[] data, float value) {
		int cnt = 0;
		for (float datalist : data) {
			if (datalist == value)
				cnt++;
		}

		return cnt;
	}

}
