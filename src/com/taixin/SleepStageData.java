package com.taixin;

import java.util.ArrayList;

public class SleepStageData {
	
	public float mSoberTime; // 清醒时间
	public float mLightSleepTime;// 浅睡时间
	public float mDeepSleepTime; // 深睡时间
	public    ArrayList<Float> mSleepStageList = new ArrayList<Float>();
	
	public SleepStageData() {
		mSoberTime=0;
		mLightSleepTime=0;
		mDeepSleepTime=0;
		mSleepStageList.clear();
	}
	

}
