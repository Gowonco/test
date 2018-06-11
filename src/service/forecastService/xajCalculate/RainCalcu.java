package service.forecastService.xajCalculate;

import util.DateUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class RainCalcu {
    float[][] p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22,p23;
    float[] addp1,addp2,addp3,addp4,addp5,addp6,addp7,addp8,addp9,addp10,addp11,addp12,addp13,addp14;
    float[] addpp,maxrain,addp15,addp16,addp17,addp18,addp19,addp20,addp21,addp22,addp23;
    float[][] pp;
    String[] maxname,timeseries;
    int NoRS1,NoRS2,NoRS3,NoRS4,NoRS5,NoRS6,NoRS7,NoRS8,NoRS9,NoRS10,NoRS11,NoRS12,NoRS13,NoRS14;
    int NoRS15,NoRS16,NoRS17,NoRS18,NoRS19,NoRS20,NoRS21,NoRS22,NoRS23;

    /**
     *
     * @param p              68个雨量站的降雨量
     * @param initialtime   初始化时间
     * @param starttime      实测开始时间
     * @param raintime       实测结束时间
     * @return
     * @throws ParseException
     */
    public Map<String, float[][]> partRain(float[][]p,String initialtime,String starttime,String raintime) throws ParseException {
        String[][] rname={{"大坡岭","薄山","正阳","长台关","南湾","南李店","息县"},
                {"班台","薄山","正阳","桂庄","庙湾","板桥", "石漫滩","漯河"},
                {"王家坝","班台","正阳","息县","南李店", "新县", "鲇鱼山", "潢川"},
                {"蒋集",  "鲇鱼山",  "吴店" ,  "梅山"},
                {"梅山","吴店", "横排头", "响洪店","佛子岭","上土寺"},
                {"鲁台子","润河集","王家坝","蒋集", "梅山","横排头","愧店","安丰塘","霍丘"},
                {"紫罗山", "昭平台", "石漫滩", "漯河","大陈闸","周口","李湾","扶沟","新郑"},
                {"周口","漯河","庙湾","班台","阜阳闸","沈丘","太康"},
                {"鲁台子","润河集","王家坝","班台", "阜阳闸"},
                {"亳县","太康","大王庙","商丘"},
                {"亳县","蒙城闸"},
                {"蒙城闸","利辛", "鲁台子","淮南","蚌埠"},
                {"鲁台子","淮南","蚌埠","安丰塘"},
                {"石角桥","明光"},
                {"临涣集","永城","商丘"},
                {"宿县闸","符离集","肖县","砀山"},
                {"临涣集","宿县闸","蒙城闸","固镇闸"},
                {"宿县闸","固镇闸","锋山闸"},
                {"泗洪","浍塘沟","肖县","符离集"},
                {"金锁镇","睢宁"},
                {"金锁镇","泗洪","五河","蚌埠","峰山"},
                {"五河","蚌埠","明光","盱眙","老子山","三河涧"},
                {"盱眙","三河闸","老子山","尚嘴","高良涧"},
        };
        p1=new float[p.length][rname[0].length];
        p2=new float[p.length][rname[1].length];
        p3=new float[p.length][rname[2].length];
        p4=new float[p.length][rname[3].length];
        p5=new float[p.length][rname[4].length];
        p6=new float[p.length][rname[5].length];
        p7=new float[p.length][rname[6].length];
        p8=new float[p.length][rname[7].length];
        p9=new float[p.length][rname[8].length];
        p10=new float[p.length][rname[9].length];
        p11=new float[p.length][rname[10].length];
        p12=new float[p.length][rname[11].length];
        p13=new float[p.length][rname[12].length];
        p14=new float[p.length][rname[13].length];
        p15=new float[p.length][rname[14].length];
        p16=new float[p.length][rname[15].length];
        p17=new float[p.length][rname[16].length];
        p18=new float[p.length][rname[17].length];
        p19=new float[p.length][rname[18].length];
        p20=new float[p.length][rname[19].length];
        p21=new float[p.length][rname[20].length];
        p22=new float[p.length][rname[21].length];
        p23=new float[p.length][rname[22].length];
        pp=new float[p.length][23];
        addp1=new float[p1[0].length];
        addp2=new float[p2[0].length];
        addp3=new float[p3[0].length];
        addp4=new float[p4[0].length];
        addp5=new float[p5[0].length];
        addp6=new float[p6[0].length];
        addp7=new float[p7[0].length];
        addp8=new float[p8[0].length];
        addp9=new float[p9[0].length];
        addp10=new float[p10[0].length];
        addp11=new float[p11[0].length];
        addp12=new float[p12[0].length];
        addp13=new float[p13[0].length];
        addp14=new float[p14[0].length];
        addp15=new float[p15[0].length];
        addp16=new float[p16[0].length];
        addp17=new float[p17[0].length];
        addp18=new float[p18[0].length];
        addp19=new float[p19[0].length];
        addp20=new float[p20[0].length];
        addp21=new float[p21[0].length];
        addp22=new float[p22[0].length];
        addp23=new float[p23[0].length];
        maxname=new String[23];
        maxrain=new float[23];
        int[][] lie= {{0, 17, 18, 1, 3, 4, 2}, {20, 17, 18, 15, 19, 16, 11, 14},
                {30, 20, 18, 2, 4, 6, 7, 5}, {8, 7, 37, 38},
                {38, 37, 39, 35, 34, 36}, {32, 31, 30, 8, 38, 39, 40,43,42},
                {9, 10, 11, 14, 12, 23, 22, 21, 13},{23, 14, 19, 20, 57, 25, 26},
                {32, 31, 30, 20, 57}, {54, 26, 27, 28}, {54, 58},
                {58, 56, 32, 41, 33}, {32, 41, 33, 43}, {44, 45},
                {55,29,28},{50,49,48,47},{55,50,58,52},
                {50,52,62},{63,51,48,49},{64,67},
                {64,63,46,33,62},{46,33,45,59,60,65},{59,65,60,61,66}};
        for (int i=0;i<p.length;i++){
            for (int j=0;j<p1[0].length;j++){p1[i][j]=p[i][lie[0][j]]; }
            for (int j=0;j<p2[0].length;j++){p2[i][j]=p[i][lie[1][j]]; }
            for (int j=0;j<p3[0].length;j++){p3[i][j]=p[i][lie[2][j]]; }
            for (int j=0;j<p4[0].length;j++){p4[i][j]=p[i][lie[3][j]]; }
            for (int j=0;j<p5[0].length;j++){p5[i][j]=p[i][lie[4][j]]; }
            for (int j=0;j<p6[0].length;j++){p6[i][j]=p[i][lie[5][j]]; }
            for (int j=0;j<p7[0].length;j++){p7[i][j]=p[i][lie[6][j]]; }
            for (int j=0;j<p8[0].length;j++){p8[i][j]=p[i][lie[7][j]]; }
            for (int j=0;j<p9[0].length;j++){p9[i][j]=p[i][lie[8][j]]; }
            for (int j=0;j<p10[0].length;j++){p10[i][j]=p[i][lie[9][j]]; }
            for (int j=0;j<p11[0].length;j++){p11[i][j]=p[i][lie[10][j]]; }
            for (int j=0;j<p12[0].length;j++){p12[i][j]=p[i][lie[11][j]]; }
            for (int j=0;j<p13[0].length;j++){p13[i][j]=p[i][lie[12][j]]; }
            for (int j=0;j<p14[0].length;j++){p14[i][j]=p[i][lie[13][j]]; }
            for (int j=0;j<p15[0].length;j++){p15[i][j]=p[i][lie[14][j]]; }
            for (int j=0;j<p16[0].length;j++){p16[i][j]=p[i][lie[15][j]]; }
            for (int j=0;j<p17[0].length;j++){p17[i][j]=p[i][lie[16][j]]; }
            for (int j=0;j<p18[0].length;j++){p18[i][j]=p[i][lie[17][j]]; }
            for (int j=0;j<p19[0].length;j++){p19[i][j]=p[i][lie[18][j]]; }
            for (int j=0;j<p20[0].length;j++){p20[i][j]=p[i][lie[19][j]]; }
            for (int j=0;j<p21[0].length;j++){p21[i][j]=p[i][lie[20][j]]; }
            for (int j=0;j<p22[0].length;j++){p22[i][j]=p[i][lie[21][j]]; }
            for (int j=0;j<p23[0].length;j++){p23[i][j]=p[i][lie[22][j]]; }
        }
        for(int a=0;a<p1[0].length;a++) {
            for (int b = 0; b < p1.length; b++) {
                addp1[a] = addp1[a] + p1[b][a];
            }
            if (addp1[a] == 0)
                NoRS1 = NoRS1 + 1;}
        for(int a=0;a<p1[0].length;a++) {
            int h = rname[0].length;
            for (int b = 0; b < p1.length; b++) {
                if(h==NoRS1){
                    pp[b][0]=0;}
                else
                pp[b][0] = pp[b][0] + p1[b][a] / (h - NoRS1);
            }
        }
        for(int a=0;a<p2[0].length;a++) {
            for (int b = 0; b < p2.length; b++) {
                addp2[a] = addp2[a] + p2[b][a];
            }
            if (addp2[a] == 0) {
                //ShortRname2[a]=Rname[1][a];
                NoRS2 = NoRS2 + 1;
            }
        }
        for(int a=0;a<p2[0].length;a++) {
                    int h=rname[1].length;
                for (int b = 0; b < p2.length; b++) {
                    if(h==NoRS2){
                        pp[b][1]=0;}
                    else
                    pp[b][1] = pp[b][1] + p2[b][a] / (h - NoRS2);
                }
            }
        for(int a=0;a<p3[0].length;a++){
            for(int b=0;b<p3.length;b++){
                addp3[a]=addp3[a]+p3[b][a];}
                if(addp3[a]==0){
                    //ShortRname3[a]=Rname[2][a];
                NoRS3=NoRS3+1;}}
        for(int a=0;a<p3[0].length;a++){
                int h=rname[2].length;
                for (int b = 0; b < p2.length; b++) {
                    if(h==NoRS3){
                        pp[b][2]=0;}
                    else
                    pp[b][2] = pp[b][2]+p3[b][a] / (h - NoRS3);
                }
            }
        for(int a=0;a<p4[0].length;a++){
            for(int b=0;b<p4.length;b++){
                addp4[a]=addp4[a]+p1[b][a];}
                if(addp4[a]==0){
                    //ShortRname4[a]=Rname[3][a];
                NoRS4=NoRS4+1;}}
        for(int a=0;a<p4[0].length;a++){
                int h=rname[3].length;
                for (int b = 0; b < p3.length; b++) {
                    if(h==NoRS4){
                        pp[b][3]=0;}
                    else
                    pp[b][3] = pp[b][3]+p4[b][a] / (h - NoRS4);
                }
            }
        for(int a=0;a<p5[0].length;a++){
            for(int b=0;b<p5.length;b++){
                addp5[a]=addp5[a]+p5[b][a];}
                if(addp5[a]==0){
                   // ShortRname5[a]=Rname[4][a];
                NoRS5=NoRS5+1;}}
        for(int a=0;a<p5[0].length;a++){
                int h=rname[4].length;
                for (int b = 0; b < p5.length; b++) {
                    if(h==NoRS5){
                        pp[b][4]=0;}
                    else
                    pp[b][4] = pp[b][4]+p5[b][a] / (h - NoRS5);
                }
            }
        for(int a=0;a<p6[0].length;a++) {
            for (int b = 0; b < p6.length; b++) {
                addp6[a] = addp6[a] + p6[b][a];
            }
            if (addp6[a] == 0) {
                // ShortRname6[a]=Rname[5][a];
                NoRS6 = NoRS6 + 1;}
            }
        for(int a=0;a<p6[0].length;a++) {
            int h = rname[5].length;
                    for (int b = 0; b < p6.length; b++) {
                        if(h==NoRS6){
                            pp[b][5]=0;}
                        else
                        pp[b][5] = pp[b][5] + p6[b][a] / (h - NoRS6);
                    }
                }
        for(int a=0;a<p7[0].length;a++){
            for(int b=0;b<p7.length;b++){
                addp7[a]=addp7[a]+p7[b][a];}
                if(addp7[a]==0){
                   // ShortRname7[a]=Rname[6][a];
                NoRS7=NoRS7+1;}}
        for(int a=0;a<p7[0].length;a++){
                int h=rname[6].length;
                for (int b = 0; b < p7.length; b++) {
                    if(h==NoRS7){
                        pp[b][6]=0;}
                    else
                    pp[b][6] = pp[b][6]+p7[b][a] / (h - NoRS7);
                }
            }
        for(int a=0;a<p8[0].length;a++){
            for(int b=0;b<p8.length;b++){
                addp8[a]=addp8[a]+p8[b][a];}
                if(addp8[a]==0){
                   // ShortRname8[a]=Rname[7][a];
                NoRS8=NoRS8+1;}}
        for(int a=0;a<p8[0].length;a++){
                int h=rname[7].length;
                for (int b = 0; b < p8.length; b++) {
                    if(h==NoRS8){
                        pp[b][7]=0;}
                    else
                    pp[b][7] = pp[b][7]+p8[b][a] / (h - NoRS8);
                }
            }
        for(int a=0;a<p9[0].length;a++){
            for(int b=0;b<p9.length;b++){
                addp9[a]=addp9[a]+p9[b][a];}
                if(addp9[a]==0){
                   // ShortRname9[a]=Rname[8][a];
                NoRS9=NoRS9+1;}}
        for(int a=0;a<p9[0].length;a++){
                int h=rname[8].length;
                for (int b = 0; b < p9.length; b++) {
                    if(h==NoRS9){
                        pp[b][8]=0;}
                    else
                    pp[b][8] = pp[b][8]+p9[b][a] / (h - NoRS9);
                }
            }
        for(int a=0;a<p10[0].length;a++){
            for(int b=0;b<p10.length;b++){
                addp10[a]=addp10[a]+p10[b][a];}
                if(addp10[a]==0){
                   // ShortRname10[a]=Rname[9][a];
                NoRS10=NoRS10+1;}}
        for(int a=0;a<p10[0].length;a++){
                int h=rname[9].length;
                for (int b = 0; b < p10.length; b++) {
                    if(h==NoRS10){
                        pp[b][9]=0;}
                    else
                    pp[b][9] = pp[b][9]+p10[b][a] / (h - NoRS10);
                }
            }
        for(int a=0;a<p11[0].length;a++){
            for(int b=0;b<p11.length;b++){
                addp11[a]=addp11[a]+p11[b][a];}
                if(addp11[a]==0){
                   // ShortRname11[a]=Rname[10][a];
                NoRS11=NoRS11+1;}}
        for(int a=0;a<p11[0].length;a++){
                int h=rname[10].length;
                for (int b = 0; b < p11.length; b++) {
                    if(h==NoRS11){
                        pp[b][10]=0;}
                    else
                    pp[b][10] = pp[b][10]+p11[b][a] / (h - NoRS11);
                }
            }
        for(int a=0;a<p12[0].length;a++) {
            for (int b = 0; b < p12.length; b++) {
                addp12[a] = addp12[a] + p12[b][a];
            }
            if (addp12[a] == 0) {
                /// ShortRname12[a]=Rname[11][a];
                NoRS12 = NoRS12 + 1;
            }}
        for(int a=0;a<p12[0].length;a++) {
            int h = rname[11].length;
            for (int b = 0; b < p12.length; b++) {
                if(h==NoRS12){
                    pp[b][11]=0;}
                else
                pp[b][11] = pp[b][11] + p12[b][a] / (h - NoRS12);
            }
        }
        for(int a=0;a<p13[0].length;a++){
            for(int b=0;b<p13.length;b++){
                addp13[a]=addp13[a]+p13[b][a];}
                if(addp13[a]==0){
                   // ShortRname13[a]=Rname[12][a];
                NoRS13=NoRS13+1;}}
        for(int a=0;a<p13[0].length;a++){
                int h=rname[12].length;
                for (int b = 0; b < p13.length; b++) {
                    if(h==NoRS13){
                        pp[b][12]=0;}
                    else
                    pp[b][12] = pp[b][12]+p13[b][a] / (h - NoRS13);
                }
            }
        for(int a=0;a<p14[0].length;a++){
            for(int b=0;b<p14.length;b++){
                addp14[a]=addp14[a]+p14[b][a];}
                if(addp14[a]==0){
                   // ShortRname14[a]=Rname[13][a];
                NoRS14=NoRS14+1;}}
        for(int a=0;a<p14[0].length;a++){
                int h=rname[13].length;
                for (int b = 0; b < p14.length; b++) {
                    if(h==NoRS14){
                        pp[b][13]=0;}
                    else
                    pp[b][13] = pp[b][13]+p14[b][a] / (h - NoRS14);
                }
            }
        for(int a=0;a<p15[0].length;a++){
            for(int b=0;b<p15.length;b++){
                addp15[a]=addp15[a]+p15[b][a];}
                if(addp15[a]==0){
                   // ShortRname15[a]=Rname[14][a];
                NoRS15=NoRS15+1;}}
        for(int a=0;a<p15[0].length;a++){
                int h=rname[14].length;
                for (int b = 0; b < p15.length; b++) {
                    if(h==NoRS15){
                        pp[b][14]=0;}
                    else
                    pp[b][14] = pp[b][14]+p15[b][a] / (h - NoRS15);
                }
            }
        for(int a=0;a<p16[0].length;a++){
            for(int b=0;b<p16.length;b++){
                addp16[a]=addp16[a]+p16[b][a];}
                if(addp16[a]==0){
                   // ShortRname16[a]=Rname[15][a];
                NoRS16=NoRS16+1;}}
        for(int a=0;a<p16[0].length;a++){
                int h=rname[15].length;
                for (int b = 0; b < p16.length; b++) {
                    if(h==NoRS16){
                        pp[b][15]=0;}
                        else
                    pp[b][15] = pp[b][15]+p16[b][a] / (h - NoRS16);
                }
            }
        for(int a=0;a<p17[0].length;a++){
            for(int b=0;b<p17.length;b++){
                addp17[a]=addp17[a]+p17[b][a];}
                if(addp17[a]==0){
                   // ShortRname17[a]=Rname[16][a];
                NoRS17=NoRS17+1;}}
        for(int a=0;a<p17[0].length;a++){
                int h=rname[16].length;
                for (int b = 0; b < p17.length; b++) {
                    if(h==NoRS17){
                        pp[b][16]=0;}
                    else
                    pp[b][16] = pp[b][16]+p17[b][a] / (h - NoRS17);
                }
            }
        for(int a=0;a<p18[0].length;a++){
            for(int b=0;b<p18.length;b++){
                addp18[a]=addp18[a]+p18[b][a];}
                if(addp18[a]==0){
                   // ShortRname18[a]=Rname[17][a];
                NoRS18=NoRS18+1;}}
        for(int a=0;a<p18[0].length;a++){
                int h=rname[17].length;
                for (int b = 0; b < p18.length; b++) {
                    if(h==NoRS18){
                        pp[b][17]=0;}
                    else
                    pp[b][17] = pp[b][17]+p18[b][a] / (h - NoRS18);
                }
            }
        for(int a=0;a<p19[0].length;a++){
            for(int b=0;b<p19.length;b++){
                addp19[a]=addp19[a]+p19[b][a];}
                if(addp19[a]==0){
                   // ShortRname19[a]=Rname[18][a];
                NoRS19=NoRS19+1;}}
        for(int a=0;a<p19[0].length;a++){
                int h=rname[18].length;
                for (int b = 0; b < p19.length; b++) {
                    if(h==NoRS19){
                        pp[b][18]=0;}
                    else
                    pp[b][18] = pp[b][18]+p19[b][a] / (h - NoRS19);
                }
            }
        for(int a=0;a<p20[0].length;a++){
            for(int b=0;b<p20.length;b++){
                addp20[a]=addp20[a]+p20[b][a];}
                if(addp20[a]==0){
                    //ShortRname20[a]=Rname[19][a];
                NoRS20=NoRS20+1;}}
        for(int a=0;a<p20[0].length;a++){
                int h=rname[19].length;
                for (int b = 0; b < p20.length; b++) {
                    if(h==NoRS20){
                        pp[b][19]=0;}
                    else
                    pp[b][19] = pp[b][19]+p20[b][a] / (h - NoRS20);
                }
            }
        for(int a=0;a<p21[0].length;a++){
            for(int b=0;b<p21.length;b++){
                addp21[a]=addp21[a]+p21[b][a];}
                if(addp21[a]==0){
                   // ShortRname21[a]=Rname[20][a];
                NoRS21=NoRS21+1;}}
        for(int a=0;a<p21[0].length;a++){
                int h=rname[20].length;
                for (int b = 0; b < p21.length; b++) {
                    if(h==NoRS21){
                        pp[b][20]=0;}
                    else
                    pp[b][20] = pp[b][20]+p21[b][a] / (h - NoRS21);
                }
            }
        for(int a=0;a<p22[0].length;a++){
            for(int b=0;b<p22.length;b++){
                addp22[a]=addp22[a]+p22[b][a];}
                if(addp22[a]==0){
                   // ShortRname22[a]=Rname[21][a];
                NoRS22=NoRS22+1;}}
        for(int a=0;a<p22[0].length;a++){
                int h=rname[21].length;
                for (int b = 0; b < p22.length; b++) {
                    if(h==NoRS22){
                        pp[b][21]=0;}
                    else
                    pp[b][21] = pp[b][21]+p22[b][a] / (h - NoRS22);
                }
            }
        for(int a=0;a<p23[0].length;a++){
            for(int b=0;b<p23.length;b++){
                addp23[a]=addp23[a]+p23[b][a];}
                if(addp23[a]==0){
                   // ShortRname23[a]=Rname[22][a];
                NoRS23=NoRS23+1;}}
        for(int a=0;a<p23[0].length;a++){
                int h=rname[22].length;
                for (int b = 0; b < p23.length; b++) {
                    if(h==NoRS23){
                        pp[b][22]=0;}
                    else
                    pp[b][22] = pp[b][22]+p23[b][a] / (h - NoRS23);
                }
            }
        List a;
        List b;
        a= DateUtil.getBetweenDates(initialtime,starttime);//预热期
        int n=a.size();
        b=DateUtil.getBetweenDates(initialtime,raintime);//预热期和实测期
        int m=b.size();
        timeseries=new String[m];
        for(int i=0;i<m;i++){
            timeseries[i] = (String) b.get(i);
        }
        addp1=new float[p1[0].length];
        for(int i=0;i<p1[0].length;i++){
            for(int j=n-1;j<p1.length;j++){
                addp1[i]=addp1[i]+p1[j][i];
            }}
        addp2=new float[p2[0].length];
        for(int i=0;i<p2[0].length;i++){
            for(int j=n-1;j<p2.length;j++){
                addp2[i]=addp2[i]+p2[j][i];}}
        addp3=new float[p3[0].length];
        for(int i=0;i<p3[0].length;i++){
            for(int j=n-1;j<p3.length;j++){
                addp3[i]=addp3[i]+p3[j][i];}}
        addp4=new float[p4[0].length];
        for(int i=0;i<p4[0].length;i++){
            for(int j=n-1;j<p4.length;j++){
                addp4[i]=addp4[i]+p4[j][i];}}
        addp5=new float[p5[0].length];
        for(int i=0;i<p5[0].length;i++){
            for(int j=n-1;j<p5.length;j++){
                addp5[i]=addp5[i]+p5[j][i];}}
        addp6=new float[p6[0].length];
        for(int i=0;i<p6[0].length;i++){
            for(int j=n-1;j<p6.length;j++){
                addp6[i]=addp6[i]+p6[j][i];}}
        addp7=new float[p7[0].length];
        for(int i=0;i<p7[0].length;i++){
            for(int j=n-1;j<p7.length;j++){
                addp7[i]=addp7[i]+p7[j][i];}}
        addp8=new float[p8[0].length];
        for(int i=0;i<p8[0].length;i++){
            for(int j=n-1;j<p8.length;j++){
                addp8[i]=addp8[i]+p8[j][i];}}
        addp9=new float[p9[0].length];
        for(int i=0;i<p9[0].length;i++){
            for(int j=n-1;j<p9.length;j++){
                addp9[i]=addp9[i]+p9[j][i];}}
        addp10=new float[p10[0].length];
        for(int i=0;i<p10[0].length;i++){
            for(int j=n-1;j<p10.length;j++){
                addp10[i]=addp10[i]+p10[j][i];}}
        addp11=new float[p11[0].length];
        for(int i=0;i<p11[0].length;i++){
            for(int j=n-1;j<p11.length;j++){
                addp11[i]=addp11[i]+p11[j][i];}}
        addp12=new float[p12[0].length];
        for(int i=0;i<p12[0].length;i++){
            for(int j=n-1;j<p12.length;j++){
                addp12[i]=addp12[i]+p12[j][i];}}
        addp13=new float[p13[0].length];
        for(int i=0;i<p13[0].length;i++){
            for(int j=n-1;j<p13.length;j++){
                addp13[i]=addp13[i]+p13[j][i];}}
        addp14=new float[p14[0].length];
        for(int i=0;i<p14[0].length;i++){
            for(int j=n-1;j<p14.length;j++){
                addp14[i]=addp14[i]+p14[j][i];}}
        addp15=new float[p15[0].length];
        for(int i=0;i<p15[0].length;i++){
            for(int j=n-1;j<p15.length;j++){
                addp15[i]=addp15[i]+p15[j][i];}}
        addp16=new float[p16[0].length];
        for(int i=0;i<p16[0].length;i++){
            for(int j=n-1;j<p16.length;j++){
                addp16[i]=addp16[i]+p16[j][i];}}
        addp17=new float[p17[0].length];
        for(int i=0;i<p17[0].length;i++){
            for(int j=n-1;j<p17.length;j++){
                addp17[i]=addp17[i]+p17[j][i];}}
        addp18=new float[p18[0].length];
        for(int i=0;i<p18[0].length;i++){
            for(int j=n-1;j<p18.length;j++){
                addp18[i]=addp18[i]+p18[j][i];}}
        addp19=new float[p19[0].length];
        for(int i=0;i<p19[0].length;i++){
            for(int j=n-1;j<p19.length;j++){
                addp19[i]=addp19[i]+p19[j][i];}}
        addp20=new float[p20[0].length];
        for(int i=0;i<p20[0].length;i++){
            for(int j=0;j<p20.length;j++){
                addp20[i]=addp20[i]+p20[j][i];}}
        addp21=new float[p21[0].length];
        for(int i=0;i<p21[0].length;i++){
            for(int j=n-1;j<p21.length;j++){
                addp21[i]=addp21[i]+p21[j][i];}}
        addp22=new float[p22[0].length];
        for(int i=0;i<p22[0].length;i++){
            for(int j=n-1;j<p22.length;j++){
                addp22[i]=addp22[i]+p22[j][i];}}
        addp23=new float[p23[0].length];
        for(int i=0;i<p23[0].length;i++){
            for(int j=n-1;j<p23.length;j++){
                addp23[i]=addp23[i]+p23[j][i];
            }}
        maxrain[0]=addp1[0];
        maxname[0]=rname[0][0];
        for(int i=0;i<addp1.length;i++){
            if(addp1[i]>maxrain[0]){
            maxrain[0]=addp1[i];
            maxname[0]=rname[0][i];}
        }
        maxrain[1]=addp2[0];
        maxname[1]=rname[1][0];
        for(int i=0;i<addp2.length;i++){
            if(addp2[i]>maxrain[1]){
                maxrain[1]=addp2[i];
            maxname[1]=rname[1][i];}
        }
        maxrain[2]=addp3[0];
        maxname[2]=rname[2][0];
        for(int i=0;i<addp3.length;i++){
            if(addp3[i]>maxrain[2]){
                maxrain[2]=addp3[i];
            maxname[2]=rname[2][i];}
        }
        maxrain[3]=addp4[0];
        maxname[3]=rname[3][0];
        for(int i=0;i<addp4.length;i++){
            if(addp4[i]>maxrain[3]){
                maxrain[3]=addp4[i];
            maxname[3]=rname[3][i];}
        }
        maxrain[4]=addp5[0];
        maxname[4]=rname[4][0];
        for(int i=0;i<addp5.length;i++){
            if(addp5[i]>maxrain[4]){
                maxrain[4]=addp5[i];
            maxname[4]=rname[4][i];}
        }
        maxrain[5]=addp6[0];
        maxname[5]=rname[5][0];
        for(int i=0;i<addp6.length;i++){
            if(addp6[i]>maxrain[5]){
                maxrain[5]=addp6[i];
            maxname[5]=rname[5][i];}
        }
        maxrain[6]=addp7[0];
        maxname[6]=rname[6][0];
        for(int i=0;i<addp7.length;i++){
            if(addp7[i]>maxrain[6]){
                maxrain[6]=addp7[i];
            maxname[6]=rname[6][i];}
        }
        maxrain[7]=addp8[0];
        maxname[7]=rname[7][0];
        for(int i=0;i<addp8.length;i++){
            if(addp8[i]>maxrain[7]){
                maxrain[7]=addp8[i];
            maxname[7]=rname[7][i];}
        }
        maxrain[8]=addp9[0];
        maxname[8]=rname[8][0];
        for(int i=0;i<addp9.length;i++){
            if(addp9[i]>maxrain[8]){
                maxrain[8]=addp9[i];
            maxname[8]=rname[8][i];}
        }
        maxrain[9]=addp10[0];
        maxname[9]=rname[9][0];
        for(int i=0;i<addp10.length;i++){
            if(addp10[i]>maxrain[9]){
                maxrain[9]=addp10[i];
            maxname[9]=rname[9][i];}
        }
        maxrain[10]=addp11[0];
        maxname[10]=rname[10][0];
        for(int i=0;i<addp11.length;i++){
            if(addp11[i]>maxrain[10]){
                maxrain[10]=addp11[i];
            maxname[10]=rname[10][i];}
        }
        maxrain[11]=addp12[0];
        maxname[11]=rname[11][0];
        for(int i=0;i<addp12.length;i++){
            if(addp12[i]>maxrain[11]){
                maxrain[11]=addp12[i];
            maxname[11]=rname[11][i];}
        }
        maxrain[12]=addp13[0];
        maxname[12]=rname[12][0];
        for(int i=0;i<addp13.length;i++){
            if(addp13[i]>maxrain[12]){
                maxrain[12]=addp13[i];
            maxname[12]=rname[12][i];}
        }
        maxrain[13]=addp14[0];
        maxname[13]=rname[13][0];
        for(int i=0;i<addp14.length;i++){
            if(addp14[i]>maxrain[13]){
                maxrain[13]=addp14[i];
            maxname[13]=rname[13][i];}
        }
        maxrain[14]=addp15[0];
        maxname[14]=rname[14][0];
        for(int i=0;i<addp15.length;i++){
            if(addp15[i]>maxrain[14]){
                maxrain[14]=addp15[i];
            maxname[14]=rname[14][i];}
        }
        maxrain[15]=addp16[0];
        maxname[15]=rname[15][0];
        for(int i=0;i<addp16.length;i++){
            if(addp16[i]>maxrain[15]){
                maxrain[15]=addp16[i];
            maxname[15]=rname[15][i];}
        }
        maxrain[16]=addp17[0];
        maxname[16]=rname[16][0];
        for(int i=0;i<addp17.length;i++){
            if(addp17[i]>maxrain[16]){
                maxrain[16]=addp17[i];
            maxname[16]=rname[16][i];}
        }
        maxrain[17]=addp18[0];
        maxname[17]=rname[17][0];
        for(int i=0;i<addp18.length;i++){
            if(addp18[i]>maxrain[17]){
                maxrain[17]=addp18[i];
            maxname[17]=rname[17][i];}
        }
        maxrain[18]=addp19[0];
        maxname[18]=rname[18][0];
        for(int i=0;i<addp19.length;i++){
            if(addp19[i]>maxrain[0]){
                maxrain[18]=addp19[i];
            maxname[18]=rname[0][i];}
        }
        maxrain[19]=addp20[0];
        maxname[19]=rname[19][0];
        for(int i=0;i<addp20.length;i++){
            if(addp20[i]>maxrain[19]){
                maxrain[19]=addp20[i];
            maxname[19]=rname[19][i];}
        }
        maxrain[20]=addp21[0];
        maxname[20]=rname[20][0];
        for(int i=0;i<addp21.length;i++){
            if(addp21[i]>maxrain[20]){
                maxrain[20]=addp21[i];
            maxname[20]=rname[20][i];}
        }
        maxrain[21]=addp22[0];
        maxname[21]=rname[21][0];
        for(int i=0;i<addp22.length;i++){
            if(addp22[i]>maxrain[21]){
                maxrain[21]=addp22[i];
            maxname[21]=rname[21][i];}
        }
        maxrain[22]=addp1[0];
        maxname[22]=rname[22][0];
        for(int i=0;i<addp23.length;i++){
            if(addp23[i]>maxrain[22]){
                maxrain[22]=addp23[i];
            maxname[22]=rname[22][i];}
        }
        addpp=new float[pp[0].length];
        for(int j=0;j<pp[0].length;j++){
            for(int i=n-1;i<pp.length;i++){
                addpp[j]=addpp[j]+pp[i][j];

            }
        }
        for(int i=0;i<addpp.length;i++){
            addpp[i]= (float) (Math.floor(addpp[i]*100))/100;
        }
        for(int i=0;i<maxrain.length;i++){
            maxrain[i]= (float) (Math.floor(maxrain[i]*100))/100;
        }
        for(int i=0;i<pp.length;i++){
            for(int j=0;j<pp[0].length;j++){
                pp[i][j]= (float) (Math.floor(pp[i][j]*100))/100;
            }
        }
        HashMap map=new HashMap();
        map.put("averageRainfall",pp);//XAJ每块面平均雨量
        map.put("totalRainfall",addpp);//面平均累计雨量
        map.put("maxTotalRainfall",maxrain);//单站累计最大雨量
        map.put("maxStationName",maxname);//单站累计最大雨量对应站名
        map.put("timeSeries",timeseries);//预热期加实测期时间序列
        return map;
    }
}



