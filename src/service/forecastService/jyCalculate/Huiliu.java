package service.forecastService.jyCalculate;

import util.DateUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Huiliu {
	
	String starttime;
	String middletime;
	String endtime;
	String[] timeseries;
	int glnn;
	int Longtime;
	String initialtime;
	
	//面平均雨量计算
	double[] ppbb;//蚌埠平均雨量
	double[] ppby;//淮北平均雨量
	double[] ppmg;    //淮南平均雨量
	double[] pphm;//湖面平均雨量
	double[] pphz;//洪泽湖平均雨量
	double[][] app;//输入16块面平均雨量，从实测开始时间到预报开始时间
	double[][] paver;
	
	//单位线地面径流计算
	double[][] w;
	double[][] qbb;
	double[][] qbbg;
	double[][] qby;
	double[][] qbyg;
	double[][] qmg;
	double[][] qmgg;
	double[][] qqobc;
	int nnbb;
	int nnby;
	int nnmg;
	
	
	//水库汇流
	double[] q;
	String[][] itc;
	double[][] idc;
	double[][] q1;
	double[][] q1g;
	double[] qcsum;
	double[] qrcsum;
	int j1;
	int j2;
	
	
	//退水流量计算
	int kbtui;
	double[][] qbboto;
	double[] qtuibb;
	double[] qtuiby;
	double[] qtuimg;
	int tt;
	
	//最终蚌埠、淮北、淮南、湖面、洪泽湖入湖流量
	int areaHM;
	int gltt;
	double[] qbbotc;
	double[] qbyc;
	double[] qmgc;
	double[] qhmc;
	double[] qhzh;
	
	//实测代表站流量蚌埠、明光、金锁镇、峰山、泗洪老、泗洪新、团结闸
	double[][] dmobs;
	double[] sumdmobs;
	double[] qobsbb;
	double[] qobsmg;
	double[][] qobsby;
	//	 特征值计算
	double min;
	double sum0;
	double[][] chara;//输出流域特征值
	double sum;//洪量计算过程中初始化值sum实测，sum1预报
	double sum1;
	double sum2;//确定性系数计算实测值的平均值初始化
	double f0;//计算确定性系数用到初始化值
	double fn;
	
	double[] averageqo = new double[5];//蚌埠、淮北、淮南、湖面、入湖实测值的平均值
	String[] qcaltime;
	String[] qobstime;
	double[][] dmaver;
	double[][] qcal;
	double[][] qobs;
	
	
	public Huiliu(double[][] app, double[][] w, double[][] qbb, double[][] qby, double[][] qmg, double[][] qbboto, double[][] q1, double[][] idc, double[][] dmobs, String starttime, String middletime, String endtime, String initialtime,String[][] itc) {
		this.app = app;// 16个分块的面平均雨量
		this.w = w;//输入断面产水量和修正断面产水量
		this.qbb = qbb;//输入蚌埠单位线配置表
		this.qby = qby;//输入淮北单位线配置表
		this.qmg = qmg;//输入淮南单位线配置表
		this.qbboto = qbboto;//日放水量表闸坝放水（插值出的日放水资料）
		this.q1 = q1;//水库来水及汇流
		this.idc = idc;//水库汇流选择，第一列为来水总量，第二列为是否汇到蚌埠
		this.dmobs = dmobs;//蚌埠、明光、金锁镇、峰山、泗洪老、泗洪新、团结闸
		this.starttime = starttime;//实测开始时间
		this.middletime = middletime;//实测结束时间
		this.endtime = endtime;//预报结束时间
		this.initialtime = initialtime;//初始化时间
		this.itc=itc;//流量大于500，开始时间，结束时间
	}
	
	public void prTime(String starttime, String endtime) {
		try {
			List a = DateUtil.getBetweenDates(starttime, endtime);
			Longtime = a.size();
			timeseries = new String[Longtime];
			for (int i = 0; i < Longtime; i++) {
				timeseries[i] = (String) a.get(i);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void obTime(String starttime, String middletime) {
		try {
			List b = DateUtil.getBetweenDates(starttime, middletime);
			glnn = b.size();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/*面平均雨量计算*/
	public void jpAverage(double[][] app, String initialtime, String starttime) throws ParseException {
		
		/*蚌埠面均雨量计算*/
		List m = DateUtil.getBetweenDates(initialtime, starttime);
		int n = m.size();
		paver = new double[glnn][16];
		for (int i = n - 1; i < paver.length; i++) {
			for (int j = 0; j < paver[0].length; j++) {
				paver[i - n + 1][j] = app[i][j];
			}
		}
		
		ppbb = new double[Longtime];
		ppby = new double[Longtime];
		ppmg = new double[Longtime];
		pphm = new double[Longtime];
		pphz = new double[Longtime];
		/*输入16块面平均雨量，从实测开始时间到预报开始时间*/
		for (int i = 0; i < glnn; i++) {
			ppbb[i] = 0;
			for (int j = 0; j < 11; j++) {
				if (paver[i][j] < 0) {
					paver[i][j] = 0;
				}
				ppbb[i] = ppbb[i] + paver[i][j] / 11;
			}
		}
		/*淮北面均雨量计算*/
		for (int i = 0; i < glnn; i++) {
			ppby[i] = 0;
			for (int j = 12; j < 15; j++) {
				if (paver[i][j] < 0) {
					paver[i][j] = 0;
				}
				ppby[i] = ppby[i] + paver[i][j] / 3;
			}
		}
		/*淮南面均雨量计算*/
		for (int i = 0; i < glnn; i++) {
			if (paver[i][11] < 0) {
				paver[i][11] = 0;
			}
			ppmg[i] = paver[i][11];
		}
		/*湖面面均雨量计算*/
		for (int i = 0; i < glnn; i++) {
			if (paver[i][15] < 0) {
				paver[i][15] = 0;
			}
			pphm[i] = paver[i][15];
		}
		for (int i = 0; i < glnn; i++) {
			pphz[i] = ppbb[i] + ppby[i] + ppmg[i] + pphm[i];
		}
		
	}
	
	/*蚌埠、淮北、淮南单位线计算*/
	public void UA(double[][] w, double[][] qbb, double[][] qby, double[][] qmg) {
		
		/*判断条件*/
		double[] bb = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
		double[] by = {3, 5, 10, 15, 20, 25, 30, 35};
		double[] mg = {10, 15, 20, 25, 30, 35, 40, 50, 60, 70, 80, 90};
		//输出VB中表jybresuno格式：时间、蚌埠、淮北、淮南,数组长度需做修改*/
		qqobc = new double[Longtime][4];
		qbbg = new double[Longtime][11];
		for (int i = 0; i < qbb.length; i++) {
			for (int j = 0; j < qbb[i].length; j++) {
				qbbg[i][j] = qbb[i][j];
			}
		}
		/*qbbotc蚌埠最终的预报流量*/
		/*第一列时间、第二列蚌埠、第三列淮北、第四列淮南*/
		/*----单位线地面径流计算流量计算-------*/
		/*蚌埠预报地面径流量计算对应qbbobc*
		/*如果数据时段长超过62未考虑*/
		if (Longtime <= 62) {
			nnbb = 62;
		} else {
			nnbb = Longtime;
		}
		if (w[0][1] >= 0 && w[0][1] <= bb[0]) {
			for (int i = 0; i < nnbb; i++) {
				qqobc[i][0] = w[0][1] / bb[0] * qbbg[i][0];
			}
		} else if (w[0][1] >= bb[0] && w[0][1] <= bb[9]) {
			for (int j = 1; j <= 10; j++) {
				if (w[0][1] <= bb[j] && w[0][1] > bb[j - 1]) {
					for (int i = 0; i < nnbb; i++) {
						qqobc[i][0] = ((w[0][1] - bb[j]) / (bb[j - 1] - bb[j])) * qbbg[i][j - 1] + ((w[0][1] - bb[j - 1]) / (bb[j] - bb[j - 1])) * qbbg[i][j];
					}
					break;
				}
			}
		} else if (w[0][1] > bb[9]) {
			for (int i = 0; i < nnbb; i++) {
				qqobc[i][0] = w[0][1] / bb[9] * qbbg[i][9];
			}
		}
		
		
		/*淮北预报地面径流量计算对应qbyc*/
		/*如果数据时段长超过42未考虑*/
		qbyg = new double[Longtime][9];
		for (int i = 0; i < qby.length; i++) {
			for (int j = 0; j < qby[i].length; j++) {
				qbyg[i][j] = qby[i][j];
			}
		}
		if (Longtime <= 42) {
			nnby = 42;
		} else {
			nnby = Longtime;
		}
		
		if (w[1][1] >= 0 && w[1][1] <= by[0]) {
			for (int i = 0; i < nnby; i++) {
				qqobc[i][2] = w[1][2] / by[0] * qbyg[i][0];
			}
		} else if (w[1][1] >= by[0] && w[1][1] <= by[7]) {
			for (int j = 1; j <= 8; j++) {
				if (w[1][1] <= by[j] && w[1][1] > by[j - 1]) {
					for (int i = 0; i < nnby; i++) {
						qqobc[i][1] = ((w[1][1] - by[j]) / (by[j - 1] - by[j])) * qbyg[i][j - 1] + ((w[1][1] - by[j - 1]) / (by[j] - by[j - 1])) * qbyg[i][j];
					}
					break;
				}
			}
		} else if (w[1][1] > by[7]) {
			for (int i = 0; i < nnby; i++) {
				qqobc[i][1] = w[1][1] / by[7] * qbyg[i][7];
			}
		}
		
		/*淮南预报地面径流量计算mgc*/
		/*如果数据时段长超过18未考虑*/
		qmgg = new double[Longtime][13];
		for (int i = 0; i < qmg.length; i++) {
			for (int j = 0; j < qmg[i].length; j++) {
				qmgg[i][j] = qmg[i][j];
			}
		}
		if (Longtime <= 18) {
			nnmg = 18;
		} else {
			nnmg = Longtime;
		}
		if (w[2][1] >= 0 && w[2][1] <= mg[0]) {
			for (int i = 0; i < nnmg; i++) {
				qqobc[i][2] = w[2][1] / mg[0] * qmgg[i][0];
			}
		} else if (w[2][1] >= mg[0] && w[2][1] <= mg[11]) {
			for (int j = 1; j <= 12; j++) {
				if (w[2][1] <= mg[j] && w[2][1] > mg[j - 1]) {
					for (int i = 0; i < nnmg; i++) {
						qqobc[i][2] = ((w[2][1] - mg[j]) / (mg[j - 1] - mg[j])) * qmgg[i][j - 1] + ((w[1][1] - mg[j - 1]) / (mg[j] - mg[j - 1])) * qmgg[i][j];
					}
					break;
				}
			}
		} else if (w[2][1] > mg[11]) {
			for (int i = 0; i < nnmg; i++) {
				qqobc[i][2] = w[2][1] / mg[11] * qmgg[i][11];
			}
		}
		
	}
	
	/*蚌埠水库汇流计算*/
	public void bbShuiku(double[][] q1, double[][] idc,String[][] itc) {
//		输入三张表格水库汇流结果，是否汇到蚌埠，考虑流量是否大于500
		/*------水库汇流------*/
		q = new double[Longtime];//qrc q考虑是否汇流到蚌埠的流量
//		itc = new String[2][3];
		qcsum = new double[Longtime];
		qrcsum = new double[Longtime];
		q1g = new double[Longtime][7];
		for (int i = 0; i < q1.length; i++) {
			for (int j = 0; j < q1[i].length; j++) {
				q1g[i][j] = q1[i][j];
			}
		}
//	对itc进行赋值，要传递String类型
//		itc[0][1] = "2000-06-17";
//		itc[0][2] = "2000-08-19";
		//int startTime=0;
		/*经过马斯京根演算后水库汇流*/
		//考虑大于500的流量进行汇流计算有误
		for (int i = 0; i < Longtime; i++) {
			q[i] = 0;
		}
		for (int i = 0; i < Longtime; i++) {
			if ((itc[0][0].equals(timeseries[i]))) {
				j1 = i;
			}
		}
		for (int i = 0; i < Longtime; i++) {
			if (itc[0][1].equals(timeseries[i])) {
				j2 = i;
			}
		}
		for (int i = j1; i <= j2; i++) {
			q[i] = q1g[i][5];
		}

// 考虑流量是否大于500
		for (int i = 0; i < Longtime; i++) {
			qcsum[i] = idc[0][1] * q1g[i][0] + idc[1][1] * q1g[i][1] + idc[1][2] * q1g[i][2];
			qrcsum[i] = idc[0][1] * q1g[i][3] + idc[1][1] * q1g[i][4] + idc[1][2] * q[i];
		}
	}
	
	/*蚌埠、淮北、淮南退水流量计算*/
	public void Dqtui(double[][] qbboto) {
		kbtui = 16;
		qtuibb = new double[Longtime];//qtuibb蚌埠的退水流量
		qtuiby = new double[Longtime];//qtuiby淮北的退水流量
		qtuimg = new double[Longtime];//qtuimg明光的退水流量
		//蚌埠退水流量计算
		qtuibb[0] = qbboto[0][1];
		for (int i = 1; i < Longtime; i++) {
			qtuibb[i] = qtuibb[i - 1] * Math.pow((Math.E), -1f / kbtui);
		}
		
		
		//淮北预报初始流量作为退水初始流量
		qtuiby[0] = qqobc[0][1];
		for (int i = 1; i < Longtime; i++) {
			qtuiby[i] = qtuiby[i - 1] * Math.pow(Math.E, -1f / kbtui);
		}
		
		//淮南预报初始流量作为退水初始流量
		qtuimg[0] = qqobc[0][2];
		for (int i = 1; i < Longtime; i++) {
			qtuimg[i] = qtuimg[i - 1] * Math.pow(Math.E, -1f / kbtui);
		}
	}
	
	
	public void hui() {
		areaHM = 1700;//洪泽湖湖面面积
		gltt = 24;//时段长
		qbbotc = new double[Longtime];//qbbotc蚌埠最终的预报流量
		qbyc = new double[Longtime];//qbyc淮北最终的预报流量
		qmgc = new double[Longtime];//qmgc淮南最终的预报流量
		qhmc = new double[Longtime];//qhmc湖面最终的预报流量
		qhzh = new double[Longtime];//qhzh洪泽湖入湖最终的预报流量
		/*蚌埠最终预报流量*/
		for (int i = 0; i < Longtime; i++) {
			qbbotc[i] = qqobc[i][0] + qtuibb[i] + qrcsum[i];
		}
		/*淮北最终预报流量*/
		for (int i = 0; i < Longtime; i++) {
			qbyc[i] = qqobc[i][1] + qtuiby[i];
		}
		
		/*淮南最终预报流量*/
		for (int i = 0; i < Longtime; i++) {
			qmgc[i] = qqobc[i][2] + qtuimg[i];
		}
		/*湖面最终预报流量*/
		for (int i = 0; i < Longtime; i++) {
			qhmc[i] = pphm[i] * areaHM / gltt / 3.6;
		}
		/*洪泽湖最终入湖预报流量*/
		for (int i = 0; i < Longtime; i++) {
			qhzh[i] = qbbotc[i] + qbyc[i] + qmgc[i] + qhmc[i];
		}
	}
	
	public void obsHui(double[][] dmobs) {
		sumdmobs = new double[Longtime];
		qobsbb = new double[Longtime];//qbbotc蚌埠最终的实测流量
		qobsby = new double[Longtime][7];//qbyc淮北实测的预报流量
		qobsmg = new double[Longtime];//qmgc淮南实测的预报流量
//		蚌埠实测流量
		for (int i = 0; i < Longtime; i++) {
			if (dmobs[i][0] < 0) {
				qobsbb[i] = 0;
			} else {
				qobsbb[i] = dmobs[i][0];
			}
			
		}
//		明光实测流量
		for (int i = 0; i < Longtime; i++) {
			if (dmobs[i][1] < 0) {
				qobsmg[i] = 0;
			} else {
				qobsmg[i] = dmobs[i][1];
			}
		}
//		金锁镇、峰山、泗洪老、泗洪新、团结闸
		for (int j = 2; j < 7; j++) {
			for (int i = 0; i < Longtime; i++) {
				if (dmobs[i][j] < 0) {
					qobsby[i][j] = 0;
				} else {
					qobsby[i][j] = dmobs[i][j];
				}
			}
		}
		
		for (int i = 0; i < Longtime; i++) {
			sumdmobs[i] = qobsby[i][2] + qobsby[i][3] + qobsby[i][4] + qobsby[i][5] + qobsby[i][6];
		}
		
	}
	
	public void Watershed(double[][] w) {
		qcal = new double[Longtime][5];
		qobs = new double[Longtime][5];
		dmaver = new double[Longtime][5];
		chara = new double[5][11];
		gltt = 24;
		min = 0.000001;
//断面总雨量
		
			for (int i = 0; i < Longtime; i++) {
				dmaver[i][0] = ppbb[i];
				dmaver[i][1] = ppby[i];
				dmaver[i][2] = ppmg[i];
				dmaver[i][3] = pphm[i];
				dmaver[i][4] = ppbb[i];
				
			}
		
//预报流量
		for (int i = 0; i < Longtime; i++) {
			qcal[i][0] = qbbotc[i];
			qcal[i][1] = qbyc[i] ;
			qcal[i][2] = qmgc[i];
			qcal[i][3] = qhmc[i];
			qcal[i][4] = qhzh[i];
		}
//实测流量
		
		for (int i = 0; i < Longtime; i++) {
			qobs[i][0] = qobsbb[i];
			qobs[i][1] = sumdmobs[i] ;
			qobs[i][2] = qobsmg[i];
			qobs[i][3] = 0;
			qobs[i][4] = 0;
		}
//总雨量
			for (int j = 0; j < 5; j++) {
			sum0 = 0;
			for (int i = 1; i < Longtime; i++) {
				sum0 = sum0 + dmaver[i][j];
			}
			chara[j][0] = dmaver[0][j] + sum0;
		}
		
//		产水总量
		for (int j = 0; j < 5; j++) {
			chara[j][1] = w[j][1];
		}


//实测洪量计算
		for (int j = 0; j < 5; j++) {
			sum = 0;
			for (int i = 1; i < Longtime - 1; i++) {
				sum = sum + qobs[i][j];
			}
			chara[j][2] = (qobs[0][j] / 2 + sum + qobs[Longtime - 1][j] / 2) * 0.36 * gltt / 10000;
		}

//预报洪量计算
		for (int j = 0; j < 5; j++) {
			sum1 = 0;
			for (int i = 1; i < Longtime - 1; i++) {
				sum1 = sum1 + qcal[i][j];
			}
			chara[j][3] = (qcal[0][j] / 2 + sum1 + qcal[Longtime - 1][j] / 2) * 0.36 * gltt / 10000;
		}
//洪量相对误差
		
		for (int j = 0; j < 5; j++) {
			chara[j][4] = (chara[j][3] - chara[j][2]) / (chara[j][2] + min) * 100;
		}
//实测洪峰
		qobstime = new String[8];
		for (int j = 0; j < 5; j++) {
			chara[j][5] = qobs[0][j];
			for (int i = 0; i < Longtime; i++)
				if (qobs[i][j] > chara[j][5]) {
					chara[j][5] = qobs[i][j];// 实测峰现时间为编号
//					chara[j][6] = i;
					qobstime[j] = timeseries[i];
				}
				
			
		}
//	预报洪峰
		qcaltime = new String[8];
		for (int j = 0; j < 5; j++) {
			chara[j][6] = qcal[0][j];
			for (int i = 0; i < Longtime; i++) {
				if (qcal[i][j] > chara[j][6]) {
					chara[j][6] = qcal[i][j];
//					chara[j][7] = i;// 预报峰现时间为编号
					qcaltime[j] = timeseries[i];
				}
			}
		}
		
		
//洪峰相对误差
		for (int j = 0; j < 5; j++) {
			chara[j][7] = (chara[j][6] - chara[j][5]) / (chara[j][5] + min) * 100;
		}
		
//		/*峰现时间绝对误差*/
//		for (int j = 0; j < 5; j++) {
//			chara[j][9] = chara[5][j] - chara[7][j];
//		}
		
		for (int j = 0; j < 5; j++) {
			chara[j][8]=99999999;//最后替换为实测峰现时间qobstime
		}
		
		for (int j = 0; j < 5; j++) {
			chara[j][9]=99999999;//最后替换为预报峰现时间qcaltime
		}
		
		/*确定性系数DC*/
		//计算实测值得平均值
		for (int j = 0; j < 5; j++) {
			sum2 = 0;
			for (int i = 0; i < glnn; i++) {
				sum2 = sum2 + qobs[i][j];
			}
			averageqo[j] = sum2 / (glnn + min);
		}
		//计算确定性系数
		for (int j = 0; j < 5; j++) {
			f0 = 0;
			fn = 0;
			for (int i = 0; i < glnn; i++) {
				f0 = f0 + Math.pow(qobs[i][j] - averageqo[j], 2);
				fn = fn + Math.pow(qcal[i][j] - qobs[i][j], 2);
			}
			f0 = f0 / (glnn + min);
			fn = fn / (glnn + min);
			chara[j][10] = 1 - fn / (f0 + min);
		}
	}
	
	
	/*输出蚌埠、淮南、淮北、湖面、入湖的面平均雨量、预报流量、实测流量、退水流量*/
	/*输出径流深、分块产水量，断面产水量*/
	public Map<String, Object> outputhuiliu() throws ParseException {
		prTime(starttime,  endtime);
		obTime( starttime, middletime);
		jpAverage(app, initialtime, starttime);
		UA(w, qbb, qby, qmg);
		bbShuiku(q1, idc,itc);
		Dqtui(qbboto);
		hui();
		obsHui(dmobs);
		Watershed(w);
//		面平均保留两位小数
		for (int i = 0; i < glnn; i++) {
			ppbb[i]= (double) (Math.round(ppbb[i] * 100)) / 100;
			ppby[i]= (double) (Math.round(ppby[i] * 100)) / 100;
			ppmg[i]= (double) (Math.round(ppmg[i] * 100)) / 100;
			ppbb[i]= (double) (Math.round(pphm[i] * 100)) / 100;
		}
//		预报保留两位小数
		for(int i=0;i<Longtime;i++){
			qbbotc[i]=(double) (Math.round(qbbotc[i] * 100)) / 100;
			qbyc[i]=(double) (Math.round(qbyc[i] * 100)) / 100;
			qmgc[i]=(double) (Math.round(qmgc[i] * 100)) / 100;
			qhmc[i]=(double) (Math.round(qhmc[i] * 100)) / 100;
			qhzh[i]=(double) (Math.round(qhzh[i] * 100)) / 100;
		}
//		实测保留两位小数
		for(int i=0;i<Longtime;i++) {
			qobsbb[i] = (double) (Math.round(qobsbb[i] * 100)) / 100;
			sumdmobs[i] = (double) (Math.round(sumdmobs[i] * 100)) / 100;
			qobsmg[i] = (double) (Math.round(qobsmg[i] * 100)) / 100;
		}
		Map<String, Object> Huiliu = new HashMap<>();
		
		Huiliu.put("蚌埠雨量", ppbb);
		Huiliu.put("淮北雨量", ppby);
		Huiliu.put("淮南雨量", ppmg);
		Huiliu.put("湖面雨量", pphm);
		Huiliu.put("洪泽湖雨量", pphz);
		
		
		Huiliu.put("蚌埠预报流量", qbbotc);
		Huiliu.put("淮北预报流量", qbyc);
		Huiliu.put("淮南预报流量", qmgc);
		Huiliu.put("湖面预报流量",qhmc );
		Huiliu.put("洪泽湖预报流量",qhzh );
		
		Huiliu.put("蚌埠实测", qobsbb);
		Huiliu.put("淮北实测", sumdmobs);
		Huiliu.put("淮南实测", qobsmg);
		
		Huiliu.put("特征值", chara);
		Huiliu.put("预报洪峰时间", qcaltime);
		Huiliu.put("实测洪峰时间", qobstime);
		return Huiliu;
	}
}

