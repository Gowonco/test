package model.baseoracle;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseRsvrR<M extends BaseRsvrR<M>> extends Model<M> implements IBean {

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

	public M setRz(java.math.BigDecimal rz) {
		set("RZ", rz);
		return (M)this;
	}
	
	public java.math.BigDecimal getRz() {
		return get("RZ");
	}

	public M setInq(java.math.BigDecimal inq) {
		set("INQ", inq);
		return (M)this;
	}
	
	public java.math.BigDecimal getInq() {
		return get("INQ");
	}

	public M setW(java.math.BigDecimal w) {
		set("W", w);
		return (M)this;
	}
	
	public java.math.BigDecimal getW() {
		return get("W");
	}

	public M setBlrz(java.math.BigDecimal blrz) {
		set("BLRZ", blrz);
		return (M)this;
	}
	
	public java.math.BigDecimal getBlrz() {
		return get("BLRZ");
	}

	public M setOtq(java.math.BigDecimal otq) {
		set("OTQ", otq);
		return (M)this;
	}
	
	public java.math.BigDecimal getOtq() {
		return get("OTQ");
	}

	public M setRwchecd(java.lang.String rwchecd) {
		set("RWCHECD", rwchecd);
		return (M)this;
	}
	
	public java.lang.String getRwchecd() {
		return getStr("RWCHECD");
	}

	public M setRwptn(java.lang.String rwptn) {
		set("RWPTN", rwptn);
		return (M)this;
	}
	
	public java.lang.String getRwptn() {
		return getStr("RWPTN");
	}

	public M setInqdr(java.math.BigDecimal inqdr) {
		set("INQDR", inqdr);
		return (M)this;
	}
	
	public java.math.BigDecimal getInqdr() {
		return get("INQDR");
	}

	public M setMsqmt(java.lang.String msqmt) {
		set("MSQMT", msqmt);
		return (M)this;
	}
	
	public java.lang.String getMsqmt() {
		return getStr("MSQMT");
	}

}
