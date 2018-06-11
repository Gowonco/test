package service.forecastService.xajCalculate.fractureCalculate;

import java.util.HashMap;
import java.util.Map;

/*模型计算公共部分*/
class DchyubasH {
    private float im,b,wum,wlm,wm,c,sm,ex,kg,ki,cg,ci,x,kk,cs,tt;
    private String ttt,sitd;
    private int itt,lhh,lag,ly,numsq,numq,it1,iy2,im2,id2,ih2,iy1,im1,id1,ih1,itd,itd1,itd2,div,lagg;
    private float csl,k1,k2,wdm,wmm,smm,c0,c1,c2,minn;
    private float []cp,wp,wup,wlp,sp,frp,qsp,qip,qgp,qq,zz;

    private float[]wdp,ep,eup,elp,edp,rp,rsp,rip,rgp;
    private float[][]qxs,qxi,qxg,rsj,rgj,rij,qs,qi,qg,qspj,qipj,qgpj;
    private float w,wu,wl,wd,e,eu,el,ed,fr,s,r,rs,ri,rg,qqs,qqi,qqg;
    private float []rd,ped,pedf,qx,pp,para4,qobs,para1,evap,wwj;
    private float[]wwuj,wwlj,ssj,ffrj,epj,qcal,pj,wj;
    private float[][]state,qcalj,zdylp;
    private float pe,qss,qii,qgg,rq,qsz,qiz,qgz,em,ek,py,rsy,riy,rgy,riny,rresy,irs,bb1,bb2,ct;
    private int nd,mp1,mpmax,L,jj,i1,i2;
    private int []mp;
    private String dyly;
    /*输出*/
    private float emy,eky,eey;
    /*在子类中需要用到的*/
    protected float[]wwdj;
    protected int m,lp,gltt,ia;
    protected float k,f;
    protected float[]rr,fp;
    protected float[][]rrj,wpj,qsig,epej,plpj,wupj,wlpj,wdpj,frpj,spj;
    private Map<String,Object>subParameter;
    public DchyubasH(String dyly,int longtimePre,float[] para1,float[][]para2,
                   float[]evap,float[]qobs,float[][]zdylp,float[][]state,Map<String,Object>subParameter) throws Exception {
        this.qobs=qobs;this.state=state;this.dyly=dyly;this.para1=para1;
        this.zdylp=zdylp;this.evap=evap;
        this.subParameter=subParameter;
        lagg=15;
        final int div=8;
        final float minn=0.0001f;
        m=longtimePre;//从实测开始到预报结束
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
        if (dyly=="bb"){
            qcal[0]=0;
        }else {
            qcal[0]=qobs[0];
        }

        for (int i=0;i<lp;i++){
            wp[i]=state[i+1][0];
            wup[i]=state[i+1][1];
            wlp[i]=state[i+1][2];
            wdp[i]=wp[i]-wup[i]-wlp[i];
            sp[i]=state[i+1][5];
            frp[i]=state[i+1][6];
            float n= (float) ((Math.round(Math.pow(10,7)*qcal[0]/lp/3))/Math.pow(10,7));
            qsp[i]=n;
            qip[i]=n;
            qgp[i]=n;
            qs[0][i]=n;
            qi[0][i]=n;
            qg[0][i]=n;
        }
        for (int i=0;i<lp;i++){
            for(int j=0;j<mp[i]+1;j++){
                qxs[i][j]=qsp[i];
                qxi[i][j]=qip[i];
                qxg[i][j]=qgp[i];
            }
        }
        //输入子流域参数
        float[]kkk= (float[]) this.subParameter.get("K");
        float[]bb=(float[])this.subParameter.get("B");
        float[]cc=(float[])this.subParameter.get("C");
        float[]wwm=(float[])this.subParameter.get("WM");
        float[]wwum=(float[])this.subParameter.get("WLM");
        float[]wwlm=(float[])this.subParameter.get("WLM");
        float[]iim=(float[])this.subParameter.get("IM");
        float[]ssm=(float[])this.subParameter.get("SM");
        float[]eex=(float[])this.subParameter.get("EX");
        float[]kkg=(float[])this.subParameter.get("KG");
        float[]kki=(float[])this.subParameter.get("KI");
        float[]ccg=(float[])this.subParameter.get("CG");
        float[]cci=(float[])this.subParameter.get("CI");
        float[]xxx=(float[])this.subParameter.get("X");
        float[]ccs=(float[])this.subParameter.get("CS");
        int []llag=(int [])this.subParameter.get("L");
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
            wmm= (float) (Math.round(10000*((1+b)*wm/(1-im))));
            wmm=wmm/10000f;
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
                pp[i]= (float) (Math.round(100f*zdylp[j][i])/100f);/*保留两位小数*/
                em=evap[j];
                ek=k*em;
                mp1=mp[i];
                pe=(float) (Math.round(1000f*(pp[i]-ek))/1000f);//quzheng
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
                    el= (float) (Math.round(c*(ek-eu)*Math.pow(10,7))/(Math.pow(10,7)));
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
            a=(float)(wmm*(1d-(Math.pow((1d-w/wm),(1d/(1d+b))))));
            r=0;
            peds=0;
            for (int ii=0;ii<nd;ii++){
                a=a+ped[ii];
                peds=peds+ped[ii];
                rri=r;
                r=peds-wm+w;
                if (a<wmm){
                    r= (float) (r+wm*(Math.pow((1d-a/wmm),(1d+b))));//稍微不同
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
            kid= (float) ((1d-Math.pow((1d-(kg+ki)),(1d/nd)))/(kg+ki));
            kgd=kid*kg;
            kid=kid*ki;
            rs=0;
            ri=0;rg=0;
            for (int ii=0;ii<nd;ii++){
                td=rd[ii]-im*ped[ii];
                xx=fr;
                fr=td/ped[ii];
                s=xx*s/fr;
                //s=xx*s*ped[ii]*(rd[ii]+im*ped[ii])/(rd[ii]*rd[ii]+im*ped[ii]*(im*ped[ii]));
                if (s>=sm){
                    rr=(ped[ii]+pedf[ii]/fr*ib+s-sm)*fr;
                    rs=rr+rs;
                    s=ped[ii]+pedf[ii]/fr*ib-rr/fr+s;
                    rg=s*kgd*fr+rg;
                    ri=s*kid*fr+ri;
                    s=s*(1-kid-kgd);
                }else {
                    au= (float) (smm*(1d-(Math.pow((1.0d-s/sm),(1.0d/(1d+ex))))));//加d
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
}
