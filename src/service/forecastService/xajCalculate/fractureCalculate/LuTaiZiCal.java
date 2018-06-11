package service.forecastService.xajCalculate.fractureCalculate;

import java.util.*;

public class LuTaiZiCal {
    private float[]rr;
    private int m,routBeginIndex,routEndIndex,longtime;
    private String routBeginTime,routEndTime;
    private float rrrr;
    private float[][]qReservoir,qobsLu;
    private float[]qq,qc,qrc,qin,qinh,qcj,qjy;
    private float[]qObs;
    private float csl;

    private String dyly;
    private float[] para1,evap,qobs;
    private float[][]para2,zdylp,state;
    /*
    * */
    private float im,b,wum,wlm,wm,c,sm,ex,kg,ki,cg,ci,x,kk,cs,tt;
    private int lag,lagg;
    private float wdm,wmm,smm,c0,c1,c2;
    private float []cp,wp,wup,wlp,sp,frp,qsp,qip,qgp;

    private float[]wdp,ep,eup,elp,edp,rp,rsp,rip,rgp;
    private float[][]qxs,qxi,qxg,rsj,rgj,rij,qs,qi,qg,qspj,qipj,qgpj;
    private float w,wu,wl,wd,e,eu,el,ed,fr,s,r,rs,ri,rg,qqs,qqi,qqg;
    private float []rd,ped,pedf,qx,pp;
    private float[][]qcalj;
    private float pe,qss,qii,qgg,rq,qsz,qiz,qgz,em,ek,irs,bb1,bb2,ct;
    private int nd,mp1,mpmax,jj,ia;
    private int []mp;
    private int realxiao;
    /*输出*/
    private float[]pj,wj;
    private float emy,eky,eey;
    private float[]wwuj,wwlj,wwdj,ssj,ffrj,epj;
    private float[]qcal;
    private int lp,gltt;
    private float k,f;
    private float[]fp,wwj;
    private float[][]rrj,wpj,qsig,epej,plpj,wupj,wlpj,wdpj,frpj,spj,complement;
    private String[]timeSeries;
    private float[]id;
    private Map<String,Object>subParameter;
    public LuTaiZiCal(String dyly,int longtime,int longtimePre, float[] paraScetion, float[][] paraInflow,
                      float[] evapLu,float evap, float[][]qobs, float[][] zdylp, float[][]ppfu,float[][] state,
                      float[][]qReservoir,String routBeginTime,String routEndTime,int realtimeindex,String[]timeSeries,float[]routOption,Map<String,Object>subLuParameter) throws Exception {
        this.dyly=dyly;this.longtime=longtime;this.m=longtimePre;this.qReservoir=qReservoir;
        this.subParameter=subLuParameter;
        this.evap=new float[longtimePre];
        if (evapLu.length==0){
            for (int i=0;i<longtimePre;i++){
                this.evap[i]=evap;
            }
        }else {
            for (int i=0;i<longtimePre;i++){
                this.evap[i]=(i<longtime?evapLu[i]:evap);
            }
        }
        this.para1=paraScetion;this.para2=paraInflow;
        this.zdylp=unite(zdylp,ppfu);
        this.state=state;

        complement=new float[longtimePre-longtime][qobs[0].length];
        for (int i=0;i<complement.length;i++){
            for (int j=0;j<complement[0].length;j++){
                complement[i][j]=0;
            }
        }
        qobsLu=preprocessing(qobs);
        for (int i=0;i<qobsLu.length;i++){
            for (int j=0;j<qobsLu[0].length;j++){
                this.qobs[i]+=qobsLu[i][j];
            }
        }
        this.routBeginTime=routBeginTime;this.routEndTime=routEndTime;
        qObs=this.qobs;
        realxiao=realtimeindex;
        this.timeSeries=timeSeries;
        id=routOption;
    }
    void dchyubas() throws Exception {
        /*汇流时间指数*/
        int routBeginIndex=0,routEndIndex=0;
        for (int i=0;i<timeSeries.length;i++){
            if (timeSeries[i]==routBeginTime){
                routBeginIndex=i;
            }
            if (timeSeries[i]==routEndTime){
                routEndIndex=i;
            }
        }
        lagg=15;
        final int div=8;
        final float minn=0.0001f;
        int ifc=10,ib=0;
        float iia=0.4f,inn=0.5f,imf=0.0f,ef=0.05f;
        tt=para1[0];
        gltt= (int) tt;
        f=para1[1];
        lp=(int)para1[2];
        ia=(int)para1[3];
        mp=new int[lp];
        fp=new float[lp];cp=new float[lp];pp=new float[lp];wp=new float[lp];wlp=new float[lp];
        wup=new float[lp];qsp=new float[lp];qip=new float[lp];qgp=new float[lp];ep=new float[lp];
        eup=new float[lp];elp=new float[lp];edp=new float[lp];rp=new float[lp];rsp=new float[lp];
        rip=new float[lp];rgp=new float[lp];rgp=new float[lp];wdp=new float[lp];frp=new float[lp];
        sp=new float[lp];

        for (int i=0;i<lp;i++){
            fp[i]=para2[0][i];
            mp[i]=(int)para2[1][i];//河段数
        }
        for(int i=0;i<lp;i++){
            cp[i]=fp[i]*f/tt/(3.6f);
        }
        mpmax=1;
        for(int i=0;i<lp;i++){
            jj=mp[i];
            mpmax=(jj>mpmax?mp[i]:mpmax);
        }
        mp1=mpmax+1;

        qxs=new float[lp][mp1];qxi=new float[lp][mp1];qxg=new float[lp][mp1];
        qx=new float[mp1];
        wpj=new float[m][lp];frpj=new float[m][lp];spj=new float[m][lp];rrj=new float[m][lp];rsj=new float[m][lp];
        rgj=new float[m][lp];rij=new float[m][lp];
        qs=new float[m+lagg][lp];qsig=new float[m+lagg][lp];qi=new float[m+lagg][lp];qg=new float[m+lagg][lp];
        qcal=new float[m+lagg];
        pj=new float[m];//考虑是否传??
        rr=new float[m];wj=new float[m];epj=new float[m];wwuj=new float[m];
        wupj=new float[m][lp];wlpj=new float[m][lp];plpj=new float[m][lp];qspj=new float[m][lp];
        qipj=new float[m][lp];qgpj=new float[m][lp];epej=new float[m][lp];wdpj=new float[m][lp];
        wwj=new float[m];wwlj=new float[m];ssj=new float[m];ffrj=new float[m];wwdj=new float[m];
        qcalj=new float[m][lp];
        //预报初值设置
        qcal[0]=qobs[0];

        for (int i=0;i<lp;i++){
            wp[i]=state[i+1][0];
            wup[i]=state[i+1][1];
            wlp[i]=state[i+1][2];
            wdp[i]=wp[i]-wup[i]-wlp[i];
            sp[i]=state[i+1][5];
            frp[i]=state[i+1][6];
            qsp[i]=qcal[0]/lp/3;
            qip[i]=qcal[0]/lp/3;
            qgp[i]=qcal[0]/lp/3;
            qs[0][i]=qcal[0]/lp/3;
            qi[0][i]=qcal[0]/lp/3;
            qg[0][i]=qcal[0]/lp/3;
        }
        for (int i=0;i<lp;i++){
            for(int j=0;j<mp[i]+1;j++){
                qxs[i][j]=qsp[i];
                qxi[i][j]=qip[i];
                qxg[i][j]=qgp[i];
            }
        }
        //输入子流域参数
        float[]kkk= (float[]) subParameter.get("K");
        float[]bb=(float[])subParameter.get("B");
        float[]cc=(float[])subParameter.get("C");
        float[]wwm=(float[])subParameter.get("WM");
        float[]wwum=(float[])subParameter.get("WLM");
        float[]wwlm=(float[])subParameter.get("WLM");
        float[]iim=(float[])subParameter.get("IM");
        float[]ssm=(float[])subParameter.get("SM");
        float[]eex=(float[])subParameter.get("EX");
        float[]kkg=(float[])subParameter.get("KG");
        float[]kki=(float[])subParameter.get("KI");
        float[]ccg=(float[])subParameter.get("CG");
        float[]cci=(float[])subParameter.get("CI");
        float[]xxx=(float[])subParameter.get("X");
        float[]ccs=(float[])subParameter.get("CS");
        int []llag=(int [])subParameter.get("L");

        for (int i=0;i<lp;i++){
            k=kkk[i];
            b=bb[i];
            c=cc[i];
            wm=wwm[i];
            wum=wwum[i];
            wlm=wwlm[i];
            im=iim[i];
            sm=ssm[i];
            ex=eex[i];
            kg=kkg[i];
            ki=kki[i];
            cg=ccg[i];
            ci=cci[i];
            x=xxx[i];
            cs=ccs[i];
            lag=llag[i];
            /*
             *
             * */
            kk=tt;
            wdm=wm-wum-wlm;
            wmm=(1+b)*wm/(1-im);
            smm=(1+ex)*sm;
            bb1=kg+ki;
            bb2=kg/ki;
            ki= (float) ((1- Math.pow(1-bb1,tt/24f))/(1+bb2));
            kg=ki*bb2;
            cg=(float) (Math.pow(cg,tt/24));
            ci=(float) (Math.pow(ci,tt/24));
            cs=(float) (Math.pow(cs,tt/24));

            c0=((0.5f)*tt-kk*x)/(kk-kk*x+(0.5f)*tt);
            c1=(kk*x+(0.5f)*tt)/(kk-kk*x+(0.5f)*tt);
            c2=(kk-kk*x-(0.5f)*tt)/(kk-kk*x+(0.5f)*tt);
            for (int j=0;j<lag;j++){
                qsig[j][i]=qobs[0]/lp;
            }

            for (int j=0;j<m;j++){
                pp[i]=zdylp[j][i];
                em=evap[j];
                ek=k*em;
                mp1=mp[i];
                pe=pp[i]-ek;
                w=wp[i];
                wu=wup[i];
                wl=wlp[i];
                wd=wdp[i];
                //产流计算
                yield(c,b,div,iia,inn,ifc,imf,ef,ib);
                ep[i]=e;
                eup[i]=eu;
                elp[i]=el;
                edp[i]=ed;
                wp[i]=w;
                wup[i]=wu;
                wlp[i]=wl;
                wdp[i]=wd;
                rp[i]=r;


                rrj[j][i]=rp[i];
                wpj[j][i]=wp[i];
                frpj[j][i]=fp[i];

                fr=frp[i];
                s=sp[i];
                qqs=qsp[i];
                qqi=qip[i];
                qqg=qgp[i];
                ct=cp[i];
                /*分水源
                 * */
                diviThree(ib);
                frp[i]=fr;
                sp[i]=s;
                rsp[i]=rs;
                rip[i]=ri;
                rgp[i]=rg;
                qsp[i]=qqs;
                qip[i]=qqi;
                qgp[i]=qqg;

                qss=qsp[i];
                qii=qip[i];
                qgg=qgp[i];

                spj[j][i]=sp[i];
                rsj[j][i]=rsp[i];
                rij[j][i]=rip[i];
                rgj[j][i]=rgp[i];

                rq=qss;
                for (int jj=0;jj<mp1;jj++){
                    qx[jj]=qxs[i][jj];
                }
                musk(mp[i],c0,c1,c2);
                qss=rq;
                for (int jj=0;jj<mp1;jj++){
                    qxs[i][jj]=qx[jj];
                }

                rq=qii;
                for (int jj=0;jj<mp1;jj++){
                    qx[jj]=qxi[i][jj];
                }
                musk(mp[i],c0,c1,c2);
                qii=rq;
                for (int jj=0;jj<mp1;jj++){
                    qxi[i][jj]=qx[jj];
                }

                rq=qgg;
                for (int jj=0;jj<mp1;jj++){
                    qx[jj]=qxg[i][jj];
                }
                musk(mp[i],c0,c1,c2);
                qgg=rq;
                for (int jj=0;jj<mp1;jj++){
                    qxg[i][jj]=qx[jj];
                }
                qsz = qss;
                qiz=qii;
                qgz=qgg;

                if (j+lag==0){
                    qs[j+lag][i]=(qcal[0]/lp/3)*cs+qsz*(1-cs);
                    qi[j+lag][i]=(qcal[0]/lp/3)*cs+qiz*(1-cs);
                    qg[j+lag][i]=(qcal[0]/lp/3)*cs+qgz*(1-cs);
                }else{
                    qs[j+lag][i]=qs[j+lag-1][i]*cs+qsz*(1-cs);
                    qi[j+lag][i]=qi[j+lag-1][i]*cs+qiz*(1-cs);
                    qg[j+lag][i]=qg[j+lag-1][i]*cs+qgz*(1-cs);
                }

                qsig[j+lag][i]=qs[j+lag][i]+qi[j+lag][i]+qg[j+lag][i];
                epej[j][i]= (float) (Math.floor((100 * ep[i]))/100);
                wpj[j][i]= (float) (Math.floor((100 * wp[i]))/100);
                wupj[j][i]= (float) (Math.floor((100 * wup[i]))/100);
                wlpj[j][i]= (float) (Math.floor((100 * wlp[i]))/100);
                wdpj[j][i]= (float) (Math.floor((100 * wlp[i]))/100);
                plpj[j][i]= (float) (Math.floor((100 * pp[i]))/100);
                rrj[j][i]= (float) (Math.floor((100 * rp[i]))/100);
                rsj[j][i]= (float) (Math.floor((100 * rip[i]))/100);
                rij[j][i]= (float) (Math.floor((100 * rsp[i]))/100);
                rgj[j][i]= (float) (Math.floor((100 * rgp[i]))/100);
                spj[j][i]= (float) (Math.floor((10000 * sp[i]))/10000);
                frpj[j][i]= (float) (Math.floor((10000 * frp[i]))/10000);
                qspj[j][i]= (float) (Math.floor((10000 * qsp[i]))/10000);
                qipj[j][i]= (float) (Math.floor((10000 * qip[i]))/10000);
                qgpj[j][i]= (float) (Math.floor((10000 * qgp[i]))/10000);
            }
        }
        /*id改写*/
        id[1]=id[3];
        id[2]=id[4];
        //水库汇流结果

        for (int j=0;j<m;j++){
            rr[j]=0;
            for (int i=0;i<6;i++){
                rr[j]=rr[j]+fp[i]*rrj[j][i];
            }
            for (int i=6;i<8;i++){
                rr[j]=rr[j]+fp[i]*rrj[j][i]*id[1];
            }
            for (int i=8;i<9;i++){
                rr[j]=rr[j]+fp[i]*rrj[j][i];
            }
        }
        for (int j=0;j<m;j++){
            pj[j]=0;
            wj[j]=0;
            qcal[j]=0;
            for (int i=0;i<lp;i++){
                pj[j]=pj[j]+fp[i]*zdylp[j][i];
                wj[j]=wj[j]+fp[i]*wpj[j][i];
                qcal[j]=qcal[j]+qsig[j][i];
            }
        }
        emy=0;
        eky=0;
        eey=0;
        for (int j=0;j<m;j++){
            wwj[j]=0;
            wwuj[j]=0;
            wwlj[j]=0;
            ssj[j]=0;
            ffrj[j]=0;
            pj[j]=0;
            epj[j]=0;
            emy=emy+evap[j];
            eky=eky+evap[j]*k;
            for (int i=0;i<lp;i++){
                epj[j]=epj[j]+fp[i]*epej[j][i];
                pj[j]=pj[j]+fp[i]*plpj[j][i];
                wwj[j]=wwj[j]+fp[i]*wpj[j][i];
                wwuj[j]=wwuj[j]+fp[i]*wupj[j][i];
                wwlj[j]=wwlj[j]+fp[i]*wlpj[j][i];
                wwdj[j]=wwdj[j]+fp[i]*wdpj[j][i];
                ffrj[j]=ffrj[j]+fp[i]*frpj[j][i];
                ssj[j]=ssj[j]+fp[i]*spj[j][i];
            }
            eey=eey+epj[j];
        }
        rrrr=0;
        for (int j=0;j<m;j++){
            rrrr=rrrr+rr[j];
        }
        rrrr=rrrr*f/100000;

        qq=new float[m];qc=new float[m];qrc=new float[m];qin=new float[m];qinh=new float[m];

        for (int j=routBeginIndex;j<=routEndIndex;j++){
            qq[j]=qReservoir[j][6];
        }
        for (int j=0;j<m;j++){
            qc[j]=id[0]*qReservoir[j][0]+id[1]*qReservoir[j][1]+id[2]*qReservoir[j][2];
            qrc[j]=id[0]*qReservoir[j][4]+id[1]*qReservoir[j][5]+id[2]*qq[j];
            qin[j]=qc[j];
            qinh[j]=qrc[j];
        }
        qcj=new float[m];
        qjy=new float[m];
        for (int j=0;j<m;j++){
            qcj[j]=qsig[j][4]+qsig[j][5]+(qsig[j][6]+qsig[j][7])*id[3]+qsig[j][8];
        }
        qjy[0]=qcj[0];
        for (int j=1;j<m;j++){
            qjy[j]=csl*qjy[j-1]+(1-csl)*qcj[j];
        }
        for (int j=0;j<m;j++){
            qjy[j]=qjy[j]+qsig[j][0]+qsig[j][1]+qsig[j][2]+qsig[j][3];
        }
        for (int j=0;j<m;j++){
            qcal[j]=qjy[j]+qinh[j];
        }
        realTimeCorrection(realxiao);
    }
    private void yield(float c, float b,float div,float iia,float inn,float ifc,float imf,
               float ef,float ib){
        float a,peds,rri,fi,minn;
        minn=0.0001f;
        if (pe<=div){
            nd=1;
            ped=new float[nd];pedf=new float[nd];
            ped[0]=pe;
        }else {
            nd=(int)(pe/div)+1;
            ped=new float[nd];pedf=new float[nd];
            for (int ii=0;ii<nd-1;ii++){
                ped[ii]=div;
            }
            ped[nd-1]=pe-(nd-1)*div+minn;
        }
        rd=new float[nd];
        if (pe<=0){
            r=0;
            if ((wu+pe)<0){
                eu=wu+ek+pe;wu=0;el=(ek-eu)*wl/wlm;
                if(wl<(c*wlm)){
                    el=c*(ek-eu);
                }
                if ((wl-el)<0){
                    ed=el-wl;el=wl;wl=0;wd=wd-ed;
                    w = wu + wl + wd;
                    e = eu + el + ed;
                }else{
                    ed=0;wl=wl-el;
                    w = wu + wl + wd;
                    e = eu + el + ed;
                }
            }else {
                eu=ek;ed=0;el=0;wu=wu+pe;
                w = wu + wl + wd;
                e = eu + el + ed;
            }
        }else {
            a=(float)(wmm*(1-(Math.pow((1-w/wm),(1/(1+b))))));
            r=0;
            peds=0;
            for (int ii=0;ii<nd;ii++){
                a=a+ped[ii];
                peds=peds+ped[ii];
                rri=r;
                r=peds-wm+w;
                if (a<wmm){
                    r= (float) (r+wm*(Math.pow((1-a/wmm),(1+b))));//稍微不同
                }
                rd[ii]=r-rri;
            }
            fi= (float) ((iia*(Math.pow(wm-w,inn))+ifc)*(ef+1));
            if (pe>=fi){
                irs=(pe-fi)*(1-r/pe)*imf;
            }else {
                irs= (float) ((pe-fi/(ef+1)*(1-Math.pow(1-pe/fi,ef+1)))*(1-r/pe)*imf);
            }
            for(int ii=0;ii<nd;ii++){
                pedf[ii]=irs/nd*ib;
            }
            eu=ek;el=0;ed=0;
            if ((wu+pe-r)<wum){
                wu=wu+pe-r;
                w=wu+wl+wd;//有点奇怪
                e=eu+el+ed;
            }else if((wu+wl+pe-r-wum)>=wlm){
                wu=wum;wl=wlm;wd=w+peds-r-wu-wl;
                if (wd>wdm){wd=wdm;}
                w=wu+wl+wd;
                e=eu+el+ed;
            }else{
                wl = wu + wl + pe - r - wum;
                wu = wum;
                w=wu+wl+wd;
                e=eu+el+ed;
            }
        }
    }
    private void diviThree(float ib){
        float rb,rr,kgd,kid,td,xx,au,ff;
        if (pe<=0){
            rs=0;
            rg=s*kg*fr;
            ri=s*ki*fr;
            s=s*(1-kg-ki);
            qqg=qqg*cg+rg*(1-cg)*ct;
            qqi=qqi*ci+ri*(1-ci)*ct;
            qqs=rs*ct;
        }else {
            rb=im*pe;
            kid= (float) ((1-Math.pow((1-(kg+ki)),(1.0f/nd)))/(kg+ki));
            kgd=kid*kg;
            kid=kid*ki;
            rs=0;
            ri=0;rg=0;
            for (int ii=0;ii<nd;ii++){
                td=rd[ii]-im*ped[ii];
                xx=fr;
                fr=td/ped[ii];
                s=xx*s/fr;
                if (s>=sm){
                    rr=(ped[ii]+pedf[ii]/fr*ib+s-sm)*fr;
                    rs=rr+rs;
                    s=ped[ii]+pedf[ii]/fr*ib-rr/fr+s;
                    rg=s*kgd*fr+rg;
                    ri=s*kid*fr+ri;
                    s=s*(1-kid-kgd);
                }else {
                    au= (float) (smm*(1f-(Math.pow((1.0f-s/sm),(1.0f/(1+ex))))));
                    ff=au+ped[ii]+pedf[ii]/fr*ib;
                    if(ff<smm){
                        ff= (float) Math.pow((1-(ped[ii]+pedf[ii]/fr*ib+au)/smm),(1+ex));
                        rr=(ped[ii]+pedf[ii]/fr*ib-sm+s+sm*ff)*fr;
                        rs=rr+rs;
                        s=ped[ii]+pedf[ii]/fr*ib-rr/fr+s;
                        rg=s*kgd*fr+rg;
                        ri=s*kid*fr+ri;
                        s=s*(1-kid-kgd);
                    }else {
                        rr=(ped[ii]+pedf[ii]/fr*ib+s-sm)*fr;
                        rs=rr+rs;
                        s=ped[ii]+pedf[ii]/fr*ib-rr/fr+s;
                        rg=s*kgd*fr+rg;
                        ri=s*kid*fr+ri;
                        s=s*(1-kid-kgd);
                    }
                }
            }
            rs=rs+rb;
            qqg=qqg*cg+rg*(1f-cg)*ct;
            qqi=qqi*ci+ri*(1f-ci)*ct;
            qqs=rs*ct;
        }
    }
    private void musk(int mt,float c0,float c1,float c2){
        float q1,q2,q3;
        int lm;
        lm=mt+1;
        if (lm==1){
            qx[0]=rq;
        }else {
            for (int jj=1;jj<lm;jj++){
                q1=rq;
                q2=qx[jj-1];
                q3=qx[jj];
                qx[jj-1]=rq;
                rq=c0*q1+c1*q2+c2*q3;
            }
            qx[lm-1]=rq;
        }
    }
    public void setCsl(float csl) {
        this.csl = csl;
    }
    private void realTimeCorrection(int realTimeIndex){
        float []er=new float[m];
        float[]qxz=new float[m];
        float ee;
        if (realTimeIndex==1){
            for (int j=0;j<longtime;j++){
                er[j]=qObs[j]-qcal[j];
            }
            qxz[0]=qObs[0];
            for (int i=1;i<longtime;i++){
                qxz[i]=qcal[i]+er[i-1]*0.9f;
            }
            if (longtime>=2){
                ee=(er[longtime]+er[longtime-1])/2f;
            }else {
                ee=er[longtime];
            }
            for (int j=longtime-1;j<m;j++){
                qxz[j]=qcal[j];
            }
            for (int i=0;i<m;i++){
                qcal[i]=qxz[i];
            }
        }
    }



