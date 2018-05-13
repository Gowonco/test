package model.baseoracle;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseWasR<M extends BaseWasR<M>> extends Model<M> implements IBean {

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

	public M setUpz(java.math.BigDecimal upz) {
		set("UPZ", upz);
		return (M)this;
	}
	
	public java.math.BigDecimal getUpz() {
		return get("UPZ");
	}

	public M setDwz(java.math.BigDecimal dwz) {
		set("DWZ", dwz);
		return (M)this;
	}
	
	public java.math.BigDecimal getDwz() {
		return get("DWZ");
	}

	public M setTgtq(java.math.BigDecimal tgtq) {
		set("TGTQ", tgtq);
		return (M)this;
	}
	
	public java.math.BigDecimal getTgtq() {
		return get("TGTQ");
	}

	public M setSwchrcd(java.lang.String swchrcd) {
		set("SWCHRCD", swchrcd);
		return (M)this;
	}
	
	public java.lang.String getSwchrcd() {
		return getStr("SWCHRCD");
	}

	public M setSupwptn(java.lang.String supwptn) {
		set("SUPWPTN", supwptn);
		return (M)this;
	}
	
	public java.lang.String getSupwptn() {
		return getStr("SUPWPTN");
	}

	public M setSdwwptn(java.lang.String sdwwptn) {
		set("SDWWPTN", sdwwptn);
		return (M)this;
	}
	
	public java.lang.String getSdwwptn() {
		return getStr("SDWWPTN");
	}

	public M setMsqmt(java.lang.String msqmt) {
		set("MSQMT", msqmt);
		return (M)this;
	}
	
	public java.lang.String getMsqmt() {
		return getStr("MSQMT");
	}

}
