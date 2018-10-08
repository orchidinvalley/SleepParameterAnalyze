package com.taixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import  com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWClassID;    
import com.mathworks.toolbox.javabuilder.MWComplexity;    
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import pictureplot.*;

//import org.apache.commons.math3.analysis.solvers.NewtonRaphsonSolver;

import com.taixin.Utils;

public class SleepDispose {
	int[] dataPool = new int[Constant.DOT_SEC * 60];
	int curCnt = 0;
	SleepData mSleepData = new SleepData();
	// BodyMoveAlgorithm mBodyMoveAlgorithm=new BodyMoveAlgorithm();
	// BreathAlgorithm mBreathAlgorithm=new BreathAlgorithm();
	// HeartAlgorithm mHeartAlgorithm=new HeartAlgorithm();
	SleepParameter mSleepParameter = new SleepParameter();
	ArrayList<Integer> mHeartNumberList = new ArrayList<Integer>();
	ArrayList<Integer> mBreathNumberList = new ArrayList<Integer>();
	ArrayList<Integer> mBodyMoveList = new ArrayList<Integer>();
	int mOsahsNum = 0; // 呼吸暂停次数
	int mHeartErrorNum = 0; // 心率异常次数
	ArrayList<ExceptionData>mHeartErrorDataList=new ArrayList<ExceptionData>();
	ArrayList<ExceptionData>mBreathSuspendDataList=new ArrayList<ExceptionData>();
	ArrayList<Integer> mHeartErrorList = new ArrayList<Integer>();
	ArrayList<Integer> mBreathSuspendList = new ArrayList<Integer>();

	ArrayList<Integer> mBodyMv = new ArrayList<Integer>();
	ArrayList<Integer> mWakeUp = new ArrayList<Integer>();
	ArrayList<Integer> mSleepLv = new ArrayList<Integer>();
	public static  ArrayList<Integer>idList=new ArrayList<Integer>();

	final int CNT_OF_DATA_ONE_LINE = 8;

