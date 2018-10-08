package com.taixin;

import java.util.ArrayList;

public class SleepData {
	ArrayList<Integer> mHeartRateList;// 整晚每分钟心率
	ArrayList<Integer> mBreathRateList;
	ArrayList<Integer> mBodyMoveList;
	ArrayList<Float> mSleepStageList;// 整晚睡眠分期数据
	ArrayList<Integer> mHeartErrorList;
	ArrayList<Integer> mBreathSuspendList;

	public float mAllSleepTime; // 总睡眠时长，格式为7.5小时.
	public float mSoberTime; // 清醒时间.
	public float mLightSleepTime; // 浅睡时间.
	public float mDeepSleepTime; // 深睡时间.
	public int mHeartRate; // 整晚平均心率
	public int mDeepRate;//深睡平均心率
	public int mLightRate;//浅睡平均心率
	public int mBreathRate; // 整晚平均呼吸率
	public int mBodyMoveTimesAll; // 整晚总体动次数.
	public int mHeartErrorTimes; // 心脏异动次数
	public ArrayList<ExceptionData>mHeartErrorDataList;
	public int mBreathSuspendtimes; // 呼吸暂停次数.
	public ArrayList<ExceptionData>mBreathSuspendDataList;
	public String mBeginDateTime; // 开始时间
	public String mEndDateTime; // 结束时间

	public SleepData() {
		mHeartRateList = new ArrayList<Integer>();
		mBreathRateList = new ArrayList<Integer>();
		mBodyMoveList = new ArrayList<Integer>();
		mSleepStageList = new ArrayList<Float>();
		mHeartErrorList = new ArrayList<Integer>();
		mBreathSuspendList = new ArrayList<Integer>();
		mHeartErrorDataList=new ArrayList<ExceptionData>();
		mBreathSuspendDataList=new ArrayList<ExceptionData>();
	}

	// 往后台保存时用，转为byte[]再转为String，之后往后台保存
	public String mSaveSleepStageString;
	public String mSaveHeartRateString;
	public String mSaveBreathRateString;
	public String mSaveBodymoveString;
	public String mSaveHeartErrorString;
	public String mSaveBreathSuspendString;

	public void clearData() {

		mHeartRateList.clear();
		mBreathRateList.clear();
		mBodyMoveList.clear();
		mSleepStageList.clear();
		mHeartErrorList.clear();
		mBreathSuspendList.clear();
		mHeartErrorDataList.clear();
		mBreathSuspendDataList.clear();

		mAllSleepTime = 0; // 总睡眠时长，格式为7.5小时.
		mSoberTime = 0; // 清醒时间.
		mLightSleepTime = 0; // 浅睡时间.
		mDeepSleepTime = 0; // 深睡时间.
		mHeartRate = 0; // 整晚平均心率
		mBreathRate = 0; // 整晚平均呼吸率
		mBodyMoveTimesAll = 0; // 整晚总体动次数.
		mHeartErrorTimes = 0; // 心脏异动次数
		mBreathSuspendtimes = 0; // 呼吸暂停次数.
		mBeginDateTime = null; // 开始时间
		mEndDateTime = null; // 结束时间

		mSaveSleepStageString = null;
		mSaveHeartRateString = null;
		mSaveBreathRateString = null;
		mSaveBodymoveString = null;
		mSaveHeartErrorString = null;
		mSaveBreathSuspendString = null;

	}

}
