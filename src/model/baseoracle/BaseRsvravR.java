package model.baseoracle;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseRsvravR<M extends BaseRsvravR<M>> extends Model<M> implements IBean {

	public M setStcd(java.lang.String stcd) {
		set("STCD", stcd);
		return (M)this;
	}
	
	public java.lang.String getStcd() {
		return getStr("STCD");
	}

	public M setIdtm(java.util.Date idtm) {
		set("IDTM", idtm);
		return (M)this;
	}
	
	public java.util.Date getIdtm() {
		return get("IDTM");
	}

	public M setSttdrcd(java.lang.String sttdrcd) {
		set("STTDRCD", sttdrcd);
		return (M)this;
	}
	
	public java.lang.String getSttdrcd() {
		return getStr("STTDRCD");
	}

	public M setAvrz(java.math.BigDecimal avrz) {
		set("AVRZ", avrz);
		return (M)this;
	}
	
	public java.math.BigDecimal getAvrz() {
		return get("AVRZ");
	}

	public M setAvinq(java.math.BigDecimal avinq) {
		set("AVINQ", avinq);
		return (M)this;
	}
	
	public java.math.BigDecimal getAvinq() {
		return get("AVINQ");
	}

	public M setAvotq(java.math.BigDecimal avotq) {
		set("AVOTQ", avotq);
		return (M)this;
	}
	
	public java.math.BigDecimal getAvotq() {
		return get("AVOTQ");
	}

	public M setAvw(java.math.BigDecimal avw) {
		set("AVW", avw);
		return (M)this;
	}
	
	public java.math.BigDecimal getAvw() {
		return get("AVW");
	}

}
