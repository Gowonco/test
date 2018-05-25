package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysCh<M extends BaseSysCh<M>> extends Model<M> implements IBean {

	public M setUCODE(java.lang.String UCODE) {
		set("UCODE", UCODE);
		return (M)this;
	}
	
	public java.lang.String getUCODE() {
		return getStr("UCODE");
	}

	public M setSETTM(String SETTM) {
		set("SETTM", SETTM);
		return (M)this;
	}
	
	public java.util.Date getSETTM() {
		return get("SETTM");
	}

	public M setCORE(java.lang.Integer CORE) {
		set("CORE", CORE);
		return (M)this;
	}
	
	public java.lang.Integer getCORE() {
		return getInt("CORE");
	}

	public M setAUTF(java.lang.Integer AUTF) {
		set("AUTF", AUTF);
		return (M)this;
	}
	
	public java.lang.Integer getAUTF() {
		return getInt("AUTF");
	}

	public M setOBP(java.lang.Integer OBP) {
		set("OBP", OBP);
		return (M)this;
	}
	
	public java.lang.Integer getOBP() {
		return getInt("OBP");
	}

	public M setFOP(java.lang.Integer FOP) {
		set("FOP", FOP);
		return (M)this;
	}
	
	public java.lang.Integer getFOP() {
		return getInt("FOP");
	}

	public M setWUP(java.lang.Integer WUP) {
		set("WUP", WUP);
		return (M)this;
	}
	
	public java.lang.Integer getWUP() {
		return getInt("WUP");
	}

	public M setAOBP(java.lang.Integer AOBP) {
		set("AOBP", AOBP);
		return (M)this;
	}
	
	public java.lang.Integer getAOBP() {
		return getInt("AOBP");
	}

	public M setAFOP(java.lang.Integer AFOP) {
		set("AFOP", AFOP);
		return (M)this;
	}
	
	public java.lang.Integer getAFOP() {
		return getInt("AFOP");
	}

	public M setAWUP(java.lang.Integer AWUP) {
		set("AWUP", AWUP);
		return (M)this;
	}
	
	public java.lang.Integer getAWUP() {
		return getInt("AWUP");
	}

	public M setDS(java.lang.Integer DS) {
		set("DS", DS);
		return (M)this;
	}
	
	public java.lang.Integer getDS() {
		return getInt("DS");
	}

	public M setIP(java.lang.Integer IP) {
		set("IP", IP);
		return (M)this;
	}
	
	public java.lang.Integer getIP() {
		return getInt("IP");
	}

	public M setAUT(String AUT) {
		set("AUT", AUT);
		return (M)this;
	}
	
	public java.util.Date getAUT() {
		return get("AUT");
	}

}
