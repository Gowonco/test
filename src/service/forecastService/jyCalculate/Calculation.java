package service.forecastService.jyCalculate;

import java.util.HashMap;
import java.util.Map;

public class Calculation {

	//double[] jkl=new double[1];
	//try {
	private	double[][] ppa;
	private double[] pp;
	private double[] rr;
	private double[] ww;
	private double[] wwd;
	private double[] wwdc;
	
	public Calculation(double[][] ppa) {
		this.ppa = ppa;
	}
	
	public void fenPian(double[][] ppa) {
		/*输入数据初始土壤湿度和各块累计雨量*/
		pp = new double[16];
		rr = new double[16];
		/*计算产流深*/
		//分块1王家坝以上干流及淮南
		
		if (ppa[0][0] == 1) {
			pp[0] = ppa[0][1] + ppa[0][2];
		}
		if (ppa[0][2] > 0) {
			if (pp[0] <= 75) {
				rr[0] = 0.01 * Math.pow(pp[0], 1.69);
			} else if (pp[0] > 75 && pp[0] < 150) {
				rr[0] = 0.0006 * Math.pow(pp[0], 2.327);
			} else if (pp[0]>= 150) {
				rr[0] = pp[0] - 80;
			}
		}
		//分块2灌河
		if (ppa[1][0] == 2) {
			pp[1] = ppa[1][1] + ppa[1][2];
		}
		if (ppa[1][2] > 0) {
			if (pp[1] < 31) {
				rr[1] = 0;
			} else if (pp[1] >= 31) {
				rr[1] = 0.765 * (pp[1] - 31);
			}
		}
		//分块3史灌河
		if (ppa[2][0] == 3) {
			pp[2] = ppa[2][1] + ppa[2][2];
		}
		if (ppa[2][2] > 0) {
			if (pp[2]<= 100) {
				rr[2] = 0.00013 * Math.pow(pp[2], 2.7);
			} else if (pp[2] > 100 && pp[2] <= 200) {
				rr[2] = 0.0125 * Math.pow(pp[2], 1.708);
			} else if (pp[2] > 200) {
				rr[2] = pp[2] - 94;
			}
		}
		//分块4王家坝-蚌埠(淮河干流)
		if (ppa[3][0] == 4) {
			pp[3] = ppa[3][1] + ppa[3][2];
		}
		if (ppa[3][2] > 0) {
			if (pp[3] < 200) {
				rr[3] = 0.0013 * Math.pow(pp[3], 2.129);
			} else if (pp[3] >= 200) {
				rr[3] = pp[3] - 100;
			}
		}
		//分块5霍邱、安丰塘
		if (ppa[4][0] == 5) {
			pp[4] = ppa[4][1] + ppa[4][2];
		}
		if (ppa[4][2] > 0) {
			if (pp[4] < 200) {
				rr[4] = 0.0038 * Math.pow(pp[4], 1.935);
			} else if (pp[4]>= 200) {
				rr[4] = pp[4] - 90;
			}
		}
		//分块6淠河
		if (ppa[5][0] == 6) {
			pp[5] = ppa[5][1] + ppa[5][2];
		}
		if (ppa[5][2] > 0) {
			if (pp[5] < 150) {
				rr[5] = 0.00205 * Math.pow(pp[5], 2.113);
			} else if (pp[5] >= 150) {
				rr[5] = pp[5] - 67;
			}
		}
		//分块7淠河
		if (ppa[6][0] == 7) {
			pp[6] = ppa[6][1] + ppa[6][2];
		}
		if (ppa[6][2] > 0) {
			if (pp[6] < 150) {
				rr[6] = 0.00487 * Math.pow(pp[6], 1.978);
			} else if (pp[6]>= 150) {
				rr[6] = pp[6] - 57;
			}
		}
		//分块8洪汝河
		if (ppa[7][0] == 8) {
			pp[7] = ppa[7][1] + ppa[7][2];
		}
		if (ppa[7][2] > 0) {
			rr[7] = 0.0267 * Math.pow(pp[7], 1.513);
		}
		//分块9沙颍河上中游
		if (ppa[8][0] == 9) {
			pp[8] = ppa[8][1] + ppa[8][2];
		}
		if (ppa[8][2] > 0) {
			rr[8] = 0.0431 * Math.pow(pp[8], 1.411);
		}
		
		//分块10颍河（周口-阜阳闸）
		if (ppa[9][0] == 10) {
			pp[9] = ppa[9][1] + ppa[9][2];
		}
		if (ppa[9][2] > 0) {
			if (pp[9] < 100) {
				rr[9] = 0.00309 * Math.pow(pp[9], 1.776);
			} else if (pp[9] >= 100) {
				rr[9] = 0.000153 * Math.pow(pp[9], 2.396);
			}
		}
		//分块11涡河（毫县-蒙城闸）
		if (ppa[10][0] == 11) {
			pp[10] = ppa[10][1] + ppa[10][2];
		}
		if (ppa[10][2] > 0) {
			if (pp[10] < 92) {
				rr[10] = 0;
			} else if (pp[10] >= 92) {
				rr[10] = 0.383 * Math.pow(pp[10] - 92, 1.16) + 10;
			}
		}
		/*原程序计算有误，需要与手册对应*/
		//分块12洪泽湖区间淮南
		if (ppa[11][0] == 12) {
			pp[11] = ppa[11][1] + ppa[11][2];
		}
		if (ppa[11][2] > 0) {
			if (pp[11]< 130) {
				rr[11] = 0.0438 * Math.pow(pp[11] - 35, 1.539) + 4;
			} else if (pp[12] >= 130) {
				rr[11] = pp[11] - 75;
			}
		}
		
		//分块13洪泽湖区间淮北1
		if (ppa[12][0] == 13) {
			pp[12] = ppa[12][1] + ppa[12][2];
		}
		if (ppa[12][2] > 0) {
			if (pp[12] <= 205) {
				rr[12] = 0.0007 * Math.pow(pp[12], 2.23);
			} else if (pp[12]>= 205) {
				rr[12] = pp[12] - 105;
			}
		}
		//分块14洪泽湖区间淮北2
		if (ppa[13][0] == 14) {
			pp[13] = ppa[13][1] + ppa[13][2];
		}
		if (ppa[13][2] > 0) {
			if (pp[13] < 215) {
				rr[13] = 0.000348 * Math.pow(pp[13], 2.34);
			} else if (pp[13] >= 215) {
				rr[13] = pp[13] - 113;
			}
		}
		//分块15洪泽湖区间淮北3
		if (ppa[14][0] == 15) {
			pp[14] = ppa[14][1] + ppa[14][2];
		}
		if (ppa[14][2] > 0) {
			if (pp[14] < 225) {
				rr[14] = 0.000123 * Math.pow(pp[14], 2.52);
			} else if (pp[14] >= 225) {
				rr[14] = pp[14] - 123;
			}
		}
		//分块16洪泽湖湖面
		if (ppa[15][0] == 16) {
			rr[15] = ppa[15][2];
		}
			
			/*int i=ppa.length;
			int j=ppa[0].length;
			int m;
			double[] jkl=new double[i];
			for(m=0;m<i;m++){
				jkl[m]=ppa[m][j-1];
				
			}
		//}catch (Exception e) {
			//e.printStackTrace();
		//}
		*/
	}
	