    public Map<String,Object>charactLuTaiZi(){
        int n=longtime;float min1=0.000001f;
        float qcm,icm,eqm,iem,pp;
        float rqo,rqc,eqobs,f0,fn,robsy,rcaly,ce,dc,qom,iom,rcali;
        icm=0;iom=0;
        rqo=qObs[0]/2f;
        rqc=qcal[0]/2f;
        pp=0;
        for (int j=0;j<m;j++){
            pp=pp+pj[j];
        }
        rcali=qin[0]/2f;
        for (int i=1;i<m-1;i++){
            rcali+=qin[i];
        }
        rcali+=(qin[m-1]/2f);
        rcali= (float) (rcali*24*3600/(Math.pow(10,8)));
        for (int j=1;j<m-1;j++){
            rqo=rqo+qObs[j];
            rqc=rqc+qcal[j];
        }
        rqo=rqo+qObs[m-1]/2;
        rqc=rqc+qcal[m-1]/2;
        robsy=rqo*0.36f*gltt/10000f;//亿立方米
        rcaly=rqc*0.36f*gltt/10000f;
        ce=(rcaly-robsy)/(robsy+min1)*100;//%
        eqobs=rqo/(m+min1);
        f0=0;
        fn=0;
        for (int j=0;j<n;j++){
            f0=f0+(qObs[j]-eqobs)*(qObs[j]-eqobs);
            fn=fn+(qcal[j]-qObs[j])*(qcal[j]-qObs[j]);
        }
        f0=f0/(n+min1);
        fn=fn/(n+min1);
        dc=1f-fn/(f0+min1);
        qom=qObs[0];
        for (int j=0;j<m;j++){
            if (qObs[j]>qom){
                qom=qObs[j];
                iom=j;
            }
        }
        qcm=qcal[0];
        for (int j=0;j<m;j++){
            if (qcal[j]>qcm){
                qcm=qcal[j];
                icm=j;
            }
        }
        eqm=(qcm-qom)/(qom+min1)*100;
        iem=icm-iom;
        Map<String,Object>charactLtz=new HashMap<>();
        charactLtz.put("总降雨量",pp);
        charactLtz.put("产流总水量",rrrr);
        charactLtz.put("来水总量",rcali);
        charactLtz.put("实测洪量",robsy);
        charactLtz.put("预报洪量",rcaly);
        charactLtz.put("洪量相对误差",ce);
        charactLtz.put("实测洪峰",qom);
        charactLtz.put("预报洪峰",qcm);
        charactLtz.put("洪峰相对误差",eqm);
        charactLtz.put("实测峰现时间",timeSeries[(int)iom]);
        charactLtz.put("预报峰现时间",timeSeries[(int)icm]);
        charactLtz.put("峰现时间误差",iem);
        charactLtz.put("确定性系数",dc);
        charactLtz.put("面平均雨量",pj);
        charactLtz.put("土壤含水量",wj);
        charactLtz.put("流域平均产流深",rr);
        charactLtz.put("上游来水演算流量",qinh);
        charactLtz.put("降水产流流量",qjy);
        charactLtz.put("实测流量",qObs);
        charactLtz.put("预报流量",qcal);
        return charactLtz;
    }
    private static float[][] unite(float[][] os1, float[][] os2) {
        List<float[]> list = new ArrayList<float[]>(Arrays.<float[]>asList(os1));
        list.addAll(Arrays.<float[]>asList(os2));
        return list.toArray(os1);
    }
    private float[][]preprocessing(float[][]intial){
        float[][]lastArray=new float[intial.length][intial[0].length];
        for (int i=0;i<intial.length;i++){
            for (int j=0;j<intial[0].length;j++){
                lastArray[i][j]=((intial[i][j]>0.0001)?intial[i][j]:0);
            }
        }
        return lastArray;
    }
}
