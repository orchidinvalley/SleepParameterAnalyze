package com.taixin;

import java.util.ArrayList;

public class SleepParameter {
	private int heartNum = 0;
	private int breathNum = 0;
	final static int LEAVE = 10;
	final static int MOVE = 500;
	final static int OSAHS_UP = 150;
	final static int OSAHS_DWON = 50;
	private int sleep_lv = 1;
	/** 当前分钟内是否多次翻身，设定持续5S体动为一次翻身，大于3次以上的翻身动作，设定该分钟全部为体动 */
	private int sleep_mv = 0;
	/** 每分钟呼吸暂停的次数 */
	private int osahsNum;
	private int wake;
	/** 每分钟的体动次数 */
	private int bodyMoveNum = 0;
	private int savedLastMove = 0;
	private int cntMove = 9;
	private int testMin = 0;

	/** 每分钟心率异常次数--心率不在50-100之间算异常 */
	private int heartErrorNumber = 0;

	public int getHeartErrorNumber() {
		return heartErrorNumber;
	}

	public int getBodyMoveNum() {
		return bodyMoveNum;
	}

	public int getLastMove() {
		return savedLastMove;
	}

	public int getOsahsNum() {
		return osahsNum;
	}

	public int getHeartNumber() {
		return heartNum;
	}

	public int getBreathNumber() {
		return breathNum;
	}

	public int getSleep_lv() {
		return sleep_lv;
	}

	public int getWake() {
		return wake;
	}

