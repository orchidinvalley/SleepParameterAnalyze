package com.taixin;

import java.util.ArrayList;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

/*
 * 对整晚的睡眠数据以分钟为单位 进行分析，得出每分钟的睡眠状态；
 *  离床、翻身、清醒（在床）及睡眠
 */
public class SleepStageNew {
	public static final int LEAVE = 4;
	public static final double WAKE = 5.0;
	public static final double LIGHT = 4.5;
	public static final double REMS =2.5;
	public static final double NREMS = 0.5;
	
	/** 单位是分钟 */
	private int wake_time; // 清醒
	private int rems_time; // 浅睡
	private int nrems_time;// 深睡

	private ArrayList<Double> sleep_stage = new ArrayList<Double>();
	/** 睡眠分期结果，绘图用 */
	
	public SleepStageData sleepstage(ArrayList<Integer> avg_rr, ArrayList<Integer> bodyMv, ArrayList<Integer> wakeUp,
			ArrayList<Integer> sleepLv) {
		SleepStageData mSleepStageData = new SleepStageData();
		
		// 睡眠时长小于15分钟则不进行睡眠分期计算
		if (avg_rr.size() < 15)
			return mSleepStageData;
		
		
		int[] lv = new int[sleepLv.size()];
		int[]mv=new int [bodyMv.size()];
		int[] wakeup = new int[wakeUp.size()];
		double []hrt=new double  [avg_rr.size()];
		
		for (int i = 0; i < sleepLv.size(); i++) {
			lv[i] = sleepLv.get(i);
			mv[i]=bodyMv.get(i);
			wakeup[i]=wakeUp.get(i);
			hrt[i]=avg_rr.get(i);
		}
		int i_wake = 0;
		for (int i = 0; i < wakeup.length - 1; i++) {
			if ((wakeup[i] == 0) && (wakeup[i + 1] == 1)) {
				i_wake = i + 1;
				break;
			}
		}
		sleep_stage.clear();
		for(int i=0;i<i_wake;i++) {
			if(lv[i]!=1)
			sleep_stage.add( 3.0);
			else
				sleep_stage.add(4.0);
		}
		
		
		
		ArrayList<Double>hrt_tmp=new ArrayList<Double>();
		ArrayList<Integer>mv_tmp=new ArrayList<Integer>();
		
		boolean flagOfPoly=false;
		
		for(int i=i_wake;i<lv.length;i++) {
			if(lv[i]==1) {
				if(flagOfPoly) {
					SleepStagePartPoly(hrt_tmp,mv_tmp);
					hrt_tmp.clear();
					mv_tmp.clear();
					flagOfPoly=false;
					sleep_stage.add(4.0);
				}
				else
				sleep_stage.add(4.0);
			}else {
				hrt_tmp.add(hrt[i]);
				mv_tmp.add(mv[i]);
				flagOfPoly=true;
			}
		}
		
		if(flagOfPoly  && hrt_tmp.size()>0) {
			SleepStagePartPoly(hrt_tmp,mv_tmp);
		}
		
		
		
		
		for (int i = 0; i < sleep_stage.size(); i++) {
			double dataDouble = sleep_stage.get(i);
			System.out.println(dataDouble);
			float dataFloat = (float) dataDouble;
			mSleepStageData.mSleepStageList.add(dataFloat);
		}
		
		for (int i = 0; i < sleep_stage.size(); i++) {
			// if (sleep_stage.get(i) < 4.0 && sleep_stage.get(i) > 3)//
			if (sleep_stage.get(i) == 3)//
				wake_time = wake_time + 1;
			else if (sleep_stage.get(i) < 3 && sleep_stage.get(i) > 1)
				nrems_time = nrems_time + 1;
			else if (sleep_stage.get(i) <= 1)
				rems_time = rems_time + 1;

		}

		mSleepStageData.mSoberTime = (float) wake_time / 60;
		mSleepStageData.mLightSleepTime = (float) nrems_time / 60;
		mSleepStageData.mDeepSleepTime = (float) rems_time / 60;

		System.out.println("mSoberTime=" + mSleepStageData.mSoberTime);
		System.out.println("mLightSleepTime=" + mSleepStageData.mLightSleepTime);
		System.out.println("mDeepSleepTime=" + mSleepStageData.mDeepSleepTime);

		
		
		return mSleepStageData;
	}
	
