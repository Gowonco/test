package model.baseoracle;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseGateR<M extends BaseGateR<M>> extends Model<M> implements IBean {

	public M setStcd(java.lang.String stcd) {
		set("STCD", stcd);
		return (M)this;
	}
	
	public java.lang.String getStcd() {
		return getStr("STCD");
	}

	public M setTm(java.util.Date tm) {
		set("TM", tm);
		return (M)this;
	}
	
	public java.util.Date getTm() {
		return get("TM");
	}

	public M setExkey(java.lang.String exkey) {
		set("EXKEY", exkey);
		return (M)this;
	}
	
	public java.lang.String getExkey() {
		return getStr("EXKEY");
	}

	public M setEqptp(java.lang.String eqptp) {
		set("EQPTP", eqptp);
		return (M)this;
	}
	
	public java.lang.String getEqptp() {
		return getStr("EQPTP");
	}

	public M setEqpno(java.lang.String eqpno) {
		set("EQPNO", eqpno);
		return (M)this;
	}
	
	public java.lang.String getEqpno() {
		return getStr("EQPNO");
	}

	public M setGtopnum(java.math.BigDecimal gtopnum) {
		set("GTOPNUM", gtopnum);
		return (M)this;
	}
	
	public java.math.BigDecimal getGtopnum() {
		return get("GTOPNUM");
	}

	public M setGtophgt(java.math.BigDecimal gtophgt) {
		set("GTOPHGT", gtophgt);
		return (M)this;
	}
	
	public java.math.BigDecimal getGtophgt() {
		return get("GTOPHGT");
	}

	public M setGtq(java.math.BigDecimal gtq) {
		set("GTQ", gtq);
		return (M)this;
	}
	
	public java.math.BigDecimal getGtq() {
		return get("GTQ");
	}

	public M setMsqmt(java.lang.String msqmt) {
		set("MSQMT", msqmt);
		return (M)this;
	}
	
	public java.lang.String getMsqmt() {
		return getStr("MSQMT");
	}

}
