package com.taixin;

import java.util.Arrays;


/**
 * matlab findpeaks(data,'minpeakdistance',Pd)
 * 
 * @author zl
 *
 */
public class FindPeaks {

	/**
	 * 
	 * @param src_data
	 * @param src_len
	 * @param distance
	 * @return 返回所找到的特征点的位置locs
	 */
	public  int[] findPeaks(double[] src_data, int src_len, int distance) {
		double[] peak1 = new double[src_len];
		int[] loc1 = new int[src_len];
		int k;

		double temp = 0;
		int tmp = 0;

		k = 0;
		for (int i = 1; i < (src_len - 1); i++) {
			if ((src_data[i] > src_data[i - 1]) && (src_data[i] > src_data[i + 1])) {
				peak1[k] = src_data[i];
				loc1[k] = i;
				k++;
			}
		}

		for (int i = 0; i < k; i++) {
			for (int j = i; j < k; j++) {
				if (peak1[i] < peak1[j]) {
					temp = peak1[i];
					peak1[i] = peak1[j];
					peak1[j] = temp;

					tmp = loc1[i];
					loc1[i] = loc1[j];
					loc1[j] = tmp;
				}
			}
		}

		int[] idelete = new int[k];
		ArrayUtil.arraySet(idelete, 0, k);

		for (int i = 0; i < k; i++) {
			int[] res1 = new int[k];
			int[] res2 = new int[k];

			if (idelete[i] == 0) {
				for (int j = 0; j < k; j++) {
					if (loc1[j] >= (loc1[i] - distance))
						res1[j] = 1;
					else
						res1[j] = 0;

					if (loc1[j] <= (loc1[i] + distance))
						res2[j] = 1;
					else
						res2[j] = 0;

					if ((res1[j] == 1) && (res2[j] == 1))
						idelete[j] = 1;
					else if (idelete[j] == 1)
						idelete[j] = 1;
					else
						idelete[j] = 0;
				}
				idelete[i] = 0;
			}
		}

		int[] loc = new int[k];
		int len = 0;
		for (int i = 0; i < k; i++) {
			if (idelete[i] == 0) {
				loc[len] = loc1[i];
				len++;
			}
		}

		int[] locs = new int[len];

		System.arraycopy(loc, 0, locs, 0, len);
		Arrays.sort(locs);

		return locs;
	}

}