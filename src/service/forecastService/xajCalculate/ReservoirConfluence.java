package service.forecastService.xajCalculate;

import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ReservoirConfluence {
    private String obsStartDay,forecastStartDay,forecastEndDay;
    private int timeInterval=24;
    private int forecastEndTime,obsEndTime,inflowNo,subBasinNo;
    private double[][] Q,routeQ,sumQ,routeSubQ,gateQ,para,readQ;
    private double c0,c1,c2;
    private double[] kk,xx,upQ,lowQ,w,gateW;
    private int [] nn;
    private int itb,ite;
    private String beginTime;
    //构造方法

    /**
     *
     * @param inflowNo      入流个数
     * @param subBasinNo    子流域个数
     * @param obsStartDay    实测开始时间
     * @param forcastStartDay  预报开始时间
     * @param forecastEndDay   预报结束时间
     */

    public ReservoirConfluence(  int inflowNo, int subBasinNo,    String obsStartDay,String forcastStartDay,String forecastEndDay) {
        //单个变量
        this.inflowNo = inflowNo;
        this.subBasinNo = subBasinNo;
        this.obsStartDay = obsStartDay;
        this.forecastStartDay=forcastStartDay;
        this.forecastEndDay=forecastEndDay;
        obsEndTime=getDayIndex(this.obsStartDay,forecastStartDay);
        forecastEndTime=getDayIndex(this.obsStartDay,this.forecastEndDay)+1;
        this.ite=getDayIndex(this.obsStartDay,this.forecastEndDay);
        //数组
        this.readQ = new double[forecastEndTime][12];
        this.Q = new double[12][forecastEndTime];
        this.para=new double[inflowNo+subBasinNo][2];//增加水库后，参数行数要改变
        this.routeQ = new double[4][forecastEndTime];
        this.sumQ = new double[4][forecastEndTime];
        this.routeSubQ = new double[inflowNo][forecastEndTime];
        this.gateQ = new double[2][forecastEndTime];
        this.kk = new double[inflowNo];
        this.xx = new double[inflowNo];
        this.nn = new int[inflowNo];
        this.upQ = new double[forecastEndTime];
        this.lowQ = new double[forecastEndTime];
        this.w = new double[3];
        this.gateW = new double[forecastEndTime];
    }

    /**
     *
     * @param q 九个水库和三个闸的流量
     */
    public void setReadQ(double[][] q) {
        readQ = q;
    }

    /**
     *
     * @param para 鲁台子汇流参数（包括子流域的参数）
     */
    public void setPara(double[][] para) {
        this.para = para;
    }

    public double[][] getReadQ() {
        return Q;
    }

    public double[][] getPara() {
        return para;
    }


    //读取9个水库，上桥闸的参数
    public  void readParameter(){
        for(int i=9;i<para.length;i++){
            xx[i-subBasinNo] = para[i][0];
            nn[i-subBasinNo] = (int)para[i][1];
            kk[i-subBasinNo] = timeInterval;
           // '9个水库，上桥闸名称
        }
        for(int i=0;i<readQ.length;i++){
            //System.out.println(readQ.length);
            //可能增加一个水库
            for (int j=0;j<inflowNo;j++){
                Q[j][i]=readQ[i][j];
            }
            gateQ[0][i]=readQ[i][10];//阜阳闸
            gateQ[1][i]=readQ[i][11];//蒙城闸
        }

    }

    //计算
    public  void qInflow(){

        int i,j,k;

        //上桥闸预报期赋值为0,从qreservoir读取
        for(i=obsEndTime;i<forecastEndTime;i++){
            Q[9][i]=0;
        }

        for(i=0;i<forecastEndTime;i++){
            for(j=0;j<4;j++){
                routeQ[j][i]=0;
                sumQ[j][i]=0;
            }
            for(j=0;j<inflowNo;j++){
                routeSubQ[j][i]=0;
            }
        }
        //马斯京根分段演算
        for(k=0;k<inflowNo;k++){
            c0= (0.5 * timeInterval - kk[k] * xx[k]) / (kk[k] - kk[k] * xx[k] + 0.5 * timeInterval);
            c1 = (kk[k] * xx[k] + 0.5 * timeInterval) / (kk[k] - kk[k] * xx[k] + 0.5 * timeInterval);
            c2 = (kk[k] - kk[k] * xx[k] - 0.5 * timeInterval) / (kk[k] - kk[k] * xx[k] + 0.5 * timeInterval);
            for(j=0;j<forecastEndTime;j++){
                upQ[j]=Q[k][j];  //inflow of upper cross-section
            }
            for(j=0;j<forecastEndTime;j++){
                lowQ[j]=0;  //'inflow of lower cross-section
            }
            for(i=0;i<nn[k];i++){
                for(j=1;j<forecastEndTime;j++){
                    lowQ[j]= c0 * upQ[j] + c1 * upQ[j - 1] + c2 * lowQ[j - 1];
                }
                if(i==nn[k]-1){
                    for(i=0;i<forecastEndTime;i++){
                        routeSubQ[k][i]=lowQ[i];//musking计算结束，把lowQ赋值给routeSubQ
                    }
                }
                for(j=0;j<forecastEndTime;j++){
                    upQ[j]=lowQ[j];
                }
            }
            //可能增加一类水库
            for(i=0;i<forecastEndTime;i++){
                sumQ[0][i]=Q[0][i];
                sumQ[1][i]=Q[1][i]+Q[2][i]+Q[3][i];
                sumQ[2][i]=Q[4][i]+Q[5][i]+Q[6][i]+Q[7][i]+Q[8][i];
                sumQ[3][i]=Q[9][i];
                routeQ[0][i]=routeSubQ[0][i];
                routeQ[1][i]=routeSubQ[1][i]+routeSubQ[2][i]+routeSubQ[3][i];
                routeQ[2][i]=routeSubQ[4][i]+routeSubQ[5][i]+routeSubQ[6][i]+routeSubQ[7][i]+routeSubQ[8][i];
                routeQ[3][i]=routeSubQ[9][i];
            }

        }

    }
    //表格创建
    public  void calReservoir(){
        //读取阜阳闸，蒙城闸，上桥闸水量
    //通过三个闸门的水量
    for(int i=0;i<obsEndTime;i++){
        gateW[0]=gateW[0]+gateQ[0][i]*24 * 3600 / 100000000;
        gateW[1]=gateW[1]+gateQ[1][i]*24 * 3600 / 100000000;
        gateW[2]=gateW[2]+sumQ[3][i]*24 * 3600 / 100000000;
    }
    //三类水库的水量
    for(int i=0;i<obsEndTime;i++){
            w[0]=w[0]+sumQ[0][i]*24 * 3600 / 100000000;
            w[1]=w[1]+sumQ[1][i]*24 * 3600 / 100000000;
            w[2]=w[2]+sumQ[2][i]*24 * 3600 / 100000000;
            }
    //确定建议汇流结束时间
        for(int i=0;i<forecastEndTime;i++){
        //如果淮南的汇流没有大于500?
            if(sumQ[2][i]>500){
                itb=i;
                beginTime=indexToDate(i,obsStartDay);//将数组下标转化为日期
                break;
            }
        }
    }



    //将数组下标转化为日期格式
    public  String indexToDate(int i,String startDay){
        String s="";
        try{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date start=sdf.parse(startDay);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, i);
        Date date1 = calendar.getTime();
        s = sdf.format(date1);
        return s;}
        catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    //将日期转化为数组下标
    public int getDayIndex(String startDay,String correctDay){
        int correctDayIndex=0;
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
            Date correctDate = new SimpleDateFormat("yyyy-MM-dd").parse(correctDay);
            long dayIndex = (correctDate.getTime()-startDate.getTime())/(24*60*60*1000);
            //从dayIndex开始计算,覆盖预报期之后的值
            correctDayIndex= Integer.parseInt(String.valueOf(dayIndex));
            return correctDayIndex;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return correctDayIndex;
    }

    //存储3张表格
    //ybbenhn，ybbensk1，ybbensk
    public Map<String, Object> ybbenhn(){
        Map<String,Object> confluenceTime=new HashMap<>();
        String[] note={"流量大于500的时间","建议考虑汇流的时间"};
        String [] startTime=new String[2];
        String [] endTime=new String[2];
        startTime[0]=beginTime;
        endTime[0]=indexToDate(forecastEndTime,obsStartDay);
        //建议考虑汇流的时间可以修改
        confluenceTime.put("项目",note);
        confluenceTime.put("起始时间",startTime);
        confluenceTime.put("结束时间",endTime);
        return confluenceTime;
    }

    public Map<String, Object> ybbensk(){
        Map<String,Object> confluenceResults=new HashMap<>();
        //日期
        confluenceResults.put("昭平台",sumQ[0]);
        confluenceResults.put("洪汝河",sumQ[1]);
        confluenceResults.put("淮南",sumQ[2]);
        confluenceResults.put("上桥闸",sumQ[3]);
        confluenceResults.put("昭平台汇流",routeQ[0]);
        confluenceResults.put("洪汝河汇流",routeQ[1]);
        confluenceResults.put("淮南汇流",routeQ[2]);
        confluenceResults.put("上桥闸汇流",routeQ[3]);
        return confluenceResults;
    }
    public Map<String, Object> ybbensk1(){
        Map<String,Object> totalInflow=new HashMap<>();
        double [] isOpen={1,1,1,1,1};
        double[] totalW=new double[5];
        for(int i=0;i<3;i++){
            totalW[i]=w[i];
        }
        for(int i=3;i<5;i++){
            totalW[i]=gateW[i-3];
        }
        String[] note={"通过阜阳闸的水库","流到宿鸭湖的水库","干流及淮南的水库","阜阳闸是否开","蒙城闸是否开"};
        totalInflow.put("说明",note);
        totalInflow.put("来水总量",totalW);
        totalInflow.put("是否汇流到蚌埠",isOpen);
        return totalInflow;
    }



}
