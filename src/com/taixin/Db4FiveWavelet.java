package com.taixin;
import java.util.ArrayList;

public class Db4FiveWavelet {

	double[] Hi_D = { -0.230377813308855, 0.714846570552542, -0.630880767929590, -0.027983769416984, 0.187034811718881,
			0.030841381835987, -0.032883011666983, -0.010597401784997 };
	double[] Lo_D = { -0.010597401784997, 0.032883011666983, 0.030841381835987, -0.187034811718881, -0.027983769416984,
			0.630880767929590, 0.714846570552542, 0.230377813308855 };

	double[] Hi_R = { -0.010597401784997, -0.032883011666983, 0.030841381835987, 0.187034811718881, -0.027983769416984,
			-0.630880767929590, 0.714846570552542, -0.230377813308855 };
	double[] Lo_R = { 0.230377813308855, 0.714846570552542, 0.630880767929590, -0.027983769416984, -0.187034811718881,
			0.030841381835987, 0.032883011666983, -0.010597401784997 };

	ArrayList<Double> aArrayList = new ArrayList<Double>();
	ArrayList<Double> dArrayList = new ArrayList<Double>();
	ArrayList<Double> d5ArrayList = new ArrayList<Double>();
	ArrayList<Integer>len=new ArrayList<Integer>();
	
	protected double [] waveletTransform(double[] x, int n) {
		waveletDecompose(x, n);
		double []y=waveletCompound(n);
		return y;
	}

	private void waveletDecompose(double[] x, int n) {
		if (x.length == 0)
			return;
		// int s=x.length;
		// double []l= {0,0,0,0,0,s};
		for (int i=0;i<n+2;i++)
			len.add(0);

		for (int i = 0; i < x.length; i++)
			aArrayList.add(x[i]);

		len.set(n+1,aArrayList.size());
		
		for (int k = 0; k < n; k++) {
			dwt(aArrayList);
//			System.out.println("dArrayList first=" + dArrayList.get(0));
//			System.out.println("dArrayList last=" + dArrayList.get(dArrayList.size() - 1));
			len.set(n-k,dArrayList.size());
			if(k==0)
				d5ArrayList=(ArrayList<Double>) dArrayList.clone();
		}
		len.set(0,aArrayList.size());
	}

	private double[] waveletCompound(int n) {

//		for (int i = 0; i < dArrayList.size() - 1; i++) {
//			d5ArrayList.add(dArrayList.get(i));
//			d5ArrayList.add(0.0);
//		}
//		d5ArrayList.add(dArrayList.get(dArrayList.size() - 1));
//
		double[] y = new double[d5ArrayList.size()];
		
		for (int i = 0; i < d5ArrayList.size(); i++)
			y[i] = d5ArrayList.get(i);
//		for(int i =0;i<y.length;i++)
//			System.out.println(y[i]);
		double delta=selectMid(y);
		delta=delta/0.6745;
		double limd=delta*Math.sqrt(Math.log(y.length)*2);
		for (int i = 0; i < y.length; i++)
			if(y[i]<limd)
				y[i]=0;
//		
		
//		int s=d5ArrayList.size()*2-Hi_R.length+2;
		
		int rmx=len.size();
		int imin=rmx-n;
		
		double[] y2 = upsconv(y, Hi_R,len.get(imin+4));
//		for (int i = 0; i < y2.length; i++)
//			System.out.println(y2[i]);
		
//		double []y3=upsconv(y2, Lo_R, len.get(imin+3));
//		double []y4=upsconv(y3, Lo_R, len.get(imin+2));
//		double []y5=upsconv(y4, Lo_R, len.get(imin+1));
//		double []y6=upsconv(y5, Lo_R, len.get(imin));
		
		return y2;
	}

	private void dwt(ArrayList<Double> y) {
		int lf = Lo_D.length - 1;
		int lx = y.size();

		int[] I = new int[lf + lx + lf];

		for (int i = 0; i < I.length; i++) {
			if (i < lf)
				I[i] = lf - i - 1;
			else if (i >= lf && i < (lf + lx))
				I[i] = i - lf;
			else
				I[i] = lx + lx + lf - 1 - i;
		}

		double[] yTmp = new double[lf + lx + lf];
		for (int i = 0; i < yTmp.length; i++)
			yTmp[i] = y.get(I[i]);
		aArrayList.clear();
		dArrayList.clear();
		double[] a = conv(yTmp, Lo_D, 2);
		for (int i = 1; i < a.length; i = i + 2)
			aArrayList.add(a[i]);
		double d[] = conv(yTmp, Hi_D, 2);
		for (int i = 1; i < d.length; i = i + 2)
			dArrayList.add(d[i]);

	}

	private double[] conv(double[] f, double[] g, int shape) {// shape 1 full 2 valid
		
		int n = f.length;
		int adjust=g.length-1;

		if (shape == 1) {
			double[] result = new double[f.length + g.length - 1];
			for (int i = 0; i < result.length; i++)
				result[i] = 0;
			
			for (int i = 0; i < result.length ; i++) {
				for (int m = 0; m < n; m++) {
					if ((i - m) >= 0 && (i - m) < g.length) {
						result[i ] += f[m] * g[i - m];
					}
				}
			}
			return result;
		} else if (shape == 2) {
			double[] result = new double[f.length - g.length + 1];
			for (int i = 0; i < result.length; i++)
				result[i] = 0;			
			for (int i = adjust; i < result.length + adjust; i++) {
				for (int m = 0; m < n; m++) {
					if ((i - m) >= 0 && (i - m) < g.length) {
						result[i - adjust] += f[m] * g[i - m];
					}
				}
			}
			return result;
		}else 
			return null;

		
	}
	
	private double [] upsconv(double []x,double []f,int s) {
//		double[] y2 = conv(y, Hi_R,1);
		double []y=new double[x.length*2-1];

		for(int i=0;i<x.length-1;i++) {
			y[i*2]=x[i];
			y[i*2+1]=0;
		}
		
		y[y.length-1]=x[x.length-1];
		double[]y2=conv(y, f, 1);
		
		int sx=y2.length;
		double  d=(sx-s)/2.0;
		int first=(int) (Math.floor(d));
		int last=(int) (sx-Math.ceil(d));
		
		double []y3=new double[last-first];
		for(int i=first;i<last;i++)
			y3[i-first]=y2[i];
		
		return y3;
	}

	// 选出中位数（比一半的元素小，比另一半的大）
		 private double selectMid(double[] a) {
//	        return selectKthMin(a, a.length / 2);
	    	double []x=new double[a.length];
			System.arraycopy(a, 0, x,0, a.length);
	    	for(int i=0;i<x.length-1;i++){//外层循环控制排序趟数
	    		for(int j=0;j<x.length-1-i;j++){//内层循环控制每一趟排序多少次
	    			if(x[j]>x[j+1]){
	    				double  temp=x[j];
	    				x[j]=x[j+1];
	    				x[j+1]=temp;
	    			}
	    		}
	    	} 
	    	
	    	if(x.length%2!=0) {
	    		 int half = x.length/2;
	    		 return x[half];
	    	}else
	    		return 0.0;
	    		
	    }
	
}