	/*计算产水量*/
	public void ChanShuiLiang() {
		/*输入经验模型characta表16块面积*/
		double[] aa={13700,2570,2150,8340,3970,3230,2640,18220,26860,10040,20440,8640,5030,9850,12340,2100};
		ww = new double[16];
		for (int i = 0; i <= 15; i++) {
			ww[i] = rr[i] * aa[i]/ Math.pow(10, 5);
		}
	}
	
	/*计算每个断面的产水量*/
	public void dmChanShuiLiang() {
		wwd = new double[5];
		/*计算蚌埠产流总量*/
		wwd[0] = 0;
		for (int i = 0; i <= 10; i++) {
			wwd[0] = wwd[0] + ww[i];
		}
		/*计算淮北区间*/
		wwd[1] = 0;
		for (int i = 12; i <= 14; i++) {
			wwd[1] = wwd[1] + ww[i];
		}
		/*计算淮南区间*/
		wwd[2] = 0;
		wwd[2] = wwd[2] + ww[11];
		/*计算湖区*/
		wwd[3] = ww[15];
		/*计算洪泽湖总水量*/
		wwd[4] = 0;
		for (int i = 0; i < 4; i++) {
			wwd[4] = wwd[4] + wwd[i];
		}
	}
	
	/*修正每个断面的产流量*/
	public void dmxChanShuiLiang() {
		/*计算蚌埠修正产流总量*/
		wwdc = new double[5];
		wwdc[0] = 0;
		for (int i = 0; i <= 10; i++) {
			wwdc[0] = wwdc[0] + ww[i];
		}
		/*计算修正淮北区间*/
		wwdc[1] = ww[1];
		/*计算修正淮南区间*/
		wwdc[2] = ww[2];
		/*计算修正湖区*/
		wwdc[3] = ww[3];
		/*计算修正洪泽湖总水量*/
		wwdc[4] = 0;
		for (int i = 0; i < 4; i++) {
			wwdc[4] = wwdc[4] + wwd[i];//原程序求和时按照原的总水量进行计算
		}
	}
	
	/*输出径流深、分块产水量，断面产水量*/
	public Map<String, Object> outputChanliu() {
		fenPian(ppa);
		ChanShuiLiang();
		dmChanShuiLiang();
		dmxChanShuiLiang();
//保留两位小数
		
		for (int i = 0; i < rr.length; i++) {
			rr[i] = (double) (Math.round(rr[i] * 100)) / 100;
		}
		for (int i = 0; i < ww.length; i++) {
			ww[i] = (double) (Math.round(ww[i] * 100)) / 100;
		}
		for (int i = 0; i < wwd.length; i++) {
			wwd[i] = (double) (Math.round(wwd[i] * 100)) / 100;
		}
		for (int i = 0; i < wwdc.length; i++) {
			wwdc[i] = (double) (Math.round(wwdc[i] * 100)) / 100;
		}
		Map<String, Object> chanLiu = new HashMap<>();
		chanLiu.put("分块径流深", rr);
		chanLiu.put("分块产水量", ww);
		chanLiu.put("断面产水量", wwd);
		chanLiu.put("修正断面产水量", wwdc);
		return chanLiu;
	}
}