	public static void main(String[] args) {
		String timePath = "D:\\project\\SleepAlgorithm_50hz\\src\\com\\taixin\\SleepTime.txt";
		String dataPath1 = "D:\\project\\SleepAlgorithm_50hz\\src\\com\\taixin\\sleep_11.txt";
		String dataPath2 = "D:\\project\\SleepAlgorithm_50hz\\src\\com\\taixin\\sleep (7).txt";
		Thread mThread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SleepDispose mDispose1 = new SleepDispose();
				mDispose1.sleepDispose(timePath, dataPath1);
			}
		});
		Thread mThread2 = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SleepDispose mDispose2 = new SleepDispose();
				mDispose2.sleepDispose(timePath, dataPath2);
			}
		});
		mThread1.start();
		// mThread2.start();
	}

	/**
	 * 
	 * @param timeFilePath
	 *            :用于传输存储起止时间的文件路径
	 * @param dataFilePath
	 *            :用于传输存储原始的文件路径
	 */
	void sleepDispose(String timeFilePath, String dataFilePath) {

		File timeFile = new File(timeFilePath);
		File dataFile = new File(dataFilePath);
		if (!timeFile.exists()) {
			System.out.println("文件不存在，请检查输入参数");
			return;
		}
		mSleepData.clearData();
		
		try {
			FileInputStream timeInput = new FileInputStream(timeFile);
			BufferedReader timeReader = new BufferedReader(new InputStreamReader(timeInput));
			String line;
			if ((line = timeReader.readLine()) != null) {
				String[] tmpStr = line.split("[|]");
				mSleepData.mBeginDateTime = tmpStr[0];
				mSleepData.mEndDateTime = tmpStr[1];
			}

			FileInputStream dataInput = new FileInputStream(dataFile);
			BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataInput));
			String data;
			while ((data = dataReader.readLine()) != null) {
				String[] dataList = data.split(" ");
				int[] tmp = new int[CNT_OF_DATA_ONE_LINE];
				if (dataList.length == CNT_OF_DATA_ONE_LINE)
					for (int i = 0; i < CNT_OF_DATA_ONE_LINE; i++)
						tmp[i] = Integer.parseInt(dataList[i]);
				System.arraycopy(dataPool, CNT_OF_DATA_ONE_LINE, dataPool, 0, dataPool.length - CNT_OF_DATA_ONE_LINE);
				System.arraycopy(tmp, 0, dataPool, dataPool.length - CNT_OF_DATA_ONE_LINE, CNT_OF_DATA_ONE_LINE);
				curCnt = curCnt + CNT_OF_DATA_ONE_LINE;
				if (curCnt == 3000) {
					curCnt = 0;

					mSleepParameter.dataOfMinuteDispose(dataPool, dataPool.length);

					mHeartNumberList.add(mSleepParameter.getHeartNumber());
					mSleepLv.add(mSleepParameter.getSleep_lv());
					mBreathNumberList.add(mSleepParameter.getBreathNumber());
					mBodyMoveList.add(mSleepParameter.getBodyMoveNum());
					mOsahsNum += mSleepParameter.getOsahsNum();
					if(mSleepParameter.getOsahsNum()!=0) {
						ExceptionData mData=new ExceptionData();
						for(int k=0; k<dataPool.length;k++)
							mData.data.add(Integer.toString(dataPool[k]));
						mBreathSuspendDataList.add(mData);
						mData=null;
					}
						
					mHeartErrorNum += mSleepParameter.getHeartErrorNumber();
					if(mSleepParameter.getHeartErrorNumber()!=0) {
						ExceptionData mData=new ExceptionData();
						for(int k=0; k<dataPool.length;k++)
							mData.data.add(Integer.toString(dataPool[k]));
						mHeartErrorDataList.add(mData);
						mData=null;
					}
					mHeartErrorList.add(mSleepParameter.getHeartErrorNumber());
					mBreathSuspendList.add(mSleepParameter.getOsahsNum());
					mBodyMv.add(mSleepParameter.getBodyMoveNum());
					mWakeUp.add(mSleepParameter.getWake());

				}
			}
			allSleepDataParse();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void allSleepDataParse() {
		
		/////////////////////////
//		MWNumericArray  mid=null;
//		MWNumericArray  msleep=null;
//		MWNumericArray  mmove=null;
//		MWNumericArray  mheart=null;
//		MWNumericArray  mbreath=null;
//		
//		try {
//			int []dims= {1,idList.size()};
//			mid=MWNumericArray.newInstance(dims, MWClassID.DOUBLE,  MWComplexity.REAL);			
//			for(int i=1; i<=idList.size();i++)
//				mid.set(i,idList.get(i-1));
//			
//			dims[1]=mSleepLv.size();
//			msleep=MWNumericArray.newInstance(dims, MWClassID.DOUBLE,  MWComplexity.REAL);			
//			for(int i=1; i<=mSleepLv.size();i++)
//				msleep.set(i,mSleepLv.get(i-1));
//			
//			dims[1]=mBodyMoveList.size();
//			mmove=MWNumericArray.newInstance(dims, MWClassID.DOUBLE,  MWComplexity.REAL);			
//			for(int i=1; i<=mBodyMoveList.size();i++)
//				mmove.set(i,mBodyMoveList.get(i-1));
//			
//			dims[1]=mHeartNumberList.size();
//			mheart=MWNumericArray.newInstance(dims, MWClassID.DOUBLE,  MWComplexity.REAL);			
//			for(int i=1; i<=mHeartNumberList.size();i++)
//				mheart.set(i,mHeartNumberList.get(i-1));
//			
//			dims[1]=mBreathNumberList.size();
//			mbreath=MWNumericArray.newInstance(dims, MWClassID.DOUBLE,  MWComplexity.REAL);			
//			for(int i=1; i<=mBreathNumberList.size();i++)
//				mbreath.set(i,mBreathNumberList.get(i-1));
//			
//			Class1 plot=new Class1();
//			plot.pictureplot(mid,msleep,mmove,mheart,mbreath);
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
		///////////////////////////////
		int body = 0;
		for (int i = 0; i < mBodyMoveList.size(); i++)
			body += mBodyMoveList.get(i);
		System.out.println("bodyMoveNum=" + body);

//		 if ((mSleepData.mBeginDateTime != null) && (mSleepData.mEndDateTime != null))
//		 mSleepData.mAllSleepTime = TimeUtil.dateDifference(mSleepData.mBeginDateTime,
//		 mSleepData.mEndDateTime);// 计算睡眠总时间
//		 else
//		 mSleepData.mAllSleepTime = 0.0f;
		int lv[] = new int[mSleepLv.size()];
		/*** 离床的预处理 **/
		if (mSleepLv.size() > 0) {

			for (int i = 0; i < mSleepLv.size(); i++)
				lv[i] = mSleepLv.get(i);
		}

		//////////// 整晚心跳预处理+平均心率
		if (mHeartNumberList.size() > 0) {
			int[] heart = new int[mHeartNumberList.size()];
			for (int i = 0; i < mHeartNumberList.size(); i++)
				heart[i] = mHeartNumberList.get(i);

			if (lv[0] != 1 && heart[0] == 0)
				heart[0] = 75;
			else if (lv[0] == 1)
				heart[0] = 0;

			for (int i = 1; i < mHeartNumberList.size() - 1; i++)// 如果前面1分钟和后1分钟的心跳为0，则中间这1分钟的心跳也应为0
			{
				if (lv[i] != 1 && heart[i] == 0)
					heart[i] = heart[i - 1];
				else if (lv[i] == 1)
					heart[i] = 0;
			}
			mSleepData.mHeartRateList.clear();
			mHeartNumberList.clear();
			System.out.println("预处理mHeartNumberList ");
			int hrtCnt = 0;
			int heartSum = 0;
			for (int i = 0; i < heart.length; i++) {
				if (heart[i] <= 0)
					hrtCnt++;
				else
					heartSum += heart[i];
				mSleepData.mHeartRateList.add(heart[i]);
				mHeartNumberList.add(heart[i]);
			}
			System.out.println("mHeartRateList size=" + mSleepData.mHeartRateList.size());
			if (heart.length == hrtCnt)
				mSleepData.mHeartRate = 0;
			else
				mSleepData.mHeartRate = heartSum / (heart.length - hrtCnt);

		} else
			mSleepData.mHeartRate = 0;

		////////////////////// 整晚呼吸 预处理 +平均呼吸率

		if (mBreathNumberList.size() > 0) {

			int[] breath = new int[mBreathNumberList.size()];
			for (int i = 0; i < mBreathNumberList.size(); i++)
				breath[i] = mBreathNumberList.get(i);
			if (lv[0] != 1 && breath[0] == 0)
				breath[0] = 18;
			for (int i = 1; i < breath.length - 1; i++)// 如果前面1分钟和后1分钟的心跳为0，则中间这1分钟的心跳也应为0
			{
				// if (breath[i - 1] == 0 && breath[i + 1] == 0)
				// breath[i] = 0;
				if (lv[i] != 1 && breath[i] == 0)
					breath[i] = breath[i - 1];
				else if (lv[i] == 1)
					breath[i] = 0;
			}
			mSleepData.mBreathRateList.clear();
			int brtCnt = 0;
			int breathSum = 0;
			for (int i = 0; i < breath.length; i++) {
				if (breath[i] <= 0)
					brtCnt++;
				else
					breathSum += breath[i];
				mSleepData.mBreathRateList.add(breath[i]);
			}
			if (breath.length == brtCnt)
				mSleepData.mBreathRate = 0;
			else
				mSleepData.mBreathRate = breathSum / (breath.length - brtCnt);

		} else
			mSleepData.mBreathRate = 0;

		// 整晚总体动次数
		int bmvErrCnt = 0;// 体动异常（>3）的次数，计算睡眠等级时用
		int bodymoveSum = 0;
		if (mBodyMoveList.size() > 0) {
			for (Integer bodymove : mBodyMoveList) {
				bodymoveSum += bodymove;
				if (bodymove > 3)
					bmvErrCnt++;
			}
			mSleepData.mBodyMoveTimesAll = bodymoveSum;

			mSleepData.mBodyMoveList = (ArrayList<Integer>) mBodyMoveList.clone();// 每分钟一个小窗
		} else {
			mSleepData.mBodyMoveTimesAll = 0;
		}

		// 调用睡眠分期，每日8:00:00到20:00:00之间的睡眠数据不做睡眠分期的分析，也不绘制睡眠分期的波形图
		if (mSleepData.mBeginDateTime == null) {
			mSleepData.mSoberTime = 0.0f;
			mSleepData.mLightSleepTime = 0.0f;
			mSleepData.mDeepSleepTime = 0.0f;
		} else {

			 SleepStageNew mNew= new SleepStageNew();
			 SleepStageData mSleepStageData=mNew.sleepstage(mHeartNumberList, mBodyMv,
			 mWakeUp, mSleepLv);

			mSleepData.mSoberTime = mSleepStageData.mSoberTime;
			mSleepData.mLightSleepTime = mSleepStageData.mLightSleepTime;
			mSleepData.mDeepSleepTime = mSleepStageData.mDeepSleepTime;

			// 整晚睡眠分期分析：清醒、浅睡、深睡绘图
			mSleepData.mSleepStageList = (ArrayList<Float>) mSleepStageData.mSleepStageList.clone();
			int deepCnt= 0;
			int sumDeep = 0;
			int lightCnt=0;
			int sumLight=0;
			int i=0;
			for(float item:mSleepData.mSleepStageList) {
				if(item<=1 && mSleepData.mHeartRateList.get(i)>0) {
					sumDeep+=mSleepData.mHeartRateList.get(i);
					deepCnt++;
				}else if(item >1&& item<3 && mSleepData.mHeartRateList.get(i)>0 ) {
					sumLight+=mSleepData.mHeartRateList.get(i);
					lightCnt++;
				}
				i++;
			}
			if(deepCnt!=0 && sumDeep!=0) {
				mSleepData.mDeepRate=sumDeep/deepCnt;
			}else
				mSleepData.mDeepRate=0;
			if(lightCnt!=0 &&sumLight!=0) {
				mSleepData.mLightRate=sumLight/lightCnt;
			}else
				mSleepData.mLightRate=0;
			
			System.out.println("mDeepRate="+mSleepData.mDeepRate);
			System.out.println("mLightRate="+mSleepData.mLightRate);
			// }
		}

		// 呼吸暂停次数
		mSleepData.mBreathSuspendtimes = mOsahsNum;
		mSleepData.mBreathSuspendList = (ArrayList<Integer>) mBreathSuspendList.clone();
		// 心率异常次数
		mSleepData.mHeartErrorTimes = mHeartErrorNum;
		mSleepData.mHeartErrorList = (ArrayList<Integer>) mHeartErrorList.clone();

		// 处理要往后台保存的睡眠分期、心率、呼吸、体动数据
		if(mHeartErrorDataList.size()>0)
			mSleepData.mHeartErrorDataList=(ArrayList<ExceptionData>) mHeartErrorDataList.clone();
		if(mBreathSuspendDataList.size()>0)
			mSleepData.mBreathSuspendDataList=(ArrayList<ExceptionData>) mBreathSuspendDataList.clone();
		
		mSleepData.mSaveSleepStageString = saveSleepStageList(mSleepData.mSleepStageList);
		mSleepData.mSaveHeartRateString = Utils.arrayListToString(mSleepData.mHeartRateList);
		// System.out.println(mSleepData.mSaveHeartRateString);
		mSleepData.mSaveBreathRateString = Utils.arrayListToString(mSleepData.mBreathRateList);
		mSleepData.mSaveBodymoveString = Utils.arrayListToString(mSleepData.mBodyMoveList);
		mSleepData.mSaveHeartErrorString = Utils.arrayListToString(mHeartErrorList);
		mSleepData.mSaveBreathSuspendString = Utils.arrayListToString(mBreathSuspendList);

	}

	/** 整晚睡眠分期数据的保存，一分钟一个数据，float类型，用三个字节表示，第一个字节是负号1为负，第二与第三个字节表示数据绝对值*10000 */
	private String saveSleepStageList(ArrayList<Float> bodymoveList) {
		String sleepStageString = null;
		byte[] sleepStageBytes = new byte[bodymoveList.size() * 3];
		int i = 0;

		for (Float bodymove : bodymoveList) {
			if (bodymove < 0)
				sleepStageBytes[i++] = 0x01;
			else
				sleepStageBytes[i++] = 0x00;

			int bodymoveInt = (int) (Math.abs(bodymove) * 10000);
			sleepStageBytes[i++] = (byte) (bodymoveInt & 0xff);
			sleepStageBytes[i++] = (byte) ((bodymoveInt >> 8) & 0xff);
		}

		sleepStageString = Utils.byteArrayToHexString(sleepStageBytes);

		return sleepStageString;
	}

	
}
