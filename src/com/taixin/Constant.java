
package com.taixin;

/**
 * 全局变量及配置等
 * 
 * @author prf
 *
 */
public final class Constant {
	/** 采样频率 呼吸/心跳/体动的采样频率一样 */
	public final static int DOT_SEC =50;

	/** 每个小窗的原始数据个数 1分钟 */
	public final static int SOURCE_DATA_NUM = 1 * 60 * DOT_SEC;
	
	public final static int N=3*DOT_SEC;

}