	private  void SleepStagePartPoly(ArrayList <Double>hrt,ArrayList<Integer>mv) {
		if(hrt.size()<10) {//在床小于10分钟设置为清醒
			for(int i=0;i<hrt.size();i++)
				sleep_stage.add(3.0);
			return;
		}
		double[] hrt_done =smooth(hrt);// 
//		ArrayList<Double>tmpList=new ArrayList<Double>();
//		for(double t:tmp)
//			tmpList.add(t);
//		double[] hrt_done=cuvre_fitting(tmpList, 20);
//		double min = ArrayUtil.min(hrt_done, hrt_done.length);
//		double max = ArrayUtil.max(hrt_done, hrt_done.length);
		double min=1000;
		 double  max=0;
		 ArrayList<Double>tmp=new ArrayList<>();
		 for(int i=0;i<hrt_done.length;i ++) {
			 if(mv.get(i)==0) {			
				 tmp.add(hrt_done[i]);
			 }
		 }
		 if(tmp.size()<20) {
			 for(int i=0;i<hrt.size();i++)
					sleep_stage.add(3.0);
				return;
		 }
		 double []tmp2=new double[tmp.size()];
		 for (int i=0;i<tmp.size();i++) 
			 tmp2[i]=tmp.get(i);
		 tmp2=bubbleSort(tmp2);
		 if(tmp2.length>20) {
			 double sum=0;
			 for(int i=0;i<9;i++)
				 sum=sum+tmp2[i];
			 min=sum/9;
			 sum=0;
			 for(int i=tmp2.length-10;i<tmp2.length-1;i++)
				 sum=sum+tmp2[i];
			 max=sum/9;
				 
		 }else {
			 max=tmp2[tmp2.length-1];
			 min=tmp2[0];
		 }
		

		double nrem_thd = 0.3 * (max - min) + min;
		double light_thd = 0.7 * (max - min) + min;
		ArrayList<Double>stage=new ArrayList<Double>();
		stage.add(WAKE);
		stage.add(WAKE);
		stage.add(WAKE);
		stage.add(WAKE);
		stage.add(WAKE);
		for(int i=0;i<hrt_done.length;i++)
			if (hrt_done[i] < nrem_thd)
				stage.add((double)NREMS);
			else if (hrt_done[i] > light_thd)
				stage.add((double)LIGHT);
			else
				stage.add((double)REMS);
		stage.add(WAKE);
		stage.add(WAKE);
		stage.add(WAKE);
		stage.add(WAKE);
		stage.add(WAKE);
		
		
		double[] poly;
			if (stage.size() < 65) {
				poly = cuvre_fitting(stage, 5);
			} else {
				poly = cuvre_fitting(stage, 15);
			}
			double  []poly_tmp=new  double[poly.length-10];
			System.arraycopy(poly, 5, poly_tmp, 0, poly_tmp.length);
		double max_poly=ArrayUtil.max(poly_tmp, poly_tmp.length);
		double min_poly=ArrayUtil.min(poly_tmp, poly_tmp.length);
		
			
			for(int k=5;k<poly.length-5;k++)
//			if (poly[k] < 3)
				sleep_stage.add((poly[k]-min_poly)/(max_poly-min_poly)*3);
//			else if (poly[k] > 3)
//				sleep_stage.add(3.0);
	}
	
	double[] cuvre_fitting(ArrayList<Double> hrt_avg, int degree) {

		WeightedObservedPoints obs = new WeightedObservedPoints();
		PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
		for (int i = 0; i < hrt_avg.size(); i++) {
			obs.add(i + 1, hrt_avg.get(i));
		}
		double[] coeff = fitter.fit(obs.toList());
		double[] hrt_new = new double[hrt_avg.size()];
		for (int i = 0; i < hrt_avg.size(); i++) {
			hrt_new[i] = 0;
			for (int j = degree; j >= 0; j--)
				hrt_new[i] += coeff[j] * Math.pow(i, j);
		}

		return hrt_new;
	}
	/**smooth  采用指数平滑，间隔采用5*/
	private double [] smooth(ArrayList<Double>data) {
		int  i=0;
		int j=0;
		double ex=0.333;
		int len=data.size();
		double sum=0;
		double [] out=new double[len];
		out[0]=data.get(0);
		for(i=1;i<len;i++){
		out[i]=ex*data.get(i)+(1-ex)*out[i-1];
		}
		return  out;
	}
	
	private  double [] bubbleSort(double array[]) {
        double t = 0;
        for (int i = 0; i < array.length - 1; i++)
            for (int j = 0; j < array.length - 1 - i; j++)
                if (array[j] > array[j + 1]) {
                    t = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = t;
                }
        return array;
    }
	
}