	public void dataOfMinuteDispose(int[] data, int len) {

//		if (testMin == 33) {
//			System.out.println("testMi="+testMin);
//		}
		testMin++;
		int N = Constant.N;
		int[] partData = new int[N];
		int winNum = 0;
		double[] integra = new double[len / N];

		sleep_lv = 0;
		sleep_mv = 0;
		heartNum = 0;
		breathNum = 0;
		wake = 0;
		osahsNum = 0;
		bodyMoveNum = 0;
		savedLastMove = 0;

		for (int i = 0; i < (len - N + 1); i += N) {
			System.arraycopy(data, i, partData, 0, N);
			integra[winNum] = ArrayUtil.std(partData, N);
			winNum++;
		}

		int[] id = new int[winNum];
		ArrayUtil.arraySet(id, 1, winNum);

		int sumMove = 0;
		int sumLeaveContinue = 0;
		int sumLeave=0;
		
		int sumOsahs = 0;

		for (int i = 0; i < winNum; i++) {
			if (integra[i] > MOVE) {
				id[i] = 3;
				sumMove++;
			} else if (integra[i] < LEAVE)
				id[i] = -1;
			else if (integra[i] < OSAHS_UP && integra[i]>OSAHS_DWON)
				id[i] = 1;
			else
				id[i] = 0;
			SleepDispose.idList.add(id[i]);
		}
		for(int i=0;i<id.length;i++) {
			if(id[i]==-1)
				sumLeave++;
		}
		
		for (int i = 0; i < id.length; i++) {
			if (id[i] == -1) {
				sumLeaveContinue++;
				if (sumLeaveContinue > 6)
					break;
			} else
				sumLeaveContinue = 0;
		}
		if (sumLeaveContinue >= 5  ||sumLeave>=10) {
			sleep_lv = 1;
			sleep_mv = 0;
			cntMove=9;
			return;
		}

		ArrayList<Integer> bth_lst = new ArrayList<Integer>();
		if (sumMove > 0) {
			bth_lst.clear();
			for (int i = 0; i < id.length; i++) {
				if (id[i] == 1) {
					if (sumOsahs == 0)
						bth_lst.add(i);
					sumOsahs++;
				} else {
					if(id[i]==3) {
					if (sumOsahs > 3) {
						bth_lst.add(i);
						if(bth_lst.size()>1) {
							int start=bth_lst.get(0);
							int end=bth_lst.get(1);
							ArrayList<Integer> bthData = new ArrayList<Integer>();
							for(int j=start*150;j<end*150;j++)
								bthData.add(data[j]);
							Osahs mOsahs=new Osahs();
							osahsNum+=mOsahs.disposeOsahs(bthData);
							if(osahsNum!=0)
								System.out.println("呼吸暂停="+testMin);
						}
						
					}
					}
					bth_lst.clear();
					sumOsahs = 0;
				}
			}
		}

		ArrayList<Integer> srcData = new ArrayList<Integer>();
		ArrayList<Integer> heartList = new ArrayList<Integer>();
		ArrayList<Integer> breathList = new ArrayList<Integer>();
		for (int i = 0; i < id.length; i++) {
			switch (i) {
			case 0:
				if (id[i] == 3 || id[i] == -1)
					srcData.clear();
				else
					for (int j = 0; j < N; j++)
						srcData.add(data[j]);
				break;
			case 1:
				if (id[i] == 3 || id[i] == -1)
					srcData.clear();
				else
					for (int j = 150; j < 150 + N; j++)
						srcData.add(data[j]);
				break;
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
				if (id[i] == 3 || id[i] == -1) {
					if (srcData.size() > 449) {
						HeartAlgorithm mHeartAlgorithm = new HeartAlgorithm();
						mHeartAlgorithm.heartOfMinuteFilter(srcData);
						heartList.add(mHeartAlgorithm.getHeartNumber());
						BreathAlgorithm mBreathAlgorithm = new BreathAlgorithm();
						mBreathAlgorithm.breathOfMinuteFilter(srcData);
						breathList.add(mBreathAlgorithm.getBreathNumber());
					}
					srcData.clear();
				} else
					for (int j = 150 * i; j < 150 * i + N; j++)
						srcData.add(data[j]);
				break;
			case 19:
				if (id[i] == 3 || id[i] == -1) {
					if (srcData.size() > 449) {
						HeartAlgorithm mHeartAlgorithm = new HeartAlgorithm();
						mHeartAlgorithm.heartOfMinuteFilter(srcData);
						heartList.add(mHeartAlgorithm.getHeartNumber());
						BreathAlgorithm mBreathAlgorithm = new BreathAlgorithm();
						mBreathAlgorithm.breathOfMinuteFilter(srcData);
						breathList.add(mBreathAlgorithm.getBreathNumber());
					}
					srcData.clear();
				} else {
					for (int j = 150 * i; j < 150 * i + N; j++)
						srcData.add(data[j]);
					if (srcData.size() > 449) {
						HeartAlgorithm mHeartAlgorithm = new HeartAlgorithm();
						mHeartAlgorithm.heartOfMinuteFilter(srcData);
						heartList.add(mHeartAlgorithm.getHeartNumber());
						BreathAlgorithm mBreathAlgorithm = new BreathAlgorithm();
						mBreathAlgorithm.breathOfMinuteFilter(srcData);
						breathList.add(mBreathAlgorithm.getBreathNumber());
					}
					srcData.clear();
				}
				break;
			default:
				break;
			}
		}
		int sum = 0;
		if (heartList != null && heartList.size() > 0) {
			for (int i = 0; i < heartList.size(); i++)
				sum = sum + heartList.get(i);
			heartNum = (int) sum / heartList.size();
			// 判断心率异常
			if ((heartNum < 50) || (heartNum > 100))
				heartErrorNumber = 1;
			else
				heartErrorNumber = 0;
		} else {
			heartNum=75;
		}
		sum = 0;
		if (breathList != null && breathList.size() > 0) {
			for (int i = 0; i < breathList.size(); i++)
				sum = sum + breathList.get(i);
			breathNum = (int) sum / breathList.size();
		}

		/** 入睡时间 计算 */
		if (sumMove == 0  ||sumMove == 1) {
			if (cntMove != 0)
				cntMove = cntMove - 1;
			else
				cntMove = 0;
		} else {

			if (cntMove != 9)
				cntMove = cntMove + 1;
			else
				cntMove = 9;

		}
//		 System.out.println("sumMove="+sumMove);
//		 System.out.println("cntMove="+cntMove);
		if (cntMove == 0)
			wake = 1;
		else
			wake = 0;
		/*** 体动次数计算 **/

		int[] bodymv = new int[id.length + 2];
		bodymv[0] = 0;
		bodymv[bodymv.length - 1] = 0;
		System.arraycopy(id, 0, bodymv, 1, id.length);

		ArrayList<Integer> mv_bdy = new ArrayList<Integer>();
		for (int i = 1; i < (bodymv.length - 1); i++) {
			if ((bodymv[i] == 3) && (bodymv[i - 1] < 3)) {
				mv_bdy.add(i);
			}
			if ((bodymv[i] == 3) && (bodymv[i + 1] < 3)) {
				mv_bdy.add(i);
			}
		}

		while ((!mv_bdy.isEmpty()) && (mv_bdy.size() >= 2)) {
			if (mv_bdy.get(1) - mv_bdy.get(0) >= 1)
				bodyMoveNum++;
			mv_bdy.remove(0);
			mv_bdy.remove(0);
		}
		if (bodyMoveNum == 0 && sumMove > 8)
			bodyMoveNum++;
		
//		System.out.println("bodyMoveNum="+bodyMoveNum);
	}

}
