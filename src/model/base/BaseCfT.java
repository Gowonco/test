package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseCfT<M extends BaseCfT<M>> extends Model<M> implements IBean {

	public M setNO(java.lang.String NO) {
		set("NO", NO);
		return (M)this;
	}
	
	public java.lang.String getNO() {
		return getStr("NO");
	}

	public M setDBCD(java.lang.String DBCD) {
		set("DBCD", DBCD);
		return (M)this;
	}
	
	public java.lang.String getDBCD() {
		return getStr("DBCD");
	}

	public M setITEM(java.lang.String ITEM) {
		set("ITEM", ITEM);
		return (M)this;
	}
	
	public java.lang.String getITEM() {
		return getStr("ITEM");
	}

	public M setSTARTTM(java.util.Date STARTTM) {
		set("STARTTM", STARTTM);
		return (M)this;
	}
	
	public java.util.Date getSTARTTM() {
		return get("STARTTM");
	}

	public M setENDTM(java.util.Date ENDTM) {
		set("ENDTM", ENDTM);
		return (M)this;
	}
	
	public java.util.Date getENDTM() {
		return get("ENDTM");
	}

}
