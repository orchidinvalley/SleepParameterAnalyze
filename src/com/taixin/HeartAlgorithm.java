package com.taixin;

import java.util.ArrayList;

/** 心跳算法 */
public class HeartAlgorithm {
	/** double类型，心跳波形图绘制所用数组 */
	private double[] heartVal = new double[Constant.SOURCE_DATA_NUM];
	/** 心跳波形图绘制所用数组heartVal的有效长度 */
	private int heartValCnt = 0;
	/** 心跳次数 */
	private int heartNumber = 0;
	/** 每分钟心率异常次数--心率不在50-100之间算异常 */
	private int heartErrorNumber = 0;

	private ArrayList<Integer> hrt_locs = new ArrayList<Integer>();

	// true表示第一次执行
	boolean firstReadHeart = true;
	static int HRT_FILTER = 1;

	public int getHeartNumber() {
		return heartNumber;
	}

	public int getHeartErrorNumber() {
		return heartErrorNumber;
	}

	public ArrayList<Integer> getHrtLocs() {
		return hrt_locs;
	}

	/**
	 * 心跳 ————— 可以做整夜的心率处理也可以做实时的心率处理，修改输入的原始数据即可
	 * 
	 * @param bufIn
	 *            原始数据
	 * @param bufInCnt
	 *            原始数据的长度
	 */
//	public void txHeart(int real_all, int sleep_lv, int[] bufIn, int bufInCnt) {
//		/** findPeaks的参数distance */
//		int Pd = 5;
//		hrt_locs.clear();
//		if ((bufInCnt == 0) || (sleep_lv == 1)) {
//			heartNumber = 0;
//			heartErrorNumber = 0;
//			return;
//		}
//		double[] filterResult = AdaptiveFilter.adaptiveFilter(HRT_FILTER, real_all, bufIn, bufInCnt);
//		// 用300阶带通滤波器滤波完的结果画图
//		if (firstReadHeart == true) {
//			System.arraycopy(filterResult, FILTER_HCOEF.HRT_FILTER_LEN - 1, heartVal, 0,
//					filterResult.length - FILTER_HCOEF.HRT_FILTER_LEN + 1);
//
//			heartValCnt = filterResult.length - (FILTER_HCOEF.HRT_FILTER_LEN - 1);// 心跳滤波完数据所存数组TX_HeartBeat_Val的有效数据长度
//
//			firstReadHeart = false;
//		} else {
//			System.arraycopy(filterResult, 0, heartVal, 0, filterResult.length);
//			heartValCnt = filterResult.length;
//		}
//
//		FindPeaks mFindPeaks = new FindPeaks();
//		int[] locs = mFindPeaks.findPeaks(heartVal, heartValCnt, Pd);
//		for (int i = 0; i < locs.length; i++)
//			hrt_locs.add(locs[i]);
//		// int[] hrtInterval = new int[locs.length - 1];
//		//
//		// for (int i = 0; i < locs.length - 1; i++) {
//		// hrtInterval[i] = locs[i + 1] - locs[i];
//		// }
//		// HeartAbnormal mHeartAbnormal=new HeartAbnormal();
//		// HeartAbnormal.AbnormalHeartDispose(hrtInterval);
//		int[] locsPart = new int[locs.length - 2];// 去掉locs的第一个和最后一个，因有时这两个点取得不对
//		System.arraycopy(locs, 1, locsPart, 0, locs.length - 2);
//
//		// 计算心跳次数
//		int sum = 0;
//		for (int i = 1; i < locsPart.length; i++) {
//			sum += locsPart[i] - locsPart[i - 1];
//		}
//
//		if (locsPart.length <= 1) {
//			heartNumber = 0;
//		} else {
//			double avg = (double) sum / (locsPart.length - 1);
//			heartNumber = (int) (60 * Constant.DOT_SEC / avg);
//		}
//
//		// 判断心率异常
//		if ((heartNumber < 50) || (heartNumber > 100))
//			heartErrorNumber = 1;
//		else
//			heartErrorNumber = 0;
//
//	}

	public void heartOfMinuteFilter(ArrayList<Integer> data) {
		int Pd = 25;
		hrt_locs.clear();

		// 采用固定频率的滤波器进行滤波

		ArrayList<Double> B = new ArrayList<Double>();
		ArrayList<Double> A = new ArrayList<Double>();
		double[] X = new double[data.size()];

		for (int i = 0; i < FILTER_HCOEF.HRT_FILTER_LEN; i++)
			B.add(FILTER_HCOEF.HRT[i]);
		A.add(1.0);
		for (int i = 0; i < data.size(); i++) {
			X[i] = (double) data.get(i);
		}

		double[] fixFilterResult = Filtfilt.doFiltfilt(B, A, X);
//		int Pd=30;
//		
//		double[] X = new double[data.size()];
//		for (int i = 0; i < data.size(); i++) {
//			X[i] = (double) data.get(i);
//		}
//		double[] fixFilterResult=new Db4FiveWavelet().waveletTransform(X, 5);
		
		FindPeaks mFindPeaks = new FindPeaks();
		int[] locs = mFindPeaks.findPeaks(fixFilterResult, fixFilterResult.length, Pd);
		
		if (locs.length <= 5) {
			heartNumber = 75;
		} else {
			double avg = (double) (locs[locs.length-2]-locs[2])/(locs.length-3);
			heartNumber = (int) (60 * Constant.DOT_SEC / avg);
		}
		
	}

}
