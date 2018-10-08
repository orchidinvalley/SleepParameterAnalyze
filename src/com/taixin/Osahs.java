package com.taixin;

import java.util.ArrayList;

/** 阻塞性睡眠呼吸暂停低通气综合征 */
public class Osahs {

	 final int THD = 10;
	

	public int disposeOsahs(ArrayList<Integer>data) {
		int osahs=0;
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
				FindPeaks mFindPeaks=new FindPeaks();
				int  []loc=mFindPeaks.findPeaks(fixFilterResult, fixFilterResult.length, 100);
				if(loc.length>1) {
				double []part=new double[loc[loc.length-1]-loc[0]+1];
				System.arraycopy(fixFilterResult, loc[0], part, 0, part.length);
				double std=ArrayUtil.std(part, part.length);
				if(std<THD) {
//					for(int i=0;i<part.length;i++)
//						System.out.println(part[i]);
					osahs=1;
				}
				}else
					osahs=1;
		return osahs;
	}
	
}
