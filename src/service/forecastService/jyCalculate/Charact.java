package service.forecastService.jyCalculate;

import util.DateUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Charact {
	
	double[][] qcal;//计算的蚌埠、淮北、淮南、洪泽湖预报流量
	double[][] qobs;//实测的蚌埠、淮北、淮南流量
	double[][] dmaver;
	String starttime;
	String middletime;
	String endtime;
	String[] timeseries;
	int glnn;//从实测开始到预报开始的时间
	int Longtime;//从实测开始到预报截止的时间
	int gltt;
	double min;
	double sum0;
	double sum;//洪量计算过程中初始化值sum实测，sum1预报
	double sum1;
	double sum2;//确定性系数计算实测值的平均值初始化
	double f0;//计算确定性系数用到初始化值
	double fn;
	double[] averageqo = new double[6];//蚌埠、淮北、淮南、湖面、入湖实测值的平均值
	double[][] chara = new double[12][8];//输出流域特征值
	String[] qcaltime;
	String[] qobstime;
	
	public Charact(double[][] qcal, double[][] qobs,double[][] dmaver, String starttime, String middletime, String endtime) {
		this.qcal = qcal;
		this.qobs = qobs;
		this.dmaver=dmaver;
		this.starttime = starttime;
		this.middletime = middletime;
		this.endtime = endtime;
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
	
	public void Watershed(double[][] qcal, double[][] qobs,double[][] dmaver) {
		
		gltt = 24;
		min = 0.000001;
//断面总雨量
		for (int j = 0; j <5; j++) {
			sum0 = 0;
			for (int i = 1; i < Longtime ; i++) {
				sum0 = sum0 + dmaver[i][j];
			}
			chara[0][j] = dmaver[0][j] + sum0;
		}
//实测洪量计算
		for (int j = 0; j <5; j++) {
			sum = 0;
			for (int i = 1; i < Longtime - 1; i++) {
				sum = sum + qobs[i][j];
			}
			chara[1][j] = (qobs[0][j] / 2 + sum + qobs[Longtime-1][j] / 2) * 0.36 * gltt / 10000;
		}
		
//预报洪量计算
		for (int j = 0; j < 5; j++) {
			sum1 = 0;
			for (int i = 1; i < Longtime - 1; i++) {
				sum1 = sum1 + qcal[i][j];
			}
			chara[2][j] = (qcal[0][j] / 2 + sum1 + qcal[Longtime-1][j] / 2) * 0.36 * gltt / 10000;
		}
		
		/*洪量相对误差*/
		for (int j = 0; j < 5; j++) {
			chara[3][j] = (chara[1][j] - chara[2][j]) / (chara[1][j] + min) * 100;
		}
		
		/*实测洪峰*/
		qobstime=new String[8];
		for (int j = 0; j <5; j++) {
			chara[4][j] = qobs[0][j];
			for (int i = 0; i < Longtime; i++)
				if (qobs[i][j] > chara[4][j]) {
					chara[4][j] = qobs[i][j];// 实测峰现时间为编号
					chara[5][j] = i;
					qobstime[j]= timeseries[i];
				}
			
		}
		
		/*预报洪峰*/
		qcaltime=new  String[8];
		for (int j = 0; j < 5; j++) {
			chara[6][j] = qcal[0][j];
			for (int i = 0; i < Longtime; i++) {
				if (qcal[i][j] > chara[6][j]) {
					chara[6][j] = qcal[i][j];
					chara[7][j] = i;// 预报峰现时间为编号
					qcaltime[j]=timeseries[i];
				}
			}
		}
		
		/*洪峰相对误差*/
		for (int j = 0; j < 5; j++) {
			chara[8][j] = (chara[4][j] - chara[6][j]) / (chara[4][j] + min) * 100;
		}
		
		/*峰现时间绝对误差*/
		for (int j = 0; j <5; j++) {
			chara[9][j] = chara[5][j] - chara[7][j];
		}
		
		/*确定性系数DC*/
		//计算实测值得平均值
		for (int j = 0; j <5; j++) {
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
			chara[10][j] = 1 - fn / (f0 + min);
		}
	}
	
	public Map<String, Object> OutputCharact() {
		
		prTime(starttime, endtime);
		obTime(starttime, middletime);
		Watershed(qcal,qobs,dmaver);
		
		Map<String, Object> charact = new HashMap<>();
		charact.put("特征值", chara);
		charact.put("预报洪峰的时间", qcaltime);
		charact.put("实测洪峰的时间", qobstime);
		return charact;
		
		
	}
}
