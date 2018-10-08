package com.taixin;

import java.util.ArrayList;

/** 呼吸算法 */
public class BreathAlgorithm {
	/** double类型，呼吸波形图绘制所用数组 */
	double[] breathVal = new double[Constant.SOURCE_DATA_NUM];
	/** 呼吸波形图绘制所用数组breathVal的有效长度 */
	int breathValCnt = Constant.SOURCE_DATA_NUM;
	/** 呼吸次数 */
	private int breathNumber = 0;
	/** 每分钟呼吸暂停的次数 */
	private int osahsNum;

	public int getBreathNumber() {
		return breathNumber;
	}

	public int getOsahsNum() {
		return osahsNum;
	}

	/**
	 * 呼吸算法 ————— 可以做整夜的心率处理也可以做实时的心率处理，修改输入的原始数据即可
	 * 
	 * @param bufIn
	 *            原始数据
	 * @param bufInCnt
	 *            原始数据的长度
	 */
	// void txBreath(int sleepLv, int wakeUp, int[] bufIn, int bufInCnt, int
	// osahs_index, ArrayList<Integer> hrt_loc) {
	// /** findPeaks的参数distance */
	// int Pd = 24;
	// osahsNum = 0;
	// if ((bufInCnt == 0) || (sleepLv == 1))// 判断原始有效数据是否为0
	// {
	// breathNumber = 0;
	// return;
	// }
	//
	// // 用零相位滤波代替上面的数据处理+filter
	// ArrayList<Double> B = new ArrayList<Double>();
	// ArrayList<Double> A = new ArrayList<Double>();
	// double[] X = new double[bufInCnt];
	// for (int i = 0; i < FILTER_HCOEF.BTH_FILTER_LEN; i++) {
	// B.add(FILTER_HCOEF.BTH[i]);
	// }
	// A.add(1.0);
	// for (int i = 0; i < bufInCnt; i++) {
	// X[i] = (double) bufIn[i];
	// }
	//
	// double[] filterResult = Filtfilt.doFiltfilt(B, A, X);
	//
	// System.arraycopy(filterResult, 0, breathVal, 0, filterResult.length);
	// breathValCnt = filterResult.length;
	//
	// FindPeaks mFindPeaks = new FindPeaks();
	//
	// int[] locs = mFindPeaks.findPeaks(breathVal, breathValCnt, Pd);
	//
	// // 判断呼吸暂停
	// if (wakeUp == 0) {
	// Osahs mOsahs = new Osahs();
	// osahsNum = mOsahs.osahsDispose(breathVal, locs, osahs_index, hrt_loc);
	// } else
	// osahsNum = 0;
	//
	// // 计算呼吸次数
	// int sum = 0;
	// for (int i = 1; i < locs.length; i++) {
	// sum += locs[i] - locs[i - 1];
	// }
	//
	// if (locs.length <= 1) {
	// breathNumber = 0;
	// } else {
	// double avg = (double) sum / (locs.length - 1);
	// breathNumber = (int) (60 * Constant.DOT_SEC / avg);
	// }
	//
	// }

	public void breathOfMinuteFilter(ArrayList<Integer> data) {
		int Pd = 100;

		// 采用固定频率的滤波器进行滤波

		ArrayList<Double> B = new ArrayList<Double>();
		ArrayList<Double> A = new ArrayList<Double>();
		double[] X = new double[data.size()];

		for (int i = 0; i < FILTER_HCOEF.BTH_FILTER_LEN; i++)
			B.add(FILTER_HCOEF.BTH[i]);
		A.add(1.0);
		for (int i = 0; i < data.size(); i++) {
			X[i] = (double) data.get(i);
		}

		double[] fixFilterResult = Filtfilt.doFiltfilt(B, A, X);
		FindPeaks mFindPeaks = new FindPeaks();
		int[] locs = mFindPeaks.findPeaks(fixFilterResult, fixFilterResult.length, Pd);
		if(locs.length==0)
			breathNumber=15;
		else if (locs.length > 1) {
			double avg = (double) (locs[locs.length - 1] - locs[0]) / (locs.length - 1);
			breathNumber = (int) (60 * Constant.DOT_SEC / avg);
		} else
			breathNumber = (int) (60 * Constant.DOT_SEC / locs[0]);

	}

}
