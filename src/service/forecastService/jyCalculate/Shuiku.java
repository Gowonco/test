package service.forecastService.jyCalculate;

import util.DateUtil;

import java.text.ParseException;
import java.util.*;

public class Shuiku {
	double[][] qshuiku;
	double[][] musk;
	int Longtime;
	int tt;
	double[][] qr;
	double[] kk;
	double[] xx;
	double[] nn;
	double[] c0;
	double[] c1;
	double[] c2;
	double[][] qc;
	double[][] qrc;
	double[][] qrr;
	double[][] qoxs;
	double[][] qcc;
	double[][] wchu;
	double[][] id;
	double[][]  it1;
	String[][] it;
	String starttime;
	String middletime;
	String endtime;
	String[] timeseries;
	int glnn;
	public Shuiku(double[][] qshuiku,double[][] musk, String starttime,String middletime, String endtime){
		this.qshuiku=qshuiku;
		this.musk=musk;
		this.starttime=starttime;
		this.middletime=middletime;
		this.endtime=endtime;
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
			List b= DateUtil.getBetweenDates(starttime, middletime);
			glnn = b.size();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/*蚌埠水库汇流计算*/
	public void fenLei(double[][] qshuiku) {
		qr = new double[Longtime][5];  //将9个水库分为5类进行马斯京根演算
		tt = 24;
		
		//9个水库的放水资料分为5类进行计算从实测开始到预报结束1-64
		for (int i = 0; i < qshuiku.length; i++) {
			if (qshuiku[i][0] < 0) {
				qshuiku[i][0] = 0;
			}
			qr[i][0] = qshuiku[i][0];//昭平台
		}
		for (int i = 0; i < qshuiku.length; i++) {
			for (int j = 1; j <= 3; j++) {
				if (qshuiku[i][j] < 0) {
					qshuiku[i][j] = 0;
				}
			}
			qr[i][1] = qshuiku[i][1] + qshuiku[0][2] + qshuiku[0][3];//石漫滩+板桥+薄山
		}
		for (int i = 0; i < qshuiku.length; i++) {
			if (qshuiku[i][4] < 0) {
				qshuiku[i][4] = 0;
			}
			qr[i][2] = qshuiku[i][4];//南湾
		}
		
		for (int i = 0; i < qshuiku.length; i++) {
			for (int j = 5; j <= 6; j++) {
				if (qshuiku[i][j] < 0) {
					qshuiku[i][j] = 0;
				}
				qr[i][3] = qshuiku[i][5] + qshuiku[i][6];//梅山+鲇雨山
			}
		}
		
		for (int i = 0; i < qshuiku.length; i++) {
			for (int j = 7; j <= 8; j++) {
				if (qshuiku[i][j] < 0) {
					qshuiku[i][j] = 0;
				}
			}
			qr[i][4] = qshuiku[i][7] + qshuiku[i][8];//佛子岭+响洪甸
		}
	}
	public void masking(double[][] musk){
		//5类水库分别进行马斯京根汇流演算
		kk=new double[5];
		xx=new double[5];
		nn=new double[5];
		c0=new double[5];
		c1=new double[5];
		c2=new double[5];
		qoxs=new double[Longtime][8];
		qcc=new double[Longtime][8];
		qrr=new double[Longtime][5];
		qc= new double[Longtime][3];
		qrc =new double[Longtime][3];
		for (int j = 0; j < musk.length; j++) {
			kk[j] = musk[j][0];
			xx[j] = musk[j][1];
			nn[j] = musk[j][2];
		}
		
		for (int j = 0; j <5; j++) {
			//马斯京根演算
			c0[j] = (0.5 * tt - kk[j] * xx[j]) / (kk[j] - kk[j] * xx[j] + 0.5 * tt);
			c1[j] = (kk[j] * xx[j] + 0.5 * tt) / (kk[j] - kk[j] * xx[j] + 0.5 * tt);
			c2[j] = (kk[j] - kk[j] * xx[j] - 0.5 * tt) / (kk[j] - kk[j] * xx[j] + 0.5 * tt);
			
			for (int i = 0; i < qr.length; i++) {
				qoxs[i][j] = qr[i][j];
			}
			
			for (int i = 0; i < qr.length; i++) {
				qcc[i][j] = 0;
			}
			
			//分断面进行马斯京根演算
			
			for (int k = 1; k <= nn[j]; k++) {
				
				for (int i = 1; i < qoxs.length; i++) {
					qcc[i][j] = c0[j] * qoxs[i][j] + c1[j] * qoxs[i - 1][j] + c2[j] * qcc[i - 1][j];
				}
				if (k == nn[j]) {
					break;
				}
				for (int i = 0; i < qoxs.length; i++) {
					qoxs[i][j] = qcc[i][j];
				}
			}
			for (int i = 0; i < qoxs.length; i++) {
				qrr[i][j] = qcc[i][j];
			}
			
		}
		//System.out.println( "一次计算");
		//3类情况下水库放水和经过马斯京根汇流
		/*定义读取VB中表ybbenskk：时间、昭平台、洪汝湖、淮南三个经马斯京根演算后结果9-5-3*/
		
		for (int i = 0; i < qr.length; i++) {
			qc[i][0] = qr[i][0];//qc 昭平台类水库的放水流量
			qc[i][1] = qr[i][1];//qc 洪汝湖类水库的放水流量
			qc[i][2] = qr[i][2] + qr[i][3] + qr[i][4];//qc 淮南类水库的放水流量
		}
		for (int i = 0; i < qrc.length; i++) {
			qrc[i][0] = qrr[i][0];//qrc  昭平台类水库的经过马斯京根汇流之后流量
			qrc[i][1] = qrr[i][1];//qrc 洪汝湖水库的经过马斯京根汇流之后流量
			qrc[i][2] = qrr[i][2] + qrr[i][3] + qrr[i][4];//qrc 淮南类水库的经过马斯京根汇流之后流量
		}
		
	}
	public void huiSelect(){
		//3个水库的来水总量
		wchu=new double[3][4];
		id=new double[3][4];
		for(int j=0;j<3;j++){
			wchu[j][0]=0;
			for(int i=0;i<glnn;i++){
				wchu[j][0]=wchu[j][0]+qc[i][j];
			}
			id[j][0]=wchu[j][0]*24*3600/100000000;
		}
	}
	
	public void huiTime() {
//计算流量大于500的时间；
//建议考虑汇流时间

		it1 = new double[3][3];
		it = new String[2][2];
		it[0][1] = timeseries[Longtime-1];
		it[1][1] = timeseries[Longtime -1];
		for (int i = 0; i < Longtime; i++) {
			if (qc[i][2] >= 500) {
				it[0][0] = timeseries[i];//进行时间转换，将时刻点转换为某年某月某日20000623
				break;
			}
		}
		it[1][0] = it[0][0];

	}



	// beginTime=indexToDate(i,startDate);//将数组下标转化为日期
			/*输出水库放水、马斯京根汇流水库水量*/
	public Map<String, Object> outputShuiKu () {
		prTime( starttime, endtime);
		obTime( starttime,  middletime);
		fenLei(qshuiku);
		masking(musk);
		huiSelect();
		huiTime();
//保留两位小数
		for (int i = 0; i < qc.length; i++) {
			for (int j = 0; j < qc[i].length; j++) {
						qc[i][j] = (double) (Math.round(qc[i][j] * 10)) / 10;
					}
				}
		for (int i = 0; i < qrc.length; i++) {
				for (int j = 0; j < qrc[i].length; j++) {
						qrc[i][j] = (double) (Math.round(qrc[i][j] * 10)) / 10;
					}
				}
				
		for (int j = 0; j < 3; j++) {
					id[j][0] = (double) (Math.round(id[j][0] * 10000)) / 10000;
			}
				
		Map<String, Object> shuiKuHui = new HashMap<>();
				shuiKuHui.put("水库放水", qc);
				shuiKuHui.put("水库马法演算", qrc);
				shuiKuHui.put("来水总量", id);
				shuiKuHui.put("流量大于500", it);
				
				return shuiKuHui;
			}
			
		}

